package io.github.fablabsmc.fablabs.api.entityattribute.v1;

import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;

public interface EntityAttributeConsumer {
	AttributeContainer getAttributes();

	/**
	 * Adds a temporary modifier to the AttributeContainer of this object.
	 * These modifiers are not saved in the tag as that comes as a result of {@link AttributeContainer#toTag()}. Use this
	 * method if you want your modifiers to be checked periodically (e.g. equipped items).
	 *
	 * @param attribute the {@link EntityAttribute} to be modified temporarily
	 * @param modifier the {@link EntityAttributeModifier} that describes your operation
	 */
	default void addTemporaryModifier(EntityAttribute attribute, EntityAttributeModifier modifier) {
		AttributeContainer container = getAttributes();
		EntityAttributeInstance entityAttributeInstance = container.getCustomInstance(attribute);

		if (entityAttributeInstance != null) {
			entityAttributeInstance.addTemporaryModifier(modifier);
		}
	}

	/**
	 * Adds a temporary modifier to the {@link AttributeContainer} of this object.
	 * These modifiers are not saved in the tag as that comes as a result of {@link AttributeContainer#toTag()}. Use this
	 * method if you want your modifiers to be checked periodically (e.g. equipped items).
	 *
	 * @param modifier the {@link FabricEntityAttributeModifier} to be applied to
	 * @param value the modifier value that the new {@link EntityAttributeModifier} will contain
	 */
	default void addTemporaryModifier(FabricEntityAttributeModifier modifier, float value) {
		this.addTemporaryModifier(modifier.getAttribute(), modifier.newVanillaModifier(value));
	}
}
