package space.leo60228.potion;

import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;
import net.minecraft.server.v1_16_R2.PotionRegistry;

public class PotionRecipe implements java.io.Serializable {
    public boolean canUpgrade;
    public boolean canExtend;
    public boolean canSplash;
    public boolean canLingering;
    public Material inputItem;
    public String inputPotions;
    public ItemStack outputPotion;
    public String splashName;
    public String lingeringName;
    public String id;
    public int extendedTime;
    public int upgradeAmount;

    public PotionRecipe(String id, Material inputItem, String inputPotions, ItemStack outputPotion, boolean canExtend, boolean canUpgrade, boolean canSplash, boolean canLingering, int extendedTime, int upgradeAmount, String splashName, String lingeringName) {
        this.canExtend = canExtend;
        this.canUpgrade = canUpgrade;
        this.canSplash = canSplash;
        this.canLingering = canLingering;
        this.extendedTime = extendedTime;
        this.upgradeAmount = upgradeAmount;
        this.id = id;
        this.inputItem = inputItem;
        this.inputPotions = inputPotions;
        this.outputPotion = outputPotion;
        this.splashName = splashName;
        this.lingeringName = lingeringName;
    }

    public PotionRecipe(String id, Material inputItem, String inputPotions, ItemStack outputPotion) {
        canExtend = true;
        canUpgrade = true;
        canSplash = true;
        canLingering = true;
        extendedTime = 8 * 60 * 20;
        upgradeAmount = 1;
        this.id = id;
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
    public void setUpgradeAmount(int upgradeAmount) { this.upgradeAmount = upgradeAmount; }
    public void setExtendedTime(int extendedTime) { this.extendedTime = extendedTime; }
}
