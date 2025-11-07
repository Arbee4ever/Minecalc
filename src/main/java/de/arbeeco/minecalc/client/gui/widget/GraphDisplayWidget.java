package de.arbeeco.minecalc.client.gui.widget;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.text.StringVisitable;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;
import org.mariuszgromada.math.mxparser.Expression;

import java.awt.*;
import java.util.HashMap;
import java.util.Objects;

public class GraphDisplayWidget extends ClickableWidget implements Drawable {
	private final int width;
	private final int height;
	private final int x;
	private final int y;
	private String function = "";
	private final HashMap<Double, Integer> values = new HashMap<>();
	private float scale = 4;
	private float minX = 0;
	private float minY = 0;
	private float maxX;
	private float maxY;
	private static final Identifier MINECALC_GRAPHING_BACKGROUND_TEXTURE = Identifier.of("minecalc", "textures/gui/graphing_calculator/background.png");

	public GraphDisplayWidget(int x, int y, int width, int height, Text message) {
		super(x, y, width, height, message);
		this.width = width;
		this.height = height;
		this.x = x;
		this.y = y;
		maxX = width / scale;
		maxY = height / scale;
	}

	@Override
	protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
		context.drawTexture(RenderPipelines.GUI_TEXTURED, MINECALC_GRAPHING_BACKGROUND_TEXTURE, x, y, minX*scale, minY*scale, width, height, (int) scale*4, (int) scale*4);
		if ((x-minX*scale + 3) > x && (y-minY*scale + 3)+height > y && (x-minX*scale + 3) < x+width && (y-minY*scale + 3)+height < y+height) {
			context.drawText(MinecraftClient.getInstance().textRenderer, "0", (int) (x - minX * scale + 3), (int) (y - minY * scale + 3) + height, Color.GRAY.getRGB(), false);
		}
		if ((y-minY*scale + 3)+height > y && (y-minY*scale + 3)+height < y+height) {
			context.drawHorizontalLine(x, x+width, (int) (y-minY*scale)+height, Color.GRAY.getRGB());
		}
		if ((x-minX*scale + 3) > x && (x-minX*scale + 3) < x+width) {
			context.drawVerticalLine((int) (x - minX * scale), y, y + height, Color.GRAY.getRGB());
		}
		if (Objects.equals(function, "")) return;
		for (int n = 0; n <= 52; n++) {
			double i = n;
			if (values.get(minX + i) == null) {
				Expression eq = new Expression(function.replace("x", String.valueOf(minX + i)));
				values.put(minX + i, (int) eq.calculate());
			}
			if(values.get(minX + i-1) != null) {
				drawLine(context, minX + i - 1, values.get(minX + i - 1), minX + i, values.get(minX + i), Color.BLACK.getRGB());
			}
		}
	}

	@Override
	protected void appendClickableNarrations(NarrationMessageBuilder builder) {

	}

	private void drawLine(DrawContext context, double x0, double y0, double x1, double y1, int color) {
		double dx = Math.abs(x1 - x0);
		double dy = Math.abs(y1 - y0);
		double sx = x0 < x1 ? 1 : -1;
		double sy = y0 < y1 ? 1 : -1;
		double err = dx - dy;
		double e2;

		while (true) {
			int pixelX = (int) (x + x0 * scale - (minX * scale));
			int pixelY = (int) ((y + height - (y0 * scale)) - (minY * scale));
			if (pixelY - scale > y - 1 && pixelY <= y + height) {
				context.fill(pixelX, pixelY, pixelX + 4, pixelY - 4, color);
			}
			if (x0 == x1 && y0 == y1) {
				break;
			}
			e2 =  2 * err;
			if (e2 > -dy) {
				err = err - dy;
				x0 = x0 + sx;
			}
			if (e2 < dx) {
				err = err + dx;
				y0 = y0 + sy;
			}
		}
	}

	@Override
	protected void onDrag(Click click, double offsetX, double offsetY) {
		if(click.button() == GLFW.GLFW_MOUSE_BUTTON_1) {
			scrollX(-(float) (offsetX / scale));
			scrollY(-(float) (offsetY / scale));
		}
		super.onDrag(click, offsetX, offsetY);
	}

	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
		zoom((int) verticalAmount);
		return true;
	}

	public void resetScroll() {
		scale = 4;
		minX = 0;
		maxX = width / scale;
		minY = 0;
		maxY = height / scale;
	}

	public void scrollX(float amount) {
		//amount = Math.round(amount);
		minX += amount;
		maxX += amount;
	}

	public void scrollY(float amount) {
		//amount = Math.round(amount);
		minY += amount;
		maxY += amount;
	}

	public void zoom(int amount) {
		scale += amount;
	}

	public void setFunction(String function) {
		values.clear();
		this.function = function;
	}
}
