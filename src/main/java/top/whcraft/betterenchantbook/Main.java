package top.whcraft.betterenchantbook;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import top.whcraft.betterenchantbook.Listener.PrepareAnvilListener;

public final class Main extends JavaPlugin {
    private static Main instance;

    CommandSender Console = Bukkit.getConsoleSender();

    @Override
    public void onLoad() {
        super.onLoad();
        Utils.Message(Console, "&r");
        Utils.Message(Console, "&fLoading &3Better&bEnchantBook&f... &7" + Bukkit.getBukkitVersion());
        Utils.Message(Console, "&r");
    }

    @Override
    public void onEnable() {
        super.onEnable();
        instance = this;
        Utils.Message(Console, "[BetterEnchantBook] &7Info &f插件正在加载...");
        this.saveDefaultConfig();
        Utils.Message(Console, "[BetterEnchantBook] &7Info &f配置文件保存成功");
        this.getServer().getPluginManager().registerEvents(new PrepareAnvilListener(), this);
        this.getLogger().info("[BetterEnchantBook] &7Info &f所有监听器已注册!");
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    public static Main getInstance() {
        return instance;
    }
}
