package com.github.jamies1211.fileSync;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import java.util.UUID;

/**
 * Created by Jamie on 31-Jul-16.
 */
public class FileDataObject {

	private UUID playerUUID;
	private byte[] fileBytes;

	public FileDataObject (UUID playerUUID, byte[] fileBytes) {
		this.playerUUID = playerUUID;
		this.fileBytes = fileBytes;
	}


	FileDataObject(byte[] serialized) {
		ByteArrayDataInput in = ByteStreams.newDataInput(serialized);
		try {
			this.playerUUID = new UUID(in.readLong(), in.readLong());
		} catch (Exception e) {
		}

		byte[] readBytes = new byte[serialized.length - 16];
		in.readFully(readBytes, 0, serialized.length - 16);
		this.fileBytes = readBytes;
	}

	public byte[] serializeToByteArray() {
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeLong(this.playerUUID.getMostSignificantBits());
		out.writeLong(this.playerUUID.getLeastSignificantBits());
		out.write(this.fileBytes);
		return out.toByteArray();
	}

	public UUID getPlayerUUID() {
		return this.playerUUID;
	}

	public byte[] getFileBytes() {
		return this.fileBytes;
	}
}