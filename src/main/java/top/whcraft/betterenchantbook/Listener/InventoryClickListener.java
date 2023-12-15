package top.whcraft.betterenchantbook.Listener;

import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;

public class InventoryClickListener implements Listener {
    public InventoryClickListener() {
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent evt) {
        if (evt.getInventory().getType() == InventoryType.ANVIL) {
            if (evt.getSlotType() == SlotType.RESULT) {
                if (Objects.requireNonNull(evt.getCurrentItem()).getType() == Material.ENCHANTED_BOOK) {
                    boolean unsafeEnchants = false;

                    for (Map.Entry<Enchantment, Integer> enchantmentIntegerEntry : ((EnchantmentStorageMeta) Objects.requireNonNull(evt.getCurrentItem().getItemMeta())).getStoredEnchants().entrySet()) {
                        Map.Entry enchantment = (Map.Entry) enchantmentIntegerEntry;
                        if (((Enchantment) enchantment.getKey()).getMaxLevel() < (Integer) enchantment.getValue()) {
                            unsafeEnchants = true;
                            break;
                        }
                    }
                }
            }
        }
    }
}
