package org.jzy.game.api.db.config.repository;

import org.bson.types.ObjectId;
import org.jzy.game.api.db.config.MongodbConfigService;
import org.jzy.game.api.db.config.enity.ShopItemCfg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 商城物品
 *
 * @author jzy
 */
@Repository
public class ShopItemCfgRepository implements CrudRepository<ShopItemCfg, ObjectId> {

    @Autowired
    MongodbConfigService mongodbConfigService;

    @Override
    public <S extends ShopItemCfg> S save(S entity) {
        return null;
    }

    @Override
    public <S extends ShopItemCfg> Iterable<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<ShopItemCfg> findById(ObjectId integer) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(ObjectId integer) {
        return false;
    }

    @Override
    public Iterable<ShopItemCfg> findAll() {
        List<ShopItemCfg> list = mongodbConfigService.getMongoOperations().findAll(ShopItemCfg.class);
        return list;
    }

    @Override
    public Iterable<ShopItemCfg> findAllById(Iterable<ObjectId> integers) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(ObjectId integer) {

    }

    @Override
    public void delete(ShopItemCfg entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends ObjectId> integers) {

    }

    @Override
    public void deleteAll(Iterable<? extends ShopItemCfg> entities) {

    }

    @Override
    public void deleteAll() {

    }
}
