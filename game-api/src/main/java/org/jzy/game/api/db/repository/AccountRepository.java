package org.jzy.game.api.db.repository;

import org.jzy.game.api.db.MongoDbService;
import org.jzy.game.api.struct.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 * 账号
 * <br>
 * API使用文档
 * https://docs.spring.io/spring-data/mongodb/docs/3.0.3.RELEASE/reference/html/#reference
 * <li>1 排序和分页：https://docs.spring.io/spring-data/mongodb/docs/3.0.3.RELEASE/reference/html/#repositories.paging-and-sorting</li>
 * <li>2 使用简介：https://docs.spring.io/spring-data/mongodb/docs/3.0.3.RELEASE/reference/html/#mongo.core</li>
 * <li>3 样例代码：https://github.com/spring-projects/spring-data-examples/tree/master/mongodb</li>
 *
 * @author jzy
 */
@Repository
public class AccountRepository implements CrudRepository<Account, Long> {

    @Autowired
    private MongoDbService mongoDbService;

    @Override
    public <S extends Account> S save(S entity) {
        mongoDbService.getMongoOperations().save(entity);
        return entity;
    }

    @Override
    public <S extends Account> List<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<Account> findById(Long aLong) {
        Account account = mongoDbService.getMongoOperations().findById(aLong, Account.class);
        if (account == null) {
            return Optional.empty();
        }
        return Optional.of(account);
    }

    @Override
    public boolean existsById(Long aLong) {
        return false;
    }

    @Override
    public List<Account> findAll() {
        return null;
    }

    @Override
    public Iterable<Account> findAllById(Iterable<Long> longs) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(Long aLong) {

    }

    @Override
    public void delete(Account entity) {
        mongoDbService.getMongoOperations().remove(entity);
    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {

    }

    @Override
    public void deleteAll(Iterable<? extends Account> entities) {

    }

    @Override
    public void deleteAll() {

    }


    public Account findByAccount(String account) {
        Account a = mongoDbService.getMongoOperations().findOne(Query.query(Criteria.where("account").is(account)), Account.class);
        return a;
    }
}
