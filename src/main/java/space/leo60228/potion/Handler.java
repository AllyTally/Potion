package space.leo60228.potion;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.BrewerInventory;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.entity.Player;

public class Handler implements Listener {
    @EventHandler
    public void fixBrew(InventoryClickEvent e) {
        Inventory inv = e.getClickedInventory();
        if (inv == null || inv.getType() != InventoryType.BREWING) {
            return;
        }
        Bukkit.getScheduler().scheduleSyncDelayedTask(Potion.getInstance(), new Runnable() {
            public void run() {
                inv.setItem(3, inv.getItem(3)); // evil hack
                ((Player) e.getView().getPlayer()).updateInventory();
            }
        }, 1);
    }
}
