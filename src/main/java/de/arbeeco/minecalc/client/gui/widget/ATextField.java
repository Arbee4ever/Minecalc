package de.arbeeco.minecalc.client.gui.widget;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.mariuszgromada.math.mxparser.Expression;

public class ATextField extends TextFieldWidget {
	public ATextField(TextRenderer textRenderer, int x, int y, int width, int height, MutableText text) {
		this(textRenderer, x, y, width, height, null, text);
	}

	public ATextField(TextRenderer textRenderer, int x, int y, int width, int height, TextFieldWidget copyFrom, MutableText text) {
		super(textRenderer, x, y, width, height, copyFrom, text);
	}

	public String calculate(String in) {
		setText(in.split("=")[0]);
		Expression eq = new Expression(getText());
		return String.valueOf(eq.calculate());
	}

	@Override
	public void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
		if (this.visible) {
			this.hovered = mouseX >= this.getX() && mouseY >= this.getY() && mouseX < this.getX() + this.width && mouseY < this.getY() + this.height;
			super.renderWidget(context, mouseX, mouseY, delta);
		}
	}
}
