package CustomVillagerTrading;

import CustomVillagerTrading.Recipes.TradingRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.village.VillagerTradesEvent;
import java.util.List;

@EventBusSubscriber(modid = CustomVillagerTrading.ModId)
public class VillagerTradeEvent {
    @SubscribeEvent
    public static void wanderTradeEvent(VillagerTradesEvent event) {
        ClientLevel level = Minecraft.getInstance().level;
        if (level == null) return;

        List<VillagerTrades.ItemListing> newbie = event.getTrades().get(1);
        List<VillagerTrades.ItemListing> apprentice = event.getTrades().get(2);
        List<VillagerTrades.ItemListing> proficient = event.getTrades().get(3);
        List<VillagerTrades.ItemListing> expert = event.getTrades().get(4);
        List<VillagerTrades.ItemListing> master = event.getTrades().get(5);

        // VillagerProfession

        for (RecipeHolder<?> holder : level.getRecipeManager().getOrderedRecipes()) {
            Recipe<?> irecipe = holder.value();
            if (!(irecipe instanceof TradingRecipe recipe)) {
                continue;
            }
            if (BuiltInRegistries.VILLAGER_PROFESSION.get(ResourceLocation.parse(recipe.Villager)) != event.getType()) {
                continue;
            }
            switch (recipe.Level) {
                case 1:
                    newbie.add(recipe);
                    break;
                case 2:
                    apprentice.add(recipe);
                    break;
                case 3:
                    proficient.add(recipe);
                    break;
                case 4:
                    expert.add(recipe);
                    break;
                case 5:
                    master.add(recipe);
                    break;
            }
        }
    }
}
