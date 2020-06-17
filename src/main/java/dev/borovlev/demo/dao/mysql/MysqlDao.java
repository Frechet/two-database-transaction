package dev.borovlev.demo.dao.mysql;

import dev.borovlev.demo.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MysqlDao {

    private final MysqlMapping mapping;

    public void insertUser(User user) {
        mapping.insertUser(user);
    }
}
