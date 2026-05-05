package com.awkwardinstrumentbalance.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeMap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;

import java.util.*;

@Mixin(RecipeManager.class)
public class RecipeMixin {

    @ModifyReturnValue(method = "prepare", at = @At(value = "RETURN"))
    private RecipeMap filterRecipesOnReturn(RecipeMap recipes) {
        ArrayList<ResourceKey<Recipe<?>>> recipesToRemove = new ArrayList<>();
        String[] materials = new String[]{"wooden", "stone", "copper", "iron", "golden"};
        String[] types = new String[]{"sword", "spear", "pickaxe", "axe", "shovel", "hoe"};
        String[] armors = new String[]{"helmet", "chestplate", "leggings", "boots"};
        for (String material : materials) {
            for (String type : types) {
                Identifier recipeId = Identifier.fromNamespaceAndPath(
                        "minecraft", String.format("%s_%s", material, type)
                );
                ResourceKey<Recipe<?>> key = ResourceKey.create(Registries.RECIPE, recipeId);
                recipesToRemove.add(key);
            }
        }

        for (String armor : armors) {
            Identifier recipeId = Identifier.fromNamespaceAndPath("minecraft", String.format("golden_%s", armor));
            ResourceKey<Recipe<?>> key = ResourceKey.create(Registries.RECIPE, recipeId);
            recipesToRemove.add(key);
        }

        List<RecipeHolder<?>> keptRecipes = new ArrayList<>();

        for (RecipeHolder<?> recipe : recipes.values()) {
            if (!recipesToRemove.contains(recipe.id())) {
                keptRecipes.add(recipe);
            }
        }

        return RecipeMap.create(keptRecipes);
    }
}
