package dev.teamcitrus.demeter.item.dev;

import dev.teamcitrus.citruslib.item.CitrusItem;
import dev.teamcitrus.citruslib.reload.DynamicHolder;
import dev.teamcitrus.demeter.data.AnimalStats;
import dev.teamcitrus.demeter.data.StatsRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class RegistryTestItem extends CitrusItem {
    /*
    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack stack = pPlayer.getItemInHand(pUsedHand);
        if (!pLevel.isClientSide()) {
            BetterFarms.LOGGER.error(StatsRegistry.INSTANCE.getValues());
            return InteractionResultHolder.success(stack);
        }
        return InteractionResultHolder.fail(stack);
    }
     */

    @Override
    public InteractionResult interactLivingEntity(ItemStack pStack, Player pPlayer, LivingEntity pInteractionTarget, InteractionHand pUsedHand) {
        if (!pPlayer.level().isClientSide()) {
            if (pInteractionTarget instanceof Animal animal) {
                //BetterFarms.LOGGER.error(StatsRegistry.INSTANCE.getKeys());
                ResourceLocation resourceLocation = BuiltInRegistries.ENTITY_TYPE.getKey(animal.getType());
                DynamicHolder<AnimalStats> stats = StatsRegistry.INSTANCE.holder(resourceLocation);
                boolean milking = stats.get().milking().isPresent();
                pPlayer.sendSystemMessage(Component.literal(stats.get().toString()));
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.FAIL;
    }
}
