package de.arbeeco.minecalc.client.gui.widget;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.mariuszgromada.math.mxparser.Expression;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class GraphDisplayWidget extends ClickableWidget implements Drawable {
	private final int width;
	private final int height;
	private final int x;
	private final int y;
	private String function = "";
	private final HashMap<Integer, Integer> values = new HashMap<>();
	private int scale = 4;
	private int minX = 0;
	private int minY = 0;
	private int maxX;
	private int maxY;
	private static final Identifier MINECALC_GRAPHING_BACKGROUND_TEXTURE = new Identifier("minecalc", "textures/gui/graphing_calculator/background.png");

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
		context.drawTexture(MINECALC_GRAPHING_BACKGROUND_TEXTURE, x, y, minX * scale, minY * scale, width, height, 16, 16);
		if (Objects.equals(function, "")) return;
		for (int i = minX - 1; i <= maxX; i++) {
			if (values.get(i) == null) {
				Expression eq = new Expression(function.replace("x", String.valueOf(i)));
				values.put(i, (int) eq.calculate());
			}
			if(values.get(i-1) != null) {
				drawLine(context, i - 1, values.get(i - 1), i, values.get(i), Color.BLACK.getRGB());
			}
		}
	}

	@Override
	protected void appendClickableNarrations(NarrationMessageBuilder builder) {

	}

	private void drawLine(DrawContext context, int x0, int y0, int x1, int y1, int color) {
		int dx = Math.abs(x1 - x0);
		int dy = Math.abs(y1 - y0);
		int sx = x0 < x1 ?  1 : -1;
		int sy = y0 < y1 ?  1 : -1;
		int err = dx - dy;
		int e2;

		while (true) {
			int pixelX = x + x0 * scale - (minX * scale);
			int pixelY = (y + height - (y0 * scale)) - (minY * scale);
			if (pixelY - scale > y - 1 && pixelY <= y + height) {
				context.fill(RenderLayer.getGuiOverlay(), pixelX, pixelY, pixelX + scale, pixelY - scale, color);
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
	public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
		/*if(button == 0) {
			minX += deltaX;
			minY += deltaY;
			maxX += deltaX;
			maxY += deltaY;
		}*/
		return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
	}

	public void resetScroll() {
		minX = 0;
		maxX = width / scale;
		minY = 0;
		maxY = height / scale;
	}

	public void scrollX(int amount) {
		minX += amount;
		maxX += amount;
	}

	public void scrollY(int amount) {
		minY -= amount;
		maxY -= amount;
	}

	public void zoom(int amount) {
		scale += amount;
	}

	public void setFunction(String function) {
		values.clear();
		this.function = function;
	}
}
