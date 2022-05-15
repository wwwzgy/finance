package com.repository;

import com.entity.Role;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RoleInterface {
    public List<Role> getAllRolesByUserid(String UserId);
}
