package org.mmo.login.db.repository;

import org.mmo.login.struct.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

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
public class AccountRepository implements IAccountRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public <S extends Account> S save(S entity) {
        mongoTemplate.save(entity);
        return entity;
    }

    @Override
    public <S extends Account> List<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<Account> findById(Long aLong) {
        Account account = mongoTemplate.findById(aLong, Account.class);
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
        mongoTemplate.remove(entity);
    }

    @Override
    public void deleteAll(Iterable<? extends Account> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public List<Account> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<Account> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Account> S insert(S entity) {
        return null;
    }

    @Override
    public <S extends Account> List<S> insert(Iterable<S> entities) {
        return null;
    }

    @Override
    public <S extends Account> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Account> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends Account> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends Account> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Account> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Account> boolean exists(Example<S> example) {
        return false;
    }


    @Override
    public Account findByAccount(String account) {
        Account a = mongoTemplate.findOne(Query.query(Criteria.where("account").is(account)), Account.class);
        return a;
    }
}
