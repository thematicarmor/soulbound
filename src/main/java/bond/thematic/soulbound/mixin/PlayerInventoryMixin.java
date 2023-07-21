package bond.thematic.soulbound.mixin;

import bond.thematic.core.registries.armors.armor.ThematicArmor;
import bond.thematic.core.registries.armors.armor.ThematicArmorAlt;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Iterator;
import java.util.List;

@Mixin(PlayerInventory.class)
public class PlayerInventoryMixin {

    @Final
    @Shadow
    private List<DefaultedList<ItemStack>> combinedInventory;

    @Final
    @Shadow
    public PlayerEntity player;


    @Inject(method = "dropAll", at = @At("HEAD"), cancellable = true)
    public void dropAll(CallbackInfo ci) {

        Iterator<DefaultedList<ItemStack>> var1 = this.combinedInventory.iterator();

        while (var1.hasNext()) {
            List<ItemStack> list = var1.next();

            for (int i = 0; i < list.size(); ++i) {
                ItemStack itemStack = list.get(i);
                if (!itemStack.isEmpty()) {
                    if (itemStack.getItem() instanceof ThematicArmor && player.getEquippedStack(EquipmentSlot.CHEST) == itemStack) continue;
                    this.player.dropItem(itemStack, true, false);
                    list.set(i, ItemStack.EMPTY);
                }
            }
        }

        ci.cancel();
    }
}
