/*
 * Written by RedCarlos26
 *
 * This file is part of WiderTab.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package dev.redcarlos26.widertab;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.fabricmc.loader.api.FabricLoader;

import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * User configuration, stored in {@code .minecraft/config/widertab.json} and
 * editable in game through the Mod Menu settings screen.
 *
 * <p>The file is created with the default values the first time the mod runs.
 * Any problem while reading it is logged and falls back to the defaults, so a
 * broken config can never crash the game.</p>
 */
public final class WiderTabConfig {
    /** Number of players the tab list is allowed to show. Vanilla uses 80. */
    public static final int DEFAULT_MAX_PLAYERS = 180;
    /** Number of rows before the tab list starts a new column. Vanilla uses 20. */
    public static final int DEFAULT_MAX_ROWS_PER_COLUMN = 20;

    public static final int MIN_PLAYERS = 10;
    public static final int MAX_PLAYERS = 1000;
    public static final int MIN_ROWS_PER_COLUMN = 1;
    public static final int MAX_ROWS_PER_COLUMN = 100;

    private static final String KEY_MAX_PLAYERS = "max_players";
    private static final String KEY_MAX_ROWS_PER_COLUMN = "max_rows_per_column";
    private static final String FILE_NAME = "widertab.json";

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private static volatile WiderTabConfig instance;

    private volatile int maxPlayers;
    private volatile int maxRowsPerColumn;

    private WiderTabConfig(int maxPlayers, int maxRowsPerColumn) {
        this.maxPlayers = maxPlayers;
        this.maxRowsPerColumn = maxRowsPerColumn;
    }

    public int maxPlayers() {
        return maxPlayers;
    }

    public int maxRowsPerColumn() {
        return maxRowsPerColumn;
    }

    /** Applies a new player limit, clamped to the supported range. Takes effect immediately. */
    public void setMaxPlayers(int value) {
        this.maxPlayers = clamp(value, MIN_PLAYERS, MAX_PLAYERS);
    }

    /** Applies a new column height, clamped to the supported range. Takes effect immediately. */
    public void setMaxRowsPerColumn(int value) {
        this.maxRowsPerColumn = clamp(value, MIN_ROWS_PER_COLUMN, MAX_ROWS_PER_COLUMN);
    }

    /** Returns the config, loading it from disk on first use. Never returns {@code null}. */
    public static WiderTabConfig get() {
        WiderTabConfig local = instance;

        if (local == null) {
            synchronized (WiderTabConfig.class) {
                local = instance;

                if (local == null) {
                    local = load();
                    instance = local;
                }
            }
        }

        return local;
    }

    /** Re-reads the config file and returns the new values. */
    public static WiderTabConfig reload() {
        WiderTabConfig reloaded = load();
        instance = reloaded;
        return reloaded;
    }

    /** Writes the current values to disk. Failures are logged, never thrown. */
    public void save() {
        Path path = configPath();

        if (path == null) {
            return;
        }

        JsonObject json = new JsonObject();
        json.addProperty(KEY_MAX_PLAYERS, this.maxPlayers);
        json.addProperty(KEY_MAX_ROWS_PER_COLUMN, this.maxRowsPerColumn);

        try {
            Path parent = path.getParent();

            if (parent != null) {
                Files.createDirectories(parent);
            }

            try (Writer writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
                GSON.toJson(json, writer);
            }
        } catch (Exception e) {
            WiderTab.warn("Could not write " + FILE_NAME + " (" + e + ").");
        }
    }

    private static int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }

    private static WiderTabConfig defaults() {
        return new WiderTabConfig(DEFAULT_MAX_PLAYERS, DEFAULT_MAX_ROWS_PER_COLUMN);
    }

    private static Path configPath() {
        try {
            return FabricLoader.getInstance().getConfigDir().resolve(FILE_NAME);
        } catch (Throwable t) {
            return null;
        }
    }

    private static WiderTabConfig load() {
        Path path = configPath();

        if (path == null) {
            return defaults();
        }

        if (!Files.isRegularFile(path)) {
            WiderTabConfig defaults = defaults();
            defaults.save();
            return defaults;
        }

        try (Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            JsonElement parsed = JsonParser.parseReader(reader);

            if (parsed == null || !parsed.isJsonObject()) {
                WiderTab.warn(FILE_NAME + " is not a JSON object, using default values.");
                return defaults();
            }

            JsonObject json = parsed.getAsJsonObject();

            return new WiderTabConfig(
                readInt(json, KEY_MAX_PLAYERS, DEFAULT_MAX_PLAYERS, MIN_PLAYERS, MAX_PLAYERS),
                readInt(json, KEY_MAX_ROWS_PER_COLUMN, DEFAULT_MAX_ROWS_PER_COLUMN,
                    MIN_ROWS_PER_COLUMN, MAX_ROWS_PER_COLUMN)
            );
        } catch (Exception e) {
            WiderTab.warn("Could not read " + FILE_NAME + " (" + e + "), using default values.");
            return defaults();
        }
    }

    private static int readInt(JsonObject json, String key, int fallback, int min, int max) {
        JsonElement element = json.get(key);

        if (element == null || !element.isJsonPrimitive()) {
            WiderTab.warn("Missing \"" + key + "\" in " + FILE_NAME + ", using " + fallback + ".");
            return fallback;
        }

        int value;

        try {
            value = element.getAsInt();
        } catch (RuntimeException e) {
            WiderTab.warn("\"" + key + "\" in " + FILE_NAME + " is not a number, using " + fallback + ".");
            return fallback;
        }

        if (value < min || value > max) {
            int clamped = clamp(value, min, max);
            WiderTab.warn("\"" + key + "\" must be between " + min + " and " + max
                + ", clamping " + value + " to " + clamped + ".");
            return clamped;
        }

        return value;
    }
}
