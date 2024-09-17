package CustomVillagerTrading.Recipes;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;

import java.util.Optional;

public class TradingRecipeSerializer implements RecipeSerializer<TradingRecipe> {
    public static final MapCodec<TradingRecipe> CODEC = RecordCodecBuilder.mapCodec(builder ->
        builder.group(
            Codec.STRING.fieldOf("villager").forGetter(x -> x.Villager),
            ItemStack.CODEC.fieldOf("input1").forGetter(x -> x.Input1),
            ItemStack.CODEC.optionalFieldOf("input2").forGetter(x -> Optional.of(x.Input2)),
            ItemStack.CODEC.fieldOf("output").forGetter(x -> x.Output),
            TradingRecipe.TradeData.CODEC.fieldOf("data").forGetter(x -> new TradingRecipe.TradeData(x.MaxUsed, x.VillagerXp, x.PriceMultiplier)),
            Codec.intRange(1, 5).fieldOf("level").forGetter(x -> x.Level)
        ).apply(builder, TradingRecipe::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, TradingRecipe> STREAMCODEC = StreamCodec.composite(
        ByteBufCodecs.STRING_UTF8, x -> x.Villager,
        ItemStack.STREAM_CODEC, x -> x.Input1,
        ItemStack.STREAM_CODEC.apply(ByteBufCodecs::optional), x -> Optional.of(x.Input2),
        ItemStack.STREAM_CODEC, x -> x.Input1,
        TradingRecipe.TradeData.STREAM_CODEC, x -> new TradingRecipe.TradeData(x.MaxUsed, x.VillagerXp, x.PriceMultiplier),
        ByteBufCodecs.INT, x -> x.Level,
        TradingRecipe::new
    );

    @Override
    public MapCodec<TradingRecipe> codec() {
        return CODEC;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, TradingRecipe> streamCodec() {
        return STREAMCODEC;
    }
}