package de.arbeeco.minecalc.client.gui.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import de.arbeeco.minecalc.client.MinecalcClient;
import de.arbeeco.minecalc.client.gui.widget.ATextField;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

public class CalcScreen extends Screen {
	private final MinecraftClient client;
	private static final Identifier TEXTURE = new Identifier("minecalc", "textures/gui/calculator.png");
	public static ATextField textField;
	private int scaledWidth;
	private int scaledHeight;
	public boolean isInit = false;
	int count = 0;
	static String[] calc = {
			"AC", "(", ")", "/",
			"7", "8", "9", "*",
			"4", "5", "6", "-",
			"1", "2", "3", "+",
			"0", ",", "«", "="
	};
	static String[] calcUtil = {
			"x",
			"y",
			"z",
			"⎘",
			"☰"
	};
	public static ButtonWidget[] buttons = new ButtonWidget[calc.length + calcUtil.length];

	public CalcScreen(MinecraftClient minecraftClient) {
		super(Text.translatable("gui.minecalc.calculator"));
		client = minecraftClient;
	}

	public void init() {
		clearChildren();
		setFocused(null);
		PlayerEntity playerEntity = this.getCameraPlayer();
		if (playerEntity != null) {
			scaledWidth = client.getWindow().getScaledWidth();
			scaledHeight = client.getWindow().getScaledHeight();
			textField = new ATextField(client.textRenderer, scaledWidth - 85, scaledHeight - 130, 80, 20, textField, Text.literal(""));
			for (int i = 0; i < calc.length; i++) {
				addButton(calc, i);
			}
			for (int i = 0; i < calcUtil.length; i++) {
				addButton(calcUtil, i);
			}
			addSelectableChild(textField);
			setInitialFocus(textField);
			isInit = true;
			super.init();
		}
	}

	public void render(MatrixStack matrices, float tickDelta) {
		PlayerEntity playerEntity = this.getCameraPlayer();
		if (playerEntity != null) {
			if (textField != null && buttons != null) {
				if (MinecalcClient.config.showCalculator) {
					renderCalculator(matrices);

					textField.render(matrices, (int) getX(), (int) getY(), tickDelta);

					for (ButtonWidget button : buttons) {
						button.render(matrices, (int) getX(), (int) getY(), tickDelta);
					}
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

	private ButtonWidget addButton(String[] in, int index) {
		if (count == calc.length + calcUtil.length) {
			count = 0;
		}
		ButtonWidget button;
		int x = scaledHeight - 25 - (calc.length - index - 1) / 4 * 20;
		int y = scaledWidth - 25 - (calc.length - index - 1) % 4 * 20;
		switch (in[index]) {
			case "AC" -> {
				button = new ButtonWidget(y, x, 20, 20, Text.literal(in[index]), (button1) -> {
					textField.setText("");
				});
			}
			case "«" -> {
				button = new ButtonWidget(y, x, 20, 20, Text.literal(in[index]), (button1) -> {
					if (textField.getText().length() > 0) {
						textField.setText(textField.getText().substring(0, textField.getText().length() - 1));
					}
				});
			}
			case "=" -> {
				button = new ButtonWidget(y, x, 20, 20, Text.literal(in[index]), (button1) -> {
					textField.calculate(textField.getText());
				});
			}
			case "⎘", "☰", "x", "y", "z" -> {
				x = scaledHeight - 25 - 20 * (calcUtil.length - index - 1);
				y = scaledWidth - 30 - 20 * 4;
				switch (in[index]) {
					case "⎘" -> {
						button = new ButtonWidget(y, x, 20, 20, Text.literal(in[index]), (button1) -> {
							if (textField.getText().contains(buttons[19].getMessage().getString())) {
								MinecraftClient.getInstance().keyboard.setClipboard(textField.getText().split(buttons[19].getMessage().getString())[1]);
							}
						});
					}
					case "☰" -> {
						button = new ButtonWidget(y, x, 20, 20, Text.literal(in[index]), (button1) -> {
							MinecraftClient.getInstance().setScreen(new CalcUtilityScreen(Text.translatable("gui.minecalc.calcmenu")));
						});
					}
					default -> {
						button = new ButtonWidget(y, x, 20, 20, Text.literal(in[index]), (button1) -> {
							textField.setText(textField.getText() + getCoord(in[index]));
						});
					}
				}
			}
			default -> {
				button = new ButtonWidget(y, x, 20, 20, Text.literal(in[index]), (button1) -> {
					textField.setText(textField.getText() + button1.getMessage().getString());
				});
			}
		}
		buttons[count] = button;
		count++;
		addSelectableChild(button);
		return button;
	}

	private PlayerEntity getCameraPlayer() {
		return !(client.getCameraEntity() instanceof PlayerEntity) ? null : (PlayerEntity)client.getCameraEntity();
	}

	private double getX() {
		if(client.mouse.isCursorLocked()) {
			return 0;
		}
		return (int)(this.client.mouse.getX() * this.client.getWindow().getScaledWidth() / (double)this.client.getWindow().getWidth());
	}

	private double getY() {
		if(client.mouse.isCursorLocked()) {
			return 0;
		}
		return (int)(this.client.mouse.getY() * this.client.getWindow().getScaledHeight() / (double)this.client.getWindow().getHeight());
	}

	private int getCoord(String axis) {
		return switch (axis) {
			case "x" -> (int) getCameraPlayer().getX();
			case "y" -> (int) getCameraPlayer().getY();
			case "z" -> (int) getCameraPlayer().getZ();
			default -> 000;
		};
	}

	@Override
	public void closeScreen() {
		super.closeScreen();
		MinecalcClient.config.showCalculator = !MinecalcClient.config.showCalculator;
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		return super.mouseClicked(mouseX, mouseY, button);
	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}

	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
			client.setScreen(null);
			return true;
		}
		if (keyCode == GLFW.GLFW_KEY_ENTER || keyCode == GLFW.GLFW_KEY_KP_ENTER) {
			textField.calculate(textField.getText());
			return true;
		}
		focusOn(textField);
		return super.keyPressed(keyCode, scanCode, modifiers);
	}
}
