package de.arbeeco.minecalc.client.gui.widget;

import de.arbeeco.minecalc.client.gui.screen.CalcScreen;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.MutableText;
import org.mariuszgromada.math.mxparser.Expression;

import java.util.Arrays;

public class ATextField extends TextFieldWidget {

	public ATextField(TextRenderer textRenderer, int x, int y, int width, int height, TextFieldWidget textFieldWidget,MutableText text) {
		super(textRenderer, x, y, width, height, textFieldWidget, text);
	}

	public void calculate(String in) {
		setText(in.split("=")[0]);
		Expression eq = new Expression(getText());
		setText(eq.getExpressionString() + "=" + eq.calculate());
	}
}
