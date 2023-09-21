package com.dungeon.game.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.dungeon.DungeonMan;
import com.dungeon.misc.Utilities;

public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();

		config.setTitle("Dungeon Man");
		config.setWindowedMode(Utilities.SCREEN_WIDTH, Utilities.SCREEN_HEIGHT);
		new Lwjgl3Application(new DungeonMan(), config);
	}
}
