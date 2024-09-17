package CustomVillagerTrading;

import CustomVillagerTrading.Recipes.RecipeTypes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLPaths;
import org.slf4j.Logger;
import java.nio.file.Path;

@Mod(CustomVillagerTrading.ModId)
public class CustomVillagerTrading {
    public static final String ModId = "custom_villager_trading";
    public static final String ModName = "Custom Villager Trading";

    public CustomVillagerTrading(IEventBus bus) {
        RecipeTypes.ModRecipeType.register(bus);
        RecipeTypes.ModSerializer.register(bus);
    }
}