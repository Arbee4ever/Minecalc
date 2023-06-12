package de.arbeeco.minecalc.registries;

import de.arbeeco.minecalc.client.MinecalcClient;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class MinecalcKeybinds {
	public static KeyBinding keyBindingR;
	public static void setupKeybinds() {
		keyBindingR = KeyBindingHelper.registerKeyBinding(new KeyBinding(
				"key.minecalc.opencalc",
				InputUtil.Type.KEYSYM,
				GLFW.GLFW_KEY_R,
				"category.minecalc.keycategory"
		));
		registerClientTickEvents();
	}

	public static void registerClientTickEvents() {
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			while (keyBindingR.wasPressed()) {
				if (!MinecalcClient.config.showCalculator) return;
				if (client.currentScreen == null) {
					client.setScreen(MinecalcClient.calcHud);
				} else {
					client.setScreen(null);
				}
			}
		});
	}
}
