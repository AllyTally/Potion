package space.leo60228.potion;

import org.bukkit.ChatColor;
import org.bukkit.Color;
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
        PotionRecipe potion = new PotionRecipe(Material.DIAMOND, Potions.WATER, outputPotion);
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
        PotionRecipe potion2 = new PotionRecipe(Material.SLIME_BALL, Potions.WATER, outputPotion2);
        potion2.setSplashName("Splash Potion of SEX");
        potion2.setLingeringName("Lingering Potion of SEX");
        Recipes.add(potion2);
    }

    @Override
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onEnable() {
        loadRecipes();
        for (PotionRecipe Recipe : Recipes) {
            try {
                Item inputItem = CraftItemStack.asNMSCopy(new ItemStack(Recipe.inputItem)).getItem();
                Method addPotion = PotionBrewer.class.getDeclaredMethod("a", PotionRegistry.class, Item.class,
                        PotionRegistry.class);
                addPotion.setAccessible(true);
                addPotion.invoke(null, Recipe.inputPotions, inputItem, Potions.EMPTY);
            } catch (Exception e) {
                System.out.println(e);
                return;
            }
        }
        getServer().getPluginManager().registerEvents(new Handler(), this);
    }
}
