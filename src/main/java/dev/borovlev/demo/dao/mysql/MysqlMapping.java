package dev.borovlev.demo.dao.mysql;

import dev.borovlev.demo.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MysqlMapping {

    void insertUser(@Param("user") User user);
}
