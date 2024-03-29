package de.arbeeco.minecalc.client;

import blue.endless.jankson.Jankson;
import blue.endless.jankson.JsonElement;
import blue.endless.jankson.JsonObject;
import de.arbeeco.minecalc.client.gui.screen.CalcScreen;
import de.arbeeco.minecalc.config.Config;
import de.arbeeco.minecalc.registries.MinecalcKeybinds;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mariuszgromada.math.mxparser.License;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;

public class MinecalcClient implements ClientModInitializer {
	public static final Logger logger = LogManager.getLogger();
	public static volatile Config config;
	public static CalcScreen calcHud;
	public static final Jankson jankson = Jankson.builder().build();

	@Override
	public void onInitializeClient() {
		License.iConfirmNonCommercialUse("Arbee");
		calcHud = new CalcScreen(MinecraftClient.getInstance());
		config = loadConfig();
		MinecalcKeybinds.setupKeybinds();
		HudRenderCallback.EVENT.register((matrixStack, deltaTick) -> {
			if (!calcHud.isInit) {calcHud.init();}
			calcHud.render(matrixStack, deltaTick);
		});
	}

	public static Config loadConfig() {
		try {
			File file = new File(FabricLoader.getInstance().getConfigDir().toString(), "minecalc.json");

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
			File file = new File(FabricLoader.getInstance().getConfigDir().toString(),"minecalc.json");

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
