package de.arbeeco.minecalc.mixin;

import de.arbeeco.minecalc.client.MinecalcClient;
import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InventoryScreen.class)
public abstract class InventoryScreenMixin extends AbstractInventoryScreen<PlayerScreenHandler> {
	@Unique private static final Identifier MINECALC_CALCULATOR_BUTTON_TEXTURE = new Identifier("minecalc", "textures/gui/calculator_button.png");
	private TexturedButtonWidget button;

	public InventoryScreenMixin(PlayerScreenHandler screenHandler, PlayerInventory playerInventory, Text text) {
		super(screenHandler, playerInventory, text);
	}

	@Inject(method = "init", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/ingame/InventoryScreen;addDrawableChild(Lnet/minecraft/client/gui/Element;)Lnet/minecraft/client/gui/Element;"))
	private void addMinecalcButton(CallbackInfo ci) {
		button = new TexturedButtonWidget(this.x + 129, this.height / 2 - 22, 20, 18, 0, 0, 19, MINECALC_CALCULATOR_BUTTON_TEXTURE, (button) -> {
			MinecalcClient.config.showCalculator = !MinecalcClient.config.showCalculator;
		});
		addDrawableChild(button);
	}

	@Inject(method = "method_19891", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/widget/ButtonWidget;setPosition(II)V"))
	private void moveRecipeBookButton(CallbackInfo ci) {
		button.setPosition(this.x + 129, this.height / 2 - 22);
	}
}
