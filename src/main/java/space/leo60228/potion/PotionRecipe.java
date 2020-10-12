package space.leo60228.potion;

import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;
import net.minecraft.server.v1_16_R2.PotionRegistry;

import java.util.HashMap;
import java.util.Map;

public class PotionRecipe implements org.bukkit.configuration.serialization.ConfigurationSerializable {
    public boolean canUpgrade;
    public boolean canExtend;
    public boolean canSplash;
    public boolean canLingering;
    public Material inputItem;
    public String inputPotions;
    public ItemStack outputPotion;
    public String potionName;
    public String splashName;
    public String lingeringName;
    public String id;
    public int extendedTime;
    public int upgradeAmount;

    public PotionRecipe(String id, String potionName, Material inputItem, String inputPotions, ItemStack outputPotion) {
        canExtend = true;
        canUpgrade = true;
        canSplash = true;
        canLingering = true;
        extendedTime = 8 * 60 * 20;
        upgradeAmount = 1;
        this.potionName = potionName;
        this.id = id;
        this.inputItem = inputItem;
        this.inputPotions = inputPotions;
        this.outputPotion = outputPotion;
    }

    public PotionRecipe(Map<String, Object> map) {
        this.canExtend     = (boolean) map.get("canExtend");
        this.canUpgrade    = (boolean) map.get("canUpgrade");
        this.canSplash     = (boolean) map.get("canSplash");
        this.canLingering  = (boolean) map.get("canLingering");
        this.extendedTime  = (int) map.get("extendedTime");
        this.upgradeAmount = (int) map.get("upgradeAmount");
        this.id            = (String) map.get("id");
        this.inputItem     = Material.getMaterial((String) map.get("inputItem"));
        this.inputPotions  = (String) map.get("inputPotions");
        this.outputPotion  = (ItemStack) map.get("outputPotion");
        this.potionName    = (String) map.get("potionName");
        this.splashName    = (String) map.get("splashName");
        this.lingeringName = (String) map.get("lingeringName");
    }

    public Map<String,Object> serialize() {
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("canExtend", canExtend);
        map.put("canUpgrade", canUpgrade);
        map.put("canSplash", canSplash);
        map.put("canLingering", canLingering);
        map.put("extendedTime", extendedTime);
        map.put("upgradeAmount", upgradeAmount);
        map.put("id", id);
        map.put("inputItem", inputItem.name());
        map.put("inputPotions", inputPotions);
        map.put("outputPotion", outputPotion);
        map.put("potionName", potionName);
        map.put("splashName", splashName);
        map.put("lingeringName", lingeringName);
        return map;
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
