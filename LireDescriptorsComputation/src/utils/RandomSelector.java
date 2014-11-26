package utils;

import java.util.Random;

public class RandomSelector {

	private static Random randomizer;
	
	public static void init () {
		randomizer = new Random();
	}
	
	public static int getRandom (int min, int max) {
		int k = randomizer.nextInt((max - min) + 1);
		return (k + min);
	}
}
