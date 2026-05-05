package com.awkwardinstrumentbalance.mixin;

import com.awkwardinstrumentbalance.AIBConfig;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.world.item.ToolMaterial;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(ToolMaterial.class)
public class ToolMaterialMixin {

	@ModifyConstant(method = "<clinit>", constant = @Constant(intValue = 59))
	private static int decreaseWoodDurability(int original) {
		return AutoConfig.getConfigHolder(AIBConfig.class).getConfig().woodenDurability;
	}
	@ModifyConstant(method = "<clinit>", constant = @Constant(intValue = 131))
	private static int decreaseStoneDurability(int original) {
		return AutoConfig.getConfigHolder(AIBConfig.class).getConfig().stoneDurability;
	}
	@ModifyConstant(method = "<clinit>", constant = @Constant(intValue = 190))
	private static int decreaseCopperDurability(int original) {
		return AutoConfig.getConfigHolder(AIBConfig.class).getConfig().copperDurability;
	}
	@ModifyConstant(method = "<clinit>", constant = @Constant(intValue = 250))
	private static int decreaseIronDurability(int original) {
		return AutoConfig.getConfigHolder(AIBConfig.class).getConfig().ironDurability;
	}
	@ModifyConstant(method = "<clinit>", constant = @Constant(intValue = 32))
	private static int decreaseGoldDurability(int original) {
		return AutoConfig.getConfigHolder(AIBConfig.class).getConfig().goldDurability;
	}
	@ModifyConstant(method = "<clinit>", constant = @Constant(intValue = 1561))
	private static int decreaseDiamondDurability(int original) {
		return AutoConfig.getConfigHolder(AIBConfig.class).getConfig().diamondDurability;
	}
	@ModifyConstant(method = "<clinit>", constant = @Constant(intValue = 2031))
	private static int decreaseNetheriteDurability(int original) {
		return AutoConfig.getConfigHolder(AIBConfig.class).getConfig().netheriteDurability;
	}

	@ModifyConstant(method = "<clinit>", constant = @Constant(floatValue = 2.0F))
	private static float decreaseWoodSpeed(float original) {
		return AutoConfig.getConfigHolder(AIBConfig.class).getConfig().woodenSpeed;
	}
	@ModifyConstant(method = "<clinit>", constant = @Constant(floatValue = 4.0F))
	private static float decreaseStoneSpeed(float original) {
		return AutoConfig.getConfigHolder(AIBConfig.class).getConfig().stoneSpeed;
	}
}