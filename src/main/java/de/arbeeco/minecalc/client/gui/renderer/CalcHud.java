package de.arbeeco.minecalc.client.gui.renderer;

import com.mojang.blaze3d.systems.RenderSystem;
import de.arbeeco.minecalc.client.MinecalcClient;
import de.arbeeco.minecalc.client.gui.widget.ATextField;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class CalcHud extends Screen {
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

	public CalcHud(MinecraftClient minecraftClient) {
		super(Text.translatable("gui.minecalc.calculator"));
		client = minecraftClient;
		isPauseScreen();
	}

	public void render(MatrixStack matrices, float tickDelta) {
		PlayerEntity playerEntity = this.getCameraPlayer();
		if (playerEntity != null) {
			if (MinecalcClient.config.showCalculator) {
				renderCalculator(matrices);

				scaledWidth = client.getWindow().getScaledWidth();
				scaledHeight = client.getWindow().getScaledHeight();
				textField = addSelectableChild(new ATextField(client.textRenderer, scaledWidth - 85, scaledHeight - 130, 80, 20, textField ,Text.translatable("test")));

				textField.render(matrices, (int) client.mouse.getX(), (int) client.mouse.getY(), tickDelta);

				for (int i = 0; i < calc.length; i++) {
					addButton(i).render(matrices, (int) client.mouse.getX(), (int) client.mouse.getY(), tickDelta);
				}
			}
		}
	}

	@Override
	public void tick() {
		textField.tick();
	}

	private void renderCalculator(MatrixStack matrixStack) {
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

	private ButtonWidget addButton(int i) {
		ButtonWidget button;
		int x = scaledHeight - 25 - (calc.length - i - 1) / 4 * 20;
		int y = scaledWidth - 25 - (calc.length - i - 1) % 4 * 20;
		switch (i) {
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
		addDrawableChild(button);
		return button;
	}

	private PlayerEntity getCameraPlayer() {
		return !(client.getCameraEntity() instanceof PlayerEntity) ? null : (PlayerEntity)client.getCameraEntity();
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
}
