package com.awkwardinstrumentbalance.mixin;

import com.awkwardinstrumentbalance.AIBItems;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.util.StringUtil;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

import static net.minecraft.world.inventory.AnvilMenu.calculateIncreasedRepairCost;


@Mixin(AnvilMenu.class)
public abstract class AnvilMixin extends ItemCombinerMenu {
    @Shadow @Final private DataSlot cost;
    @Shadow @Final private static int COST_RENAME;
    @Shadow @Final private String itemName;
    @Shadow private int repairItemCountCost;

    private static final Map<Item, Item> GOLD_CRAFTING_RECIPE = Map.ofEntries(
        Map.entry(Items.IRON_SWORD, Items.GOLDEN_SWORD),
        Map.entry(Items.IRON_SPEAR, Items.GOLDEN_SPEAR),
        Map.entry(Items.IRON_PICKAXE, Items.GOLDEN_PICKAXE),
        Map.entry(Items.IRON_AXE, Items.GOLDEN_AXE),
        Map.entry(Items.IRON_SHOVEL, Items.GOLDEN_SHOVEL),
        Map.entry(Items.IRON_HOE, Items.GOLDEN_HOE),
        Map.entry(Items.IRON_HELMET, Items.GOLDEN_HELMET),
        Map.entry(Items.IRON_CHESTPLATE, Items.GOLDEN_CHESTPLATE),
        Map.entry(Items.IRON_LEGGINGS, Items.GOLDEN_LEGGINGS),
        Map.entry(Items.IRON_BOOTS, Items.GOLDEN_BOOTS)
    );

    public AnvilMixin(@Nullable MenuType<?> menuType, int containerId, Inventory inventory, ContainerLevelAccess access, ItemCombinerMenuSlotDefinition itemInputSlots) {
        super(menuType, containerId, inventory, access, itemInputSlots);
    }

    @Inject(method = "createResult", at = @At("HEAD"), cancellable = true)
    private void upgradeIronToGold(CallbackInfo ci) {
        ItemStack leftStack = this.inputSlots.getItem(0);
        ItemStack rightStack = this.inputSlots.getItem(1);
        int price = 0;

        if (GOLD_CRAFTING_RECIPE.containsKey(leftStack.getItem()) && rightStack.is(Items.GOLD_INGOT)) {
            ItemStack result = new ItemStack(GOLD_CRAFTING_RECIPE.get(leftStack.getItem()));
            result.setDamageValue(leftStack.getDamageValue());

            EnchantmentHelper.setEnchantments(result, leftStack.getEnchantments());

            if (this.itemName != null && !StringUtil.isBlank(this.itemName)) {
                if (!this.itemName.equals(leftStack.getHoverName().getString())) {
                    price += 1;
                    result.set(DataComponents.CUSTOM_NAME, Component.literal(this.itemName));
                }
            } else if (leftStack.has(DataComponents.CUSTOM_NAME)) {
                price += 1;
                result.remove(DataComponents.CUSTOM_NAME);
            }

            if (result.isDamageableItem() && result.isValidRepairItem(rightStack)) {
                int repairAmount = Math.min(result.getDamageValue(), result.getMaxDamage() / 4);

                int count;
                for (count = 0; repairAmount > 0 && count < rightStack.getCount(); count++) {
                    int resultDamage = result.getDamageValue() - repairAmount;
                    result.setDamageValue(resultDamage);
                    price++;
                    repairAmount = Math.min(result.getDamageValue(), result.getMaxDamage() / 4);
                }
                this.repairItemCountCost = count;
            }

            price += 2;
            this.cost.set(price);

            if (!result.isEmpty()) {
                int baseCost = result.getOrDefault(DataComponents.REPAIR_COST, 0);
                if (COST_RENAME != price || COST_RENAME == 0) {
                    baseCost = calculateIncreasedRepairCost(baseCost);
                }
                result.set(DataComponents.REPAIR_COST, baseCost);
            }

            this.resultSlots.setItem(0, result);
            this.broadcastChanges();
            ci.cancel();
        }
    }

    @Inject(method = "createResult", at = @At("HEAD"), cancellable = true)
    private void createIronPlate(CallbackInfo ci) {
        ItemStack leftStack = this.inputSlots.getItem(0);
        ItemStack rightStack = this.inputSlots.getItem(1);
        int price = 0;

        if (Items.IRON_INGOT.equals(leftStack.getItem()) && rightStack.isEmpty()) {
            ItemStack result = new ItemStack(AIBItems.IRON_PLATE, leftStack.count());

            if (this.itemName != null && !StringUtil.isBlank(this.itemName)) {
                if (!this.itemName.equals(leftStack.getHoverName().getString())) {
                    price += 1;
                    result.set(DataComponents.CUSTOM_NAME, Component.literal(this.itemName));
                }
            } else if (leftStack.has(DataComponents.CUSTOM_NAME)) {
                price += 1;
                result.remove(DataComponents.CUSTOM_NAME);
            }

            price += (int) (double) (leftStack.count() / 2);
            this.cost.set(price);

            this.resultSlots.setItem(0, result);
            this.broadcastChanges();
            ci.cancel();
        }
    }
}