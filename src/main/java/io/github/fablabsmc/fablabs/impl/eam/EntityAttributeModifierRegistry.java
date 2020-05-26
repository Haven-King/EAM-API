package io.github.fablabsmc.fablabs.impl.eam;

import java.util.HashMap;
import java.util.UUID;

import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;

public class EntityAttributeModifierRegistry {
	private static final HashMap<UUID, UUID> REGISTERED = new HashMap<>();

	private static final HashMap<EntityAttribute, UUID> REGISTERED_MODIFIERS = new HashMap<>();

	static {
		REGISTERED.put(Item.ATTACK_DAMAGE_MODIFIER_ID, Item.ATTACK_DAMAGE_MODIFIER_ID);
		REGISTERED.put(Item.ATTACK_SPEED_MODIFIER_ID, Item.ATTACK_SPEED_MODIFIER_ID);

		REGISTERED_MODIFIERS.put(EntityAttributes.GENERIC_ATTACK_DAMAGE, Item.ATTACK_DAMAGE_MODIFIER_ID);
		REGISTERED_MODIFIERS.put(EntityAttributes.GENERIC_ATTACK_SPEED, Item.ATTACK_SPEED_MODIFIER_ID);

		for (UUID uUID : ArmorItem.MODIFIERS) {
			REGISTERED.put(uUID, uUID);
			REGISTERED_MODIFIERS.put(EntityAttributes.GENERIC_ARMOR, uUID);
			REGISTERED_MODIFIERS.put(EntityAttributes.GENERIC_ARMOR_TOUGHNESS, uUID);
		}
	}

	public static UUID getID(EntityAttribute attribute) {
		REGISTERED_MODIFIERS.putIfAbsent(attribute, UUID.randomUUID());

		return REGISTERED_MODIFIERS.get(attribute);
	}

	public static void register(UUID uuid) {
		REGISTERED.putIfAbsent(uuid, uuid);
	}

	public static boolean contains(UUID uuid) {
		return REGISTERED.containsKey(uuid);
	}

	public static UUID get(UUID uuid) {
		return REGISTERED.getOrDefault(uuid, uuid);
	}
}
