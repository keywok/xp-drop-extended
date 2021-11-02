package com.xpdropextended;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class XpDropExtendedTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(XpDropExtendedPlugin.class);
		RuneLite.main(args);
	}
}