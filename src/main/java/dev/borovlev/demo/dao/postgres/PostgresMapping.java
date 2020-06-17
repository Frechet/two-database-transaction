package dev.borovlev.demo.dao.postgres;

import dev.borovlev.demo.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface PostgresMapping {

    void insertUser(@Param("user") User user);
}
