/*
 * Author: Dabo Ross
 * Website: www.daboross.net
 * Email: daboross@daboross.net
 */
package net.daboross.bukkitdev.deathdemote;

import java.util.logging.Level;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author daboross
 */
public class DeathDemoteMain extends JavaPlugin implements Listener {

    private Permission permissionHandler;

    @Override
    public void onEnable() {
        PluginManager pm = getServer().getPluginManager();
        if (pm.isPluginEnabled("Vault")) {
            RegisteredServiceProvider<Permission> rsp = Bukkit.getServer().getServicesManager().getRegistration(Permission.class);
            permissionHandler = rsp.getProvider();
            if (permissionHandler == null) {
                getLogger().log(Level.INFO, "Vault found but Permission Handler not found! Can't enable!");
                pm.disablePlugin(this);
                return;
            } else {
                getLogger().log(Level.INFO, "Vault found. Permission found.");
            }
        } else {
            getLogger().log(Level.INFO, "Vault not found! Can't enable!");
            pm.disablePlugin(this);
            return;
        }
        pm.registerEvents(this, this);

    }

    @Override
    public void onDisable() {
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent pde) {
        for (String group : permissionHandler.getPlayerGroups((String) null, pde.getEntity().getName())) {
            if (group != null) {
                permissionHandler.playerRemoveGroup((String) null, pde.getEntity().getName(), group);
            }
        }
    }
}
