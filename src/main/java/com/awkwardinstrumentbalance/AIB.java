package com.awkwardinstrumentbalance;

import com.awkwardinstrumentbalance.loot.AIBLootTableEvents;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ModInitializer;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AIB implements ModInitializer {
	public static final String MOD_ID = "awkward-instrument-balance";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		AIBItems.initialize();
		AIBLootTableEvents.register();
		LOGGER.info("{} has been initialized", StringUtils.capitalize(MOD_ID));
	}
}