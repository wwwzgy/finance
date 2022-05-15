package com.repository;

import com.entity.Resource;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ResourceInterface {
    public List<Resource> getAllUrl();
    public List<Resource> getResourceByRoleId(String roleId);
}
