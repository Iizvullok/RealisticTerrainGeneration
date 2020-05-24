package iizvullokIcemountains.mod.world;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;

import iizvullokIcemountains.mod.init.BiomeInit;
import net.minecraft.init.Biomes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeProvider;

public class BiomeProviderIceIslands extends BiomeProvider
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

    public BiomeProviderIceIslands(World world)
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
        
        for(int i = 0; i < 16; i++) {
        	for(int j = 0; j < 16; j++) {
        		if(this.world.rand.nextInt(4) == 0) {
        			oldBiomeList[getPosition(i, j)] = Biomes.COLD_TAIGA;
        		}
        		else {
        			oldBiomeList[getPosition(i, j)] = Biomes.COLD_TAIGA;
        			//oldBiomeList[getPosition(i, j)] = BiomeInit.ICE_ISLANDS;
        		}
            }
        }
        return oldBiomeList;
    }
    
    public static int getPosition(int x, int z) {
    	return z * 16 + x;
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