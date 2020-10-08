package space.leo60228.potion;

import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_16_R2.potion.CraftPotionUtil;
import org.bukkit.craftbukkit.v1_16_R2.inventory.CraftItemStack;
import net.minecraft.server.v1_16_R2.Item;
import net.minecraft.server.v1_16_R2.PotionRegistry;
import net.minecraft.server.v1_16_R2.PotionBrewer;
import net.minecraft.server.v1_16_R2.Potions;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

public class Potion extends JavaPlugin {
    private static Potion instance;

    public static Potion getInstance() {
        return instance;
    }

    @Override
    public void onLoad() {
        instance = this;
    }

    /*static RecipeItemStack recipeToNMS(RecipeChoice bukkit, boolean requireNotEmpty) {
        RecipeItemStack stack;

        if (bukkit == null) {
            stack = RecipeItemStack.a;
        } else if (bukkit instanceof RecipeChoice.MaterialChoice) {
            stack = new RecipeItemStack(((RecipeChoice.MaterialChoice) bukkit).getChoices().stream().map((mat) -> new net.minecraft.server.v1_16_R2.RecipeItemStack.StackProvider(CraftItemStack.asNMSCopy(new ItemStack(mat)))));
        } else {
            throw new IllegalArgumentException("Unknown recipe stack instance " + bukkit);
        }

        stack.buildChoices();
        if (requireNotEmpty && stack.choices.length == 0) {
            throw new IllegalArgumentException("Recipe requires at least one non-air choice!");
        }

        return stack;
    }*/

    @Override
    public void onEnable() {
        try {
            /*Item dirt = CraftItemStack.asNMSCopy(new ItemStack(Material.DIRT)).getItem();
            Item cobble = CraftItemStack.asNMSCopy(new ItemStack(Material.COBBLESTONE)).getItem();
            RecipeChoice choice = new RecipeChoice.MaterialChoice(Material.GLASS_BOTTLE);
            RecipeItemStack recipeStack = recipeToNMS(choice, true);
            System.out.println("getDeclaredClasses");
            Class<?> predicatedCombination = PotionBrewer.class.getDeclaredClasses()[0];
            System.out.println("getDeclaredConstructors");
            Constructor<?> constructor = predicatedCombination.getDeclaredConstructors()[0];
            System.out.println("setAccessible");
            constructor.setAccessible(true);
            System.out.println("newInstance");
            Object recipe = constructor.newInstance(dirt, recipeStack, cobble);
            System.out.println("getDeclaredField");
            Field b = PotionBrewer.class.getDeclaredField("b");
            System.out.println("setAccessible");
            b.setAccessible(true);
            System.out.println("get");
            List<?> list = (List<?>) b.get(null);
            System.out.println("getMethod");
            Method add = list.getClass().getMethod("add", Object.class);
            System.out.println("invoke");
            add.invoke(list, recipe);*/
            Item cobble = CraftItemStack.asNMSCopy(new ItemStack(Material.COBBLESTONE)).getItem();
            Method addPotion = PotionBrewer.class.getDeclaredMethod("a", PotionRegistry.class, Item.class, PotionRegistry.class);
            addPotion.setAccessible(true);
            addPotion.invoke(null, Potions.WATER, cobble, Potions.LUCK);
        } catch (Exception e) {
            System.out.println(e);
            return;
        }
        getServer().getPluginManager().registerEvents(new Handler(), this);
    }
}
