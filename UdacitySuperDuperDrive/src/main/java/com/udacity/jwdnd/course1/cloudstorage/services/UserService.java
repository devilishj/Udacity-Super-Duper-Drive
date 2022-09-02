package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.UsersMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Random;


@Service
public class UserService {
    private UsersMapper usersMapper;
    private HashService hashService;

    public UserService(UsersMapper usersMapper, HashService hashService) {
        this.usersMapper = usersMapper;
        this.hashService = hashService;
    }

    public int insertUser(User user){
        return usersMapper.createUser(user);
    }

    public User getUser(String userName){
        return usersMapper.getUser(userName);
    }


    public boolean isUsernameAvailable(String userName){
        return getUser(userName)==null;
    }

    public void hashUserPassword(User user){
        Random random = new Random();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        String encodedSalt = Base64.getEncoder().encodeToString(salt);
        String hashedPassword = hashService.getHashedValue(user.getPassWord(), encodedSalt);

        user.setSalt(encodedSalt);
        user.setPassWord(hashedPassword);

    }
}
