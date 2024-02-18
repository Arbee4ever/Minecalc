package de.arbeeco.minecalc.client.gui.screen;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class CalcUtilityScreen extends Screen {
	private int screenWidth;
	private int screenHeight;
	private int width = 360;
	private int height = 175;
	private int x;
	private int y;
	private static final Identifier BACKGROUND_TEXTURE = new Identifier("minecalc", "textures/gui/calculator_utility.png");

	protected CalcUtilityScreen(Text text) {
		super(text);
	}

	@Override
	protected void init() {
		super.init();
		screenWidth = client.getWindow().getScaledWidth();
		screenHeight = client.getWindow().getScaledHeight();
		x = screenWidth/2-width/2;
		y = screenHeight/2-height/2;
		ButtonWidget button = ButtonWidget.builder(Text.translatable("gui.minecalc.graphing"), (button1) -> {
				MinecraftClient.getInstance().setScreen(new GraphingCalcScreen(Text.translatable("gui.minecalc.graphing")));
			})
			.dimensions(x+5, y+5, 110, 20)
			.build();
		addDrawableChild(button);
	}

	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		super.render(context, mouseX, mouseY, delta);
	}

	@Override
	public void renderInGameBackground(DrawContext context) {
		super.renderInGameBackground(context);
		context.drawTexture(BACKGROUND_TEXTURE, x, y, 0, 0, width, height, 360, 360);
	}
}
