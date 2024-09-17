package CustomVillagerTrading.Recipes;

import CustomVillagerTrading.CustomVillagerTrading;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class RecipeTypes {
    public static final DeferredRegister<RecipeType<?>> ModRecipeType = DeferredRegister.create(Registries.RECIPE_TYPE, CustomVillagerTrading.ModId);
    public static final DeferredRegister<RecipeSerializer<?>> ModSerializer = DeferredRegister.create(Registries.RECIPE_SERIALIZER, CustomVillagerTrading.ModId);

    public static final DeferredHolder<RecipeType<?>, RecipeType<TradingRecipe>> Trading = register("trading");
    public static final DeferredHolder<RecipeSerializer<?>, TradingRecipeSerializer> TradingSerializer = ModSerializer.register("trading", TradingRecipeSerializer::new);

    private static <T extends Recipe<?>> DeferredHolder<RecipeType<?>, RecipeType<T>> register(String id) {
        RecipeType<T> type = RecipeType.simple(ResourceLocation.fromNamespaceAndPath(CustomVillagerTrading.ModId, id));
        return ModRecipeType.register(id, () -> type);
    }
}
