package de.arbeeco.minecalc.client.gui.screen;

import de.arbeeco.minecalc.client.MinecalcClient;
import de.arbeeco.minecalc.client.gui.widget.ATextField;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.input.KeyInput;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

public class CalcScreen extends Screen {
	private static MinecraftClient client = null;
	private static final Identifier TEXTURE = Identifier.of("minecalc", "textures/gui/calculator.png");
	public static ATextField textField;
	public static ATextField resultField;
	private static int scaledWidth;
	private static int scaledHeight;
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
		if (client.getWindow() != null) {
			scaledWidth = client.getWindow().getScaledWidth();
			scaledHeight = client.getWindow().getScaledHeight();
			textField = new ATextField(client.textRenderer, scaledWidth - 85, scaledHeight - 150, 80, 20, Text.literal(""));
			resultField = new ATextField(client.textRenderer, scaledWidth - 85, scaledHeight - 130, 80, 20, Text.literal(""));
			for (int i = 0; i < calc.length; i++) {
				addButton(calc, i);
			}
			for (int i = 0; i < calcUtil.length; i++) {
				addButton(calcUtil, i);
			}
			addDrawableChild(textField);
			setInitialFocus(textField);
			addDrawableChild(resultField);
			isInit = true;
			super.init();
		}
	}

	public void render(DrawContext context, RenderTickCounter renderTickCounter) {
		renderBackground(context, (int) getX(), (int) getY(), renderTickCounter.getDynamicDeltaTicks());
		if (textField != null && resultField != null && buttons != null) {
			if (MinecalcClient.config.showCalculator) {
				textField.render(context, (int) getX(), (int) getY(), renderTickCounter.getDynamicDeltaTicks());
				resultField.render(context, (int) getX(), (int) getY(), renderTickCounter.getDynamicDeltaTicks());
				for (ButtonWidget button : buttons) {
					button.render(context, (int) getX(), (int) getY(), renderTickCounter.getDynamicDeltaTicks());
				}
			}
		}
	}

	@Override
	public void renderBackground(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
		if (MinecalcClient.config.showCalculator) {
			scaledWidth = client.getWindow().getScaledWidth();
			scaledHeight = client.getWindow().getScaledHeight();
			context.drawTexture(RenderPipelines.GUI_TEXTURED, TEXTURE, scaledWidth - 90, scaledHeight - 155, 0, 0, 90, 155, 256, 256);
		}
	}

	private ButtonWidget addButton(String[] in, int index) {
		if (count == calc.length + calcUtil.length) {
			count = 0;
		}
		ButtonWidget button;
		int y = scaledHeight - 25 - (calc.length - index - 1) / 4 * 20;
		int x = scaledWidth - 25 - (calc.length - index - 1) % 4 * 20;
		switch (in[index]) {
			case "AC" -> button = ButtonWidget.builder(Text.literal(in[index]), (button1) -> {
					textField.setText("");
				})
				.dimensions(x, y, 20, 20)
				.build();
			case "«" -> button = ButtonWidget.builder(Text.literal(in[index]), (button1) -> {
					if (textField.getText().length() > 0) {
						textField.setText(textField.getText().substring(0, textField.getText().length() - 1));
					}
				})
				.dimensions(x, y, 20, 20)
				.build();
			case "=" -> button = ButtonWidget.builder(Text.literal(in[index]), (button1) -> {
					resultField.setText(textField.calculate(textField.getText()));
				})
				.dimensions(x, y, 20, 20)
				.build();
			case "⎘", "☰", "x", "y", "z" -> {
				y = scaledHeight - 25 - 20 * (calcUtil.length - index - 1);
				x = scaledWidth - 30 - 20 * 4;
				switch (in[index]) {
					case "⎘" -> button = ButtonWidget.builder(Text.literal(in[index]), (button1) -> {
							if (textField.getText().contains(buttons[19].getMessage().getString())) {
								MinecraftClient.getInstance().keyboard.setClipboard(textField.getText().split(buttons[19].getMessage().getString())[1]);
							}
						})
						.dimensions(x, y, 20, 20)
						.build();
					case "☰" -> button = ButtonWidget.builder(Text.literal(in[index]), (button1) -> {
							MinecraftClient.getInstance().setScreen(new CalcUtilityScreen(Text.translatable("gui.minecalc.calcmenu")));
						})
						.dimensions(x, y, 20, 20)
						.build();
					default -> button = ButtonWidget.builder(Text.literal(in[index]), (button1) -> {
							textField.setText(textField.getText() + getCoord(in[index]));
						})
						.dimensions(x, y, 20, 20)
						.build();
				}
			}
			default -> button = ButtonWidget.builder(Text.literal(in[index]), (button1) -> {
					textField.setText(textField.getText() + button1.getMessage().getString());
				})
				.dimensions(x, y, 20, 20)
				.build();
		}
		buttons[count] = button;
		count++;
		addDrawableChild(button);
		return button;
	}

	private PlayerEntity getCameraPlayer() {
		return !(client.getCameraEntity() instanceof PlayerEntity) ? null : (PlayerEntity) client.getCameraEntity();
	}

	private static double getX() {
		return (int) (client.mouse.getX() * client.getWindow().getScaledWidth() / (double) client.getWindow().getWidth());
	}

	private static double getY() {
		return (int) (client.mouse.getY() * client.getWindow().getScaledHeight() / (double) client.getWindow().getHeight());
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
	public void close() {
		MinecalcClient.config.showCalculator = !MinecalcClient.config.showCalculator;
	}

	@Override
	public boolean mouseClicked(Click click, boolean doubled) {
		return super.mouseClicked(click, doubled);
	}

	@Override
	public boolean shouldPause() {
		return false;
	}

	@Override
	public boolean keyPressed(KeyInput input) {
		if (input.getKeycode() == GLFW.GLFW_KEY_ESCAPE) {
			client.setScreen(null);
			return true;
		}
		if (input.getKeycode() == GLFW.GLFW_KEY_ENTER || input.getKeycode() == GLFW.GLFW_KEY_KP_ENTER) {
			resultField.setText(textField.calculate(textField.getText()));
			return true;
		}
		setFocused(textField);
		return super.keyPressed(input);
	}
}
