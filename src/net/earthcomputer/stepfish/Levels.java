package net.earthcomputer.stepfish;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;

import net.earthcomputer.stepfish.Level.LevelObject;

public class Levels {
	private static final int CURRENT_LEVEL_VERSION = 0;

	private static final Map<Integer, String> levelNamesById = new HashMap<Integer, String>();
	private static final Map<String, Integer> levelIdsByName = new HashMap<String, Integer>();

	static {
		Scanner indexesScanner = new Scanner(
				new BufferedInputStream(Stepfish.class.getResourceAsStream("/levels/indexes")));
		Pattern whitespacePattern = Pattern.compile("\\s*");
		int levelIndex = 0;
		while (indexesScanner.hasNextLine()) {
			String levelName = indexesScanner.nextLine();
			if (!whitespacePattern.matcher(levelName).matches()) {
				levelName = levelName.trim();
				levelNamesById.put(levelIndex, levelName);
				levelIdsByName.put(levelName, levelIndex);
			}
			levelIndex++;
		}
		indexesScanner.close();
	}

	public static String getNameById(int id) {
		return levelNamesById.get(id);
	}

	public static int getIdFromName(String name) {
		return levelIdsByName.get(name);
	}

	public static Level loadLevel(int id) throws LevelFormatException, IOException {
		return loadLevel(getNameById(id));
	}

	public static Level loadLevel(String name) throws LevelFormatException, IOException {
		return loadLevel(
				new BufferedInputStream(Levels.class.getResourceAsStream(String.format("/levels/%s.gglevel", name))));
	}

	private static Level loadLevel(InputStream input) throws LevelFormatException, IOException {
		DataInputStream dataInput = new DataInputStream(input);

		if (dataInput.readInt() != 0x4748474D)
			throw new LevelFormatException();

		int version = dataInput.readUnsignedByte();
		if (version > CURRENT_LEVEL_VERSION)
			throw new LevelFormatException();

		String levelName = dataInput.readUTF();
		int levelWidth = dataInput.readUnsignedShort();
		int levelHeight = dataInput.readUnsignedShort();

		int objectCount = dataInput.readUnsignedShort();
		LevelObject[] objects = new LevelObject[objectCount];
		for (int i = 0; i < objectCount; i++) {
			int x = dataInput.readInt();
			int y = dataInput.readInt();
			int id = dataInput.readUnsignedShort();
			objects[i] = new LevelObject(x, y, id);
		}

		return new Level(levelName, levelWidth, levelHeight, objects);
	}

	public static int getLevelCount() {
		return levelNamesById.size();
	}

	public static class LevelFormatException extends Exception {
		private static final long serialVersionUID = 5901910426359021333L;
	}
}
