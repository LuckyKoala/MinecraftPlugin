package tech.zuosi.minecraft.randomoverride;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Core extends JavaPlugin {

    @Override
    public void onEnable() {
        super.onEnable();
        this.saveDefaultConfig();
        overrideFile();
    }

    private void overrideFile() {
        Path fromDir = Paths.get(getConfig().getString("fromDir"));
        Path toFile = getDataFolder().toPath().getParent()
                .resolve(getConfig().getString("pluginName"))
                .resolve(getConfig().getString("relativePath"));
        if(Files.exists(fromDir) && Files.exists(toFile)) {
            if (Files.isDirectory(fromDir)) {
                try {
                    List<Path> fileList = Files.list(fromDir).collect(Collectors.toList());
                    int size = fileList.size();
                    if(size==0) {
                        getLogger().warning("fromDir中没有文件");
                        return;
                    }

                    int randomIndex = new Random().nextInt(fileList.size());
                    Path selectedFile = fileList.get(randomIndex);
                    Files.copy(selectedFile, toFile, StandardCopyOption.REPLACE_EXISTING);
                    getLogger().info("修改成功!");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                getLogger().warning("fromDir 不是有效的目录");
            }
        } else {
            getLogger().warning("fromDir或toFile不是有效的路径，或指定路径的文件不存在");
        }
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }
}
