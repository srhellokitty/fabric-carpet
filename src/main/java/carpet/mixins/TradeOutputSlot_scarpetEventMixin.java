package carpet.mixins;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.TradeOutputSlot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.village.Merchant;
import net.minecraft.village.TradeOffer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import static carpet.script.CarpetEventServer.Event.PLAYER_TRADES;

@Mixin(value = TradeOutputSlot.class)
public abstract class TradeOutputSlot_scarpetEventMixin {
    @Shadow @Final private Merchant merchant;

    @Inject(method = "onTakeItem", require = 0, at = @At(value = "INVOKE",
            target = "Lnet/minecraft/village/Merchant;trade(Lnet/minecraft/village/TradeOffer;)V"),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void onTrade(PlayerEntity player, ItemStack stack, CallbackInfoReturnable cir, TradeOffer tradeOffer) {
        if(PLAYER_TRADES.isNeeded() && !this.merchant.getMerchantWorld().isClient())
        {
            PLAYER_TRADES.onTrade((ServerPlayerEntity) player, merchant, tradeOffer);
        }
    }
}
