package iizvullokIcemountains.mod.world;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import javax.annotation.Nullable;

import iizvullokIcemountains.mod.util.Heightmap;
import iizvullokIcemountains.mod.util.OpenSimplexNoise;
import net.minecraft.block.BlockConcretePowder;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockSnow;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.FlatGeneratorInfo;
import net.minecraft.world.gen.FlatLayerInfo;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.MapGenBase;
import net.minecraft.world.gen.MapGenCaves;
import net.minecraft.world.gen.MapGenRavine;
import net.minecraft.world.gen.feature.WorldGenDungeons;
import net.minecraft.world.gen.feature.WorldGenLakes;
import net.minecraft.world.gen.structure.MapGenMineshaft;
import net.minecraft.world.gen.structure.MapGenScatteredFeature;
import net.minecraft.world.gen.structure.MapGenStronghold;
import net.minecraft.world.gen.structure.MapGenStructure;
import net.minecraft.world.gen.structure.MapGenVillage;
import net.minecraft.world.gen.structure.StructureOceanMonument;
import net.minecraft.world.gen.structure.WoodlandMansion;

public class ChunkGeneratorIceMountains2 implements IChunkGenerator
{
	public static final int SIZE = 1024;
    private final World world;
    private final Random random;
    private final IBlockState[] cachedBlockIDs = new IBlockState[256];
    private final Map<String, MapGenStructure> structureGenerators = new HashMap<String, MapGenStructure>();
    private final boolean hasDecoration;
    private final boolean hasDungeons;
    private WorldGenLakes waterLakeGenerator;
    private WorldGenLakes lavaLakeGenerator;

    private MapGenBase caveGenerator = new MapGenCaves();;
    private MapGenVillage villageGenerator = new MapGenVillage();
    private MapGenBase ravineGenerator = new MapGenRavine();
    
    private boolean print = true;
    public static int maxY = 0;

    public ChunkGeneratorIceMountains2(World worldIn, long seed, boolean generateStructures, String flatGeneratorSettings)
    {
        this.world = worldIn;
        this.random = new Random(seed);
        
        this.structureGenerators.put("Mineshaft", new MapGenMineshaft());
        this.structureGenerators.put("Stronghold", new MapGenStronghold());

        this.waterLakeGenerator = new WorldGenLakes(Blocks.WATER);
        this.lavaLakeGenerator = new WorldGenLakes(Blocks.LAVA);

        this.hasDungeons = true;
        int j = 0;
        int k = 0;

        worldIn.setSeaLevel(j);
        this.hasDecoration = true;
    }

    /**
     * Generates the chunk at the specified position, from scratch
     */
    public Chunk generateChunk(int x, int z){
        ChunkPrimer chunkprimer = new ChunkPrimer();

        double [][][] chunkData = Heightmap.loadChunkData(x, z, this.world.getSeed());
        
        OpenSimplexNoise noise = new OpenSimplexNoise(this.world.getSeed());
        /*double [][][] noiseMap = new double [16][64][16];
        for(int i = 0; i < 16; i++) {
        	for(int j = 0; j < 64; j++) {
        		for(int k = 0; k < 16; k++) {
        			double FEATURE_SIZE_XZ = 64;
        			double FEATURE_SIZE_Y = 16;
        			double value = noise.eval((x * 16 + i) / FEATURE_SIZE_XZ, j / FEATURE_SIZE_Y, (z * 16 + k) / FEATURE_SIZE_XZ);
        			FEATURE_SIZE_XZ = 32;
        			FEATURE_SIZE_Y = 32;
    				for(int l = 0; l < 1; l++) {
    					value += noise.eval((x * 16 + i) / (FEATURE_SIZE_XZ / Math.pow(2, l + 1)), j / (FEATURE_SIZE_Y / Math.pow(2, l + 1)), (z * 16 + k) / (FEATURE_SIZE_XZ / Math.pow(2, l + 1))) / Math.pow(2, l + 1);
    				}
    				noiseMap[i][j][k] = value;
        		}
        	}
        }
        */
        Random random = new Random();
        random.setSeed((x + 3985735358L) * (z + 407340697436L) * this.world.getSeed());
        for (int j = 0; j < 16; ++j){
        	for (int k = 0; k < 16; ++k){
        		double absolute_max_roughness = Math.max(0, Math.min(1, (chunkData[j][k][5] - 200) / 40));
        		absolute_max_roughness = 0;
        		double max_roughness = Math.min(absolute_max_roughness * 12, (chunkData[j][k][1] + 1) * (chunkData[j][k][2] + 1) / 8);
        		double roughness = Math.min(max_roughness, Math.max(max_roughness - (Math.max(0, chunkData[j][k][0] - 100)) / 10, 0));
        		for(int i = 0; i < 256; i++) {
        			//double d = chunkData[j][k][0] + noiseMap[j][(int) Math.max(0, Math.min(63, i - chunkData[j][k][0] + 32))][k] * roughness;
        			double d = chunkData[j][k][0];
        			if(i < d) {
        				double rockyness = Math.max(0, d - 170 + chunkData[j][k][3] / 16) / 100;
        				if(random.nextDouble() < rockyness) {
        					chunkprimer.setBlockState(j, i, k, Blocks.COBBLESTONE.getDefaultState());
        				}
        				else {
        					chunkprimer.setBlockState(j, i, k, Blocks.STONE.getDefaultState());
        				}
        			}
        		}
        		boolean inStone = false;
        		int level = 0;
        		for(int i = 255; i >= 0; i--) {
        			if(!chunkprimer.getBlockState(j, i, k).equals(Blocks.AIR.getDefaultState()) && !inStone) {
        				inStone = true;
        				//Do a coverage here
        				int dirtLayer = (int) Math.max(0, 7D - chunkData[j][k][1] / 4D + chunkData[j][k][2] / 32D - chunkData[j][k][0] / 32D) - 2 * level;
        				double temperature = chunkData[j][k][3];
                		//0 to 255
                		double localTemperature = ((temperature - 64) - chunkData[j][k][0]) / 64;
                		//-64 to 192		0 to 255		-320 to 192		-5 to 3
                		int snowLayer = (int)(Math.min(8, Math.max(0, localTemperature * -4D - chunkData[j][k][1] / 8))) - 2 * level;
                		if(snowLayer > 0 && i >= 63) {
                			//chunkprimer.setBlockState(j, i + 1, k, Blocks.SNOW_LAYER.getDefaultState().withProperty(BlockSnow.LAYERS, snowLayer));
                		}
                		if(snowLayer > 0 && i < 63) {
                			chunkprimer.setBlockState(j, 63, k, Blocks.ICE.getDefaultState());
                		}
                		level++;
                		double localFertility = (chunkData[j][k][4] / 64) + localTemperature;
                		//0 to 4				-5 to 3				-5 to 7
                		
                		if(dirtLayer > 0 && i > dirtLayer) {
                			if(localFertility < -4) {
                				chunkprimer.setBlockState(j, i, k, Blocks.GRAVEL.getDefaultState());
                			}
                			if(localFertility < -3) {
                				chunkprimer.setBlockState(j, i, k, Blocks.DIRT.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.COARSE_DIRT));
                			}
                			if(localFertility < 2.5) {
                				chunkprimer.setBlockState(j, i, k, Blocks.GRASS.getDefaultState());
                			}
                			else {
                				chunkprimer.setBlockState(j, i, k, Blocks.DIRT.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.PODZOL));
                			}
                			if(i < 65) {
                				if(chunkData[j][k][6] +  chunkData[j][k][0] * 10 < 720) {
                					chunkprimer.setBlockState(j, i, k, Blocks.SAND.getDefaultState());
                				}
                				else {
                					chunkprimer.setBlockState(j, i, k, Blocks.GRAVEL.getDefaultState());
                				}
                			}
                			double glacierValue = 0;
                			if(snowLayer > 0) {
                				glacierValue = (snowLayer + (chunkData[j][k][1] / 32)) * chunkData[j][k][2] / 64 - Math.max(0, localFertility);
                			}
                			if(glacierValue > 4) {
                				chunkprimer.setBlockState(j, i, k, Blocks.SNOW.getDefaultState());
                			}
                			boolean set = true;
                			//dirtLayer = 4;
                			for(int l = 1; l < dirtLayer; l++) {
                				if(set && !chunkprimer.getBlockState(j, i - l, k).equals(Blocks.AIR.getDefaultState())) {
                					chunkprimer.setBlockState(j, i - l, k, Blocks.DIRT.getDefaultState());
                				}
                				else {
                					set = false;
                				}
                			}
        					if(i < 63 && chunkData[j][k][6] +  chunkData[j][k][0] * 10 > 720) {
        						chunkprimer.setBlockState(j, i - 1, k, Blocks.CLAY.getDefaultState());
        					}
                		}
                		double swampBlend = Math.min(1D, Math.max(0D, (chunkData[j][k][7] - 100D) / 56D));
                		double heightBlend = 1 - Math.min(1D, Math.max(0D, (chunkData[j][k][0] - 63D) / 4D));
        				double midBlend = 0;
        				if(chunkData[j][k][6] >= 80 && chunkData[j][k][6] < 98) {
        					double blend = (chunkData[j][k][6] - 80) / 18;
        					midBlend = blend;
        				}
        				else if(chunkData[j][k][6] >= 98 && chunkData[j][k][6] < 158) {
        					midBlend = 1D;
        				}
        				else if(chunkData[j][k][6] >= 158 && chunkData[j][k][6] < 176) {
        					double blend = (chunkData[j][k][6] - 158) / 18;
        					midBlend = 1D - blend;
        				}
                		if(random.nextDouble() < swampBlend * heightBlend * midBlend) {
                			if(i > 63) {
                				int p = random.nextInt(4);
                				if(p == 0) {
                					chunkprimer.setBlockState(j, i, k, Blocks.DIRT.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.PODZOL));
                				}
                				else if(p == 1) {
                					chunkprimer.setBlockState(j, i, k, Blocks.DIRT.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.COARSE_DIRT));
                				}
                				else if(p == 2) {
                					chunkprimer.setBlockState(j, i, k, Blocks.CONCRETE_POWDER.getDefaultState().withProperty(BlockConcretePowder.COLOR, EnumDyeColor.BROWN));
                				}
                				else{
                					chunkprimer.setBlockState(j, i, k, Blocks.GRASS_PATH.getDefaultState());
                				}
                			}
                			else {
                				int p = random.nextInt(3);
                				if(p == 0) {
                					chunkprimer.setBlockState(j, i, k, Blocks.DIRT.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.PODZOL));
                				}
                				else if(p == 1) {
                					chunkprimer.setBlockState(j, i, k, Blocks.DIRT.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.COARSE_DIRT));
                				}
                				else {
                					chunkprimer.setBlockState(j, i, k, Blocks.CONCRETE_POWDER.getDefaultState().withProperty(BlockConcretePowder.COLOR, EnumDyeColor.BROWN));
                				}
                			}
                		}
        			}
        			if(chunkprimer.getBlockState(j, i, k).equals(Blocks.AIR.getDefaultState()) && inStone) {
        				inStone = false;
        			}
        			if(i < 64 && chunkprimer.getBlockState(j, i, k).equals(Blocks.AIR.getDefaultState())) {
        				chunkprimer.setBlockState(j, i, k, Blocks.WATER.getDefaultState());
        			}
					if(chunkData[j][k][0] == 63) {
						double d410 = (chunkData[j][k][7] - 156) / 100;
						if(random.nextDouble() < d410 && chunkData[j][k][6] +  chunkData[j][k][0] * 10 > 720) {
	    					chunkprimer.setBlockState(j, 64, k, Blocks.WATERLILY.getDefaultState());
						}
					}
        			if(random.nextInt(5) > i - 1) {
        				chunkprimer.setBlockState(j, i, k, Blocks.BEDROCK.getDefaultState());
        			}
        			chunkprimer.setBlockState(0, 0, 0, Blocks.BARRIER.getDefaultState());
        		}
        	}

        	//decorate(x, z, chunkprimer);
        	
        	this.ravineGenerator.generate(this.world, x, z, chunkprimer);
        	this.caveGenerator.generate(this.world, x, z, chunkprimer);
        	this.villageGenerator.generate(this.world, x, z, chunkprimer);
        }

        for (MapGenBase mapgenbase : this.structureGenerators.values())
        {
            mapgenbase.generate(this.world, x, z, chunkprimer);
        }

        Chunk chunk = new Chunk(this.world, chunkprimer, x, z);
        Biome[] abiome = this.world.getBiomeProvider().getBiomes((Biome[])null, x * 16, z * 16, 16, 16);
        byte[] abyte = chunk.getBiomeArray();

        for (int l = 0; l < abyte.length; ++l)
        {
            abyte[l] = (byte)Biome.getIdForBiome(abiome[l]);
        }

        chunk.generateSkylightMap();
        return chunk;
    }
    
    public void placeTree(int x, int z, ChunkPrimer chunkprimer, int size, long treeSeed) {
    	
    }
    
    public void placeBlock(int x, int y, int z, IBlockState state, ChunkPrimer chunkprimer) {
    	if(x >= 0 && x < 16 && y >= 0 && y < 256 && z >= 0 && z < 16) {
    		chunkprimer.setBlockState(x, y, z, state);
    	}
    }
    
    public void decorate(int x, int z, ChunkPrimer chunkprimer) {
    	for(int i = -1; i < 2; i++) {
    		for(int j = -1; j < 2; j++) {
        		decorateChunk(x + i, z + j, chunkprimer);
        	}
    	}
    }
    
    public void decorateChunk(int x, int z, ChunkPrimer chunkprimer) {
    	for(int i = 0; i < 4; i++) {
    		int posX = (int) (Math.random() * 16);
    		int posZ = (int) (Math.random() * 16);
    		generateTree(posX, posZ, chunkprimer);
    	}
    }
    
    public void generateTree(int x, int z, ChunkPrimer chunkprimer) {
    	int y = chunkprimer.findGroundBlockIdx(x, z);
    	setBlockState(x, y, z, chunkprimer, Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.SPRUCE));
    }
    
    public void setBlockState(int x, int y, int z, ChunkPrimer chunkprimer, IBlockState block) {
    	if(x >= 0 && x < 16 && y >= 0 && y < 256 && z >= 0 && z < 16) {
    		chunkprimer.setBlockState(x, y, z, block);
    	}
    }

    /**
     * Generate initial structures in this chunk, e.g. mineshafts, temples, lakes, and dungeons
     */
    //TODO
    public void populate(int x, int z)
    {
        net.minecraft.block.BlockFalling.fallInstantly = true;
        int i = x * 16;
        int j = z * 16;
        BlockPos blockpos = new BlockPos(i, 0, j);
        Biome biome = this.world.getBiome(new BlockPos(i + 16, 0, j + 16));
        boolean flag = false;
        this.random.setSeed(this.world.getSeed());
        long k = this.random.nextLong() / 2L * 2L + 1L;
        long l = this.random.nextLong() / 2L * 2L + 1L;
        this.random.setSeed((long)x * k + (long)z * l ^ this.world.getSeed());
        ChunkPos chunkpos = new ChunkPos(x, z);

        net.minecraftforge.event.ForgeEventFactory.onChunkPopulate(true, this, this.world, this.random, x, z, flag);
        
        for (MapGenStructure mapgenstructure : this.structureGenerators.values())
        {
            boolean flag1 = mapgenstructure.generateStructure(this.world, this.random, chunkpos);
            if (mapgenstructure instanceof MapGenVillage)
            {
                flag |= flag1;
            }
        }

        if (this.waterLakeGenerator != null && !flag && this.random.nextInt(4) == 0)
        {
            this.waterLakeGenerator.generate(this.world, this.random, blockpos.add(this.random.nextInt(16) + 8, this.random.nextInt(256), this.random.nextInt(16) + 8));
        }

        if (this.lavaLakeGenerator != null && !flag && this.random.nextInt(8) == 0)
        {
            BlockPos blockpos1 = blockpos.add(this.random.nextInt(16) + 8, this.random.nextInt(this.random.nextInt(248) + 8), this.random.nextInt(16) + 8);

            if (blockpos1.getY() < this.world.getSeaLevel() || this.random.nextInt(10) == 0)
            {
                this.lavaLakeGenerator.generate(this.world, this.random, blockpos1);
            }
        }

        if (this.hasDungeons)
        {
            for (int i1 = 0; i1 < 8; ++i1)
            {
                (new WorldGenDungeons()).generate(this.world, this.random, blockpos.add(this.random.nextInt(16) + 8, this.random.nextInt(256), this.random.nextInt(16) + 8));
            }
        }

        if (this.hasDecoration)
        {
            biome.decorate(this.world, this.random, blockpos);
        }

        net.minecraftforge.event.ForgeEventFactory.onChunkPopulate(false, this, this.world, this.random, x, z, flag);
        net.minecraft.block.BlockFalling.fallInstantly = false;
    }

    /**
     * Called to generate additional structures after initial worldgen, used by ocean monuments
     */
    public boolean generateStructures(Chunk chunkIn, int x, int z)
    {
        return false;
    }

    public List<Biome.SpawnListEntry> getPossibleCreatures(EnumCreatureType creatureType, BlockPos pos)
    {
        Biome biome = this.world.getBiome(pos);
        return biome.getSpawnableList(creatureType);
    }

    @Nullable
    public BlockPos getNearestStructurePos(World worldIn, String structureName, BlockPos position, boolean findUnexplored)
    {
        MapGenStructure mapgenstructure = this.structureGenerators.get(structureName);
        return mapgenstructure != null ? mapgenstructure.getNearestStructurePos(worldIn, position, findUnexplored) : null;
    }

    public boolean isInsideStructure(World worldIn, String structureName, BlockPos pos)
    {
        MapGenStructure mapgenstructure = this.structureGenerators.get(structureName);
        return mapgenstructure != null ? mapgenstructure.isInsideStructure(pos) : false;
    }

    /**
     * Recreates data about structures intersecting given chunk (used for example by getPossibleCreatures), without
     * placing any blocks. When called for the first time before any chunk is generated - also initializes the internal
     * state needed by getPossibleCreatures.
     */
    public void recreateStructures(Chunk chunkIn, int x, int z)
    {
        for (MapGenStructure mapgenstructure : this.structureGenerators.values())
        {
            mapgenstructure.generate(this.world, x, z, (ChunkPrimer)null);
        }
    }
}