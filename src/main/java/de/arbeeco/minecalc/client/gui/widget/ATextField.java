package de.arbeeco.minecalc.client.gui.widget;

import io.github.cottonmc.cotton.gui.widget.WTextField;
import org.lwjgl.glfw.GLFW;
import org.mariuszgromada.math.mxparser.Expression;

public class ATextField extends WTextField {
	@Override
	public void onKeyPressed(int ch, int key, int modifiers) {
		super.onKeyPressed(ch, key, modifiers);
		if (key == 36) {
			releaseFocus();
			calculate(getText());
		}
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
