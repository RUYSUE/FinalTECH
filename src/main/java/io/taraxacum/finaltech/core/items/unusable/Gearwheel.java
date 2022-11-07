package io.taraxacum.finaltech.core.items.unusable;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.taraxacum.finaltech.FinalTech;
import io.taraxacum.finaltech.api.interfaces.RecipeItem;
import io.taraxacum.finaltech.util.RecipeUtil;
import org.bukkit.inventory.ItemStack;

/**
 * @author Final_ROOT
 * @since 2.0
 */
public class Gearwheel extends UnusableSlimefunItem implements RecipeItem {
    public Gearwheel(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, ItemStack output) {
        super(itemGroup, item, recipeType, recipe, output);
    }

    @Override
    public void registerDefaultRecipes() {
        // TODO
//        ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(this.getAddon().getJavaPlugin(), this.getId()), this.getItem());
//        recipe.shape("ggg","ddd","aaa");
//        recipe.setIngredient('g', Material.GRANITE);
//        recipe.setIngredient('d', Material.DIORITE);
//        recipe.setIngredient('a', Material.ANDESITE);
//        Bukkit.addRecipe(recipe);

        RecipeUtil.registerDescriptiveRecipe(FinalTech.getLanguageManager(), this);
    }
}
