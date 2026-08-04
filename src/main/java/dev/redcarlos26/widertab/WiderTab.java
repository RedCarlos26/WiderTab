/*
 * Written by RedCarlos26
 *
 * This file is part of WiderTab.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package dev.redcarlos26.widertab;

import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WiderTab implements ClientModInitializer {
    public static final String NAMESPACE = "widertab";

    private static final Logger LOGGER = LoggerFactory.getLogger("Wider Tab");
    private static WiderTab INSTANCE;

    @Override
    public void onInitializeClient() {
        INSTANCE = this;

        WiderTabConfig config = WiderTabConfig.get();
        log("Initializing Wider Tab (max players: " + config.maxPlayers()
            + ", max rows per column: " + config.maxRowsPerColumn() + ")");
    }

    public static void log(String info) {
        LOGGER.info("[Wider Tab] {}", info);
    }

    public static void warn(String info) {
        LOGGER.warn("[Wider Tab] {}", info);
    }

    public static WiderTab get() {
        return INSTANCE;
    }
}
