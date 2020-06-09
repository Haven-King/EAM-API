package io.github.fablabsmc.fablabs.impl.entityattribute;

import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.util.registry.Registry;

public class FabricEntityAttributes {
	public static EntityAttribute MINING_SPEED = Registry.register(Registry.ATTRIBUTES, "player.mining_speed", new ClampedEntityAttribute("attribute.name.player.mining_speed", 1.0D, -10D, 10D));
}
