package bond.thematic.soulbound.mixin;

import bond.thematic.core.registries.armors.armor.ThematicArmor;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.world.GameRules;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin {
    @Final
    @Shadow
    private PlayerInventory inventory;

    @Final
    @Shadow
    protected abstract void vanishCursedItems();

    @Inject(method = "dropInventory", at = @At("HEAD"), cancellable = true)
    public void dropInventory(CallbackInfo ci) {
        PlayerEntity player = ((PlayerEntity) (Object) this);
        ItemStack armorStack = player.getEquippedStack(EquipmentSlot.CHEST);

        if (armorStack.getItem() instanceof ThematicArmor) {
            this.inventory.dropAll();
            ci.cancel();
        }
        this.vanishCursedItems();
    }
}
