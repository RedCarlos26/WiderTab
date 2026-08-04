/*
 * Written by RedCarlos26
 *
 * This file is part of WiderTab.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package dev.redcarlos26.widertab.gui;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

/**
 * Hooks the settings screen into Mod Menu's mod list.
 *
 * <p>Mod Menu is an optional dependency: this class is only ever loaded by Mod
 * Menu itself through the {@code modmenu} entrypoint, so the mod keeps working
 * normally when Mod Menu is not installed.</p>
 */
public class ModMenuIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return WiderTabOptionsScreen::new;
    }
}
