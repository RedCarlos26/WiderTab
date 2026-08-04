/*
 * Written by RedCarlos26
 *
 * This file is part of WiderTab.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package dev.redcarlos26.widertab.mixin;

import dev.redcarlos26.widertab.WiderTabConfig;
import net.minecraft.client.gui.components.PlayerTabOverlay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(PlayerTabOverlay.class)
public class PlayerTabOverlayMixin {
    /**
     * Vanilla caps the tab list with {@code .limit(80L)} inside {@code getPlayerInfos}.
     */
    @ModifyConstant(method = "getPlayerInfos", constant = @Constant(longValue = 80L))
    private long widertab$widenPlayerLimit(long vanillaLimit) {
        return WiderTabConfig.get().maxPlayers();
    }

    /**
     * Vanilla starts a new column once a column holds more than 20 rows
     * ({@code MAX_ROWS_PER_COL}, inlined into {@code extractRenderState}).
     */
    @ModifyConstant(method = "extractRenderState", constant = @Constant(intValue = 20))
    private int widertab$widenRowsPerColumn(int vanillaRowsPerColumn) {
        return WiderTabConfig.get().maxRowsPerColumn();
    }
}
