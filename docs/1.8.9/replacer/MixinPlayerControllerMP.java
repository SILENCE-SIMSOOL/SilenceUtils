package silence.simsool.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

@Mixin(PlayerControllerMP.class)
public class MixinPlayerControllerMP {
	
	@Shadow private Minecraft mc;
	@Shadow private ItemStack currentItemHittingBlock;
	@Shadow private BlockPos currentBlock;
//	@Shadow private int blockHitDelay;

	@Overwrite
	private boolean isHittingPosition(BlockPos pos) {
		ItemStack itemstack = this.mc.thePlayer.getHeldItem();
		boolean flag = this.currentItemHittingBlock == null && itemstack == null;
		if (this.currentItemHittingBlock != null && itemstack != null) {
			String iName = itemstack.getDisplayName();
			if (Config.Replacer && (iName.contains("Drill") || iName.contains("Gemstone Gauntlet") || iName.contains("Pickonimbus"))) return pos.equals(pos);
			flag = itemstack.getItem() == this.currentItemHittingBlock.getItem() && ItemStack.areItemStackTagsEqual(itemstack, this.currentItemHittingBlock) && (itemstack.isItemStackDamageable() || itemstack.getMetadata() == this.currentItemHittingBlock.getMetadata());
		}
		return pos.equals(this.currentBlock) && flag;
	}

//	@Inject(method = "onPlayerDamageBlock", at = @At("HEAD"))
//	private void tweakHitDelay(BlockPos posBlock, EnumFacing directionFacing, CallbackInfoReturnable<Boolean> cir) {
//		if (Config.Replacer) this.blockHitDelay = 0;
//	}
}
