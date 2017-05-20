package com.example.ekanban.dozer.converter;

import com.example.ekanban.entity.User;
import org.dozer.DozerConverter;


/**
 * Created by razamd on 3/30/2017.
 */
public class UserLongConverter extends DozerConverter<User,Long> {

    public UserLongConverter() {
        super(User.class,Long.class);
    }

    @Override
    public User convertFrom(Long id, User user) {
        if(id == null || id == 0L){
            return null;
        }

        return new User(id);
    }

    @Override
    public Long convertTo(User user, Long id) {
        if(user == null) {
            return null;
        }
        return user.getId();
    }
}
