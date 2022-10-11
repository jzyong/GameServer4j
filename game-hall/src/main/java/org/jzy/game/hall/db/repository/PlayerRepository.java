package org.jzy.game.hall.db.repository;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import org.jzy.game.hall.db.struct.Player;
import org.jzy.game.hall.db.MongoGameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.stereotype.Repository;

/**
 * 数据操作
 *
 * @author JiangZhiYong
 * @mail 359135103@qq.com
 */
@Repository
public class PlayerRepository implements IPlayerRepository {

    @Autowired
    private MongoGameService mongoGameService;

    @Override
    public <S extends Player> List<S> saveAll(Iterable<S> entites) {
        return null;
    }

    @Override
    public List<Player> findAll() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Player> findAll(Sort sort) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <S extends Player> S insert(S entity) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <S extends Player> List<S> insert(Iterable<S> entities) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <S extends Player> List<S> findAll(Example<S> example) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <S extends Player> List<S> findAll(Example<S> example, Sort sort) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Page<Player> findAll(Pageable pageable) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <S extends Player> S save(S entity) {
        mongoGameService.getMongoOperations().save(entity);
        return entity;
    }

    @Override
    public Optional<Player> findById(Long id) {
        Player player =mongoGameService.getMongoOperations().findById(id, Player.class);
        if (player == null) {
            return Optional.empty();
        }
        return Optional.of(player);
    }


    @Override
    public boolean existsById(Long id) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Iterable<Player> findAllById(Iterable<Long> ids) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long count() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void deleteById(Long id) {
        // TODO Auto-generated method stub

    }

    @Override
    public void delete(Player entity) {
        // TODO Auto-generated method stub

    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {

    }

    @Override
    public void deleteAll(Iterable<? extends Player> entities) {
        // TODO Auto-generated method stub

    }

    @Override
    public void deleteAll() {
        // TODO Auto-generated method stub

    }

    @Override
    public <S extends Player> Optional<S> findOne(Example<S> example) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <S extends Player> Page<S> findAll(Example<S> example, Pageable pageable) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <S extends Player> long count(Example<S> example) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public <S extends Player> boolean exists(Example<S> example) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public <S extends Player, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }

    @Override
    public Player findByName(String name) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Player> findByUserId(long userId) {
        List<Player> players = mongoGameService.getMongoOperations().find(Query.query(Criteria.where("userId").is(userId)), Player.class);
        return players;
    }



}
