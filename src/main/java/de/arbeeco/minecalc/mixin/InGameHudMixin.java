package de.arbeeco.minecalc.mixin;

import de.arbeeco.minecalc.client.gui.renderer.CalcHud;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class InGameHudMixin {
	private CalcHud calcHud;
	@Inject(method = "<init>", at = @At(value = "TAIL"))
	public void init(MinecraftClient minecraftClient, ItemRenderer itemRenderer, CallbackInfo ci) {
		this.calcHud = new CalcHud(minecraftClient, itemRenderer);
	}

	@Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;renderHotbar(FLnet/minecraft/client/util/math/MatrixStack;)V"))
	public void render(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
		this.calcHud.render(matrices, tickDelta);
	}

	public CalcHud getCalcHud() {
		return this.calcHud;
	}
}
