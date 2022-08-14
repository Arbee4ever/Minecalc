package de.arbeeco.minecalc.client.gui.hud;

import com.mojang.blaze3d.systems.RenderSystem;
import de.arbeeco.minecalc.client.MinecalcClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

public class CalulatorHud extends DrawableHelper {
	private static final Identifier TEXTURE = new Identifier("minecraft", "textures/gui/container/dispenser.png");
	private final MinecraftClient client;

	public CalulatorHud(MinecraftClient minecraftClient) {
		this.client = minecraftClient;
	}

	public void render(MatrixStack matrices, float tickDelta) {
		PlayerEntity playerEntity = this.getCameraPlayer();
		if (playerEntity != null) {
			if (isCalculatorShown()) {
				matrices.pop();
				matrices.translate(4.0, 8.0, 0.0);
				matrices.scale(1.0F, 1.0F, 1.0F);
				RenderSystem.setShader(GameRenderer::getPositionTexShader);
				RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
				RenderSystem.setShaderTexture(0, TEXTURE);
				int x = 0;
				int y = 0;
				drawTexture(matrices, x, y, 0, 0, 100, 100);
			/*int i = this.getVisibleLineCount();
			int j = this.visibleMessages.size();
			if (j > 0) {
				boolean bl = this.isChatFocused();
				float f = (float)this.getChatScale();
				int k = MathHelper.ceil((float)this.getWidth() / f);
				matrices.push();
				matrices.translate(4.0, 8.0, 0.0);
				matrices.scale(f, f, 1.0F);
				double d = (Double)this.client.options.getChatOpacity().get() * 0.8999999761581421 + 0.10000000149011612;
				double e = (Double)this.client.options.getTextBackgroundOpacity().get();
				double g = (Double)this.client.options.getChatLineSpacing().get();
				int l = this.method_44752();
				double h = -8.0 * (g + 1.0) + 4.0 * g;
				int m = 0;

				int o;
				int q;
				int r;
				int t;
				int u;
				for(int n = 0; n + this.scrolledLines < this.visibleMessages.size() && n < i; ++n) {
					ChatHudLine.C_ozgyzxme c_ozgyzxme = (ChatHudLine.C_ozgyzxme)this.visibleMessages.get(n + this.scrolledLines);
					if (c_ozgyzxme != null) {
						o = tickDelta - c_ozgyzxme.addedTime();
						if (o < 200 || bl) {
							double p = bl ? 1.0 : getMessageOpacityMultiplier(o);
							q = (int)(255.0 * p * d);
							r = (int)(255.0 * p * e);
							++m;
							if (q > 3) {
								int s = false;
								t = -n * l;
								u = (int)((double)t + h);
								matrices.push();
								matrices.translate(0.0, 0.0, 50.0);
								fill(matrices, -4, t - l, 0 + k + 4 + 4, t, r << 24);
								C_bzcwstys c_bzcwstys = c_ozgyzxme.tag();
								if (c_bzcwstys != null) {
									int v = c_bzcwstys.indicatorColor() | q << 24;
									fill(matrices, -4, t - l, -2, t, v);
									if (bl && c_ozgyzxme.endOfEntry() && c_bzcwstys.icon() != null) {
										int w = this.method_44720(c_ozgyzxme);
										Objects.requireNonNull(this.client.textRenderer);
										int x = u + 9;
										this.m_edltpzmv(matrices, w, x, c_bzcwstys.icon());
									}
								}

								RenderSystem.enableBlend();
								matrices.translate(0.0, 0.0, 50.0);
								this.client.textRenderer.drawWithShadow(matrices, c_ozgyzxme.content(), 0.0F, (float)u, 16777215 + (q << 24));
								RenderSystem.disableBlend();
								matrices.pop();
							}
						}
					}
				}

				long y = this.client.method_44714().method_44944();
				int z;
				if (y > 0L) {
					o = (int)(128.0 * d);
					z = (int)(255.0 * e);
					matrices.push();
					matrices.translate(0.0, 0.0, 50.0);
					fill(matrices, -2, 0, k + 4, 9, z << 24);
					RenderSystem.enableBlend();
					matrices.translate(0.0, 0.0, 50.0);
					this.client.textRenderer.drawWithShadow(matrices, Text.translatable("chat.queue", new Object[]{y}), 0.0F, 1.0F, 16777215 + (o << 24));
					matrices.pop();
					RenderSystem.disableBlend();
				}

				if (bl) {
					o = this.method_44752();
					z = j * o;
					int aa = m * o;
					q = this.scrolledLines * aa / j;
					r = aa * aa / z;
					if (z != aa) {
						int s = q > 0 ? 170 : 96;
						t = this.hasUnreadNewMessages ? 13382451 : 3355562;
						u = k + 4;
						fill(matrices, u, -q, u + 2, -q - r, t + (s << 24));
						fill(matrices, u + 2, -q, u + 1, -q - r, 13421772 + (s << 24));
					}
				}

				matrices.pop();
			}*/
			}
		}
	}

	private boolean isCalculatorShown() {
		return MinecalcClient.config.showCalculator;
	}

	public void clear() {
		this.client.method_44714().method_44945();
	}

	private PlayerEntity getCameraPlayer() {
		return !(this.client.getCameraEntity() instanceof PlayerEntity) ? null : (PlayerEntity)this.client.getCameraEntity();
	}
}
