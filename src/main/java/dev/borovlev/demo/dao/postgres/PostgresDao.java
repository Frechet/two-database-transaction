package dev.borovlev.demo.dao.postgres;

import dev.borovlev.demo.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PostgresDao {

    private final PostgresMapping mapping;

    public void insertUser(User user) {
        mapping.insertUser(user);
    }
}
