package de.arbeeco.minecalc.client.screens;

import io.github.cottonmc.cotton.gui.GuiDescription;
import io.github.cottonmc.cotton.gui.client.CottonClientScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;

public class CalcScreen extends CottonClientScreen {
	public CalcScreen(GuiDescription description) {
		super(description);
	}

	public CalcScreen(Text title, GuiDescription description) {
		super(title, description);
	}
}
