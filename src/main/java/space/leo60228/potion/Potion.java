package space.leo60228.potion;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_16_R2.inventory.CraftItemStack;
import net.minecraft.server.v1_16_R2.Item;
import net.minecraft.server.v1_16_R2.PotionRegistry;
import net.minecraft.server.v1_16_R2.PotionBrewer;
import net.minecraft.server.v1_16_R2.Potions;
import java.lang.reflect.Method;

public class Potion extends JavaPlugin {
    private static Potion instance;

    public static Potion getInstance() {
        return instance;
    }

    @Override
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onEnable() {
        try {
            Item cobble = CraftItemStack.asNMSCopy(new ItemStack(Material.COBBLESTONE)).getItem();
            Method addPotion = PotionBrewer.class.getDeclaredMethod("a", PotionRegistry.class, Item.class,
                    PotionRegistry.class);
            addPotion.setAccessible(true);
            addPotion.invoke(null, Potions.WATER, cobble, Potions.LUCK);
        } catch (Exception e) {
            System.out.println(e);
            return;
        }
        getServer().getPluginManager().registerEvents(new Handler(), this);
    }
}
