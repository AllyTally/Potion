package space.leo60228.potion;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_16_R2.inventory.CraftItemStack;
import net.minecraft.server.v1_16_R2.Item;
import net.minecraft.server.v1_16_R2.PotionRegistry;
import net.minecraft.server.v1_16_R2.PotionBrewer;
import net.minecraft.server.v1_16_R2.Potions;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class Potion extends JavaPlugin {
    private static Potion instance;

    public ArrayList<PotionRecipe> Recipes = new ArrayList<PotionRecipe>();

    public static Potion getInstance() {
        return instance;
    }

    public void loadRecipes() {
        ItemStack outputPotion = new ItemStack(Material.POTION);
        PotionMeta outputMeta = (PotionMeta) outputPotion.getItemMeta();
        outputMeta.setColor(Color.YELLOW);
        PotionEffect effect = new PotionEffect(PotionEffectType.FAST_DIGGING, 3600, 0);
        outputMeta.addCustomEffect(effect, true);
        outputMeta.setDisplayName(ChatColor.RESET + "Potion of Haste");
        outputPotion.setItemMeta(outputMeta);
        PotionRecipe potion = new PotionRecipe("haste_potion", Material.DIAMOND, "water", outputPotion);
        potion.setSplashName("Splash Potion of Haste");
        potion.setLingeringName("Lingering Potion of Haste");
        Recipes.add(potion);

        ItemStack outputPotion2 = new ItemStack(Material.POTION);
        PotionMeta outputMeta2 = (PotionMeta) outputPotion2.getItemMeta();
        outputMeta2.setColor(Color.RED);
        PotionEffect effect2 = new PotionEffect(PotionEffectType.JUMP, 3600, 126);
        outputMeta2.addCustomEffect(effect2, true);
        outputMeta2.setDisplayName(ChatColor.RESET + "Potion of SEX");
        outputPotion2.setItemMeta(outputMeta2);
        PotionRecipe potion2 = new PotionRecipe("sex_potion", Material.SLIME_BALL, "water", outputPotion2);
        potion2.setSplashName("Splash Potion of SEX");
        potion2.setLingeringName("Lingering Potion of SEX");
        Recipes.add(potion2);
    }

    /*public void loadRecipes() {
        File file = new File(getDataFolder(), "potions.yml");
        if (file.exists()) {
            Configuration config = YamlConfiguration.loadConfiguration(file);
            Recipes = (ArrayList<PotionRecipe>) config.getList("potions");
        }
    }*/

    public void saveRecipes() throws IOException {
        File file = new File(getDataFolder(), "potions.yml");
        YamlConfiguration config = new YamlConfiguration();
        config.set("potions", Recipes);
        config.save(file);
    }

    public void registerPotion(PotionRecipe Recipe) {
        try {
            Item inputItem = CraftItemStack.asNMSCopy(new ItemStack(Recipe.inputItem)).getItem();
            Method addPotion = PotionBrewer.class.getDeclaredMethod("a", PotionRegistry.class, Item.class,
                    PotionRegistry.class);
            addPotion.setAccessible(true);
            addPotion.invoke(null, PotionRegistry.a(Recipe.inputPotions), inputItem, Potions.EMPTY);
        } catch (Exception e) {
            System.out.println(e);
            return;
        }
    }

    @Override
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onEnable() {
        // Fill the Recipes list
        loadRecipes();

        // Add redstone and glowstone recipes. Note that gunpowder and dragons breath recipes already
        // exist by default, so they don't need to be added.
        try {
            Item inputItem1 = CraftItemStack.asNMSCopy(new ItemStack(Material.REDSTONE)).getItem();
            Item inputItem2 = CraftItemStack.asNMSCopy(new ItemStack(Material.GLOWSTONE_DUST)).getItem();
            Method addPotion = PotionBrewer.class.getDeclaredMethod("a", PotionRegistry.class, Item.class,
                    PotionRegistry.class);
            addPotion.setAccessible(true);
            addPotion.invoke(null, Potions.EMPTY, inputItem1, Potions.EMPTY);
            addPotion.invoke(null, Potions.EMPTY, inputItem2, Potions.EMPTY);
        } catch (Exception e) {
            System.out.println(e);
            return;
        }

        // Add all recipes in the Recipes list
        for (PotionRecipe Recipe : Recipes) {
            registerPotion(Recipe);
        }
        getServer().getPluginManager().registerEvents(new Handler(), this);
    }

    @Override
    public void onDisable() {
        System.out.println("[Potion] attempting to save potions...");
        try {
            saveRecipes();
            System.out.println("[Potion] done!");
        } catch (Exception e) {
            System.out.println("[Potion] failed to save potions!");
            e.printStackTrace();
            System.out.println(e);
            return;
        }

    }
}
