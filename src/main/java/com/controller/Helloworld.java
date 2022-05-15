package com.controller;

import com.entity.Resource;
import com.entity.User;
import com.repository.ResourceInterface;
import com.repository.UserInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
public class Helloworld {
    @Autowired
    UserInterface userInterface;
    @Autowired
    ResourceInterface resourceInterface;
    @RequestMapping("/auth")
    public  String getRoles(){
        List <User> users=userInterface.getAllUsers();
        return users.get(0).getName();
    }
    @RequestMapping("/res")
    public List<Resource> getAllResource(){
        return resourceInterface.getAllUrl();
    }
    @RequestMapping("/admin/adduser")
    public String addUser(){
        User user=new User();
        user.setId(UUID.randomUUID().toString());
        user.setName("测试");
        userInterface.saveUser(user);
        return user.getUsername();
    }
}
