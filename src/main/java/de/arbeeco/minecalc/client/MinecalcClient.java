package de.arbeeco.minecalc.client;

import blue.endless.jankson.Jankson;
import blue.endless.jankson.JsonElement;
import blue.endless.jankson.JsonObject;
import com.mojang.blaze3d.platform.InputUtil;
import de.arbeeco.minecalc.client.gui.hud.CalcHud;
import de.arbeeco.minecalc.config.Config;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBind;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.loader.api.QuiltLoader;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;
import org.quiltmc.qsl.lifecycle.api.client.event.ClientTickEvents;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;

public class MinecalcClient implements ClientModInitializer {
	public static final Logger logger = LogManager.getLogger();
	public static volatile Config config;
	public static KeyBind keyBinding;

	public static final Jankson jankson = Jankson.builder().build();
	@Override
	public void onInitializeClient(ModContainer mod) {
		config = loadConfig();
		HudRenderCallback.EVENT.register((matrixStack, deltaTick) -> {
			new CalcHud(MinecraftClient.getInstance()).init(matrixStack, deltaTick);
		});
		keyBinding = KeyBindingHelper.registerKeyBinding(new KeyBind(
				"key.minecalc.opencalc",
				InputUtil.Type.KEYSYM,
				GLFW.GLFW_KEY_R,
				"category.minecalc.keycategory"
		));
		ClientTickEvents.END.register(client -> {
			while (keyBinding.wasPressed()) {
				if(client.currentScreen == null) {
					client.setScreen(new CalcHud(client));
				} else {
					client.setScreen(null);
				}
			}
		});
	}

	public static Config loadConfig() {
		try {
			File file = new File(QuiltLoader.getConfigDir().toString(),"minecalc.json");

			if (!file.exists()) saveConfig(new Config());

			JsonObject json = jankson.load(file);
			config =  jankson.fromJson(json, Config.class);
		} catch (Exception e) {
			logger.error("Error loading config: {}", e.getMessage());
		}
		return config;
	}

	public static void saveConfig(Config config) {
		try {
			File file = new File(QuiltLoader.getConfigDir().toString(),"minecalc.json");

			JsonElement json = jankson.toJson(config);
			String result = json.toJson(true, true);
			try (FileOutputStream out = new FileOutputStream(file, false)) {
				out.write(result.getBytes(StandardCharsets.UTF_8));
			}
		} catch (Exception e) {
			logger.error("Error saving config: {}", e.getMessage());
		}
	}
}
