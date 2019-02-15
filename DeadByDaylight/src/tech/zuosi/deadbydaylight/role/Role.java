package tech.zuosi.deadbydaylight.role;

import org.bukkit.entity.Player;

/**
 * Created by iwar on 2016/7/8.
 */
public class Role {
    private Player player;
    private boolean isVictim;
    private int speedAmplifier;

    public Role(Player player) {
        this.player = player;
        this.isVictim = true;
        this.speedAmplifier = 0;
    }
    public Role(Player player,boolean isVictim) {
        this.player = player;
        this.isVictim = isVictim;
        this.speedAmplifier = 0;
    }
    public Role(Player player,boolean isVictim,int speedAmplifier) {
        this.player = player;
        this.isVictim = isVictim;
        this.speedAmplifier = speedAmplifier;
    }

    public void echo(String message) {
        this.player.sendMessage(message);
    }

    public Player getPlayer() {
        return player;
    }
    public boolean isVictim() {
        return isVictim;
    }

    public int getSpeedAmplifier() {
        return speedAmplifier;
    }

    public boolean setSpeedAmplifier(int speedAmplifier) {
        if (speedAmplifier < 0 || speedAmplifier > 2) {
            speedAmplifier = 0;
            return false;
        }
        this.speedAmplifier = speedAmplifier;
        return true;
    }
}
