package org.jzy.game.hall.struct;

import com.jzy.javalib.network.io.handler.TcpHandler;
import org.jzy.game.hall.db.struct.Player;
import org.jzy.game.hall.service.HallManager;

/**
 * 游戏Handler
 * @author jzy
 */
public abstract class HallHandler extends TcpHandler {

    public Player getPlayer(){
        return HallManager.getInstance().getPlayerService().getPlayer(id);
    }


}
