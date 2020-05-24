package iizvullokIcemountains.mod.util;

import java.awt.image.BufferedImage;

public class MapData {
	public int posX;
	public int posZ;
	public long seed;
	public BufferedImage terrain;
	public BufferedImage population;
	public BufferedImage biome;
	
	public MapData(int x, int z, long s, BufferedImage t, BufferedImage p, BufferedImage b) {
		posX = x;
		posZ = z;
		seed = s;
		terrain = t;
		population = p;
		biome = b;
	}
}