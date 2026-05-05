package com.awkwardinstrumentbalance.client;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;

import com.awkwardinstrumentbalance.AIBItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.*;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.world.item.crafting.Ingredient;
import org.jspecify.annotations.NonNull;

public class AIBRecipeProvider extends FabricRecipeProvider {

    public record RecipeData(
        Item result,
        RecipeIngredient material,
        Item unlockedBy,
        RecipePattern pattern
    ) {
        public void build(RecipeProvider p, RecipeOutput o, HolderLookup.Provider reg) {
            pattern.build(this, p, o, reg);
        }
    }

    private static final List<RecipeData> RECIPES = List.of(
        new RecipeData(Items.WOODEN_SPEAR, RecipeIngredient.of(ItemTags.LOGS), Items.CRAFTING_TABLE, RecipePattern.SPEAR),
        new RecipeData(Items.STONE_SPEAR, RecipeIngredient.of(ItemTags.STONE_TOOL_MATERIALS), Items.COBBLESTONE, RecipePattern.STONE_SPEAR),
        new RecipeData(Items.COPPER_SPEAR, RecipeIngredient.of(ItemTags.COPPER_TOOL_MATERIALS), Items.COPPER_INGOT, RecipePattern.SPEAR),
        new RecipeData(Items.IRON_SPEAR, RecipeIngredient.of(AIBItems.IRON_PLATE), Items.IRON_INGOT, RecipePattern.SPEAR),

        new RecipeData(Items.WOODEN_SWORD, RecipeIngredient.of(ItemTags.LOGS), Items.CRAFTING_TABLE, RecipePattern.SWORD),
        new RecipeData(Items.STONE_SWORD, RecipeIngredient.of(ItemTags.STONE_TOOL_MATERIALS), Items.COBBLESTONE, RecipePattern.STONE_SWORD),
        new RecipeData(Items.COPPER_SWORD, RecipeIngredient.of(ItemTags.COPPER_TOOL_MATERIALS), Items.COPPER_INGOT, RecipePattern.SWORD),
        new RecipeData(Items.IRON_SWORD, RecipeIngredient.of(AIBItems.IRON_PLATE), Items.IRON_INGOT, RecipePattern.SWORD),

        new RecipeData(Items.WOODEN_PICKAXE, RecipeIngredient.of(ItemTags.LOGS), Items.CRAFTING_TABLE, RecipePattern.PICKAXE),
        new RecipeData(Items.STONE_PICKAXE, RecipeIngredient.of(ItemTags.STONE_TOOL_MATERIALS), Items.COBBLESTONE, RecipePattern.STONE_PICKAXE),
        new RecipeData(Items.COPPER_PICKAXE, RecipeIngredient.of(ItemTags.COPPER_TOOL_MATERIALS), Items.COPPER_INGOT, RecipePattern.PICKAXE),
        new RecipeData(Items.IRON_PICKAXE, RecipeIngredient.of(AIBItems.IRON_PLATE), Items.IRON_INGOT, RecipePattern.PICKAXE),

        new RecipeData(Items.WOODEN_AXE, RecipeIngredient.of(ItemTags.PLANKS), Items.CRAFTING_TABLE, RecipePattern.AXE),
        new RecipeData(Items.STONE_AXE, RecipeIngredient.of(ItemTags.STONE_TOOL_MATERIALS), Items.COBBLESTONE, RecipePattern.STONE_AXE),
        new RecipeData(Items.COPPER_AXE, RecipeIngredient.of(ItemTags.COPPER_TOOL_MATERIALS), Items.COPPER_INGOT, RecipePattern.AXE),
        new RecipeData(Items.IRON_AXE, RecipeIngredient.of(AIBItems.IRON_PLATE), Items.IRON_INGOT, RecipePattern.AXE),

        new RecipeData(Items.WOODEN_SHOVEL, RecipeIngredient.of(ItemTags.LOGS), Items.CRAFTING_TABLE, RecipePattern.SHOVEL),
        new RecipeData(Items.STONE_SHOVEL, RecipeIngredient.of(ItemTags.STONE_TOOL_MATERIALS), Items.COBBLESTONE, RecipePattern.STONE_SHOVEL),
        new RecipeData(Items.COPPER_SHOVEL, RecipeIngredient.of(ItemTags.COPPER_TOOL_MATERIALS), Items.COPPER_INGOT, RecipePattern.SHOVEL),
        new RecipeData(Items.IRON_SHOVEL, RecipeIngredient.of(AIBItems.IRON_PLATE), Items.IRON_INGOT, RecipePattern.SHOVEL),

        new RecipeData(Items.WOODEN_HOE, RecipeIngredient.of(ItemTags.LOGS), Items.CRAFTING_TABLE, RecipePattern.HOE),
        new RecipeData(Items.STONE_HOE, RecipeIngredient.of(ItemTags.STONE_TOOL_MATERIALS), Items.COBBLESTONE, RecipePattern.STONE_HOE),
        new RecipeData(Items.COPPER_HOE, RecipeIngredient.of(ItemTags.COPPER_TOOL_MATERIALS), Items.COPPER_INGOT, RecipePattern.HOE),
        new RecipeData(Items.IRON_HOE, RecipeIngredient.of(AIBItems.IRON_PLATE), Items.IRON_INGOT, RecipePattern.HOE)
    );

    @Override
    protected RecipeProvider createRecipeProvider(HolderLookup.Provider registryLookup, RecipeOutput exporter) {
        return new RecipeProvider(registryLookup, exporter) {
            @Override
            public void buildRecipes() {
                RECIPES.forEach(recipe -> recipe.build(this, exporter, registryLookup));
            }
        };
    }

    @Override
    public @NonNull String getName() {
        return "AIBRecipeProvider";
    }

    public AIBRecipeProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @FunctionalInterface
    interface RecipeBuilder {
        void apply(RecipeData data, RecipeProvider p, RecipeOutput out, HolderLookup.Provider reg);
    }

    @FunctionalInterface
    public interface RecipeIngredient {
        Ingredient toIngredient(HolderLookup.Provider registries);

        static RecipeIngredient of(Item item) {
            return (reg) -> Ingredient.of(item);
        }

        static RecipeIngredient of(TagKey<Item> tag) {
            return (reg) -> Ingredient.of(reg.lookupOrThrow(Registries.ITEM).getOrThrow(tag));
        }
    }

    public enum RecipePattern {
        SPEAR((d, p, o, reg) -> {
            p.shaped(RecipeCategory.COMBAT, d.result)
                    .pattern(" tm")
                    .pattern(" st")
                    .pattern("s  ")
                    .define('m', d.material.toIngredient(reg))
                    .define('s', Items.STICK)
                    .define('t', Items.STRING)
                    .unlockedBy(p.getHasName(d.unlockedBy), p.has(d.unlockedBy))
                    .save(o);
        }),
        SWORD((d, p, o, reg) -> {
            p.shaped(RecipeCategory.COMBAT, d.result)
                    .pattern(" m ")
                    .pattern(" m ")
                    .pattern("tst")
                    .define('m', d.material.toIngredient(reg))
                    .define('s', Items.STICK)
                    .define('t', Items.STRING)
                    .unlockedBy(p.getHasName(d.unlockedBy), p.has(d.unlockedBy))
                    .save(o);
        }),
        PICKAXE((d, p, o, reg) -> {
            p.shaped(RecipeCategory.TOOLS, d.result)
                    .pattern("mmm")
                    .pattern("tst")
                    .pattern(" s ")
                    .define('m', d.material.toIngredient(reg))
                    .define('s', Items.STICK)
                    .define('t', Items.STRING)
                    .unlockedBy(p.getHasName(d.unlockedBy), p.has(d.unlockedBy))
                    .save(o);
        }),
        AXE((d, p, o, reg) -> {
            p.shaped(RecipeCategory.COMBAT, d.result)
                    .pattern("mmt")
                    .pattern("mst")
                    .pattern(" s ")
                    .define('m', d.material.toIngredient(reg))
                    .define('s', Items.STICK)
                    .define('t', Items.STRING)
                    .unlockedBy(p.getHasName(d.unlockedBy), p.has(d.unlockedBy))
                    .save(o);
        }),
        SHOVEL((d, p, o, reg) -> {
            p.shaped(RecipeCategory.TOOLS, d.result)
                    .pattern(" m ")
                    .pattern("tst")
                    .pattern(" s ")
                    .define('m', d.material.toIngredient(reg))
                    .define('s', Items.STICK)
                    .define('t', Items.STRING)
                    .unlockedBy(p.getHasName(d.unlockedBy), p.has(d.unlockedBy))
                    .save(o);
        }),
        HOE((d, p, o, reg) -> {
            p.shaped(RecipeCategory.TOOLS, d.result)
                    .pattern("mmt")
                    .pattern(" st")
                    .pattern(" s ")
                    .define('m', d.material.toIngredient(reg))
                    .define('s', Items.STICK)
                    .define('t', Items.STRING)
                    .unlockedBy(p.getHasName(d.unlockedBy), p.has(d.unlockedBy))
                    .save(o);
        }),

        STONE_SPEAR((d, p, o, reg) -> {
            p.shaped(RecipeCategory.COMBAT, d.result)
                    .pattern(" tm")
                    .pattern("tst")
                    .pattern("st ")
                    .define('m', d.material.toIngredient(reg))
                    .define('s', Items.STICK)
                    .define('t', Items.STRING)
                    .unlockedBy(p.getHasName(d.unlockedBy), p.has(d.unlockedBy))
                    .save(o);
        }),
        STONE_SWORD((d, p, o, reg) -> {
            p.shaped(RecipeCategory.COMBAT, d.result)
                    .pattern(" m ")
                    .pattern("tmt")
                    .pattern("tst")
                    .define('m', d.material.toIngredient(reg))
                    .define('s', Items.STICK)
                    .define('t', Items.STRING)
                    .unlockedBy(p.getHasName(d.unlockedBy), p.has(d.unlockedBy))
                    .save(o);
        }),
        STONE_PICKAXE((d, p, o, reg) -> {
            p.shaped(RecipeCategory.TOOLS, d.result)
                    .pattern("mmm")
                    .pattern("tst")
                    .pattern("tst")
                    .define('m', d.material.toIngredient(reg))
                    .define('s', Items.STICK)
                    .define('t', Items.STRING)
                    .unlockedBy(p.getHasName(d.unlockedBy), p.has(d.unlockedBy))
                    .save(o);
        }),
        STONE_AXE((d, p, o, reg) -> {
            p.shaped(RecipeCategory.COMBAT, d.result)
                    .pattern("mmt")
                    .pattern("mst")
                    .pattern("tst")
                    .define('m', d.material.toIngredient(reg))
                    .define('s', Items.STICK)
                    .define('t', Items.STRING)
                    .unlockedBy(p.getHasName(d.unlockedBy), p.has(d.unlockedBy))
                    .save(o);
        }),
        STONE_SHOVEL((d, p, o, reg) -> {
            p.shaped(RecipeCategory.TOOLS, d.result)
                    .pattern("tmt")
                    .pattern("tst")
                    .pattern(" s ")
                    .define('m', d.material.toIngredient(reg))
                    .define('s', Items.STICK)
                    .define('t', Items.STRING)
                    .unlockedBy(p.getHasName(d.unlockedBy), p.has(d.unlockedBy))
                    .save(o);
        }),
        STONE_HOE((d, p, o, reg) -> {
            p.shaped(RecipeCategory.TOOLS, d.result)
                    .pattern("mmt")
                    .pattern("tst")
                    .pattern(" s ")
                    .define('m', d.material.toIngredient(reg))
                    .define('s', Items.STICK)
                    .define('t', Items.STRING)
                    .unlockedBy(p.getHasName(d.unlockedBy), p.has(d.unlockedBy))
                    .save(o);
        });

        private final RecipeBuilder builder;
        RecipePattern(RecipeBuilder builder) { this.builder = builder; }
        void build(RecipeData d, RecipeProvider p, RecipeOutput o, HolderLookup.Provider r) {
            builder.apply(d, p, o, r);
        }
    }
}