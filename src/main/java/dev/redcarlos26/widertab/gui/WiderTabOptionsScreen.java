/*
 * Written by RedCarlos26
 *
 * This file is part of WiderTab.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package dev.redcarlos26.widertab.gui;

import dev.redcarlos26.widertab.WiderTabConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.Options;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.options.OptionsSubScreen;
import net.minecraft.network.chat.Component;

/**
 * Settings screen, reachable from Mod Menu.
 *
 * <p>Built entirely from vanilla option widgets, so it looks and behaves like
 * the regular Minecraft option screens and needs no extra libraries. Changes
 * apply as soon as a slider moves and are written to disk when the screen is
 * closed.</p>
 */
public class WiderTabOptionsScreen extends OptionsSubScreen {
    private static final Component TITLE = Component.translatable("widertab.config.title");

    private final OptionInstance<Integer> maxPlayers;
    private final OptionInstance<Integer> maxRowsPerColumn;

    public WiderTabOptionsScreen(Screen lastScreen) {
        super(lastScreen, Minecraft.getInstance().options, TITLE);

        WiderTabConfig config = WiderTabConfig.get();

        this.maxPlayers = new OptionInstance<>(
            "widertab.option.max_players",
            OptionInstance.cachedConstantTooltip(
                Component.translatable("widertab.option.max_players.tooltip")),
            (caption, value) -> Options.genericValueLabel(caption, value),
            new OptionInstance.IntRange(WiderTabConfig.MIN_PLAYERS, WiderTabConfig.MAX_PLAYERS),
            config.maxPlayers(),
            value -> WiderTabConfig.get().setMaxPlayers(value)
        );

        this.maxRowsPerColumn = new OptionInstance<>(
            "widertab.option.max_rows_per_column",
            OptionInstance.cachedConstantTooltip(
                Component.translatable("widertab.option.max_rows_per_column.tooltip")),
            (caption, value) -> Options.genericValueLabel(caption, value),
            new OptionInstance.IntRange(
                WiderTabConfig.MIN_ROWS_PER_COLUMN, WiderTabConfig.MAX_ROWS_PER_COLUMN),
            config.maxRowsPerColumn(),
            value -> WiderTabConfig.get().setMaxRowsPerColumn(value)
        );
    }

    @Override
    protected void addOptions() {
        if (this.list != null) {
            this.list.addSmall(this.maxPlayers, this.maxRowsPerColumn);
        }
    }

    @Override
    public void onClose() {
        super.onClose();
        WiderTabConfig.get().save();
    }

    @Override
    public void removed() {
        // Deliberately not calling super: WiderTab never touches options.txt,
        // so there is nothing in the vanilla options to save here.
    }
}
