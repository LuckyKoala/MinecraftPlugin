package net.myplugin.visualintensify.util;

import net.myplugin.visualintensify.VisualIntensify;

import java.util.Random;

/**
 * Created by iwar on 2015/12/18.
 */
public class Probability {
    private VisualIntensify plugin;
    private Random random;

    public Probability(VisualIntensify plugin){
        this.plugin = plugin;
        this.random = new Random();
    }

    public boolean getResult(String path) {
        if (plugin.getConfig().getInt(path) > 100) return true;
        return random.nextInt(100) <= plugin.getConfig().getInt(path);
    }

    public boolean getResult(Integer chance) {
        if (chance > 100) return true;
        return random.nextInt(100) <= chance;
    }
}
