package de.arbeeco.minecalc.mixin;

import de.arbeeco.minecalc.client.MinecalcClient;
import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.checkerframework.checker.units.qual.A;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InventoryScreen.class)
public abstract class InventoryScreenMixin extends AbstractInventoryScreen<PlayerScreenHandler> {
	private static final Identifier CALCULATOR_BUTTON_TEXTURE = new Identifier("minecalc", "textures/gui/calculator_button.png");
	private TexturedButtonWidget button;

	public InventoryScreenMixin(PlayerScreenHandler screenHandler, PlayerInventory playerInventory, Text text) {
		super(screenHandler, playerInventory, text);
	}

	@Inject(method = "init", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/ingame/InventoryScreen;addDrawableChild(Lnet/minecraft/client/gui/Element;)Lnet/minecraft/client/gui/Element;"))
	public void init(CallbackInfo ci) {
		button = new TexturedButtonWidget(this.x + 129, this.height / 2 - 22, 20, 18, 0, 0, 19, CALCULATOR_BUTTON_TEXTURE, (button) -> {
			MinecalcClient.config.showCalculator = !MinecalcClient.config.showCalculator;
		});
		addDrawableChild(button);
	}

	@Inject(method = {"m_ekzothcc", "lambda$init$0", "method_19891"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/widget/TexturedButtonWidget;setPos(II)V"))
	public void addDrawableChild(CallbackInfo ci) {
		button.setPos(this.x + 129, this.height / 2 - 22);
	}
}
