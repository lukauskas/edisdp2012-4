package balle.main;

import java.util.ArrayList;

import balle.memory.utility.Saves;

public class Config implements Saves {
	public static final int NAME = 0;
	public static final int GREEN_STRATEGY = 1;
	public static final int RED_STRATEGY = 2;
	public static final int POWER_VELO_DATA = 3;

	// Instance

	protected String[] args;

	public Config(String[] args) {
		this.args = args;
	}

	public Config() {
		this.args = defaults();
	}

	protected static String[] defaults() {
		String[] args = new String[4];
		args[0] = "Default";
		args[1] = "NullStrategy";
		args[2] = "NullStrategy";
		args[3] = "[DEFAULT]";
		return args;
	}


	// Memory

	@Override
	public String save() {
		String out = "";
		for (String str : args)
			out += str + "\n";
		return out;
	}

	public static Config load(ArrayList<String> lines) {
		String[] defaults = defaults();
		String[] strings = new String[defaults.length];

		for (int i = 0; i < defaults.length; i++)
			if (i >= lines.size())
				strings[i] = defaults[i];
			else
				strings[i] = lines.get(i);
		return new Config(strings);
	}

	// Interface

	public String get(int index) {
		return args[index];
	}

	public void set(int index, String str) {
		args[index] = str;
	}
}
