/*
 * Copyright (c) 2018, Cameron <https://github.com/noremac201>, SoyChai <https://github.com/SoyChai>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.xpdropextended;

import com.google.inject.Provides;
import net.runelite.api.*;
import net.runelite.api.events.ScriptPostFired;
import net.runelite.api.events.ScriptPreFired;
import net.runelite.api.widgets.Widget;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import javax.inject.Inject;

import static net.runelite.api.ScriptID.XPDROPS_SETDROPSIZE;

@PluginDescriptor(
	name = "XP Drop Extended",
	description = "Extended functionality to the existing XP Drop plugin.",
	tags = {"experience", "levels", "tick", "skilling", "skill", "xpdrop"}
)
public class XpDropExtendedPlugin extends Plugin {
	@Inject
	private Client client;

	@Inject
	private XpDropExtendedConfig config;
	private Widget currentWidget;

	@Provides
	XpDropExtendedConfig provideConfig(ConfigManager configManager) {
		return configManager.getConfig(XpDropExtendedConfig.class);
	}

	@Subscribe
	public void onScriptPreFired(ScriptPreFired scriptPreFired) {
		if (scriptPreFired.getScriptId() == XPDROPS_SETDROPSIZE) {
			final int[] intStack = client.getIntStack();
			final int intStackSize = client.getIntStackSize();
			// This runs prior to the proc being invoked, so the arguments are still on the stack.
			// Grab the first argument to the script.
			currentWidget = client.getWidget(intStack[intStackSize - 4]);
			final int widgetId = intStack[intStackSize - 4];
		}
	}

	@Subscribe
	public void onScriptPostFired(ScriptPostFired scriptPostFired) {
		if (scriptPostFired.getScriptId() == XPDROPS_SETDROPSIZE) {
			if (config.hideMaxXPIcon()) {
				hideMaxXPIcon(currentWidget);
			}
		}
	}

	private void hideMaxXPIcon(Widget xpDrop) {
		Widget text = xpDrop.getChild(0);
		Widget icon = xpDrop.getChild(1);
		if (text == null || icon == null) {
			return;
		}
		if (text.getText().startsWith("<img=11>")) {
			text.setText(text.getText().substring(9));
			text.revalidate();
			icon.setOriginalX(14);
			icon.revalidate();
		}
	}
}

