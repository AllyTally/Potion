package space.leo60228.potion;

import net.minecraft.server.v1_16_R2.PotionBrewer;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_16_R2.inventory.CraftItemStack;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
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

import java.util.ArrayList;

public class Handler implements Listener {
    @EventHandler
    public void fixInventory(InventoryClickEvent e) {
        Inventory inv = e.getClickedInventory();
        Player player = ((Player) e.getView().getPlayer());
        if (inv == null || player.getOpenInventory().getType() != InventoryType.BREWING) {
            return;
        }
        Bukkit.getScheduler().runTaskLater(Potion.getInstance(), player::updateInventory, 1L);
    }

    @EventHandler
    public void brewPotion(BrewEvent e) {
        BrewerInventory inv = e.getContents();
        Block block = e.getBlock();
        if (inv.getIngredient() == null) return;
        ArrayList<PotionRecipe> Recipes = Potion.getInstance().Recipes;
        for (PotionRecipe Recipe : Recipes) {
            if (inv.getIngredient().getType() == Recipe.inputItem) {
                e.setCancelled(true);
                block.getWorld().playEffect(block.getLocation(), Effect.BREWING_STAND_BREW, null);
                inv.getIngredient().setAmount(inv.getIngredient().getAmount() - 1);
                for (int i = 0; i < 3; i++) {
                    ItemStack input = inv.getItem(i);
                    if (input == null || input.getType() == Material.AIR)
                        continue;
                    /*ItemStack copyOutput = Recipe.outputPotion.clone();
                    ItemMeta outputMeta = copyOutput.getItemMeta();
                    PersistentDataContainer container = outputMeta.getPersistentDataContainer();
                    NamespacedKey key = new NamespacedKey(Potion.getInstance(), "customPotion");
                    container.set(key, PersistentDataType.INTEGER,1);
                    copyOutput.setItemMeta(outputMeta);
                    inv.setItem(i, copyOutput);*/
                    inv.setItem(i, Recipe.outputPotion);
                }
            }
        }

        /*ItemStack itemstack = inv.getItem(3);

        for (int i = 0; i < 3; ++i) {
            inv.setItem(i, CraftItemStack.asBukkitCopy(PotionBrewer.d(CraftItemStack.asNMSCopy(itemstack), CraftItemStack.asNMSCopy(inv.getItem(i)))));
        }

        e.setCancelled(true);*/


        // Handle redstone/glowstone/gunpowder/dragons breath modifiers
        Material ingredientType = inv.getIngredient().getType();
        if ((ingredientType == Material.REDSTONE) ||
            (ingredientType == Material.GLOWSTONE_DUST) ||
            (ingredientType == Material.GUNPOWDER) ||
            (ingredientType == Material.DRAGON_BREATH)) {
            block.getWorld().playEffect(block.getLocation(), Effect.BREWING_STAND_BREW, null);
            inv.getIngredient().setAmount(inv.getIngredient().getAmount() - 1);
            e.setCancelled(true); // Don't bother letting the game handle potions
            for (int i = 0; i < 3; i++) {
                ItemStack input = inv.getItem(i);
                boolean customPotion = false;
                if (input == null || input.getType() == Material.AIR) continue;
                ItemMeta inputMeta = input.getItemMeta();
                /*PersistentDataContainer container = inputMeta.getPersistentDataContainer();
                NamespacedKey key = new NamespacedKey(Potion.getInstance(), "customPotion");*/
                for (PotionRecipe Recipe : Recipes) {
                    if (input.getItemMeta() == Recipe.outputPotion.getItemMeta()) { // This is the result of a custom potion.
                    //if (container.has(key, PersistentDataType.INTEGER)) {
                        customPotion = true;
                        if (ingredientType == Material.REDSTONE && !Recipe.canExtend) continue;
                        if (ingredientType == Material.GLOWSTONE_DUST && !Recipe.canUpgrade) continue;
                        if (ingredientType == Material.GUNPOWDER && !Recipe.canSplash) continue;
                        if (ingredientType == Material.DRAGON_BREATH && !Recipe.canLingering) continue;
                        if (ingredientType == Material.DRAGON_BREATH && Recipe.outputPotion.getType() != Material.SPLASH_POTION)
                            continue;

                        if (ingredientType == Material.GUNPOWDER) {
                            ItemStack modifiedPotion = input.clone();
                            modifiedPotion.setType(Material.SPLASH_POTION);
                            if (Recipe.splashName == null) {
                                inv.setItem(i, modifiedPotion);
                                continue;
                            }
                            ItemMeta outputMeta = modifiedPotion.getItemMeta();
                            outputMeta.setDisplayName(ChatColor.RESET + Recipe.splashName);
                            modifiedPotion.setItemMeta(outputMeta);
                            modifiedPotion.setType(Material.SPLASH_POTION);
                            inv.setItem(i, modifiedPotion);
                        }
                        if (ingredientType == Material.DRAGON_BREATH) {
                            ItemStack modifiedPotion = input.clone();
                            modifiedPotion.setType(Material.LINGERING_POTION);
                            if (Recipe.lingeringName == null) {
                                inv.setItem(i, modifiedPotion);
                                continue;
                            }
                            ItemMeta outputMeta = modifiedPotion.getItemMeta();
                            outputMeta.setDisplayName(ChatColor.RESET + Recipe.lingeringName);
                            modifiedPotion.setItemMeta(outputMeta);
                            inv.setItem(i, modifiedPotion);
                        }
                    }
                }
                if (!customPotion) {
                    // This isn't a custom potion, so just let vanilla handle it.
                    System.out.println("letting vanilla handle");
                    inv.setItem(
                        i,
                        CraftItemStack.asBukkitCopy(
                            PotionBrewer.d(
                                CraftItemStack.asNMSCopy(
                                    inv.getIngredient()
                                ),
                                CraftItemStack.asNMSCopy(
                                    inv.getItem(i)
                                )
                            )
                        )
                    );
                }
            }
        }
    }
}
