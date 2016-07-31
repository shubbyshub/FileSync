package com.github.jamies1211.fileSync;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

import com.github.jamies1211.fileSync.Actions.SendFile;
import com.github.jamies1211.fileSync.Config.FileSyncConfig;
import com.github.jamies1211.fileSync.Config.FileSyncDataInteraction;
import com.google.common.io.Files;
import com.google.inject.Inject;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.plugin.Dependency;
import org.spongepowered.api.plugin.Plugin;

import net.kaikk.mc.synx.SynX;
import net.kaikk.mc.synx.packets.ChannelListener;
import net.kaikk.mc.synx.packets.Packet;
import org.spongepowered.api.text.channel.MessageChannel;
import org.spongepowered.api.text.serializer.TextSerializers;

@Plugin(id = "filesync", name = "File Sync", version = "1.0.0",
		description = "Syncs files between servers",
		authors = {"JamieS1211"},
		dependencies = @Dependency(id="synx"))


public class FileSync implements ChannelListener {	@Inject

	public static FileSync plugin;

	@Inject
	@ConfigDir(sharedRoot = false)
	private Path configDir;

	public static FileSync getPlugin() {
		return plugin;
	}

	public Path getConfigDir() {
		return configDir;
	}

	@Listener
	public void onPreInitialization(GamePreInitializationEvent event) {
		plugin = this;

		// Create config Directory for VoteTools.
		if (!java.nio.file.Files.exists(configDir)) {
			try {
				java.nio.file.Files.createDirectories(configDir);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		// Set up data and config files.
		FileSyncConfig.getConfig().setup();
	}
	
	@Listener
	public void onServerStart(GameStartedServerEvent event) {
		HashMap<String, String> files = FileSyncDataInteraction.getFileMap();

		for (Object key : files.keySet()) {
			SynX.instance().register(this, key.toString(), this);
		}
	}

	@Listener
	public void onPlayerJoin(ClientConnectionEvent.Join event) {

		event.setMessageCancelled(true);

		for (Object key : FileSyncDataInteraction.getFileMap().keySet()) {
			String path = key.toString().replace("%uuid%", event.getTargetEntity().getUniqueId().toString());
			if(!new File(FileSyncDataInteraction.getFileString(path)).exists()) {
				event.setMessageCancelled(true);
			}
		}
	}

	@Listener
	public void onPlayerQuit(ClientConnectionEvent.Disconnect event) throws IOException {

		Player player = event.getTargetEntity();

		if (player.hasPermission("synctools.sync")) {

			HashMap<String, String> files = FileSyncDataInteraction.getFileMap();

			for (String chanelIndex : files.keySet()) {

				String type = files.get(chanelIndex);
				String finalPath = FileSyncDataInteraction.getServerRootDirectory() + type.replace("%uuid%", player.getUniqueId().toString());

				File file = new File(finalPath);

				if (file.exists()) {
					SendFile.sendFile(chanelIndex, file, player.getUniqueId());
				} else {
					MessageChannel.TO_CONSOLE.send(TextSerializers.FORMATTING_CODE.deserialize(
							"&9[FileSync] &eFile not found " + file.getAbsolutePath()));
				}
			}
		}
	}

	@Override
	public void onPacketReceived(final Packet packet) {

		int packetChanelLength = packet.getChannel().length();

		if (packetChanelLength==4) { // Sync channel

			Sponge.getScheduler().createTaskBuilder().execute(new Runnable() {
				@Override
				public void run() {

					FileDataObject returnedObject = new FileDataObject(packet.getData());
					String filePath = FileSyncDataInteraction.getFileMap().get(packet.getChannel());
					filePath = filePath.replace("%uuid%", returnedObject.getPlayerUUID().toString());

					File file = new File(filePath);
					try {
						Files.write(returnedObject.getFileBytes(), file);
					} catch (IOException e) {
						e.printStackTrace();
					}

					MessageChannel.TO_CONSOLE.send(TextSerializers.FORMATTING_CODE.deserialize(
							"&9[FileSync] &eData received. File path of received file:" + file.getAbsolutePath()));
				}
			}).submit(this);
		}
	}
}
