package tech.zuosi.koalaattribute.data;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import tech.zuosi.koalaattribute.KoalaAttribute;
import tech.zuosi.koalaattribute.attribute.Attribute;
import tech.zuosi.koalaattribute.attribute.CustomAttribute;
import tech.zuosi.koalaattribute.tool.HealthAndSpeed;

import java.util.*;

/**
 * Created by iwar on 2016/8/11.
 */
public class AttributeFile {
    private KoalaAttribute instance;

    public AttributeFile(KoalaAttribute plugin) {
        this.instance = plugin;
    }

    public void writeAll() {
        OfflinePlayer[] offlinePlayers = Bukkit.getOfflinePlayers();
        for (OfflinePlayer offlinePlayer : offlinePlayers) {
            write(offlinePlayer.getName());
        }
    }

    public void readAll() {
        OfflinePlayer[] offlinePlayers = Bukkit.getOfflinePlayers();

        loadCustomAttribute();
        for (OfflinePlayer offlinePlayer : offlinePlayers) {
            String name = offlinePlayer.getName();
            AttributeCache.INSTANCE.setAttribute(name,read(name));
        }
    }

    private void write(String playerName) {
        FileManager file = new FileManager(instance,playerName+".data");
        FileConfiguration configuration = file.getConfig();
        configuration.set("extraHealth", HealthAndSpeed.safeGetExtraHealth(playerName));
        configuration.set("availPoint", AttributeCache.INSTANCE.getAttribute(playerName).getAvailablePoint());
        configuration.set("attribute", AttributeCache.INSTANCE.getAttribute(playerName).saveToMap());
        file.saveConfig();
    }

    private Attribute read(String playerName) {
        FileManager file = new FileManager(instance,playerName+".data");
        if (!file.exists()) return null;
        FileConfiguration configuration = file.getConfig();
        Attribute attribute = new Attribute();
        Map<String,Integer> pointMap = new HashMap<>();
        ConfigurationSection cs = configuration.getConfigurationSection("attribute");

        HealthAndSpeed.putExtraHealth(playerName,configuration.getDouble("extraHealth",0D));
        attribute.setAvailablePoint(configuration.getInt("availPoint"));
        for (String CA : cs.getKeys(false)) {
            pointMap.put(CA,cs.getInt(CA));
        }

        attribute.build(pointMap);
        attribute.updateCustomAttribute();
        //file.delete();

        return attribute;
    }

    private void loadCustomAttribute() {
        final ConfigurationSection section = instance.getConfig().getConfigurationSection("Custom");
        Iterator<String> iterator = section.getKeys(false).iterator();
        ConfigurationSection attributeSection;
        List<CustomAttribute> customAttributes = new ArrayList<>();
        while (iterator.hasNext()) {
            attributeSection = section.getConfigurationSection(iterator.next());
            CustomAttribute customAttribute = new CustomAttribute(attributeSection.getString("name"));
            customAttribute.setDescription(attributeSection.getString("description"));
            attributeSection = attributeSection.getConfigurationSection("attribute");
            for (String attributeName : attributeSection.getKeys(false)) {
                double percent = attributeSection.getDouble(attributeName);
                setCA(customAttribute, attributeName, percent);
            }
            customAttributes.add(customAttribute);
        }
        AttributeCache.INSTANCE.setCustomAttributeList(customAttributes);
    }

    private void setCA(CustomAttribute customAttribute,String attributeName,double percent) {
        switch (attributeName) {
            case "damage":
                customAttribute.setDamage(percent);
                break;
            case "dodge":
                customAttribute.setDodge(percent);
                break;
            case "speed":
                customAttribute.setSpeed(percent);
                break;
            case "exp":
                customAttribute.setExp(percent);
                break;
            case "critical":
                customAttribute.setCritical(percent);
                break;
            case "health":
                customAttribute.setHealth(percent);
                break;
            case "armor":
                customAttribute.setArmor(percent);
                break;
            case "blood":
                customAttribute.setBlood(percent);
                break;
        }
    }
}
