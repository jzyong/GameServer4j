package org.mmo.game.db.repository;

import java.util.List;
import java.util.Optional;

import org.mmo.game.db.struct.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
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
	private MongoTemplate mongoTemplate;

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
		mongoTemplate.save(entity);
		return entity;
	}

	@Override
	public Optional<Player> findById(Long id) {
		return null;
	}
	
	public Player findPlayerById(Long id) {
		return mongoTemplate.findById(id, Player.class);
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
	public Player findByName(String name) {
		// TODO Auto-generated method stub
		return null;
	}

}
