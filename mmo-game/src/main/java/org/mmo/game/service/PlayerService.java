package org.mmo.game.service;

import org.mmo.game.db.repository.IPlayerRepository;
import org.mmo.game.db.struct.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 玩家
 *
 * @author jzy
 */
@Service
public class PlayerService {
    @Autowired
    IPlayerRepository playerRepository;

    /**
     * 在线玩家
     */
    private final Map<Long, Player> onlinePlayers = new ConcurrentHashMap<>();

    public Map<Long, Player> getOnlinePlayers() {
        return onlinePlayers;
    }


    /**
     * @param id
     * @param loadFromDb 从数据库中获取
     * @return
     */
    public Player getPlayer(long id, boolean loadFromDb) {
        Player player = onlinePlayers.get(id);
        if (player == null && loadFromDb) {
            Optional<Player> optionalPlayer = playerRepository.findById(id);
            if (!optionalPlayer.isPresent()) {
                return null;
            }
            return optionalPlayer.get();
        }
        return player;
    }

    public Player getPlayer(long id) {
        return getPlayer(id, false);
    }
}
