package com.github.jamies1211.fileSync.Config;

import ninja.leaping.configurate.ConfigurationNode;

import java.util.HashMap;

/**
 * Created by Jamie on 28-Jul-16.
 */
public class FileSyncDataInteraction {



	public static String getServerRootDirectory () {

		ConfigurationNode configFile = FileSyncConfig.getConfig().get();

		return configFile.getNode("ServerRootDirectoryPath").getString();
	}

	/** Returns map of file paths and chancels
	 *
	 * @return
	 */
	public static HashMap<String, String> getFileMap () {

		ConfigurationNode configFile = FileSyncConfig.getConfig().get();

		HashMap<String, String> fileMap = new HashMap<>();

		for (Object key : configFile.getNode("SyncedFiles").getChildrenMap().keySet()) {
			fileMap.put(key.toString(), getFileString(key.toString()));
		}

		return fileMap;
	}

	/** Returns command string from chanel
	 *
	 * @param channel
	 * @return
	 */
	public static String getFileString (String channel) {

		ConfigurationNode configFile = FileSyncConfig.getConfig().get();

		return configFile.getNode("SyncedFiles", channel).getString();
	}
}
