package com.alamedapps.questionsandanswers.serviceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alamedapps.questionsandanswers.entity.User;
import com.alamedapps.questionsandanswers.repository.UserRepository;
import com.alamedapps.questionsandanswers.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void saveOrUpdate(User object) {
        this.userRepository.save(object);
    }

    @Override
    public void delete(User object) {
    	this.userRepository.delete(object);
    }

    @Override
    public Optional<User> findById(int id) {
        return this.userRepository.findById(id);
    }

    @Override
    public List<User> findAll() {
        return this.userRepository.findAll();
    }

    @Override
    public User findByEmail(String email) {
        return this.userRepository.findByEmail(email);
    }

}
