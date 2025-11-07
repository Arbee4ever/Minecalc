package de.arbeeco.minecalc.registries;

import de.arbeeco.minecalc.client.MinecalcClient;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

public class MinecalcKeybinds {
	private static final KeyBinding.Category MINECALC = KeyBinding.Category.create(Identifier.of("minecalc", "keycategory"));
	public static KeyBinding keyBindingToggleMode;
	public static KeyBinding keyBindingToggleUI;
	public static void setupKeybinds() {
		keyBindingToggleMode = KeyBindingHelper.registerKeyBinding(new KeyBinding(
				"key.minecalc.opencalc",
				InputUtil.Type.KEYSYM,
				GLFW.GLFW_KEY_R,
				MINECALC
		));
		registerClientTickEvents();
	}

	public static void registerClientTickEvents() {
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			while (keyBindingToggleMode.wasPressed()) {
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
