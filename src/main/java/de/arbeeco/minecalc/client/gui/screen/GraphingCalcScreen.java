package de.arbeeco.minecalc.client.gui.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import de.arbeeco.minecalc.client.gui.widget.ATextField;
import de.arbeeco.minecalc.client.gui.widget.GraphDisplayWidget;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

public class GraphingCalcScreen extends Screen {
	private int screenWidth;
	private int screenHeight;
	private int width = 226;
	private int height = 245;
	private int x;
	private int y;
	private static final Identifier MINECALC_GRAPHING_WINDOW_TEXTURE = new Identifier("minecalc", "textures/gui/graphing_calculator/window.png");
	private ATextField functionField;
	private GraphDisplayWidget graphWidget;
	private ButtonWidget xUp;
	private ButtonWidget xDown;
	private ButtonWidget yUp;
	private ButtonWidget yDown;
	private ButtonWidget resetScoll;

	protected GraphingCalcScreen(Text title) {
		super(title);
	}

	@Override
	protected void init() {
		super.init();
		screenWidth = client.getWindow().getScaledWidth();
		screenHeight = client.getWindow().getScaledHeight();
		x = screenWidth / 2 - width / 2;
		y = screenHeight / 2 - height / 2;
		functionField = new ATextField(client.textRenderer, x + 7, y + 5, width - 14, 20, Text.literal(""));
		functionField.setMaxLength(999);
		graphWidget = new GraphDisplayWidget(x + 9, y + 28, width - 18, height - 37, Text.translatable("gui.minecalc.graphing"));
		xDown = ButtonWidget.builder(Text.literal("←"), (button) -> {
				graphWidget.scrollX(-1);
			})
			.dimensions(x, y + height, 20, 20)
			.build();
		xUp = ButtonWidget.builder(Text.literal("→"), (button) -> {
				graphWidget.scrollX(1);
			})
			.dimensions(x + width - 20, y + height, 20, 20)
			.build();
		yUp = ButtonWidget.builder(Text.literal("↑"), (button) -> {
				graphWidget.scrollY(1);
			})
			.dimensions(x - 20, y, 20, 20)
			.build();
		yDown = ButtonWidget.builder(Text.literal("↓"), (button) -> {
				graphWidget.scrollY(-1);
			})
			.dimensions(x - 20, y + height - 20, 20, 20)
			.build();
		resetScoll = ButtonWidget.builder(Text.literal("0"), (button -> {
				graphWidget.resetScroll();
			}))
			.dimensions(x - 20, y + height, 20, 20)
			.build();
		addDrawableChild(xDown);
		addDrawableChild(xUp);
		addDrawableChild(yUp);
		addDrawableChild(yDown);
		addDrawableChild(resetScoll);
		addSelectableChild(functionField);
		setInitialFocus(functionField);
		addDrawableChild(graphWidget);
	}

	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		super.render(context, mouseX, mouseY, delta);
		if (functionField != null) {
			functionField.render(context, mouseX, mouseY, delta);
		}
		this.drawWindow(context);
	}

	public void drawWindow(DrawContext context) {
		RenderSystem.enableBlend();
		context.drawTexture(MINECALC_GRAPHING_WINDOW_TEXTURE, x, y, 0, 0, width, height, 256, 256);
	}

	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
			client.setScreen(null);
			return true;
		}
		if (keyCode == GLFW.GLFW_KEY_ENTER || keyCode == GLFW.GLFW_KEY_KP_ENTER) {
			graphWidget.setFunction(functionField.getText());
			return true;
		}
		focusOn(functionField);
		return super.keyPressed(keyCode, scanCode, modifiers);
	}
}
