package de.arbeeco.minecalc.mixin;

import de.arbeeco.minecalc.client.MinecalcClient;
import de.arbeeco.minecalc.client.gui.hud.CalulatorHud;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class InGameHudMixin extends DrawableHelper {
	private static CalulatorHud calculatorHud;

	@Inject(method = "<init>", at = @At(value = "TAIL"))
	public void init(MinecraftClient minecraftClient, ItemRenderer itemRenderer, CallbackInfo ci) {
		calculatorHud = new CalulatorHud(minecraftClient);
	}

	@Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/ChatHud;render(Lnet/minecraft/client/util/math/MatrixStack;I)V"))
	public void render(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
		calculatorHud.render(matrices, tickDelta);
	}

	@Inject(method = "clear", at = @At(value = "TAIL"))
	public void clear(CallbackInfo ci) {
		calculatorHud.clear();
	}

	public CalulatorHud getCalculatorHud() {
		return calculatorHud;
	}
}
