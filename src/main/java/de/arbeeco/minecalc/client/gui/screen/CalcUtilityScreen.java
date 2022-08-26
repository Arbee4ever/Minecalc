package de.arbeeco.minecalc.client.gui.screen;

import com.mojang.blaze3d.systems.RenderSystem;
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
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		renderBackground(matrices);
		scaledWidth = client.getWindow().getScaledWidth();
		scaledHeight = client.getWindow().getScaledHeight();
		Text orderedText = Text.translatable("gui.minecalc.wip");
		textRenderer.drawWithShadow(matrices, orderedText, (float)(scaledWidth/2 - textRenderer.getWidth(orderedText) / 2), (float)scaledHeight/2-5, 16777215);
		super.render(matrices, mouseX, mouseY, delta);
	}

	@Override
	public void renderBackground(MatrixStack matrices) {
		super.renderBackground(matrices);
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.setShaderTexture(0, BACKGROUND_TEXTURE);
		int width = 360;
		int height = 175;
		drawTexture(matrices, scaledWidth/2-width/2, scaledHeight/2-height/2, 0, 0, width, height, 360, 360);
	}
}
