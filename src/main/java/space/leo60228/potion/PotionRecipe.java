package space.leo60228.potion;

import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;
import net.minecraft.server.v1_16_R2.PotionRegistry;

public class PotionRecipe {
    public boolean canUpgrade;
    public boolean canExtend;
    public boolean canSplash;
    public boolean canLingering;
    public Material inputItem;
    public PotionRegistry inputPotions;
    public ItemStack outputPotion;
    String splashName;
    String lingeringName;

    public PotionRecipe(Material inputItem, PotionRegistry inputPotions, ItemStack outputPotion) {
        canExtend = true;
        canUpgrade = true;
        canSplash = true;
        canLingering = true;
        this.inputItem = inputItem;
        this.inputPotions = inputPotions;
        this.outputPotion = outputPotion;
    }

    public void setCanExtend (boolean canExtend)  { this.canExtend  = canExtend;  }
    public void setCanUpgrade(boolean canUpgrade) { this.canUpgrade = canUpgrade; }
    public void setCanSplash (boolean canSplash)  {
        this.canSplash  = canSplash;
        if (!canSplash) canLingering = false;
    }
    public void setCanLingering(boolean canLingering)  {
        this.canLingering  = canLingering;
        if (canLingering) canSplash = true;
    }
    public void setSplashName(String splashName) { this.splashName = splashName; }
    public void setLingeringName(String lingeringName) { this.lingeringName = lingeringName; }
}
