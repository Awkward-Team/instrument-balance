package com.awkwardinstrumentbalance.loot;

import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.InvertedLootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class AIBLootTableEvents {
    public static final String MOD_ID = "awkward-instrument-balance";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static void register() {
        LootTableEvents.REPLACE.register((key, _, source, provider) -> {
            String path = key.identifier().getPath();
            if (source.isBuiltin() && path.endsWith("_log")) {
                LootTable.Builder builder = LootTable.lootTable();
                ItemPredicate.Builder axePredicate = ItemPredicate.Builder.item().of(provider.lookupOrThrow(Registries.ITEM), ItemTags.AXES);
                Identifier logBlockId = Identifier.fromNamespaceAndPath(
                        key.identifier().getNamespace(),
                        path.replace("blocks/", "")
                );
                Identifier plankBlockId = Identifier.fromNamespaceAndPath(
                        key.identifier().getNamespace(),
                        path.replace("blocks/", "")
                                .replace("_log", "_planks")
                                .replace("_wood", "_planks")
                                .replace("_stem", "_planks")
                );

                builder.pool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0f))
                        .add(LootItem.lootTableItem(Items.STICK))
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 4)))
                        .when(LootItemRandomChanceCondition.randomChance(0.7f))
                        .when(InvertedLootItemCondition.invert(MatchTool.toolMatches(axePredicate))).build()
                );
                LOGGER.info(BuiltInRegistries.ITEM.getOptional(plankBlockId).toString());
                BuiltInRegistries.ITEM.getOptional(plankBlockId).ifPresent(block -> builder.pool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0f))
                        .add(LootItem.lootTableItem(block))
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2)))
                        .when(LootItemRandomChanceCondition.randomChance(0.4f))
                        .when(InvertedLootItemCondition.invert(MatchTool.toolMatches(axePredicate))).build()
                ));

                LOGGER.info("Loot table replaced | {} {}", logBlockId, plankBlockId);

                BuiltInRegistries.BLOCK.getOptional(logBlockId).ifPresent(block -> builder.pool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0f))
                        .add(LootItem.lootTableItem(block))
                        .when(MatchTool.toolMatches(axePredicate))
                        .build()
                ));

                return builder.build();
            }
            return null;
        });
    }
}
