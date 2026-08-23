package com.jamesstevenson.brmc.gate;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

import com.jamesstevenson.brmc.BrmcMod;

import net.fabricmc.loader.api.FabricLoader;

/**
 * Hop fallback is never the player-facing gate language. It is off unless an
 * operator opts in for isolated backend tests.
 */
public final class BrmcGateConfig {
	public static final String FLAG = "brmc.devAllowHopGates";
	public static final String ENV = "BRMC_DEV_ALLOW_HOP_GATES";

	private BrmcGateConfig() {
	}

	/**
	 * True only when an explicit opt-in is set. A Fabric development workspace
	 * is not enough — playtests often run from one.
	 */
	public static boolean allowHopGates() {
		return truthy(System.getProperty(FLAG))
			|| truthy(System.getenv(ENV))
			|| truthy(readConfigValue());
	}

	private static boolean truthy(String raw) {
		return raw != null && Boolean.parseBoolean(raw.trim());
	}

	private static String readConfigValue() {
		Path file = FabricLoader.getInstance().getConfigDir().resolve("brmc.properties");
		if (!Files.isRegularFile(file)) {
			return null;
		}

		Properties properties = new Properties();
		try (InputStream in = Files.newInputStream(file)) {
			properties.load(in);
		} catch (IOException exception) {
			BrmcMod.LOGGER.error("Failed to read {}", file, exception);
			return null;
		}

		return properties.getProperty(FLAG);
	}
}
