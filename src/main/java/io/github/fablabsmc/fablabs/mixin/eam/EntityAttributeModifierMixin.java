package io.github.fablabsmc.fablabs.mixin.eam;

import java.util.UUID;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import io.github.fablabsmc.fablabs.impl.eam.EntityAttributeModifierRegistry;

import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.nbt.CompoundTag;

@Mixin(EntityAttributeModifier.class)
public class EntityAttributeModifierMixin {
	/**
	 * Required because Mojang uses reference comparison for some silly things, and since Item objects store their
	 * Entity Attribute Modifiers as tags, each time it's accessed it has a new UUID object. This ensures we use the
	 * same UUID object for each instance of our Entity Attribute Modifier that has the same UUID value.
	 */
	@Redirect(method = "fromTag", at = @At(value = "INVOKE", target = "Lnet/minecraft/nbt/CompoundTag;getUuidNew(Ljava/lang/String;)Ljava/util/UUID;"))
	private static UUID getRegisteredUUID(CompoundTag compoundTag, String key) {
		UUID uUID = compoundTag.getUuidNew(key);

		if (EntityAttributeModifierRegistry.contains(uUID)) {
			return EntityAttributeModifierRegistry.get(uUID);
		}

		return uUID;
	}
}
