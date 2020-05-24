package iizvullokIcemountains.mod.util;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;

public class Heightmap {
	public static ArrayList<MapData> mapData = new ArrayList<MapData>();
	public static final int WIDTH = 1024;
	public static final int HEIGHT = 1024;
	public static final int COMPUTATION_EDGE = 128;
	public static final int COMPUTATION_WIDTH = WIDTH + 2 * COMPUTATION_EDGE;
	public static final int COMPUTATION_HEIGHT = HEIGHT + 2 * COMPUTATION_EDGE;
	public static final int IMAGE_EDGE = 32;
	public static final int IMAGE_WIDTH = WIDTH + 2 * IMAGE_EDGE;
	public static final int IMAGE_HEIGHT = HEIGHT + 2 * IMAGE_EDGE;
	public static final double FEATURE_SIZE = 128;
	public double [][] heightmap;
	public double [][] eroded;
	public double [][] slope;
	BufferedImage img;

	public Heightmap() {
		heightmap = new double [COMPUTATION_WIDTH][COMPUTATION_HEIGHT];
		eroded = new double [COMPUTATION_WIDTH][COMPUTATION_HEIGHT];
		slope = new double [COMPUTATION_WIDTH][COMPUTATION_HEIGHT];
	}
	
	public static double [][][] loadChunkData(int chunkX, int chunkZ, long seed) {
		int mapX = (int)(Math.floor((double)(chunkX) / 128D));
    	int mapZ = (int)(Math.floor((double)(chunkZ) / 128D));
		if(chunkX % 128 != 0 && chunkX % 128 != 127 && chunkZ % 128 != 0 && chunkZ % 128 != 127) {
			return loadRawChunkData(chunkX, chunkZ, seed, mapX, mapZ);
		}
		else {
			double [][][] chunkData = loadRawChunkData(chunkX, chunkZ, seed, mapX, mapZ);
			if(chunkX % 128 == 0) {
				double [][][] ownChunkData = loadRawChunkData(chunkX, chunkZ, seed, mapX, mapZ);
				double [][][] neighborChunkData = loadRawChunkData(chunkX, chunkZ, seed, mapX - 1, mapZ);
				for(int i = 0; i < 16; i++) {
					double blendOwn = 0.5 + i / 32;
					double blendNeighbor = 1 - blendOwn;
		        	for(int j = 0; j < 16; j++) {
		        		chunkData[i][j][0] = blendOwn * ownChunkData[i][j][0] + blendNeighbor * neighborChunkData[i][j][0];
		        		chunkData[i][j][1] = blendOwn * ownChunkData[i][j][1] + blendNeighbor * neighborChunkData[i][j][1];
		        		chunkData[i][j][2] = blendOwn * ownChunkData[i][j][2] + blendNeighbor * neighborChunkData[i][j][2];
		        	}
		        }
			}
			if(chunkX % 128 == 127) {
				double [][][] ownChunkData = loadRawChunkData(chunkX, chunkZ, seed, mapX, mapZ);
				double [][][] neighborChunkData = loadRawChunkData(chunkX, chunkZ, seed, mapX + 1, mapZ);
				for(int i = 0; i < 16; i++) {
					double blendOwn = 1 - i / 32;
					double blendNeighbor = 1 - blendOwn;
		        	for(int j = 0; j < 16; j++) {
		        		chunkData[i][j][0] = blendOwn * ownChunkData[i][j][0] + blendNeighbor * neighborChunkData[i][j][0];
		        		chunkData[i][j][1] = blendOwn * ownChunkData[i][j][1] + blendNeighbor * neighborChunkData[i][j][1];
		        		chunkData[i][j][2] = blendOwn * ownChunkData[i][j][2] + blendNeighbor * neighborChunkData[i][j][2];
		        	}
		        }
			}
			if(chunkZ % 128 == 0) {
				double [][][] ownChunkData = loadRawChunkData(chunkX, chunkZ, seed, mapX, mapZ);
				double [][][] neighborChunkData = loadRawChunkData(chunkX, chunkZ, seed, mapX, mapZ - 1);
				for(int i = 0; i < 16; i++) {
		        	for(int j = 0; j < 16; j++) {
		        		double blendOwn = 0.5 + j / 32;
						double blendNeighbor = 1 - blendOwn;
						chunkData[i][j][0] = blendOwn * ownChunkData[i][j][0] + blendNeighbor * neighborChunkData[i][j][0];
						chunkData[i][j][1] = blendOwn * ownChunkData[i][j][1] + blendNeighbor * neighborChunkData[i][j][1];
						chunkData[i][j][2] = blendOwn * ownChunkData[i][j][2] + blendNeighbor * neighborChunkData[i][j][2];
		        	}
		        }
			}
			if(chunkZ % 128 == 127) {
				double [][][] ownChunkData = loadRawChunkData(chunkX, chunkZ, seed, mapX, mapZ);
				double [][][] neighborChunkData = loadRawChunkData(chunkX, chunkZ, seed, mapX, mapZ + 1);
				for(int i = 0; i < 16; i++) {
		        	for(int j = 0; j < 16; j++) {
		        		double blendOwn = 1 - j / 32;
						double blendNeighbor = 1 - blendOwn;
						chunkData[i][j][0] = blendOwn * ownChunkData[i][j][0] + blendNeighbor * neighborChunkData[i][j][0];
						chunkData[i][j][1] = blendOwn * ownChunkData[i][j][1] + blendNeighbor * neighborChunkData[i][j][1];
						chunkData[i][j][2] = blendOwn * ownChunkData[i][j][2] + blendNeighbor * neighborChunkData[i][j][2];
		        	}
		        }
			}
			return chunkData;
		}
	}
	
	public static double [][][] loadRawChunkData(int chunkX, int chunkZ, long seed, int mapX, int mapZ) {
    	MapData data = null;
    	boolean couldLoad = false;
    	
    	for(int i = 0; i < mapData.size(); i++) {
    		if(mapData.get(i).posX == mapX && mapData.get(i).posZ == mapZ && mapData.get(i).seed == seed) {
    			couldLoad = true;
    			data = mapData.get(i);
    		}
    	}
    	
    	if(!couldLoad) {
    		String nameTerrain = "terrain_X" + mapX + "Z" + mapZ + ".png";
    		String namePopulation = "population_X" + mapX + "Z" + mapZ + ".png";
    		String nameBiomeProperties = "biomeProperties_X" + mapX + "Z" + mapZ + ".png";
    		String dir = System.getProperty("user.dir") + "\\icemountains\\" + seed;
    		String filedirTerrain = dir + "\\" + nameTerrain;
    		String filedirPopulation = dir + "\\" + namePopulation;
    		String filedirBiomeProperties = dir + "\\" + nameBiomeProperties;
    		File fileTerrain  = new File(filedirTerrain);
    		File filePopulation  = new File(filedirPopulation);
    		File fileBiomeProperties  = new File(filedirBiomeProperties);
    		File directory = new File(dir);
            
            if(!directory.exists()) {
            	directory.mkdirs();
            }
            
            if(fileTerrain.exists() && filePopulation.exists() && fileBiomeProperties.exists()) {
            	BufferedImage imgTerrain = null;
            	try {
            	    imgTerrain = ImageIO.read(fileTerrain);
            	} 
            	catch (IOException e) {
            		
            	}
            	BufferedImage imgPopulation = null;
            	try {
            		imgPopulation = ImageIO.read(filePopulation);
            	} 
            	catch (IOException e) {
            		
            	}
            	BufferedImage imgBiomeProperties = null;
            	try {
            		imgBiomeProperties = ImageIO.read(fileBiomeProperties);
            	} 
            	catch (IOException e) {
            		
            	}
            	if(imgTerrain != null && imgPopulation != null && imgBiomeProperties != null) {
            		couldLoad = true;
            		data = new MapData(mapX, mapZ, seed, imgTerrain, imgPopulation, imgBiomeProperties);
            		mapData.add(data);
            	}
            }
    	}
    	
        if(!couldLoad) {
        	Heightmap.generateMapData(mapX, mapZ, seed);
        	data = mapData.get(mapData.size() - 1);
        }
        
        /*	
         * 	return the data for the chunk
         * 	layer0: Heightmap
         * 	layer1: slopemap
         * 	layer2: erosionmap
         * 	layer3: temperaturemap
         * 	layer4: fertilitymap
         * 	layer5: placeholder for something
         * 	layer6: biome noise modifier
         *  layer7: muddyness
         */
        
        double [][][] chunkData = new double [16][16][9];
        Random rand = new Random(seed * chunkX + chunkZ);
        double [][] uH = new double [18][18];
        
        for(int i = -1; i < 17; i++) {
        	for(int j = -1; j < 17; j++) {
        		int imgX = IMAGE_EDGE + 8 * chunkX + i / 2 - 1024 * mapX;
        		int imgZ = IMAGE_EDGE + 8 * chunkZ + j / 2 - 1024 * mapZ;
        		double xminzmin = new Color(data.terrain.getRGB(imgX, imgZ)).getRed();
        		double xmaxzmin = new Color(data.terrain.getRGB(imgX + 1, imgZ)).getRed();
        		double xminzmax = new Color(data.terrain.getRGB(imgX, imgZ + 1)).getRed();
        		double xmaxzmax = new Color(data.terrain.getRGB(imgX + 1, imgZ + 1)).getRed();
        		double h = interpolate200(xminzmin, xmaxzmin, xminzmax, xmaxzmax, Math.floorMod(i, 2), Math.floorMod(j, 2), rand);
        		uH[i + 1][j + 1] = h;
        	}
        }
        
        for(int i = 0; i < 16; i++) {
        	for(int j = 0; j < 16; j++) {
        		int imgX = IMAGE_EDGE + 8 * chunkX + i / 2 - 1024 * mapX;
        		int imgZ = IMAGE_EDGE + 8 * chunkZ + j / 2 - 1024 * mapZ;
        		
        		double height = (uH[i + 1][j + 1] * 2 + uH[i][j + 1] + uH[i + 2][j + 1] + uH[i + 1][j] + uH[i + 1][j + 2]) / 6;
        		
        		//TODO (maybe)
        		
        		//mountains
        		chunkData[i][j][0] = height;
        		
        		//plains
        		//chunkData[i][j][0] = 50 + height / 4;
        		
        		double xminzmin = new Color(data.terrain.getRGB(imgX, imgZ)).getGreen();
        		double xmaxzmin = new Color(data.terrain.getRGB(imgX + 1, imgZ)).getGreen();
        		double xminzmax = new Color(data.terrain.getRGB(imgX, imgZ + 1)).getGreen();
        		double xmaxzmax = new Color(data.terrain.getRGB(imgX + 1, imgZ + 1)).getGreen();
        		double h = interpolate200(xminzmin, xmaxzmin, xminzmax, xmaxzmax, Math.floorMod(i, 2), Math.floorMod(j, 2), rand);
        		chunkData[i][j][1] = h;
        		
        		xminzmin = new Color(data.terrain.getRGB(imgX, imgZ)).getBlue();
        		xmaxzmin = new Color(data.terrain.getRGB(imgX + 1, imgZ)).getBlue();
        		xminzmax = new Color(data.terrain.getRGB(imgX, imgZ + 1)).getBlue();
        		xmaxzmax = new Color(data.terrain.getRGB(imgX + 1, imgZ + 1)).getBlue();
        		h = interpolate200(xminzmin, xmaxzmin, xminzmax, xmaxzmax, Math.floorMod(i, 2), Math.floorMod(j, 2), rand);
        		chunkData[i][j][2] = h;
        	}
        }
        
        int imgX = Math.floorMod(chunkX, 128);
		int imgZ = Math.floorMod(chunkZ, 128);
		double xminzmin = new Color(data.population.getRGB(imgX, imgZ)).getRed();
		double xmaxzmin = new Color(data.population.getRGB(imgX + 1, imgZ)).getRed();
		double xminzmax = new Color(data.population.getRGB(imgX, imgZ + 1)).getRed();
		double xmaxzmax = new Color(data.population.getRGB(imgX + 1, imgZ + 1)).getRed();
		double xstepzmin = (xmaxzmin - xminzmin) / 16;
		double xstepzmax = (xmaxzmax - xminzmax) / 16;
        for(int i = 0; i < 16; i++) {
        	double xzmin = xminzmin + i * xstepzmin;
        	double xzmax = xminzmax + i * xstepzmax;
        	double zstep = (xzmax - xzmin) / 16;
        	for(int j = 0; j < 16; j++) {
        		double z = xzmin + zstep * j;
        		chunkData[i][j][3] = z;
        	}
        }
        xminzmin = new Color(data.population.getRGB(imgX, imgZ)).getGreen();
        xmaxzmin = new Color(data.population.getRGB(imgX + 1, imgZ)).getGreen();
		xminzmax = new Color(data.population.getRGB(imgX, imgZ + 1)).getGreen();
		xmaxzmax = new Color(data.population.getRGB(imgX + 1, imgZ + 1)).getGreen();
		xstepzmin = (xmaxzmin - xminzmin) / 16;
		xstepzmax = (xmaxzmax - xminzmax) / 16;
        for(int i = 0; i < 16; i++) {
        	double xzmin = xminzmin + i * xstepzmin;
        	double xzmax = xminzmax + i * xstepzmax;
        	double zstep = (xzmax - xzmin) / 16;
        	for(int j = 0; j < 16; j++) {
        		double z = xzmin + zstep * j;
        		chunkData[i][j][4] = z;
        	}
        }
        xminzmin = new Color(data.population.getRGB(imgX, imgZ)).getBlue();
		xmaxzmin = new Color(data.population.getRGB(imgX + 1, imgZ)).getBlue();
		xminzmax = new Color(data.population.getRGB(imgX, imgZ + 1)).getBlue();
		xmaxzmax = new Color(data.population.getRGB(imgX + 1, imgZ + 1)).getBlue();
		xstepzmin = (xmaxzmin - xminzmin) / 16;
		xstepzmax = (xmaxzmax - xminzmax) / 16;
        for(int i = 0; i < 16; i++) {
        	double xzmin = xminzmin + i * xstepzmin;
        	double xzmax = xminzmax + i * xstepzmax;
        	double zstep = (xzmax - xzmin) / 16;
        	for(int j = 0; j < 16; j++) {
        		double z = xzmin + zstep * j;
        		chunkData[i][j][5] = z;
        	}
        }
        
		xminzmin = new Color(data.biome.getRGB(imgX, imgZ)).getRed();
		xmaxzmin = new Color(data.biome.getRGB(imgX + 1, imgZ)).getRed();
		xminzmax = new Color(data.biome.getRGB(imgX, imgZ + 1)).getRed();
		xmaxzmax = new Color(data.biome.getRGB(imgX + 1, imgZ + 1)).getRed();
		xstepzmin = (xmaxzmin - xminzmin) / 16;
		xstepzmax = (xmaxzmax - xminzmax) / 16;
        for(int i = 0; i < 16; i++) {
        	double xzmin = xminzmin + i * xstepzmin;
        	double xzmax = xminzmax + i * xstepzmax;
        	double zstep = (xzmax - xzmin) / 16;
        	for(int j = 0; j < 16; j++) {
        		double z = xzmin + zstep * j;
        		chunkData[i][j][6] = z;
        	}
        }
        xminzmin = new Color(data.biome.getRGB(imgX, imgZ)).getGreen();
        xmaxzmin = new Color(data.biome.getRGB(imgX + 1, imgZ)).getGreen();
		xminzmax = new Color(data.biome.getRGB(imgX, imgZ + 1)).getGreen();
		xmaxzmax = new Color(data.biome.getRGB(imgX + 1, imgZ + 1)).getGreen();
		xstepzmin = (xmaxzmin - xminzmin) / 16;
		xstepzmax = (xmaxzmax - xminzmax) / 16;
        for(int i = 0; i < 16; i++) {
        	double xzmin = xminzmin + i * xstepzmin;
        	double xzmax = xminzmax + i * xstepzmax;
        	double zstep = (xzmax - xzmin) / 16;
        	for(int j = 0; j < 16; j++) {
        		double z = xzmin + zstep * j;
        		chunkData[i][j][7] = z;
        	}
        }
        xminzmin = new Color(data.biome.getRGB(imgX, imgZ)).getBlue();
		xmaxzmin = new Color(data.biome.getRGB(imgX + 1, imgZ)).getBlue();
		xminzmax = new Color(data.biome.getRGB(imgX, imgZ + 1)).getBlue();
		xmaxzmax = new Color(data.biome.getRGB(imgX + 1, imgZ + 1)).getBlue();
		xstepzmin = (xmaxzmin - xminzmin) / 16;
		xstepzmax = (xmaxzmax - xminzmax) / 16;
        for(int i = 0; i < 16; i++) {
        	double xzmin = xminzmin + i * xstepzmin;
        	double xzmax = xminzmax + i * xstepzmax;
        	double zstep = (xzmax - xzmin) / 16;
        	for(int j = 0; j < 16; j++) {
        		double z = xzmin + zstep * j;
        		chunkData[i][j][8] = z;
        	}
        }
        return chunkData;
	}
	
	public static double interpolate1600(double xminzmin, double xmaxzmin, double xminzmax, double xmaxzmax, int x, int z) {
    	double xweight = Math.floorMod(x, 16);
    	double zweight = Math.floorMod(z, 16);
    	
    	double xdiffzmin = xmaxzmin - xminzmin;
    	double xhpzmin = xdiffzmin * xweight;
    	double xhzmin = xhpzmin + xminzmin;
    	
    	double xdiffzmax = xmaxzmax - xminzmax;
    	double xhpzmax = xdiffzmax * xweight;
    	double xhzmax = xhpzmax + xminzmax;
    	
    	double zdiff = xhzmax - xhzmin;
    	double zhp = zdiff * zweight;
    	double zh = zhp + xhzmin;
    	return zh;
	}
	
	public static double interpolate200(double xminzmin, double xmaxzmin, double xminzmax, double xmaxzmax, int x, int z, Random rand) {
		if(x == 0 && z == 0) {
			return xminzmin;
		}
		else if(x == 1 && z == 0) {
			return (xminzmin + xmaxzmin) / 2;
		}
		else if(x == 0 && z == 1) {
			return (xminzmin + xminzmax) / 2;
		}
		else {
			if(Math.abs(xminzmin - xmaxzmax) == 1) {
				//xminzmin += 1;//rand.nextInt(3) - 1;
			}
			return (xminzmin + xmaxzmax) / 2;
		}
	}

	public static void generateMapData(int regX, int regY, long seed) {
		String dir = System.getProperty("user.dir") + "\\icemountains\\" + seed;
		BufferedImage imageBiomeProperties = new BufferedImage(129, 129, BufferedImage.TYPE_INT_RGB);
		OpenSimplexNoise noise00 = new OpenSimplexNoise(seed * 5);
		OpenSimplexNoise noise01 = new OpenSimplexNoise(seed * 6);
		OpenSimplexNoise noise02 = new OpenSimplexNoise(seed * 7);
		
		double [][][] biomeStuff = new double [161][161][3];
		
		for (int y = 0; y < 161; y++){
			for (int x = 0; x < 161; x++){
				//TODO 256
				double noiseSize = 256;
				double value0 = noise00.eval((x + regX * 128 - 16) / noiseSize, (y + regY * 128 - 16) / noiseSize);
				for(int i = 0; i < 1; i++) {
					value0 += noise00.eval((x + regX * 128 - 16) / (noiseSize / Math.pow(2, i + 1)), (y + regY * 128 - 16) / (noiseSize / Math.pow(2, i + 1))) / Math.pow(2, i + 1);
				}
				double value1 = noise01.eval((x + regX * 128 - 16) / noiseSize, (y + regY * 128 - 16) / noiseSize);
				for(int i = 0; i < 3; i++) {
					value1 += noise01.eval((x + regX * 128 - 16) / (noiseSize / Math.pow(2, i + 1)), (y + regY * 128 - 16) / (noiseSize / Math.pow(2, i + 1))) / Math.pow(2, i + 1);
				}
				double value2 = noise02.eval((x + regX * 128 - 16) / noiseSize, (y + regY * 128 - 16) / noiseSize);
				for(int i = 0; i < 3; i++) {
					value2 += noise02.eval((x + regX * 128 - 16) / (noiseSize / Math.pow(2, i + 1)), (y + regY * 128 - 16) / (noiseSize / Math.pow(2, i + 1))) / Math.pow(2, i + 1);
				}
				value0 = (value0 * 192D) + 128D;
				if(value0 < 0) {
					value0 = 0;
				}
				if(value0 > 255) {
					value0 = 255;
				}
				value1 = (value1 * 256D) + 128D;
				if(value1 < 0) {
					value1 = 0;
				}
				if(value1 > 255) {
					value1 = 255;
				}
				//value0 = 128;
				//value1 = 255;
				int r = 0x010000 * (int)(value0);
				int g = 0x000100 * (int)(value1);
				int b = 0x000001 * (int)((value2 + 2) * 64) * 0;
				biomeStuff[x][y][0] = value0;
				biomeStuff[x][y][1] = value1;
				biomeStuff[x][y][2] = (value2 + 2D) * 64D;
				if(x >= 16 && x < 145 && y >= 16 && y < 145) {
					imageBiomeProperties.setRGB(x - 16, y - 16, r + g + b);
				}
			}
		}
		
		String nameBiomeProperties = "biomeProperties_X" + regX + "Z" + regY + ".png";
        String filedirBiomeProperties = dir + "\\" + nameBiomeProperties;
        File fileBiomeProperties  = new File(filedirBiomeProperties);
		try {
			ImageIO.write(imageBiomeProperties, "png", fileBiomeProperties);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		OpenSimplexNoise noise = new OpenSimplexNoise(seed);
		Heightmap map = new Heightmap();
		for (int y = 0; y < COMPUTATION_HEIGHT; y++){
			for (int x = 0; x < COMPUTATION_WIDTH; x++){
				double value = noise.eval((x + regX * WIDTH - COMPUTATION_EDGE) / FEATURE_SIZE, ((y + regY * HEIGHT) - COMPUTATION_EDGE) / FEATURE_SIZE);
				for(int i = 0; i < 6; i++) {
					value += noise.eval((x + regX * WIDTH - COMPUTATION_EDGE) / (FEATURE_SIZE / Math.pow(2, i + 1)), (y + regY * HEIGHT - COMPUTATION_EDGE) / (FEATURE_SIZE / Math.pow(2, i + 1))) / Math.pow(2, i + 1);
					//+-1.984375
					//sea level -0.68888888889
				}
				//TODO bla flachere hügel
				double val = (value + 0.68888888889) * Math.min(1, Math.max(0.5, noise.eval((x + regX * WIDTH - COMPUTATION_EDGE) / 64, ((y + regY * HEIGHT) - COMPUTATION_EDGE) / 64) + 1))  * Math.min(1, Math.max(0.3, noise.eval((x + regX * WIDTH - COMPUTATION_EDGE) / 2048, ((y + regY * HEIGHT) - COMPUTATION_EDGE) / 2048) + 1)) - 0.68888888889;
				
				//mountains
				//min = 0
				//factor = 1
				
				double min;
				double factor;
				
				double d00 = biomeStuff[x / 8][y / 8][0];
				double d01 = biomeStuff[x / 8][y / 8 + 1][0];
				double d10 = biomeStuff[x / 8 + 1][y / 8][0];
				double d11 = biomeStuff[x / 8 + 1][y / 8 + 1][0];
				double d0 = d00 + ((d01 - d00) / 8) * (y % 8);
				double d1 = d10 + ((d11 - d10) / 8) * (y % 8);
				double d = d0 + ((d1 - d0) / 8) * (x % 8);
				
				double mountainMin = 0;
				double mountainFactor = 1;
				double midMin;
				double midFactor;
				double oceanMin = -0.8;
				double oceanFactor = 0.2;
				
				//TODO Swamp things
				double forestMin = -0.55;
				double forestFactor = 0.35;
				double swampMin = -0.69;
				double swampFactor = 0.02;
				
				double e00 = biomeStuff[x / 8][y / 8][1];
				double e01 = biomeStuff[x / 8][y / 8 + 1][1];
				double e10 = biomeStuff[x / 8 + 1][y / 8][1];
				double e11 = biomeStuff[x / 8 + 1][y / 8 + 1][1];
				double e0 = e00 + ((e01 - e00) / 8) * (y % 8);
				double e1 = e10 + ((e11 - e10) / 8) * (y % 8);
				double e = e0 + ((e1 - e0) / 8) * (x % 8);
				
				double swampBlend = Math.min(1D, Math.max(0D, (e - 100D) / 56D));
				double midBlend = 0;
				
				//mid is forest/plains
				if(e < 100) {
					midMin = forestMin;
					midFactor = forestFactor;
				}
				//mid is between forest/plains and swamp
				else if(e >= 100 && e < 156) {
					double blend = (e - 100) / 56;
					double minDiff = swampMin - forestMin;
					midMin = forestMin + blend * minDiff;
					double factorDiff = swampFactor - forestFactor;
					midFactor = forestFactor + blend * factorDiff;
				}
				//mid is swamp
				else {
					midMin = swampMin;
					midFactor = swampFactor;
				}
				//midMin = swampMin;
				//midFactor = swampFactor;
				
				//oceans
				if(d < 80) {
					min = oceanMin;
					factor = oceanFactor;
				}
				//ocean/forest
				else if(d >= 80 && d < 98) {
					double blend = (d - 80) / 18;
					double minDiff = midMin - oceanMin;
					min = oceanMin + blend * minDiff;
					double factorDiff = midFactor - oceanFactor;
					factor = oceanFactor + blend * factorDiff;
					midBlend = blend;
				}
				//forest
				else if(d >= 98 && d < 158) {
					min = midMin;
					factor = midFactor;
					midBlend = 1D;
				}
				//forest/mountains
				else if(d >= 158 && d < 176) {
					double blend = (d - 158) / 18;
					double minDiff = mountainMin - midMin;
					min = midMin + blend * minDiff;
					double factorDiff = mountainFactor - midFactor;
					factor = midFactor + blend * factorDiff;
					midBlend = 1D - blend;
				}
				//mountains
				else {
					min = mountainMin;
					factor = mountainFactor;
				}
				
				//mountains
				//min = 0
				//factor = 1
				
				//oceans
				//min = -0.8
				//factor = 0.2
				
				//swamps
				//min = -0.68888888889
				//factor = 0
				/*
				 * Extra:
				 * value += noise.eval((x + regX * WIDTH - COMPUTATION_EDGE) / 3, (y + regY * HEIGHT - COMPUTATION_EDGE) / 3) * 0.04;
				 * value += noise.eval((x + regX * WIDTH - COMPUTATION_EDGE) / 1, (y + regY * HEIGHT - COMPUTATION_EDGE) / 1) * 0.03;
				 */
				
				//plains and forests
				//min = -0.55
				//factor = 0.35
				
				value = min + value * factor;
				//value += noise.eval((x + regX * WIDTH - COMPUTATION_EDGE) / 3, (y + regY * HEIGHT - COMPUTATION_EDGE) / 3) * 0.02 * swampBlend * midBlend;
				//value += noise.eval((x + regX * WIDTH - COMPUTATION_EDGE) / 1, (y + regY * HEIGHT - COMPUTATION_EDGE) / 1) * 0.01 * swampBlend * midBlend;
				//value += noise.eval((x + regX * WIDTH - COMPUTATION_EDGE) / 1, (y + regY * HEIGHT - COMPUTATION_EDGE) * 3) * 0.01 * swampBlend * midBlend;
				map.heightmap[x][y] = value;
			}
		}
		
		map.simulateErosion();
		
		Random random = new Random();
		random.setSeed(seed + regX * regY + regY);
		for (int y = 0; y < COMPUTATION_HEIGHT; y++){
			for (int x = 0; x < COMPUTATION_WIDTH; x++){
				double d00 = biomeStuff[x / 8][y / 8][0];
				double d01 = biomeStuff[x / 8][y / 8 + 1][0];
				double d10 = biomeStuff[x / 8 + 1][y / 8][0];
				double d11 = biomeStuff[x / 8 + 1][y / 8 + 1][0];
				double d0 = d00 + ((d01 - d00) / 8) * (y % 8);
				double d1 = d10 + ((d11 - d10) / 8) * (y % 8);
				double d = d0 + ((d1 - d0) / 8) * (x % 8);
				
				double e00 = biomeStuff[x / 8][y / 8][1];
				double e01 = biomeStuff[x / 8][y / 8 + 1][1];
				double e10 = biomeStuff[x / 8 + 1][y / 8][1];
				double e11 = biomeStuff[x / 8 + 1][y / 8 + 1][1];
				double e0 = e00 + ((e01 - e00) / 8) * (y % 8);
				double e1 = e10 + ((e11 - e10) / 8) * (y % 8);
				double e = e0 + ((e1 - e0) / 8) * (x % 8);

				double swampBlend = Math.min(1D, Math.max(0D, (e - 100D) / 56D));
				double midBlend = 0;
				if(d >= 80 && d < 98) {
					double blend = (d - 80) / 18;
					midBlend = blend;
				}
				//forest
				else if(d >= 98 && d < 158) {
					midBlend = 1D;
				}
				//forest/mountains
				else if(d >= 158 && d < 176) {
					double blend = (d - 158) / 18;
					midBlend = 1D - blend;
				}
				
				double value = noise.eval((x + regX * WIDTH - COMPUTATION_EDGE) / 3, (y + regY * HEIGHT - COMPUTATION_EDGE) / 1) * 0.01 * swampBlend * midBlend;
				value += noise.eval((x + regX * WIDTH - COMPUTATION_EDGE) / 1, (y + regY * HEIGHT - COMPUTATION_EDGE) * 3) * 0.005 * swampBlend * midBlend;
				value += noise.eval((x + regX * WIDTH - COMPUTATION_EDGE) / 1, (y + regY * HEIGHT - COMPUTATION_EDGE) * 6) * 0.004 * swampBlend * midBlend;
				value -= random.nextDouble() * swampBlend * midBlend * 0.012;
				map.heightmap[x][y] += value;
			}
		}
		
		int slopeSmoothness = 4;
		for (int y = slopeSmoothness; y < COMPUTATION_HEIGHT - slopeSmoothness; y++){
			for (int x = slopeSmoothness; x < COMPUTATION_WIDTH - slopeSmoothness; x++){
				double value = Math.abs(map.heightmap[x + slopeSmoothness][y] - map.heightmap[x - slopeSmoothness][y]) + Math.abs(map.heightmap[x][y + slopeSmoothness] - map.heightmap[x][y - slopeSmoothness]);
				map.slope[x][y] = value / slopeSmoothness;
			}
		}
		
		BufferedImage imageAfter = new BufferedImage(IMAGE_WIDTH, IMAGE_HEIGHT, BufferedImage.TYPE_INT_RGB);
		for (int y = COMPUTATION_EDGE - IMAGE_EDGE; y < IMAGE_HEIGHT + COMPUTATION_EDGE - IMAGE_EDGE; y++){
			for (int x = COMPUTATION_EDGE - IMAGE_EDGE; x < IMAGE_WIDTH + COMPUTATION_EDGE - IMAGE_EDGE; x++){
				int r = 0x010000 * (int)((map.heightmap[x][y] + 1.4) * 90);
				//-0.584375 to 3.384375 -> 3.96875	-52.59375 to 304.59375 -> 357.1875
				int g = 0x000100 * (int)((map.slope[x][y] * 256));
				int b = 0x000001 * (int)((map.eroded[x][y]) * 128);
				imageAfter.setRGB(x - (COMPUTATION_EDGE - IMAGE_EDGE), y - (COMPUTATION_EDGE - IMAGE_EDGE), r + g + b);
			}
		}
		
		String nameTerrain = "terrain_X" + regX + "Z" + regY + ".png";
		
        String filedirTerrain = dir + "\\" + nameTerrain;
        File fileTerrain  = new File(filedirTerrain);
        File directory = new File(dir);
        
        if(!directory.exists()){
        	directory.mkdirs();
        }
		try {
			ImageIO.write(imageAfter, "png", fileTerrain);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		BufferedImage imagePopulation = new BufferedImage(129, 129, BufferedImage.TYPE_INT_RGB);
		OpenSimplexNoise noise0 = new OpenSimplexNoise(seed * 2);
		OpenSimplexNoise noise1 = new OpenSimplexNoise(seed * 3);
		OpenSimplexNoise noise2 = new OpenSimplexNoise(seed * 4);
		
		for (int y = 0; y < 129; y++){
			for (int x = 0; x < 129; x++){
				double value0 = noise0.eval((x + regX * 128) / FEATURE_SIZE, (y + regY * 128) / FEATURE_SIZE);
				for(int i = 0; i < 3; i++) {
					value0 += noise0.eval((x + regX * 128) / (FEATURE_SIZE / Math.pow(2, i + 1)), (y + regY * 128) / (FEATURE_SIZE / Math.pow(2, i + 1))) / Math.pow(2, i + 1);
				}
				double value1 = noise1.eval((x + regX * 128) / FEATURE_SIZE, (y + regY * 128) / FEATURE_SIZE);
				for(int i = 0; i < 3; i++) {
					value1 += noise1.eval((x + regX * 128) / (FEATURE_SIZE / Math.pow(2, i + 1)), (y + regY * 128) / (FEATURE_SIZE / Math.pow(2, i + 1))) / Math.pow(2, i + 1);
				}
				double value2 = noise2.eval((x + regX * 128) / FEATURE_SIZE, (y + regY * 128) / FEATURE_SIZE);
				for(int i = 0; i < 3; i++) {
					value2 += noise2.eval((x + regX * 128) / (FEATURE_SIZE / Math.pow(2, i + 1)), (y + regY * 128) / (FEATURE_SIZE / Math.pow(2, i + 1))) / Math.pow(2, i + 1);
				}
				value0 += noise0.eval((x + regX * 128) / 512, (y + regY * 128) / 512) * 0.5;
				if(value0 < 0) {
					value0 = 0;
				}
				if(value0 > 3.95) {
					value0 = 3.95;
				}
				int r = 0x010000 * (int)((value0 + 2) * 64);
				int g = 0x000100 * (int)((value1 + 2) * 64);
				int b = 0x000001 * (int)((value2 + 2) * 64);
				imagePopulation.setRGB(x, y, r + g + b);
			}
		}
		
		String namePopulation = "population_X" + regX + "Z" + regY + ".png";
        String filedirPopulation = dir + "\\" + namePopulation;
        File filePopulation  = new File(filedirPopulation);
		try {
			ImageIO.write(imagePopulation, "png", filePopulation);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		mapData.add(new MapData(regX, regY, seed, imageAfter, imagePopulation, imageBiomeProperties));
	}
	
	/**
	 * Everything beyond this point probably does not have to be touched anymore. Probably far from being optimal, but it works.
	 */
	//TODO Proper erosion amount
	public void simulateErosion() {
		for(int i = 1; i <= 500000; i++) {
			simulateDrop(Math.random() * COMPUTATION_WIDTH, Math.random() * COMPUTATION_HEIGHT);
			if(i % 1750 == 0) {
				thermalErosion();
			}
		}
		smoothErosionmap();
		for(int i = 1; i <= 16; i++) {
			smooth();
		}
	}
	
	public double acceleration(double x, double y) {
		double g = 9.81;
		double xslope = interpolate((int)(x + 1), y) - interpolate((int)(x), y);
		double yslope = interpolate(x, (int)(y + 1)) - interpolate(x, (int)(y));
		double slope = xslope + yslope;
		double Agh = Math.sin(Math.atan(slope)) * g;
		return Agh;
	}
	
	public void thermalErosion() {
		double [][] copy = new double[COMPUTATION_WIDTH][COMPUTATION_HEIGHT];
		for(int i = 0; i < COMPUTATION_WIDTH; i++) {
			for(int j = 0; j < COMPUTATION_HEIGHT; j++) {
				copy[i][j] = heightmap[i][j];
			}
		}
		for(int i = 2; i < COMPUTATION_WIDTH - 2; i++) {
			for(int j = 2; j < COMPUTATION_HEIGHT - 2; j++) {
				collapse(i, j, copy);
			}
		}
	}
	
	public void smooth() {
		double [][] copy = new double[COMPUTATION_WIDTH][COMPUTATION_HEIGHT];
		for(int i = 0; i < COMPUTATION_WIDTH; i++) {
			for(int j = 0; j < COMPUTATION_HEIGHT; j++) {
				copy[i][j] = heightmap[i][j];
			}
		}
		for(int i = 2; i < COMPUTATION_WIDTH - 2; i++) {
			for(int j = 2; j < COMPUTATION_HEIGHT - 2; j++) {
				smoothpoint(i, j, copy);
			}
		}
	}
	
	public void smoothErosionmap() {
		for(int k = 0; k < 24; k++) {
			double [][] copy = new double[COMPUTATION_WIDTH][COMPUTATION_HEIGHT];
			for(int i = 0; i < COMPUTATION_WIDTH; i++) {
				for(int j = 0; j < COMPUTATION_HEIGHT; j++) {
					copy[i][j] = eroded[i][j];
				}
			}
			for(int i = 2; i < COMPUTATION_WIDTH - 2; i++) {
				for(int j = 2; j < COMPUTATION_HEIGHT - 2; j++) {
					smoothpointErosion(i, j, copy);
				}
			}
		}
	}
	
	public void smoothpointErosion(int posX, int posY, double [][] copy) {
		double smoothfactor = 1;
		double v = 0;
		for(int i = -1; i < 2; i++) {
			for(int j = -1; j < 2; j++) {
				if(!(i == 0 && j == 0)) {
					if(i != 0 && j != 0) {
						v += copy[posX + i][posY + j] * 0.5 * smoothfactor;
					}
					else {
						v += copy[posX + i][posY + j] * smoothfactor;
					}
				}
				else {
					v += copy[posX + i][posY + j] * 2;
				}
			}
		}
		double d = 2 + 6 * smoothfactor;
		eroded[posX][posY] = v / d;
	}
	
	//TODO Smooth properly
	public void smoothpoint(int posX, int posY, double [][] copy) {
		double smoothfactor = Math.max(Math.max(eroded[posX][posY] - 0.12, 0) * 2.5 - (heightmap[posX][posY] + 0.7) * 0.5, 0) / 4;
		double v = 0;
		for(int i = -1; i < 2; i++) {
			for(int j = -1; j < 2; j++) {
				if(!(i == 0 && j == 0)) {
					if(i != 0 && j != 0) {
						v += copy[posX + i][posY + j] * 0.5 * smoothfactor;
					}
					else {
						v += copy[posX + i][posY + j] * smoothfactor;
					}
				}
				else {
					v += copy[posX + i][posY + j] * 2;
				}
			}
		}
		double d = 2 + 6 * smoothfactor;
		heightmap[posX][posY] = v / d;
	}
	
	public void collapse(int posX, int posY, double [][] copy) {
		double maxSlopeDown = (copy[posX][posY] + 2) * 0.04;
		int maxSlopeX = 0;
		int maxSlopeY = 0;
		double maxSlope = 0;
		boolean doSomething = false;
		for(int i = -1; i < 2; i++) {
			for(int j = -1; j < 2; j++) {
				double hcenter = copy[posX][posY];
				double h = copy[posX + i][posY + j];
				double slope = hcenter - h;
				if(slope > 0) {
					if(i != 0 && j != 0) {
						slope /= Math.sqrt(2);
					}
					if(slope > maxSlopeDown) {
						doSomething = true;
						if(slope > maxSlope) {
							maxSlopeX = i;
							maxSlopeY = j;
							maxSlope = slope;
						}
					}
				}
			}
		}
		if(doSomething) {
			heightmap[posX + maxSlopeX][posY + maxSlopeY] += (maxSlope - maxSlopeDown) / 2;
			heightmap[posX][posY] -= (maxSlope - maxSlopeDown) / 2;
		}
	}

	public void simulateDrop(double startX, double startY) {
		double posX = startX;
		double posY = startY;
		double velX = 0;
		double velY = 0;
		double accX = 0;
		double accY = 0;
		double sediments = 0;
		
		int max = (int)(Math.random() * 500) + 500;
		max = 500;
		for(int i = 0; i < max; i++) {
			double g = -9.81;
			if((int)(posX) < 0 || (int)(posX) > COMPUTATION_WIDTH - 3 || (int)(posY) < 0 || (int)(posY) > COMPUTATION_HEIGHT - 3) {
				return;
			}
			double xslope = interpolate((int)(posX + 1), posY) - interpolate((int)(posX), posY);
			double yslope = interpolate(posX, (int)(posY + 1)) - interpolate(posX, (int)(posY));
			accX = Math.asin(Math.atan(xslope)) * g;
			accY = Math.asin(Math.atan(yslope)) * g;
			velX += accX;
			velY += accY;
			double sedimentCapacity = (Math.abs(xslope) + Math.abs(yslope)) * Math.sqrt(velX * velX + velY * velY);
			
			if(sediments < sedimentCapacity) {
				double suspension = (4 - (heightmap[(int)(posX)][(int)(posY)] + 2)) * 0.1;
				double tooLittle = sedimentCapacity - sediments;
				if((int)(posX + velX) < 0 || (int)(posX + velX) > COMPUTATION_WIDTH - 3 || (int)(posY + velY) < 0 || (int)(posY + velY) > COMPUTATION_HEIGHT - 3) {
					return;
				}
				double suspend = Math.max(Math.min(tooLittle * suspension, heightmap[(int)(posX)][(int)(posY)] - heightmap[(int)(posX + velX)][(int)(posY + velY)]), 0);
				if(suspend > 0) {
					
					heightmap[(int)(posX)][(int)(posY)] -= suspend;
					eroded[(int)(posX)][(int)(posY)] += suspend;
				}
				sediments += suspend;
			}
			else {
				double disposition = (4 - (heightmap[(int)(posX)][(int)(posY)] + 2)) * 0.1;
				double tooMuch = sediments - sedimentCapacity;
				if((int)(posX - velX) < 0 || (int)(posX - velX) > COMPUTATION_WIDTH - 3 || (int)(posY - velY) < 0 || (int)(posY - velY) > COMPUTATION_HEIGHT - 3) {
					return;
				}
				double dispose = Math.max(Math.min(tooMuch * disposition, -heightmap[(int)(posX)][(int)(posY)] + heightmap[(int)(posX - velX)][(int)(posY - velY)]), 0);
				if(dispose > 0) {
					heightmap[(int)(posX)][(int)(posY)] += dispose;
					eroded[(int)(posX)][(int)(posY)] += dispose;
					
				}
				sediments -= dispose;
			}
			posX += velX * 0.5;
			posY += velY * 0.5;
			velX *= 0.97;
			velY *= 0.97;
		}
	}
	
	public double interpolate(double x, double y){
		if((int)(x) < 0 || (int)(x) >= COMPUTATION_WIDTH || (int)(y) < 0 || (int)(y) >= COMPUTATION_HEIGHT) {
			return 0;
		}
    	double xminzminh = heightmap[(int)(x)][(int)(y)];
    	double xmaxzminh = heightmap[(int)(x + 1)][(int)(y)];
    	double xminzmaxh = heightmap[(int)(x)][(int)(y + 1)];
    	double xmaxzmaxh = heightmap[(int)(x + 1)][(int)(y + 1)];
    	
    	double xweight = floorMod(x);
    	double zweight = floorMod(y);
    	
    	double xdiffzmin = xmaxzminh - xminzminh;
    	double xhpzmin = xdiffzmin * xweight;
    	double xhzmin = xhpzmin + xminzminh;
    	
    	double xdiffzmax = xmaxzmaxh - xminzmaxh;
    	double xhpzmax = xdiffzmax * xweight;
    	double xhzmax = xhpzmax + xminzmaxh;
    	
    	double zdiff = xhzmax - xhzmin;
    	double zhp = zdiff * zweight;
    	double zh = zhp + xhzmin;
    	return zh;
    }
	
	public double floorMod(double d) {
		if(d < 0) {
			return d - (int)(d) + 1;
		}
		else {
			return d - (int)(d);
		}
	}
}
