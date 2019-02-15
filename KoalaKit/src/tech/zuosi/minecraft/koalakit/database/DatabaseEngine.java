package tech.zuosi.minecraft.koalakit.database;

public interface DatabaseEngine {
    boolean init(String host, String username, String password, String database, int port);
    boolean compareAndInc(String username, String kitname, int limit, int period);
}
