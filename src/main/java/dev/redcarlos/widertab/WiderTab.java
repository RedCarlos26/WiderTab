/*
 * Copyright © 2023 RedCarlos26
 *
 * This file is part of WiderTab.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package dev.redcarlos.widertab;

import net.fabricmc.api.ClientModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WiderTab implements ClientModInitializer {
    public static final String NAMESPACE = "widertab";
    private static WiderTab INSTANCE;
    public final Logger logger = LogManager.getLogger(NAMESPACE);

    @Override
    public void onInitializeClient() {
        INSTANCE = this;
        this.log("Initializing WiderTab...");
    }

    public void log(String info) {
        this.logger.info("[WiderTab] " + info);
    }

    public void warn(String info) {
        this.logger.warn("[WiderTab] " + info);
    }

    public static WiderTab get() {
        return INSTANCE;
    }
}
