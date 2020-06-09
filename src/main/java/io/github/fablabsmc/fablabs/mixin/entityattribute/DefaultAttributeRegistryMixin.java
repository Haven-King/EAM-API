package io.github.fablabsmc.fablabs.mixin.entityattribute;

import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.DefaultAttributeRegistry;

@Mixin(DefaultAttributeRegistry.class)
public class DefaultAttributeRegistryMixin {
	@Shadow private static Map<EntityType<? extends LivingEntity>, DefaultAttributeContainer> DEFAULT_ATTRIBUTE_REGISTRY;
	private static HashMap<EntityType<? extends LivingEntity>, DefaultAttributeContainer> TEMPORARY_BUILDING_MAP;

	@Inject(method = "<clinit>*", at = @At("HEAD"))
	private static void initializeOurmap(CallbackInfo ci) {
		// Necessary so that our map is initialized before the redirects. Presumably this is because Mixin puts our Mixin's
		// static blocks after those of the original class.
		TEMPORARY_BUILDING_MAP = new HashMap<>();
	}

	@Redirect(method = "<clinit>*", at = @At(value = "INVOKE", target = "com/google/common/collect/ImmutableMap$Builder.put(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;"))
	private static ImmutableMap.Builder<EntityType<? extends LivingEntity>, DefaultAttributeContainer> captureValues(ImmutableMap.Builder<EntityType<? extends LivingEntity>, DefaultAttributeContainer> builder, Object entityType, Object attributeContainer) {
		//noinspection unchecked
		TEMPORARY_BUILDING_MAP.put((EntityType<? extends LivingEntity>) entityType, (DefaultAttributeContainer) attributeContainer);

		return builder;
	}

	@Inject(method = "<clinit>*", at = @At("TAIL"))
	private static void assignOurMap(CallbackInfo ci) {
		DEFAULT_ATTRIBUTE_REGISTRY = TEMPORARY_BUILDING_MAP;
		System.out.printf("Our map is %d entries long", DEFAULT_ATTRIBUTE_REGISTRY.size());
	}
}
