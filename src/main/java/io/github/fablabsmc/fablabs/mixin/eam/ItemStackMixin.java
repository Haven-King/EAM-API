package io.github.fablabsmc.fablabs.mixin.eam;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import io.github.fablabsmc.fablabs.impl.eam.EntityAttributeModifierRegistry;
import io.github.fablabsmc.fablabs.impl.eam.FabricEntityAttributes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.BlockState;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
	@Shadow public abstract Item getItem();

	@Shadow public abstract boolean hasTag();

	@Shadow private CompoundTag tag;

	@Final
	@Deprecated
	@Shadow private Item item;

	private static double applyModifier(Double baseValue, EntityAttributeModifier modifier) {
		switch (modifier.getOperation()) {
		case ADDITION:
			baseValue += modifier.getValue();
			break;

		case MULTIPLY_TOTAL:
			if (baseValue == 0D) baseValue += 1D;
			baseValue *= modifier.getValue();
			break;
		case MULTIPLY_BASE:
			// TODO: I don't actually know what this one is supposed to do or how it works.
		}

		return baseValue;
	}

	/**
	 * The goal of this overwrite is to flatten all attribute modifiers on the ItemStack into one per EntityAttribute.
	 * We only use a Multimap because that's what the signature requires. We are essentially turning the MultiMap of
	 * (EntityAttribute, EntityAttributeModifier) to a normal HashMap.
	 *
	 * @author Haven King
	 */
	@Overwrite
	public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(EquipmentSlot equipmentSlot) {
		Multimap<EntityAttribute, EntityAttributeModifier> itemModifiers = this.getItem().getAttributeModifiers(equipmentSlot);

		if (this.hasTag() && this.tag.contains("AttributeModifiers", 9)) {
			Map<EntityAttribute, Double> modifierMap = new LinkedHashMap<>();

			itemModifiers.forEach((attribute, modifier) -> modifierMap.put(attribute, modifier.getValue()));

			ListTag modifierList = this.tag.getList("AttributeModifiers", 10);

			for (int i = 0; i < modifierList.size(); ++i) {
				CompoundTag entry = modifierList.getCompound(i);

				if (!entry.contains("Slot", 8) || entry.getString("Slot").equals(equipmentSlot.getName())) {
					Optional<EntityAttribute> optional = Registry.ATTRIBUTES.getOrEmpty(Identifier.tryParse(entry.getString("AttributeName")));

					if (optional.isPresent()) {
						EntityAttribute attribute = optional.get();

						EntityAttributeModifier modifier = EntityAttributeModifier.fromTag(entry);

						if (modifier != null && modifier.getId().getLeastSignificantBits() != 0L && modifier.getId().getMostSignificantBits() != 0L) {
							double value = applyModifier(modifierMap.getOrDefault(attribute, 0D), modifier);

							modifierMap.put(attribute, value);
						}
					}
				}
			}

			Multimap<EntityAttribute, EntityAttributeModifier> modifiers = LinkedHashMultimap.create();

			for (Map.Entry<EntityAttribute, Double> entry : modifierMap.entrySet()) {
				modifiers.put(entry.getKey(),
							new EntityAttributeModifier(
								EntityAttributeModifierRegistry.getID(entry.getKey()),
								"Names don't matter at all! :D",
								entry.getValue(),
								EntityAttributeModifier.Operation.ADDITION
							)
				);
			}

			return modifiers;
		} else {
			return itemModifiers;
		}
	}

	@Inject(method = "getMiningSpeedMultiplier", at = @At("HEAD"), cancellable = true)
	private void addMiningSpeedAttribute(BlockState state, CallbackInfoReturnable<Float> cir) {
		double d = this.item.getMiningSpeedMultiplier((ItemStack) (Object) this, state);

		Multimap<EntityAttribute, EntityAttributeModifier> modifiers = this.getAttributeModifiers(EquipmentSlot.MAINHAND);

		for (EntityAttributeModifier modifier : modifiers.get(FabricEntityAttributes.MINING_SPEED)) {
			d = applyModifier(d, modifier);
		}

		cir.setReturnValue((float) d);
	}
}
