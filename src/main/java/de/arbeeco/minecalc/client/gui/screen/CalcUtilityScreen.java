package de.arbeeco.minecalc.client.gui.screen;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class CalcUtilityScreen extends Screen {
	private int scaledWidth;
	private int scaledHeight;
	private static final Identifier BACKGROUND_TEXTURE = new Identifier("minecalc", "textures/gui/calculator_utility.png");

	protected CalcUtilityScreen(Text text) {
		super(text);
	}

	@Override
	public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
		renderBackground(graphics);
		scaledWidth = client.getWindow().getScaledWidth();
		scaledHeight = client.getWindow().getScaledHeight();
		Text text = Text.translatable("gui.minecalc.wip");
		textRenderer.draw(text, (float) (scaledWidth / 2 - textRenderer.getWidth(text) / 2), (float) scaledHeight / 2 - 5, 16777215, true, graphics.getMatrices().peek().getModel(), graphics.getVertexConsumers(), TextRenderer.TextLayerType.NORMAL, 0, 255);
		super.render(graphics, mouseX, mouseY, delta);
	}

	@Override
	public void renderBackground(GuiGraphics graphics) {
		super.renderBackground(graphics);
		int width = 360;
		int height = 175;
		graphics.drawTexture(BACKGROUND_TEXTURE, scaledWidth / 2 - width / 2, scaledHeight / 2 - height / 2, 0, 0, width, height, 360, 360);
	}
}
