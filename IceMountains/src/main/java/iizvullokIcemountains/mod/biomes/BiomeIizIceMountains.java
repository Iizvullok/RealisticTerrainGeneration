package iizvullokIcemountains.mod.biomes;

import java.util.Random;

import com.jcraft.jorbis.Block;

import iizvullokIcemountains.mod.gen.GenSpruce;
import iizvullokIcemountains.mod.gen.GenWeepingWillow;
import iizvullokIcemountains.mod.init.BlockInit;
import iizvullokIcemountains.mod.util.Heightmap;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockOldLeaf;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockDoublePlant.EnumPlantType;
import net.minecraft.block.BlockSnow;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeTaiga;
import net.minecraft.world.biome.Biome.SpawnListEntry;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenBigTree;
import net.minecraft.world.gen.feature.WorldGenBlockBlob;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenPumpkin;
import net.minecraft.world.gen.feature.WorldGenTaiga1;
import net.minecraft.world.gen.feature.WorldGenTaiga2;
import net.minecraft.world.gen.feature.WorldGenTallGrass;
import net.minecraft.world.gen.feature.WorldGenerator;

public class BiomeIizIceMountains extends Biome {
    private static final WorldGenBlockBlob FOREST_ROCK_GENERATOR = new WorldGenBlockBlob(Blocks.MOSSY_COBBLESTONE, 0);
    private static final WorldGenTaiga1 PINE_GENERATOR = new WorldGenTaiga1();
    private static final WorldGenTaiga2 SPRUCE_GENERATOR = new WorldGenTaiga2(false);
    public static int depth = 0;
    public BlockPos chunkPos;

	public BiomeIizIceMountains() {
		super(new BiomeProperties("Epic Ice Mountains").setTemperature(0.25f).setRainfall(0));
		//Biomes
		this.decorator.coalGen = new WorldGenMinable(Blocks.STONE.getDefaultState(), 1);
		this.decorator.andesiteGen = new WorldGenMinable(Blocks.STONE.getDefaultState(), 1);
		this.decorator.dioriteGen = new WorldGenMinable(Blocks.STONE.getDefaultState(), 1);
		this.decorator.andesiteGen = new WorldGenMinable(Blocks.STONE.getDefaultState(), 1);
		this.decorator.treesPerChunk = 10;
		//this.de
	}
	
	public void decorate(World worldIn, Random rand, BlockPos pos)
    {
		boolean populated = false;
		//System.out.println(pos.getX() + " " + pos.getY() + " " + pos.getZ());
		if(worldIn.getBlockState(pos) == Blocks.BARRIER.getDefaultState()) {
			//System.out.println("Barrier found! Chunk is unpopulated");
		}
		else {
			populated = true;
		}
		boolean shouldBePopulated = false;
		int chunks = 0;
		for(int i = -1; i < 2; i++) {
			for(int j = -1; j < 2; j++) {
				if(worldIn.isChunkGeneratedAt(pos.getX() / 16 + i, pos.getZ() / 16 + j)) {
					chunks++;
				}
			}
		}
		if(chunks > 7) {
			shouldBePopulated = true;
			//System.out.println("Chunk should be populated at: " + pos.getX() + " " + pos.getZ());
		}
		
		if(!(!populated && shouldBePopulated)) {
			return;
		}
		//mark this chunk as populated
		worldIn.setBlockState(pos, Blocks.BEDROCK.getDefaultState());
		
		//check wether neighbor chunks should be populated
		for(int i = -1; i < 2; i++) {
			for(int j = -1; j < 2; j++) {
				if(worldIn.isChunkGeneratedAt(pos.getX() / 16 + i, pos.getZ() / 16 + j)) {
					//chunk exists and might be up for population
					chunks = 0;
					for(int ii = -1; ii < 2; ii++) {
						for(int jj = -1; jj < 2; jj++) {
							if(worldIn.isChunkGeneratedAt(pos.getX() / 16 + i + ii, pos.getZ() / 16 + j + jj)) {
								chunks++;
							}
						}
					}
					if(chunks > 7) {
						if(worldIn.getBlockState(new BlockPos(pos.getX() + i * 16, 0, pos.getZ() + j * 16)) == Blocks.BARRIER.getDefaultState()) {
							//System.out.println("Barrier found! Neighbor chunk is unpopulated");
							depth++;
							if(depth < 100) {
								decorate(worldIn, rand, new BlockPos(pos.getX() + i * 16, 0, pos.getZ() + j * 16));
							}
							depth--;
						}
					}
				}
			}
		}
		
		this.chunkPos = pos;
		double [][][] chunkData00 = Heightmap.loadChunkData(pos.getX() / 16, pos.getZ() / 16, worldIn.getSeed());
		double [][][] chunkData01 = Heightmap.loadChunkData(pos.getX() / 16, pos.getZ() / 16 + 16, worldIn.getSeed());
		double [][][] chunkData10 = Heightmap.loadChunkData(pos.getX() / 16 + 16, pos.getZ() / 16, worldIn.getSeed());
		double [][][] chunkData11 = Heightmap.loadChunkData(pos.getX() / 16 + 16, pos.getZ() / 16 + 16, worldIn.getSeed());
        if (net.minecraftforge.event.terraingen.TerrainGen.decorate(worldIn, rand, new net.minecraft.util.math.ChunkPos(pos), net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType.ROCK))
        {
        	
            int i = rand.nextInt(32);

            for (int j = 0; j < i; ++j)
            {
                int k = rand.nextInt(16);
                int l = rand.nextInt(16);
                BlockPos blockpos = worldIn.getHeight(pos.add(k, 0, l));
                double temperature;
                double localTemperature;
                int snowLayer;
                double localFertility = 0;
                double slope = 0;
                if(k < 16 && l < 16) {
                	temperature = chunkData00[k][l][3];
                	localTemperature = ((temperature - 64) - chunkData00[k][l][0]) / 64;
                	snowLayer = (int)(Math.min(8, Math.max(0, localTemperature * -4D - chunkData00[k][l][1] / 8)));
                	localFertility = (chunkData00[k][l][4] / 64) + localTemperature;
                	slope = chunkData00[k][l][1];
                }
                if(k < 16 && l >= 16) {
                	temperature = chunkData01[k][l - 16][3];
                	localTemperature = ((temperature - 64) - chunkData01[k][l - 16][0]) / 64;
                	snowLayer = (int)(Math.min(8, Math.max(0, localTemperature * -4D - chunkData01[k][l - 16][1] / 8)));
                	localFertility = (chunkData01[k][l - 16][4] / 64) + localTemperature;
                	slope = chunkData01[k][l - 16][1];
                }
                if(k >= 16 && l < 16) {
                	temperature = chunkData10[k - 16][l][3];
                	localTemperature = ((temperature - 64) - chunkData10[k - 16][l][0]) / 64;
                	snowLayer = (int)(Math.min(8, Math.max(0, localTemperature * -4D - chunkData10[k - 16][l][1] / 8)));
                	localFertility = (chunkData10[k - 16][l][4] / 64) + localTemperature;
                	slope = chunkData10[k - 16][l][1];
                }
                if(k >= 16 && l >= 16) {
                	temperature = chunkData11[k - 16][l - 16][3];
                	localTemperature = ((temperature - 64) - chunkData11[k - 16][l - 16][0]) / 64;
                	snowLayer = (int)(Math.min(8, Math.max(0, localTemperature * -4D - chunkData11[k - 16][l - 16][1] / 8)));
                	localFertility = (chunkData11[k - 16][l - 16][4] / 64) + localTemperature;
                	slope = chunkData11[k - 16][l - 16][1];
                }
        		int bla = rand.nextInt(3);
        		double swampBlend = Math.min(1D, Math.max(0D, (chunkData00[k][l][7] - 100D) / 56D));
				double midBlend = 0;
				if(chunkData00[k][l][6] >= 80 && chunkData00[k][l][6] < 98) {
					double blend = (chunkData00[k][l][6] - 80) / 18;
					midBlend = blend;
				}
				else if(chunkData00[k][l][6] >= 98 && chunkData00[k][l][6] < 158) {
					midBlend = 1D;
				}
				else if(chunkData00[k][l][6] >= 158 && chunkData00[k][l][6] < 176) {
					double blend = (chunkData00[k][l][6] - 158) / 18;
					midBlend = 1D - blend;
				}
        		if(bla != 0) {
        			 /*WorldGenAbstractTree worldgenabstracttree = this.getRandomTreeFeature(rand);
                     worldgenabstracttree.setDecorationDefaults();
                     blockpos = worldIn.getHeight(pos.add(k, 0, l));
                     double p = Math.max(0, localFertility) - blockpos.getY() / 100;
                     if(p > rand.nextDouble() * 4 && blockpos.getY() < 150 && localFertility > 0.5) {
                    	 if (worldgenabstracttree.generate(worldIn, rand, blockpos))
                         {
                             worldgenabstracttree.generateSaplings(worldIn, rand, blockpos);
                         }
                     }*/
        			
        			blockpos = worldIn.getHeight(pos.add(k, 0, l));
        			double p = Math.max(0, localFertility) - blockpos.getY() / 100;
        			if(p > rand.nextDouble() * 4 && blockpos.getY() < 150 && localFertility > 0.5) {
        				double factor = 1;
        				if(blockpos.getY() > 100) {
        					factor = ((100D - blockpos.getY()) / 50D) + 1D;
        				}
        				
        				if(rand.nextDouble() < swampBlend * midBlend) {
        					BlockPos swampBlockpos = getHeightIgnoringWater(worldIn, blockpos);
            				GenWeepingWillow.generate(swampBlockpos, worldIn, (localFertility - 0.5) * 8 * factor + 3, worldIn.getSeed() * (blockpos.getX() + 40000000) * (blockpos.getZ() + 40000000) * rand.nextInt(1000000));
        				}
        				else {
        					double oakblend = Math.min(1, Math.max(0, (chunkData00[k][l][3] - 78D) / 100D)) * (1 - Math.min(1, Math.max(0, (chunkData00[k][l][0] - 70D) / 25D)));
        					//System.out.println(chunkData00[k][l][3] + " " + oakblend);
        					if(rand.nextDouble() < oakblend) {
        						WorldGenBigTree tree = new WorldGenBigTree(false);
            					tree.generate(worldIn, rand, blockpos);
        					}
        					else {
                				GenSpruce.generate(blockpos, worldIn, (localFertility - 0.5) * 8 * factor + 3, worldIn.getSeed() * (blockpos.getX() + 40000000) * (blockpos.getZ() + 40000000) * rand.nextInt(1000000));
        					}
        				}
        			}
        		}
        		else {
                    if((1D - swampBlend * midBlend) * localFertility > rand.nextInt(4) && blockpos.getY() < 128 && slope < 8 && blockpos.getY() > 63) {
                    	double blockProbability = 0.5;
    					BlockPos pos2 = getHeightIgnoringWater(worldIn, blockpos);
                    	if(worldIn.getBlockState(pos2) == Blocks.DIRT.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.PODZOL)) {
                    		blockProbability = 1;
                    	}
                    	if(worldIn.getBlockState(pos2) == Blocks.GRAVEL.getDefaultState()) {
                    		blockProbability = 0;
                    	}
                    	if(worldIn.getBlockState(pos2) == Blocks.SAND.getDefaultState()) {
                    		blockProbability = 0;
                    	}
                    	if(worldIn.getBlockState(pos2) == Blocks.GRASS.getDefaultState()) {
                    		blockProbability = 0.1;
                    	}
                    	if(worldIn.getBlockState(pos2.up()) == Blocks.DIRT.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.PODZOL)) {
                    		blockProbability = 1;
                    	}
                    	if(worldIn.getBlockState(pos2.up()) == Blocks.GRAVEL.getDefaultState()) {
                    		blockProbability = 0;
                    	}
                    	if(worldIn.getBlockState(pos2.up()) == Blocks.SAND.getDefaultState()) {
                    		blockProbability = 0;
                    	}
                    	if(worldIn.getBlockState(pos2.up()) == Blocks.GRASS.getDefaultState()) {
                    		blockProbability = 0.1;
                    	}
                    	if(worldIn.getBlockState(pos2.down()) == Blocks.DIRT.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.PODZOL)) {
                    		blockProbability = 1;
                    	}
                    	if(worldIn.getBlockState(pos2.down()) == Blocks.GRAVEL.getDefaultState()) {
                    		blockProbability = 0;
                    	}
                    	if(worldIn.getBlockState(pos2.down()) == Blocks.SAND.getDefaultState()) {
                    		blockProbability = 0;
                    	}
                    	if(worldIn.getBlockState(pos2.down()) == Blocks.GRASS.getDefaultState()) {
                    		blockProbability = 0.1;
                    	}
                    	if(rand.nextDouble() < blockProbability) {
                            FOREST_ROCK_GENERATOR.generate(worldIn, rand, blockpos);
                    	}
                    }
        		}
            }
        }

        DOUBLE_PLANT_GENERATOR.setPlantType(BlockDoublePlant.EnumPlantType.FERN);

        //if(net.minecraftforge.event.terraingen.TerrainGen.decorate(worldIn, rand, new net.minecraft.util.math.ChunkPos(pos), net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType.FLOWERS))
        for (int i1 = 0; i1 < 128; ++i1)
        {
            int j1 = rand.nextInt(16);
            int k1 = rand.nextInt(16);
            double temperature = chunkData00[j1][k1][3];
            double localTemperature = ((temperature - 64) - chunkData00[j1][k1][0]) / 64;
            double snowLayer = (int)(Math.min(8, Math.max(0, localTemperature * -4D - chunkData00[j1][k1][1] / 8)));
            double localFertility = (chunkData00[j1][k1][4] / 64) + localTemperature;
            if(rand.nextDouble() * 7 < localFertility && localFertility > 0) {
            	int l1 = worldIn.getHeight(pos.add(j1, 0, k1)).getY();
                BlockPos p = new BlockPos(j1 + pos.getX(), l1, pos.getZ() + k1);
                if(worldIn.isAirBlock(p) && worldIn.getBlockState(p.down()) == Blocks.SAND.getDefaultState()) {
                	if(worldIn.getBlockState(p.down().north()) == Blocks.WATER.getDefaultState() || worldIn.getBlockState(p.down().south()) == Blocks.WATER.getDefaultState() || worldIn.getBlockState(p.down().east()) == Blocks.WATER.getDefaultState() || worldIn.getBlockState(p.down().west()) == Blocks.WATER.getDefaultState()) {
                		worldIn.setBlockState(p, Blocks.REEDS.getDefaultState());
                		worldIn.setBlockState(p.up(), Blocks.REEDS.getDefaultState());
                		if(rand.nextDouble() < 0.7) {
                			worldIn.setBlockState(p.up(2), Blocks.REEDS.getDefaultState());
                			if(rand.nextDouble() < 0.3) {
                				worldIn.setBlockState(p.up(3), Blocks.REEDS.getDefaultState());
                			}
                		}
                	}
                }
                if (worldIn.isAirBlock(p) && (!worldIn.provider.isNether() || p.getY() < 254) && Blocks.DOUBLE_PLANT.canPlaceBlockAt(worldIn, p)){
                	if(rand.nextInt(4) == 0) {
                    	if(rand.nextInt(3) == 0) {
                        	if(rand.nextBoolean()) {
                                Blocks.DOUBLE_PLANT.placeAt(worldIn, p, EnumPlantType.FERN, 2);
                        	}
                        	else {
                        		if(rand.nextInt(8) == 0) {
                        			Blocks.DOUBLE_PLANT.placeAt(worldIn, p, EnumPlantType.ROSE, 2);
                        		}
                        		else {
                        			Blocks.DOUBLE_PLANT.placeAt(worldIn, p, EnumPlantType.GRASS, 2);
                        		}
                        	}
                    	}
                    	else {
                    		if(rand.nextBoolean()) {
            					worldIn.setBlockState(p, Blocks.TALLGRASS.getDefaultState().withProperty(BlockTallGrass.TYPE, BlockTallGrass.EnumType.GRASS));
                    		}
                    		else {
                    			if(rand.nextInt(12) == 0) {
                    				if(rand.nextBoolean()) {
                    					worldIn.setBlockState(p, Blocks.YELLOW_FLOWER.getDefaultState());
                    				}
                    				else {
                    					worldIn.setBlockState(p, Blocks.RED_FLOWER.getDefaultState());
                    				}
                    			}
                    			else {
                					worldIn.setBlockState(p, Blocks.TALLGRASS.getDefaultState().withProperty(BlockTallGrass.TYPE, BlockTallGrass.EnumType.FERN));
                    			}
                    		}
                    	}
                	}
                	else {
                		double forestBlend = 1 - Math.min(1D, Math.max(0D, (chunkData00[j1][k1][7] - 100D) / 56D));
        				double midBlend = 0;
        				if(chunkData00[j1][k1][6] >= 80 && chunkData00[j1][k1][6] < 98) {
        					double blend = (chunkData00[j1][k1][6] - 80) / 18;
        					midBlend = blend;
        				}
        				else if(chunkData00[j1][k1][6] >= 98 && chunkData00[j1][k1][6] < 158) {
        					midBlend = 1D;
        				}
        				else if(chunkData00[j1][k1][6] >= 158 && chunkData00[j1][k1][6] < 176) {
        					double blend = (chunkData00[j1][k1][6] - 158) / 18;
        					midBlend = 1D - blend;
        				}
        				if(rand.nextDouble() < forestBlend * midBlend) {
        					//TODO
        					double d0 = rand.nextDouble();

        		            BlockFlower.EnumFlowerType blockflower$enumflowertype = BlockFlower.EnumFlowerType.values()[(int)(d0 * (double)BlockFlower.EnumFlowerType.values().length)];
        	                BlockFlower blockflower = blockflower$enumflowertype.getBlockType().getBlock();
        		            worldIn.setBlockState(p, blockflower.getDefaultState().withProperty(blockflower.getTypeProperty(), blockflower$enumflowertype));
        				}
                	}
                }
            }
        }
        
        if (rand.nextInt(16) == 0)
        {
            int i5 = rand.nextInt(16);
            int k9 = rand.nextInt(16);
            int j13 = worldIn.getHeight(this.chunkPos.add(i5, 0, k9)).getY() * 2;

            if (j13 > 0)
            {
                int k16 = rand.nextInt(j13);
                (new WorldGenPumpkin()).generate(worldIn, rand, this.chunkPos.add(i5, k16, k9));
            }
        }
        
        for(int i = 0; i < 16; i++) {
        	for(int j = 0; j < 16; j++) {
        		double temperature = chunkData00[i][j][3];
                double localTemperature = ((temperature - 64) - chunkData00[i][j][0]) / 64;
        		int snowLayer = (int)(Math.min(8, Math.max(0, localTemperature * -4D - chunkData00[i][j][1] / 8)));
        		int y = worldIn.getHeight(i + pos.getX(), j + pos.getZ());
        		if(y >= 64 && snowLayer > 0) {
        			worldIn.setBlockState(new BlockPos(i + pos.getX(), y, j + pos.getZ()) , Blocks.SNOW_LAYER.getDefaultState().withProperty(BlockSnow.LAYERS, snowLayer));
        		}
        		BlockPos position = new BlockPos(i + pos.getX(), y - 1, j + pos.getZ());
        		IBlockState block = worldIn.getBlockState(position);
        		IBlockState logX = Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.SPRUCE).withProperty(BlockLog.LOG_AXIS, BlockLog.EnumAxis.X);
        		IBlockState logY = Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.SPRUCE).withProperty(BlockLog.LOG_AXIS, BlockLog.EnumAxis.Y);
        		IBlockState logZ = Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.SPRUCE).withProperty(BlockLog.LOG_AXIS, BlockLog.EnumAxis.Z);
        		IBlockState leaf = Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.SPRUCE).withProperty(BlockLeaves.CHECK_DECAY, Boolean.valueOf(false));
        		if(block == logX || block == logY || block == logZ || block == leaf) {
        			boolean canPutSnow = true;
        			int down = 0;
        			while(canPutSnow) {
        				down++;
        				if(y - down < 64) {
        					canPutSnow = false;
        				}
        				block = worldIn.getBlockState(position.down(down));
        				if(block == logX || block == logY || block == logZ || block == leaf) {
        					canPutSnow = false;
        				}
        				if(worldIn.getBlockState(position.down(down)) != Blocks.AIR.getDefaultState() && worldIn.getBlockState(position.down(down - 1)) == Blocks.AIR.getDefaultState()) {
        					if(y >= 64 && snowLayer > 0) {
        	        			worldIn.setBlockState(new BlockPos(i + pos.getX(), y, j + pos.getZ()) , Blocks.SNOW_LAYER.getDefaultState().withProperty(BlockSnow.LAYERS, snowLayer));
        	        		}
        				}
        			}
        		}
        	}
        }
        this.genStandardOre1(worldIn, rand, 1, new WorldGenMinable(Blocks.COAL_ORE.getDefaultState(), 30), 0, 100);
        this.genStandardOre1(worldIn, rand, 16, new WorldGenMinable(Blocks.IRON_ORE.getDefaultState(), 12), 0, 80);
        this.genStandardOre1(worldIn, rand, 8, new WorldGenMinable(Blocks.REDSTONE_ORE.getDefaultState(), 8), 0, 40);
        this.genStandardOre1(worldIn, rand, 2, new WorldGenMinable(Blocks.DIAMOND_ORE.getDefaultState(), 6), 0, 20);
        this.genStandardOre1(worldIn, rand, 6, new WorldGenMinable(Blocks.LAPIS_ORE.getDefaultState(), 8), 0, 30);
        this.genStandardOre1(worldIn, rand, 4, new WorldGenMinable(Blocks.GOLD_ORE.getDefaultState(), 6), 0, 30);
        this.genStandardOre1(worldIn, rand, 3, new WorldGenMinable(Blocks.EMERALD_ORE.getDefaultState(), 4), 0, 40);
       //super.decorate(worldIn, rand, pos);
    }
	
	public BlockPos getHeightIgnoringWater(World worldObj, BlockPos pos) {
		pos = worldObj.getHeight(pos);
		for(int i = 255; i >= 0; i--) {
			BlockPos p = new BlockPos(pos.getX(), i, pos.getZ());
			if(worldObj.getBlockState(p) != Blocks.AIR.getDefaultState() && worldObj.getBlockState(p) != Blocks.WATER.getDefaultState()) {
				return p.up();
			}
		}
		return new BlockPos(pos.getX(), 0, pos.getZ());
	}
	
	protected void genStandardOre1(World worldIn, Random random, int blockCount, WorldGenerator generator, int minHeight, int maxHeight)
    {
        if (maxHeight < minHeight)
        {
            int i = minHeight;
            minHeight = maxHeight;
            maxHeight = i;
        }
        else if (maxHeight == minHeight)
        {
            if (minHeight < 255)
            {
                ++maxHeight;
            }
            else
            {
                --minHeight;
            }
        }

        for (int j = 0; j < blockCount; ++j)
        {
            BlockPos blockpos = this.chunkPos.add(random.nextInt(16), random.nextInt(maxHeight - minHeight) + minHeight, random.nextInt(16));
            generator.generate(worldIn, random, blockpos);
        }
    }
	
	public int getHeight(World worldObj, int posX, int posZ) {
		for(int i = 255; i >= 0; i--) {
			if(!worldObj.getBlockState(new BlockPos(posX, i, posZ)).equals(Blocks.AIR.getDefaultState())) {
				return i;
			}
		}
		return 0;
	}

    public WorldGenAbstractTree getRandomTreeFeature(Random rand)
    {
    	return (WorldGenAbstractTree)(rand.nextInt(3) == 0 ? PINE_GENERATOR : SPRUCE_GENERATOR);
    }

    /**
     * Gets a WorldGen appropriate for this biome.
     */
    public WorldGenerator getRandomWorldGenForGrass(Random rand)
    {
        return rand.nextInt(5) > 0 ? new WorldGenTallGrass(BlockTallGrass.EnumType.FERN) : new WorldGenTallGrass(BlockTallGrass.EnumType.GRASS);
    }
    


    public void genTerrainBlocks(World worldIn, Random rand, ChunkPrimer chunkPrimerIn, int x, int z, double noiseVal)
    {
        if (true)
        {
            this.topBlock = Blocks.GRASS.getDefaultState();
            this.fillerBlock = Blocks.DIRT.getDefaultState();

            if (noiseVal > 1.75D)
            {
                this.topBlock = Blocks.DIRT.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.COARSE_DIRT);
            }
            else if (noiseVal > -0.95D)
            {
                this.topBlock = Blocks.DIRT.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.PODZOL);
            }
        }

        this.generateBiomeTerrain(worldIn, rand, chunkPrimerIn, x, z, noiseVal);
    }
}