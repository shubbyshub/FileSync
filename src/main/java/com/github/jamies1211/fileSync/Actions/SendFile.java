package com.github.jamies1211.fileSync.Actions;

import com.github.jamies1211.fileSync.FileDataObject;
import com.google.common.io.Files;
import net.kaikk.mc.synx.SynX;
import net.kaikk.mc.synx.packets.Node;
import org.spongepowered.api.text.channel.MessageChannel;
import org.spongepowered.api.text.serializer.TextSerializers;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Created by Jamie on 28-Jul-16.
 */
public class SendFile {

	public static void sendFile(String chanelIndex, File file, UUID playerUUID) throws IOException {

		Set<Node> servers = new HashSet<Node>();
		servers.addAll(SynX.instance().getNodes().values());

		FileDataObject packagedData = new FileDataObject(playerUUID, Files.toByteArray(file));

		SynX.instance().send(chanelIndex, packagedData.serializeToByteArray(), SynX.instance().defaultTimeOfDeath(), SynX.instance().getNodes().values().toArray(new Node[servers.size()]));
		MessageChannel.TO_CONSOLE.send(TextSerializers.FORMATTING_CODE.deserialize("&9[FileSync] &e" + file.getPath() + " has been sent to all servers"));
	}

}
