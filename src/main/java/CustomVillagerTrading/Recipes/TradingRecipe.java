package CustomVillagerTrading.Recipes;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;
import java.util.Optional;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class TradingRecipe implements Recipe<TradingRecipeInput>, VillagerTrades.ItemListing {
    public final String Villager;
    public final ItemStack Input1;
    public final ItemStack Input2;
    public final ItemStack Output;
    public final int MaxUsed;
    public final int VillagerXp;
    public final float PriceMultiplier;
    public final int Level;

    public TradingRecipe(String villager, ItemStack input1, Optional<ItemStack> input2, ItemStack output, TradeData data, int level) {
        Villager = villager;
        Input1 = Objects.requireNonNull(input1);
        Input2 = Objects.requireNonNull(input2.orElse(ItemStack.EMPTY));
        Output = Objects.requireNonNull(output);
        MaxUsed = data.MaxUsed;
        VillagerXp = data.VillagerXp;
        PriceMultiplier = data.PriceMultiplier;
        Level = level;
    }

    @Override
    public boolean matches(TradingRecipeInput input, Level level) {
        return input.input1().is(Input1.getItem()) && (Input2.isEmpty() || input.input2().is(Input2.getItem()));
    }

    @Override
    public ItemStack assemble(TradingRecipeInput input, HolderLookup.Provider provider) {
        return getResultItem().copy();
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return false;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider pRegistries) {
        return getResultItem();
    }

    public ItemStack getResultItem() {
        return Output;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeTypes.TradingSerializer.get();
    }

    @Override
    public RecipeType<?> getType() {
        return RecipeTypes.Trading.get();
    }

    @Override
    public @Nullable MerchantOffer getOffer(Entity pTrader, RandomSource pRandom) {
        return new MerchantOffer(
            new ItemCost(Input1.getItem(), Input1.getCount()),
            Input2 == ItemStack.EMPTY ? Optional.empty() : Optional.of(new ItemCost(Input2.getItem(), Input2.getCount())),
            Output, MaxUsed, VillagerXp, PriceMultiplier
        );
    }

    public static class TradeData {
        public static Codec<TradeData> CODEC = Codec.lazyInitialized(() ->
            RecordCodecBuilder.create(builder ->
                builder.group(
                    ExtraCodecs.POSITIVE_INT.fieldOf("max_used").forGetter(x -> x.MaxUsed),
                    ExtraCodecs.POSITIVE_INT.fieldOf("villager_xp").forGetter(x -> x.VillagerXp),
                    ExtraCodecs.POSITIVE_FLOAT.fieldOf("price_multiplier").forGetter(x -> x.PriceMultiplier)
                ).apply(builder, TradeData::new)
            )
        );
        public static final StreamCodec<RegistryFriendlyByteBuf, TradeData> STREAM_CODEC = new StreamCodec<>() {
            @Override
            public void encode(RegistryFriendlyByteBuf buf, TradeData data) {
                ByteBufCodecs.INT.encode(buf, data.MaxUsed);
                ByteBufCodecs.INT.encode(buf, data.VillagerXp);
                ByteBufCodecs.FLOAT.encode(buf, data.PriceMultiplier);
            }

            @Override
            public TradeData decode(RegistryFriendlyByteBuf buf) {
                return new TradeData(
                    ByteBufCodecs.INT.decode(buf),
                    ByteBufCodecs.INT.decode(buf),
                    ByteBufCodecs.FLOAT.decode(buf)
                );
            }
        };

        public final int MaxUsed;
        public final int VillagerXp;
        public final float PriceMultiplier;

        public TradeData(int used, int xp, float price) {
            MaxUsed = used;
            VillagerXp = xp;
            PriceMultiplier = price;
        }
    }
}
