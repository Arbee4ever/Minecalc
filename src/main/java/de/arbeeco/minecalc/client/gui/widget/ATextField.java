package de.arbeeco.minecalc.client.gui.widget;

import io.github.cottonmc.cotton.gui.widget.WTextField;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.MutableText;
import org.lwjgl.glfw.GLFW;
import org.mariuszgromada.math.mxparser.Expression;

public class ATextField extends TextFieldWidget {

	public ATextField(TextRenderer textRenderer, int x, int y, int width, int height, MutableText text) {
		super(textRenderer, x, y, width, height, text);
	}

	public void calculate(String in) {
		setText(in.split("=")[0]);
		Expression eq = new Expression(getText());
		setText(eq.getExpressionString() + "=" + eq.calculate());
		if(getText().endsWith(".0")) {
			setText(getText().substring(0, getText().length() - 2));
		}
	}
}
