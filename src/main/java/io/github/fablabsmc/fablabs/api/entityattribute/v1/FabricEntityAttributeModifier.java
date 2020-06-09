package io.github.fablabsmc.fablabs.api.entityattribute.v1;

import java.util.UUID;

import io.github.fablabsmc.fablabs.impl.entityattribute.EntityAttributeModifierRegistry;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.ItemStack;

public final class FabricEntityAttributeModifier {
	private final UUID modifierID;
	private final EntityAttribute attribute;
	private final EntityAttributeModifier.Operation operation;

	public FabricEntityAttributeModifier(EntityAttribute attribute, EntityAttributeModifier.Operation operation) {
		this.modifierID = EntityAttributeModifierRegistry.getID(attribute);
		this.attribute = attribute;
		this.operation = operation;

		EntityAttributeModifierRegistry.register(this.modifierID);
	}

	public UUID getId() {
		return this.modifierID;
	}

	public EntityAttribute getAttribute() {
		return this.attribute;
	}

	public EntityAttributeModifier newVanillaModifier(float value) {
		return new EntityAttributeModifier(
						this.modifierID,
						"NAME",
						value,
						this.operation
		);
	}

	/**
	 * Convenience method to apply an EntityAttributeModifier to an ItemStack.
	 *
	 * @param itemStack     the ItemStack to be applied to
	 * @param value         the modifier value
	 * @param equipmentSlot the EquipmentSlot this ItemStack must be equipped in to gain this benefit
	 */
	public void applyTo(ItemStack itemStack, float value, EquipmentSlot equipmentSlot) {
		itemStack.addAttributeModifier(this.attribute, this.newVanillaModifier(value), equipmentSlot);
	}
}
