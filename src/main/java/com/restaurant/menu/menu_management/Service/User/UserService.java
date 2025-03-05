package com.restaurant.menu.menu_management.Service.User;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.restaurant.menu.menu_management.Domain.User;
import com.restaurant.menu.menu_management.Repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public List<User> fetchAllUser(){
        return this.userRepository.findAll();
    }

    public User fetchUserById(long id){
        Optional<User> userOptional = this.userRepository.findById(id);
        if(userOptional.isPresent()){
            return userOptional.get();
        }
        return null;
    }

    public User handleCreateUser(User user) {
        return this.userRepository.save(user);
    }

    public void handleDeleteUser(long id) {
        this.userRepository.deleteById(id);
    }

    public User handleUpdateUser(User user){
        User userUpdate = fetchUserById(user.getId());
        
        if(userUpdate != null){
            userUpdate.setEmail(user.getEmail());
            userUpdate.setName(user.getName());
            userUpdate.setPassword(user.getPassword());
            //update
            userUpdate = this.userRepository.save(userUpdate);
        }
        return userUpdate;
    }

    public User handleGetUserByUsername(String username) {
        return this.userRepository.findByEmail(username);
    }

}
