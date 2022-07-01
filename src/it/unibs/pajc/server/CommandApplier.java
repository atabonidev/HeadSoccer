package it.unibs.pajc.server;

import it.unibs.pajc.client.PlayerKeyboardListener;
import it.unibs.pajc.model.Player;

import java.awt.event.KeyEvent;
import java.util.HashMap;

/**
 * Classe che identifica il protocollo di comunicazione fra client e server.
 * Mappa una serie di stringhe (comandi inviati dal client al server) ad azioni specifiche da attuare sul model
 */

public class CommandApplier {

    private Player player;
    private HashMap<String, Runnable> commandMap = new HashMap<>();

    public CommandApplier(Player player) {
        this.player = player;
        intializeCommandMap();
    }

    private void intializeCommandMap() {
        commandMap.put(""+ KeyEvent.VK_LEFT, () -> {
            this.player.move(false);
            this.player.startAnimation();
        });

        commandMap.put(""+KeyEvent.VK_RIGHT, () -> {
            this.player.move(true);
            this.player.startAnimation();
        });

        commandMap.put(""+KeyEvent.VK_UP, () -> {
            this.player.jump();
        });

        commandMap.put(""+KeyEvent.VK_SPACE, () -> {
            this.player.kick(true);
        });

        commandMap.put(""+ PlayerKeyboardListener.STOP_ANIMATION, () -> {
            this.player.setSpeed(0, 0);
            this.player.stopAnimation();
        });

        commandMap.put(""+ PlayerKeyboardListener.STOP_KICKING, () -> {
            this.player.stopKicking();
        });
    }

    public void applyCommands(String[] commands) {
        for (String command: commands) {
            commandMap.get(command).run();
        }
    }
}
