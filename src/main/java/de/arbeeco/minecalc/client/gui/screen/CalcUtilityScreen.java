package de.arbeeco.minecalc.client.gui.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
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
	public void render(DrawContext drawContext, int mouseX, int mouseY, float delta) {
		renderBackground(drawContext);
		scaledWidth = client.getWindow().getScaledWidth();
		scaledHeight = client.getWindow().getScaledHeight();
		Text orderedText = Text.translatable("gui.minecalc.wip");
		textRenderer.draw(orderedText, (float)(scaledWidth/2 - textRenderer.getWidth(orderedText) / 2), (float)scaledHeight/2-5, 16777215, true, drawContext.getMatrices().peek().getPositionMatrix(), drawContext.getVertexConsumers(), TextRenderer.TextLayerType.NORMAL, 0, 255);
		super.render(drawContext, mouseX, mouseY, delta);
	}

	@Override
	public void renderBackground(DrawContext drawContext) {
		super.renderBackground(drawContext);
		int width = 360;
		int height = 175;
		drawContext.drawTexture(BACKGROUND_TEXTURE, scaledWidth/2-width/2, scaledHeight/2-height/2, 0, 0, width, height, 360, 360);
	}
}
