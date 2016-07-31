package com.github.jamies1211.fileSync.Config;

import com.github.jamies1211.fileSync.FileSync;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.spongepowered.api.text.channel.MessageChannel;
import org.spongepowered.api.text.serializer.TextSerializers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by Jamie on 27-Jul-16.
 */
public class FileSyncConfig {

	private static FileSyncConfig config = new FileSyncConfig();

	public static FileSyncConfig getConfig() {
		return config;
	}

	private Path configFile = Paths.get(FileSync.getPlugin().getConfigDir() + "/fileSync.conf");
	private ConfigurationLoader<CommentedConfigurationNode> configLoader = HoconConfigurationLoader.builder().setPath(configFile).build();
	private CommentedConfigurationNode configNode;

	public void setup() {
		if (!Files.exists(configFile)) {
			try {
				Files.createFile(configFile);
				load();
				enterData();
				save();
				MessageChannel.TO_CONSOLE.send(TextSerializers.FORMATTING_CODE.deserialize("&9[FileSync] &eNew config file has been generated fresh"));
			} catch (IOException e) {
				e.printStackTrace();
				MessageChannel.TO_CONSOLE.send(TextSerializers.FORMATTING_CODE.deserialize("&9[FileSync] &eAn error has occurred when loading the config file"));

			}
		} else {
			load();

			MessageChannel.TO_CONSOLE.send(TextSerializers.FORMATTING_CODE.deserialize("&9[FileSync] &eConfig file successfully loaded"));
		}
	}

	public void load() {
		try {
			configNode = configLoader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void save() {
		try {
			configLoader.save(configNode);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public CommentedConfigurationNode get() {
		return configNode;
	}

	public void enterData() {
		get().getNode("ServerRootDirectoryPath").setValue("C:/Users/Jamie/Desktop/TestServer/");
		get().getNode("SyncedFiles", "0001").setValue("world/playerdata/%uuid%.dat");
		get().getNode("SyncedFiles", "0002").setValue("world/pokemon/%uuid%.pk");
		get().getNode("SyncedFiles", "0003").setValue("world/pokemon/%uuid%.pktemp");
		get().getNode("SyncedFiles", "0004").setValue("world/pokemon/%uuid%.comp");
		get().getNode("SyncedFiles", "0005").setValue("world/pokemon/%uuid%.comptemp");
	}
}
