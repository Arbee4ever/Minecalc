package de.arbeeco.minecalc.client.gui;

import de.arbeeco.minecalc.client.gui.widget.ATextField;
import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WButton;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.data.Insets;
import net.minecraft.text.Text;

public class CalcGui extends LightweightGuiDescription {
	WGridPanel root = new WGridPanel();
	ATextField textField = new ATextField();
	String[] calc = {
			"AC", "(", ")", "/",
			"7", "8", "9", "*",
			"4", "5", "6", "-",
			"1", "2", "3", "+",
			"0", ".", "«", "="
	};

	public CalcGui() {
		setRootPanel(root);
		root.setInsets(Insets.ROOT_PANEL);

		root.add(textField, 0, 0, 4, 3);
		textField.canResize();
		textField.setMaxLength(999999999);

		for(int i = 0; i<calc.length; i++) {
			switch (i) {
				case 0 -> addButton(i).setOnClick(() -> {
					textField.setText("");
				});
				case 18 -> addButton(i).setOnClick(() -> {
					textField.setText(textField.getText().substring(0, textField.getText().length() - 1));
				});
				case 19 -> addButton(i).setOnClick(() -> {
					textField.calculate(textField.getText());
				});
				default -> addButton(i);
			}
		}
		root.validate(this);
	}

	private WButton addButton(int i) {
		WButton button = new WButton(Text.literal(calc[i]));
		button.setOnClick(() -> {
			textField.setText(textField.getText() + calc[i]);
		});
		root.add(button, i % 4, i / 4 + 2);
		return button;
	}
}
