package org.game.api.db.config.repository;

import org.game.api.db.config.MongodbConfigService;
import org.game.api.db.config.enity.CShopItem;
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
public class CShopItemRepository implements CrudRepository<CShopItem, Integer> {

    @Autowired
    MongodbConfigService mongodbConfigService;

    @Override
    public <S extends CShopItem> S save(S entity) {
        return null;
    }

    @Override
    public <S extends CShopItem> Iterable<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<CShopItem> findById(Integer integer) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(Integer integer) {
        return false;
    }

    @Override
    public Iterable<CShopItem> findAll() {
        List<CShopItem> list = mongodbConfigService.getMongoOperations().findAll(CShopItem.class);
        return list;
    }

    @Override
    public Iterable<CShopItem> findAllById(Iterable<Integer> integers) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(Integer integer) {

    }

    @Override
    public void delete(CShopItem entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends Integer> integers) {

    }

    @Override
    public void deleteAll(Iterable<? extends CShopItem> entities) {

    }

    @Override
    public void deleteAll() {

    }
}
