package de.arbeeco.minecalc.registries;

import com.mojang.blaze3d.platform.InputUtil;
import de.arbeeco.minecalc.client.MinecalcClient;
import de.arbeeco.minecalc.client.gui.hud.CalcHud;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBind;
import org.lwjgl.glfw.GLFW;
import org.quiltmc.qsl.lifecycle.api.client.event.ClientTickEvents;

public class MinecalcKeybinds {
	public static KeyBind keyBindingR;
	public static void setupKeybinds() {
		keyBindingR = KeyBindingHelper.registerKeyBinding(new KeyBind(
				"key.minecalc.opencalc",
				InputUtil.Type.KEYSYM,
				GLFW.GLFW_KEY_R,
				"category.minecalc.keycategory"
		));
		registerClientTickEvents();
	}

	public static void registerClientTickEvents() {
		ClientTickEvents.END.register(client -> {
			while (keyBindingR.wasPressed()) {
				if(client.currentScreen == null) {
					client.setScreen(MinecalcClient.calcHud);
				} else {
					client.setScreen(null);
				}
			}
		});
	}
}
