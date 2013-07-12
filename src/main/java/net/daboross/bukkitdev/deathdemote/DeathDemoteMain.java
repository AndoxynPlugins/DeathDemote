/*
 * Copyright (C) 2013 Dabo Ross <http://www.daboross.net/>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
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
