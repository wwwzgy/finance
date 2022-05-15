package com.repository;

import com.entity.Role;
import com.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserInterface {
    public List<User> getAllUsers(); //获取所有用户
    public void saveUser(User user); //保存新用户
    public User getUserById(String id); //根据id查找用户
    public List<Role> getRolesByUserId(String id); //根据id获得用户角色
    public void updatePassword(String NewPassword, String userId); //更改用户密码
    public void deleteUserInRole(String id); //删除“用户-角色”表中用户id为id的所有记录
    public void deleteUserById(String id);  //删除用户id为id的用户
}
