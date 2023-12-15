package top.whcraft.betterenchantbook.Listener;

import java.util.Collections;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import top.whcraft.betterenchantbook.Main;

public class PrepareAnvilListener implements Listener {
    public PrepareAnvilListener() {
    }

    @EventHandler
    public void onPrepareAnvil(PrepareAnvilEvent evt) {
        FileConfiguration config = Main.getInstance().getConfig();
        ItemStack itemFirst = evt.getInventory().getItem(0);
        ItemStack itemPlus = evt.getInventory().getItem(1);
        if (itemFirst != null && itemPlus != null) {
            if (itemFirst.getType() == Material.ENCHANTED_BOOK && itemPlus.getType() == Material.ENCHANTED_BOOK) {
                Map<Enchantment, Integer> itemFirstEnchantments = ((EnchantmentStorageMeta) itemFirst.getItemMeta()).getStoredEnchants();
                Map<Enchantment, Integer> itemPlusEnchantments = ((EnchantmentStorageMeta) itemPlus.getItemMeta()).getStoredEnchants();
                if (itemFirstEnchantments.size() == 0) {
                    itemFirstEnchantments = itemFirst.getEnchantments();
                }

                if (itemPlusEnchantments.size() == 0) {
                    itemPlusEnchantments = itemPlus.getEnchantments();
                }

                if (itemFirstEnchantments.size() == 1 && itemPlusEnchantments.size() == 1 && itemFirstEnchantments.keySet().toArray()[0] != itemPlusEnchantments.keySet().toArray()[0]) {
                    return;
                }

                double expCost = 0.0;
                Map<Enchantment, Integer> totalEnchantment = new HashMap();
                Iterator var10 = itemFirstEnchantments.keySet().iterator();

                Enchantment enchantmentFirst;
                label106:
                while (var10.hasNext()) {
                    enchantmentFirst = (Enchantment) var10.next();
                    Iterator var12 = itemPlusEnchantments.keySet().iterator();

                    while (true) {
                        int nextLv;
                        int maxLv;
                        do {
                            Enchantment enchantmentPlus;
                            do {
                                do {
                                    do {
                                        if (!var12.hasNext()) {
                                            continue label106;
                                        }

                                        enchantmentPlus = (Enchantment) var12.next();
                                    } while (enchantmentFirst != enchantmentPlus);
                                } while (config.getStringList("banEnchants").contains(enchantmentFirst.getName()));
                            } while (!((Integer) itemFirstEnchantments.get(enchantmentFirst)).equals(itemPlusEnchantments.get(enchantmentPlus)));

                            nextLv = (Integer) itemFirstEnchantments.get(enchantmentFirst) + 1;
                            maxLv = config.getInt("maxLevelLimit." + enchantmentFirst.getName());
                        } while (maxLv > 0 && nextLv > maxLv);

                        totalEnchantment.put(enchantmentFirst, nextLv);
                        expCost += (double) ((Integer) itemFirstEnchantments.get(enchantmentFirst) + 1) * config.getDouble("expMultiplier");
                    }
                }

                var10 = itemFirstEnchantments.keySet().iterator();

                while (var10.hasNext()) {
                    enchantmentFirst = (Enchantment) var10.next();
                    if (totalEnchantment.get(enchantmentFirst) == null) {
                        totalEnchantment.put(enchantmentFirst, itemFirstEnchantments.get(enchantmentFirst));
                    }
                }

                var10 = itemPlusEnchantments.keySet().iterator();

                while (var10.hasNext()) {
                    enchantmentFirst = (Enchantment) var10.next();
                    if (totalEnchantment.get(enchantmentFirst) == null) {
                        totalEnchantment.put(enchantmentFirst, itemPlusEnchantments.get(enchantmentFirst));
                    }
                }

                evt.getInventory().setRepairCost((int) ((double) evt.getInventory().getRepairCost() + expCost));
                ItemStack result = new ItemStack(Material.ENCHANTED_BOOK, 1);
                EnchantmentStorageMeta resultMeta = (EnchantmentStorageMeta) result.getItemMeta();
                totalEnchantment.forEach((enchantment, integer) -> {
                    resultMeta.addStoredEnchant(enchantment, integer, true);
                });
                if (!config.getString("bookLore").equals("")) {
                    resultMeta.setLore(Collections.singletonList(config.getString("bookLore")));
                }

                result.setItemMeta(resultMeta);
                evt.setResult(result);
            }

            if (itemFirst.getType() != Material.ENCHANTED_BOOK && itemPlus.getType() == Material.ENCHANTED_BOOK) {
                AtomicBoolean hasNBLevel = new AtomicBoolean(false);
                AtomicReference<Double> expCost = new AtomicReference(0.0);
                Map<Enchantment, Integer> itemPlusEnchantments = ((EnchantmentStorageMeta) itemPlus.getItemMeta()).getStoredEnchants();
                itemPlusEnchantments.forEach((enchantment, integer) -> {
                    if (enchantment.getMaxLevel() < integer) {
                        hasNBLevel.set(true);
                        expCost.updateAndGet((v) -> {
                            return v + (double) integer * config.getDouble("itemExpMultiplier");
                        });
                    }

                });
                if (!hasNBLevel.get()) {
                    return;
                }

                evt.getInventory().setRepairCost((int) ((double) evt.getInventory().getRepairCost() + (Double) expCost.get()));
                ItemStack result = itemFirst.clone();
                result.addUnsafeEnchantments(itemPlusEnchantments);
                evt.setResult(result);
            }

        }
    }
}