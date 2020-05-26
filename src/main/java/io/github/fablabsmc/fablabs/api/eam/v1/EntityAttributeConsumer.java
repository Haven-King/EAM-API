package io.github.fablabsmc.fablabs.api.eam.v1;

import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;

public interface EntityAttributeConsumer {
	void addAttributeModifier(EntityAttribute attribute, EntityAttributeModifier modifier);
}
