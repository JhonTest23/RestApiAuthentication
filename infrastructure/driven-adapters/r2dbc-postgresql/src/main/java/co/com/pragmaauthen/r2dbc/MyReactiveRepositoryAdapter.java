package co.com.pragmaauthen.r2dbc;

import co.com.pragmaauthen.model.user.User;
import co.com.pragmaauthen.model.user.gateways.UserRepository;
import co.com.pragmaauthen.r2dbc.entity.UserData;
import co.com.pragmaauthen.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import lombok.*;

@Repository
public class MyReactiveRepositoryAdapter extends ReactiveAdapterOperations<
        User/* change for domain model */,
        UserData/* change for adapter model */,
        String,
    MyReactiveRepository
> implements UserRepository {
    public MyReactiveRepositoryAdapter(MyReactiveRepository repository, ObjectMapper mapper) {
        /**
         *  Could be use mapper.mapBuilder if your domain model implement builder pattern
         *  super(repository, mapper, d -> mapper.mapBuilder(d,ObjectModel.ObjectModelBuilder.class).build());
         *  Or using mapper.map with the class of the object model
         */
        super(repository, mapper, d -> mapper.map(d, User.class/* change for domain model */));
    }

    @Transactional
    @Override
    public Mono<User> save(User user) {
        return super.save(user);
    }

}
