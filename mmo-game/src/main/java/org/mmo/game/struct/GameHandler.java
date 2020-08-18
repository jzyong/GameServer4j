package org.mmo.game.struct;

import org.mmo.engine.io.handler.TcpHandler;
import org.mmo.game.db.struct.Player;
import org.mmo.game.service.GameManager;

/**
 * 游戏Handler
 * @author jzy
 */
public abstract class GameHandler extends TcpHandler {

    public Player getPlayer(){
        return GameManager.getInstance().getPlayerService().getPlayer(pid);
    }


}
