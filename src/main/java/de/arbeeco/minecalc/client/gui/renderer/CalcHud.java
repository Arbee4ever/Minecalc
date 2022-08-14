package de.arbeeco.minecalc.client.gui.renderer;

import com.mojang.blaze3d.systems.RenderSystem;
import de.arbeeco.minecalc.client.MinecalcClient;
import de.arbeeco.minecalc.client.gui.widget.ATextField;
import io.github.cottonmc.cotton.gui.widget.WButton;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class CalcHud extends Screen implements HudRenderCallback {
	private final MinecraftClient client;
	private static final Identifier TEXTURE = new Identifier("minecalc", "textures/gui/calculator.png");
	private static ATextField textField;
	private int scaledWidth;
	private int scaledHeight;
	String[] calc = {
			"AC", "(", ")", "/",
			"7", "8", "9", "*",
			"4", "5", "6", "-",
			"1", "2", "3", "+",
			"0", ".", "«", "="
	};

	public CalcHud(MinecraftClient minecraftClient, ItemRenderer itemRenderer) {
		super(Text.translatable("gui.minecalc.calculator"));
		client = minecraftClient;
	}

	public void render(MatrixStack matrices, float tickDelta) {
		renderCalculator(tickDelta, matrices);
	}

	protected void init() {
	}

	/*public void tick() {
		textField.tick();
	}*/

	private void renderCalculator(float tickDelta, MatrixStack matrixStack) {
		PlayerEntity playerEntity = this.getCameraPlayer();
		if (playerEntity != null) {
			if (MinecalcClient.config.showCalculator) {
				scaledWidth = client.getWindow().getScaledWidth();
				scaledHeight = client.getWindow().getScaledHeight();
				RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
				RenderSystem.setShader(GameRenderer::getPositionTexShader);
				RenderSystem.setShaderTexture(0, TEXTURE);
				int j = this.getZOffset();
				this.setZOffset(-90);
				this.drawTexture(matrixStack, scaledWidth - 90, scaledHeight - 135, 0, 0, 90, 135);

				this.setZOffset(j);
			}
		}
	}

	private ButtonWidget addButton(int i) {
		ButtonWidget button;
		int x = scaledHeight - 25 - (calc.length - i - 1) / 4 * 20;
		int y = scaledWidth - 25 - (calc.length - i - 1) % 4 * 20;
		switch (i%4) {
			case 0 -> {
				button = new ButtonWidget(y, x, 20, 20, Text.translatable("gui.minecalc." + calc[i]), (button1) -> {
					textField.setText("");
				});
			}
			case 18 -> {
				button = new ButtonWidget(y, x, 20, 20, Text.translatable("gui.minecalc." + calc[i]), (button1) -> {
					textField.setText(textField.getText().substring(0, textField.getText().length() - 1));
				});
			}
			case 19-> {
				button = new ButtonWidget(y, x, 20, 20, Text.translatable("gui.minecalc." + calc[i]), (button1) -> {
					textField.calculate(textField.getText());
				});
			}
			default -> {
				button = new ButtonWidget(y, x, 20, 20, Text.translatable("gui.minecalc." + calc[i]), (button1) -> {
					textField.setText(textField.getText() + calc[i]);
				});
			}
		}
		addDrawable(button);
		return button;
	}

	private PlayerEntity getCameraPlayer() {
		return !(client.getCameraEntity() instanceof PlayerEntity) ? null : (PlayerEntity)client.getCameraEntity();
	}

	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		if (keyCode == 256 && this.shouldCloseOnEsc()) {
			closeScreen();
			return true;
		}
		if (keyCode == 82 && this.shouldCloseOnEsc()) {
			closeScreen();
			return true;
		}
		return super.keyPressed(keyCode, scanCode, modifiers);
	}

	@Override
	public void closeScreen() {
		super.closeScreen();
		MinecalcClient.config.showCalculator = !MinecalcClient.config.showCalculator;
	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}

	@Override
	public void onHudRender(MatrixStack matrixStack, float tickDelta) {
		scaledWidth = client.getWindow().getScaledWidth();
		scaledHeight = client.getWindow().getScaledHeight();
		textField = addSelectableChild(new ATextField(client.textRenderer, scaledWidth - 85, scaledHeight - 130, 80, 20, Text.translatable("test")));

		for(int i = 0; i<calc.length; i++) {
			addButton(i);
		}
	}
}
