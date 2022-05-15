package com.repository;

import com.entity.Menu;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MenuInterface {
    public List<Menu> getMenuByRoleId(String RoleId);
}
