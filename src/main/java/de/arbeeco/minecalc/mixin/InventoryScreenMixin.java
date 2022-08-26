package de.arbeeco.minecalc.mixin;

import de.arbeeco.minecalc.client.MinecalcClient;
import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.screen.recipebook.RecipeBookProvider;
import net.minecraft.client.gui.screen.recipebook.RecipeBookWidget;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InventoryScreen.class)
public abstract class InventoryScreenMixin extends AbstractInventoryScreen<PlayerScreenHandler> {
	@Shadow
	@Final
	private RecipeBookWidget recipeBook;
	private static final Identifier CALCULATOR_BUTTON_TEXTURE = new Identifier("minecalc", "textures/gui/calculator_button.png");

	public InventoryScreenMixin(PlayerScreenHandler screenHandler, PlayerInventory playerInventory, Text text) {
		super(screenHandler, playerInventory, text);
	}

	@Inject(method = "init", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/ingame/InventoryScreen;addDrawableChild(Lnet/minecraft/client/gui/Element;)Lnet/minecraft/client/gui/Element;"))
	public void init(CallbackInfo ci) {
		this.x = recipeBook.findLeftEdge(this.width, this.backgroundWidth);
		addDrawableChild(new TexturedButtonWidget(this.x + 129, this.height / 2 - 22, 20, 18, 0, 0, 19, CALCULATOR_BUTTON_TEXTURE, (button) -> {
			this.x = recipeBook.findLeftEdge(this.width, this.backgroundWidth);
			((TexturedButtonWidget)button).setPos(this.x + 129, this.height / 2 - 22);
			MinecalcClient.config.showCalculator = !MinecalcClient.config.showCalculator;
		}));
	}
}
