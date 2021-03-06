package space.leo60228.potion;

import net.minecraft.server.v1_16_R2.PotionBrewer;
import net.minecraft.server.v1_16_R2.Potions;
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
                    ItemStack copyOutput = Recipe.outputPotion.clone();
                    ItemMeta outputMeta = copyOutput.getItemMeta();
                    if (input.getType() == Material.SPLASH_POTION) {
                        copyOutput.setType(Material.SPLASH_POTION);
                        if (Recipe.splashName != null) {
                            outputMeta.setDisplayName(ChatColor.RESET + Recipe.splashName);
                        }
                    } else if (input.getType() == Material.LINGERING_POTION) {
                        copyOutput.setType(Material.LINGERING_POTION);
                        if (Recipe.lingeringName != null) {
                            outputMeta.setDisplayName(ChatColor.RESET + Recipe.lingeringName);
                        }
                    } else {
                        outputMeta.setDisplayName(ChatColor.RESET + Recipe.potionName);
                    }
                    PersistentDataContainer container = outputMeta.getPersistentDataContainer();
                    NamespacedKey key = new NamespacedKey(Potion.getInstance(), "customPotionId");
                    container.set(key, PersistentDataType.STRING,Recipe.id);
                    NamespacedKey key2 = new NamespacedKey(Potion.getInstance(), "upgraded");
                    container.set(key2, PersistentDataType.INTEGER,0);
                    NamespacedKey key3 = new NamespacedKey(Potion.getInstance(), "extended");
                    container.set(key3, PersistentDataType.INTEGER,0);
                    copyOutput.setItemMeta(outputMeta);
                    inv.setItem(i, copyOutput);
                }
            }
        }
        if (inv.getIngredient() == null) return;

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
            e.setCancelled(true); // Don't bother letting the game handle potions
            for (int i = 0; i < 3; i++) {
                ItemStack input = inv.getItem(i);
                boolean customPotion = false;
                if (input == null || input.getType() == Material.AIR) continue;
                ItemMeta inputMeta = input.getItemMeta();
                PersistentDataContainer container = inputMeta.getPersistentDataContainer();
                NamespacedKey key = new NamespacedKey(Potion.getInstance(), "customPotionId");
                NamespacedKey key2 = new NamespacedKey(Potion.getInstance(), "modified");
                for (PotionRecipe Recipe : Recipes) {
                    //if (input.equals(Recipe.outputPotion)) { // This is the result of a custom potion.
                    //    System.out.println("handling custom potion");
                    if (container.has(key, PersistentDataType.STRING) && container.get(key, PersistentDataType.STRING).equals(Recipe.id)) {// This is the result of a custom potion.
                        System.out.println("handling custom potion");
                        customPotion = true;
                        if (ingredientType == Material.REDSTONE && !Recipe.canExtend) continue;
                        if (ingredientType == Material.GLOWSTONE_DUST && !Recipe.canUpgrade) continue;
                        if (ingredientType == Material.GUNPOWDER && !Recipe.canSplash) continue;
                        if (ingredientType == Material.DRAGON_BREATH && !Recipe.canLingering) continue;
                        if (ingredientType == Material.DRAGON_BREATH && input.getType() != Material.SPLASH_POTION)
                            continue;

                        if (ingredientType == Material.REDSTONE) {
                            if (!container.has(key2, PersistentDataType.INTEGER) || container.get(key2, PersistentDataType.INTEGER) == 0) {
                                ItemStack modifiedPotion = input.clone();
                                PotionMeta outputMeta = (PotionMeta) modifiedPotion.getItemMeta();
                                PotionMeta oldMeta = outputMeta.clone();
                                if (outputMeta.hasCustomEffects()) {
                                    outputMeta.clearCustomEffects();
                                    for (PotionEffect effect : oldMeta.getCustomEffects()) {
                                        PotionEffect newEffect = new PotionEffect(effect.getType(), Recipe.extendedTime, effect.getAmplifier());
                                        outputMeta.addCustomEffect(newEffect, true);
                                    }
                                }
                                PersistentDataContainer container2 = outputMeta.getPersistentDataContainer();
                                container2.set(key2, PersistentDataType.INTEGER,1);
                                modifiedPotion.setItemMeta(outputMeta);
                                inv.setItem(i, modifiedPotion);
                            }
                        }

                        if (ingredientType == Material.GLOWSTONE_DUST) {
                            if (!container.has(key2, PersistentDataType.INTEGER) || container.get(key2, PersistentDataType.INTEGER) == 0) {
                                ItemStack modifiedPotion = input.clone();
                                PotionMeta outputMeta = (PotionMeta) modifiedPotion.getItemMeta();
                                PotionMeta oldMeta = outputMeta.clone();
                                if (outputMeta.hasCustomEffects()) {
                                    outputMeta.clearCustomEffects();
                                    for (PotionEffect effect : oldMeta.getCustomEffects()) {
                                        PotionEffect newEffect = new PotionEffect(effect.getType(), effect.getDuration(), effect.getAmplifier() + Recipe.upgradeAmount);
                                        outputMeta.addCustomEffect(newEffect, true);
                                    }
                                }
                                PersistentDataContainer container2 = outputMeta.getPersistentDataContainer();
                                container2.set(key2, PersistentDataType.INTEGER,1);
                                modifiedPotion.setItemMeta(outputMeta);
                                inv.setItem(i, modifiedPotion);
                            }
                        }

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

                    // ORIGINAL:
                    /*
                    for (int i = 0; i < 3; ++i) {
                        this.items.set(i, PotionBrewer.d(itemstack, (ItemStack) this.items.get(i)));
                    }*/
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
            block.getWorld().playEffect(block.getLocation(), Effect.BREWING_STAND_BREW, null);
            inv.getIngredient().setAmount(inv.getIngredient().getAmount() - 1);
        }
    }
}
