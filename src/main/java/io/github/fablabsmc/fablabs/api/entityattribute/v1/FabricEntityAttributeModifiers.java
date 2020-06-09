package io.github.fablabsmc.fablabs.api.entityattribute.v1;

import io.github.fablabsmc.fablabs.impl.entityattribute.FabricEntityAttributes;

import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;

public class FabricEntityAttributeModifiers {
	public static final FabricEntityAttributeModifier ATTACK_DAMAGE_MODIFIER = new FabricEntityAttributeModifier(
					EntityAttributes.GENERIC_ATTACK_DAMAGE,
					EntityAttributeModifier.Operation.ADDITION
	);

	public static final FabricEntityAttributeModifier ATTACK_SPEED_MODIFIER = new FabricEntityAttributeModifier(
					EntityAttributes.GENERIC_ATTACK_SPEED,
					EntityAttributeModifier.Operation.ADDITION
	);

	public static final FabricEntityAttributeModifier ARMOR_MODIFIER = new FabricEntityAttributeModifier(
					EntityAttributes.GENERIC_ARMOR,
					EntityAttributeModifier.Operation.ADDITION
	);

	public static final FabricEntityAttributeModifier ARMOR_TOUGHNESS_MODIFIER = new FabricEntityAttributeModifier(
					EntityAttributes.GENERIC_ARMOR_TOUGHNESS,
					EntityAttributeModifier.Operation.ADDITION
	);

	public static final FabricEntityAttributeModifier MINING_SPEED_MODIFIER = new FabricEntityAttributeModifier(
					FabricEntityAttributes.MINING_SPEED,
					EntityAttributeModifier.Operation.MULTIPLY_TOTAL
	);
}
