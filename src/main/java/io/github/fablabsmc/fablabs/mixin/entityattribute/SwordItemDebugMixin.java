package io.github.fablabsmc.fablabs.mixin.entityattribute;

import org.spongepowered.asm.mixin.Mixin;
import io.github.fablabsmc.fablabs.api.entityattribute.v1.FabricEntityAttributeModifiers;

import net.minecraft.block.Blocks;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;

import net.fabricmc.loader.api.FabricLoader;

@Mixin(SwordItem.class)
public class SwordItemDebugMixin extends ToolItem {
	public SwordItemDebugMixin(ToolMaterial material, Settings settings) {
		super(material, settings);
	}

	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		if (FabricLoader.getInstance().isDevelopmentEnvironment() && context.getWorld().getBlockState(context.getBlockPos()).getBlock() == Blocks.IRON_BLOCK) {
			FabricEntityAttributeModifiers.ATTACK_DAMAGE_MODIFIER.applyTo(context.getStack(), 1.0f, EquipmentSlot.MAINHAND);
		}

		return super.useOnBlock(context);
	}

	@Override
	public boolean useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
		if (FabricLoader.getInstance().isDevelopmentEnvironment() && user != null) {
			FabricEntityAttributeModifiers.MINING_SPEED_MODIFIER.applyTo(user.getStackInHand(hand), 1.5f, EquipmentSlot.MAINHAND);
		}

		return super.useOnEntity(stack, user, entity, hand);
	}
}
