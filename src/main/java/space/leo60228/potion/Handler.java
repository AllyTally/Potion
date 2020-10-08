package space.leo60228.potion;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.Material;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.BrewerInventory;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.entity.Player;
import org.bukkit.block.Block;

public class Handler implements Listener {
    @EventHandler
    public void fixInventory(InventoryClickEvent e) {
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

    @EventHandler
    public void brewPotion(BrewEvent e) {
        BrewerInventory inv = e.getContents();
        if (inv.getIngredient().getType() == Material.COBBLESTONE) {
            e.setCancelled(true);
            inv.getIngredient().setAmount(inv.getIngredient().getAmount() - 1);
            for (int i = 0; i < 3; i++) {
                ItemStack input = inv.getItem(i);
                if (input == null || input.getType() == Material.AIR)
                    continue;
                ItemStack output = new ItemStack(Material.POTION);
                PotionMeta outputMeta = (PotionMeta) output.getItemMeta();
                outputMeta.setColor(Color.YELLOW);
                PotionEffect effect = new PotionEffect(PotionEffectType.FAST_DIGGING, 3600, 0);
                outputMeta.addCustomEffect(effect, true);
                outputMeta.setDisplayName(ChatColor.RESET + "Potion of Haste");
                output.setItemMeta(outputMeta);
                inv.setItem(i, output);
            }
        }
    }
}
