package iizvullokIcemountains.mod.world;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;

import net.minecraft.init.Biomes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeProvider;

public class BiomeProviderIceMountains extends BiomeProvider
{
    /** The biome generator object. */
    private final Biome biome;
    private World world;
    private boolean print = true;
    public static double [][] heightmap;
    public static double [][] fertilitymap;
    public static double [][] temperaturemap;
    public static int chunkX;
    public static int chunkZ;

    public BiomeProviderIceMountains(World world)
    {
        this.biome = Biomes.ICE_MOUNTAINS;
        this.world = world;
    }

    /**
     * Returns the biome generator
     */
    public Biome getBiome(BlockPos pos)
    {
    	//return Biomes.PLAINS;
        return this.biome;
    }

    /**
     * Returns an array of biomes for the location input.
     */
    public Biome[] getBiomesForGeneration(Biome[] biomes, int x, int z, int width, int height)
    {
        if (biomes == null || biomes.length < width * height)
        {
            biomes = new Biome[width * height];
        }

        Arrays.fill(biomes, 0, width * height, this.biome);
        return biomes;
    }

    /**
     * Gets biomes to use for the blocks and loads the other data like temperature and humidity onto the
     * WorldChunkManager.
     */
    public Biome[] getBiomes(@Nullable Biome[] oldBiomeList, int x, int z, int width, int depth)
    {
        if (oldBiomeList == null || oldBiomeList.length < width * depth)
        {
            oldBiomeList = new Biome[width * depth];
        }
        
        if(x < 0) {
        	x -= 1;
        }
        if(z < 0) {
        	z -= 1;
        }
        
        double[][] fmap = generateFertilityMap(x / 16, z / 16);
        double[][] tmap = generateTemperatureMap(x / 16, z / 16);
        double[][] hmap = generateHeightMap(x / 16, z / 16);
        
        fertilitymap = fmap;
        temperaturemap = tmap;
        heightmap = hmap;
        chunkX = x;
        chunkZ = z;
        
        for(int i = 0; i < 16; i++) {
        	for(int j = 0; j < 16; j++) {
        		Biome biome;
        		if(fmap[i][j] > 1) {
        			if(tmap[i][j] < 0.2) {
        				biome = Biomes.MUTATED_TAIGA_COLD;
        			}
        			else {
        				biome = Biomes.MUTATED_TAIGA;
        			}
        		}
        		else if(fmap[i][j] > 0.5) {
        			if(tmap[i][j] < -0.2) {
        				biome = Biomes.COLD_TAIGA;
        			}
        			else {
        				biome = Biomes.TAIGA;
        			}
        		}
        		else if(fmap[i][j] > 0) {
        			if(tmap[i][j] < -0.2) {
        				biome = Biomes.ICE_PLAINS;
        			}
        			else {
        				biome = Biomes.PLAINS;
        			}
        		}
        		else {
        			if(tmap[i][j] < -0.2) {
        				biome = Biomes.COLD_BEACH;
        			}
        			else {
        				biome = Biomes.STONE_BEACH;
        			}
        		}
        		if(hmap[i][j] > 128) {
        			biome = Biomes.ICE_MOUNTAINS;
        		}
        		if(hmap[i][j] < 64) {
        			if(tmap[i][j] < -0.2) {
        				biome = Biomes.FROZEN_OCEAN;
        			}
        			else {
        				biome = Biomes.OCEAN;
        			}
        		}
        		//biome = Biomes.ICE_MOUNTAINS;
        		oldBiomeList[getPosition(i, j)] = biome;
            }
        }
        return oldBiomeList;
    }
    
    public static int getPosition(int x, int z) {
    	return z * 16 + x;
    }
    
    public double[][] generateHeightMap(int x, int z){
    	double [][] heightmap = new double [16][16];
    	for(int i = 0; i < 16; i++) {
    		for(int j = 0; j < 16; j++) {
    			int ax = x * 16 + i;
    			int az = z * 16 + j;
    			heightmap[i][j] = generateOctave(128, 128, ax, az, this.world.getSeed());
    			heightmap[i][j] += generateOctave(64, 64, ax, az, this.world.getSeed());
        		heightmap[i][j] += generateOctave(32, 32, ax, az, this.world.getSeed());
        		heightmap[i][j] += generateOctave(16, 16, ax, az, this.world.getSeed());
        		heightmap[i][j] += generateOctave(8, 8, ax, az, this.world.getSeed());
        		heightmap[i][j] += generateOctave(4, 4, ax, az, this.world.getSeed());
        		heightmap[i][j] += generateOctave(2, 2, ax, az, this.world.getSeed());
        		double d = generateOctave(256, 1, ax, az, this.world.getSeed());
        		heightmap[i][j] += d * d * d * 192;
        		d = generateOctave(512, 1, ax, az, this.world.getSeed());
        		heightmap[i][j] += d * d * d * d * 384;
        	}
    	}
    	for(int i = 0; i < 16; i++) {
    		for(int j = 0; j < 16; j++) {
    			int ax = x * 16 + i;
    			int az = z * 16 + j;
    			double f = generateOctave(512, 1, ax, az, this.world.getSeed());
    			f += generateOctave(256, 0.5, ax, az, this.world.getSeed());
    			f += generateOctave(128, 0.25, ax, az, this.world.getSeed());
    			f /= 2;
    			double f2 = f * f;
    			f2 *= 4;
    			heightmap[i][j] *= f;
        	}
    	}
    	for(int i = 0; i < 16; i++) {
    		for(int j = 0; j < 16; j++) {
    			int ax = x * 16 + i;
    			int az = z * 16 + j;
    			if(heightmap[i][j] < 75) {
    				double d = 75 - heightmap[i][j];
    				heightmap[i][j] = 75 - (d / 2);
    				if(heightmap[i][j] < 70) {
        				d = 70 - heightmap[i][j];
        				heightmap[i][j] = 70 - (d / 3);
        				if(heightmap[i][j] < 64) {
            				d = 64 - heightmap[i][j];
            				heightmap[i][j] = 64 - (d * 10);
            				d = 64 - heightmap[i][j];
            				heightmap[i][j] = 64 - (Math.sqrt(d) * 4);
            			}
        			}
    			}
        	}
    	}
    	return heightmap;
    }
    
    public double[][] generateFertilityMap(int x, int z){
    	double [][] fertilitymap = new double [16][16];
    	for(int i = 0; i < 16; i++) {
    		for(int j = 0; j < 16; j++) {
    			int ax = x * 16 + i;
    			int az = z * 16 + j;
    			fertilitymap[i][j] = generateOctave(256, 1, ax, az, this.world.getSeed());
    			fertilitymap[i][j] += generateOctave(128, 0.5, ax, az, this.world.getSeed());
    			fertilitymap[i][j] *= 1.25;
    			fertilitymap[i][j] -= 0.25;
    			if(fertilitymap[i][j] > 1) {
    				fertilitymap[i][j] = 1;
    			}
        	}
    	}
    	return fertilitymap;
    }
    
    public double[][] generateTemperatureMap(int x, int z){
    	double [][] fertilitymap = new double [16][16];
    	for(int i = 0; i < 16; i++) {
    		for(int j = 0; j < 16; j++) {
    			int ax = x * 16 + i;
    			int az = z * 16 + j;
    			fertilitymap[i][j] = generateOctave(256, 1, ax, az, this.world.getSeed());
    			fertilitymap[i][j] += generateOctave(128, 0.5, ax, az, this.world.getSeed());
    			fertilitymap[i][j] -= 1;
        	}
    	}
    	return fertilitymap;
    }
    
    public double generateOctave(int frequency, double amplitude, int x, int z, long seed){
    	x = Math.abs(x + 34630988);
    	z = Math.abs(z + 84534737);
    	int xmin = x / frequency;
    	int xmax = xmin + 1;
    	int zmin = z / frequency;
    	int zmax = zmin + 1;
    	
    	double xminzminh = Math.sin(xmin * xmin * seed * zmin * zmin) * 0.5 + 0.5;
    	double xmaxzminh = Math.sin(xmax * xmax * seed * zmin * zmin) * 0.5 + 0.5;
    	double xminzmaxh = Math.sin(xmin * xmin * seed * zmax * zmax) * 0.5 + 0.5;
    	double xmaxzmaxh = Math.sin(xmax * xmax * seed * zmax * zmax) * 0.5 + 0.5;
    	
    	double xweight = (double)(Math.floorMod(x, frequency)) / (double)(frequency);
    	double zweight = (double)(Math.floorMod(z, frequency)) / (double)(frequency);
    	
    	double xdiffzmin = xmaxzminh - xminzminh;
    	double xhpzmin = xdiffzmin * xweight;
    	double xhzmin = xhpzmin + xminzminh;
    	
    	double xdiffzmax = xmaxzmaxh - xminzmaxh;
    	double xhpzmax = xdiffzmax * xweight;
    	double xhzmax = xhpzmax + xminzmaxh;
    	
    	double zdiff = xhzmax - xhzmin;
    	double zhp = zdiff * zweight;
    	double zh = zhp * amplitude + xhzmin * amplitude;
    	return zh;
    }

    /**
     * Gets a list of biomes for the specified blocks.
     */
    public Biome[] getBiomes(@Nullable Biome[] listToReuse, int x, int z, int width, int length, boolean cacheFlag)
    {
        return this.getBiomes(listToReuse, x, z, width, length);
    }

    @Nullable
    public BlockPos findBiomePosition(int x, int z, int range, List<Biome> biomes, Random random)
    {
        return biomes.contains(this.biome) ? new BlockPos(x - range + random.nextInt(range * 2 + 1), 0, z - range + random.nextInt(range * 2 + 1)) : null;
    }

    /**
     * checks given Chunk's Biomes against List of allowed ones
     */
    public boolean areBiomesViable(int x, int z, int radius, List<Biome> allowed)
    {
        return allowed.contains(this.biome);
    }

    public boolean isFixedBiome()
    {
        return true;
    }

    public Biome getFixedBiome()
    {
        return this.biome;
    }
}