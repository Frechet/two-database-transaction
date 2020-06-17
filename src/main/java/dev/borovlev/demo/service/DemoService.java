package dev.borovlev.demo.service;

import dev.borovlev.demo.dao.mysql.MysqlDao;
import dev.borovlev.demo.dao.postgres.PostgresDao;
import dev.borovlev.demo.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class DemoService {

    private PostgresDao postgresDao;
    private MysqlDao mysqlDao;

    public DemoService(PostgresDao postgresDao, MysqlDao mysqlDao) {
        this.postgresDao = postgresDao;
        this.mysqlDao = mysqlDao;
    }

    @Transactional
    public void doSomethingTransactional() {
        log.info("Start transaction");
        User user = new User();
        user.setId(1L);
        user.setFirstname("first");
        user.setLastname("last");
        mysqlDao.insertUser(user);
        postgresDao.insertUser(user);
        log.info("Finish inserting");
        throw new RuntimeException("Test transaction with 2 database");
    }
}
