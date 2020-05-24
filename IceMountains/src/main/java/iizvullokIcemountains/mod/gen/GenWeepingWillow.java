package iizvullokIcemountains.mod.gen;

import java.util.Random;

import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockNewLog;
import net.minecraft.block.BlockOldLeaf;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class GenWeepingWillow {
	public static void generate(BlockPos pos, World worldObj, double maxSize, long treeSeed) {
		Random rand = new Random();
		rand.setSeed(treeSeed);
		if(worldObj.getBlockState(pos.down()) != Blocks.DIRT.getDefaultState() && worldObj.getBlockState(pos.down()) != Blocks.GRASS.getDefaultState() && worldObj.getBlockState(pos.down()) != Blocks.DIRT.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.PODZOL) && worldObj.getBlockState(pos.down()) != Blocks.DIRT.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.COARSE_DIRT) && worldObj.getBlockState(pos.down()) != Blocks.GRASS_PATH && worldObj.getBlockState(pos.down()) != Blocks.CONCRETE_POWDER) {
			return;
		}
		if(worldObj.getBlockState(pos.up(2)) == Blocks.WATER.getDefaultState() && rand.nextDouble() > 0.9) {
			return;
		}
		if(worldObj.getBlockState(pos.up(3)) == Blocks.WATER.getDefaultState()) {
			return;
		}
		if(pos.getY() > 67) {
			return;
		}
		IBlockState logX = Blocks.LOG2.getDefaultState().withProperty(BlockNewLog.VARIANT, BlockPlanks.EnumType.DARK_OAK).withProperty(BlockLog.LOG_AXIS, BlockLog.EnumAxis.X);
		IBlockState logY = Blocks.LOG2.getDefaultState().withProperty(BlockNewLog.VARIANT, BlockPlanks.EnumType.DARK_OAK).withProperty(BlockLog.LOG_AXIS, BlockLog.EnumAxis.Y);
		IBlockState logZ = Blocks.LOG2.getDefaultState().withProperty(BlockNewLog.VARIANT, BlockPlanks.EnumType.DARK_OAK).withProperty(BlockLog.LOG_AXIS, BlockLog.EnumAxis.Z);
		IBlockState leaf = Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.OAK).withProperty(BlockLeaves.CHECK_DECAY, Boolean.valueOf(false));
		IBlockState air = Blocks.AIR.getDefaultState();
		IBlockState water = Blocks.WATER.getDefaultState();
		
		for(int j = -1; j < 2; j++) {
			for(int k = -1; k < 2; k++) {
				double d = j * j + k * k;
				if(d != 0) {
					double s = rand.nextDouble() * (maxSize / (10D * d)) + (maxSize / (10D * d));
					BlockPos p = new BlockPos(pos.getX() + j, pos.getY(), pos.getZ() + k);
					for(int i = 0; i < (int)(s + 1); i++) {
						worldObj.setBlockState(p.up(i - 1), logY);
					}
					if(worldObj.getBlockState(p.up((int)(s))) == air && rand.nextInt(3) == 0) {
						worldObj.setBlockState(p.up((int)(s)), Blocks.DARK_OAK_FENCE.getDefaultState());
					}
				}
			}
		}
		
		for(int j = 0; j < rand.nextInt(3) + 1; j++) {
			int size = (int)(Math.min(maxSize, 20) * (rand.nextDouble() * 0.25 + 0.75));
			double x = rand.nextDouble() - 0.5;
			double y = 0.3;
			double z = rand.nextDouble() - 0.5;
			double pX = 0;
			double pZ = 0;
			for(int i = 0; i < (double)size / y; i++) {
				BlockPos p = new BlockPos(pos.getX() + (int)(pX), pos.getY() + (int)((double)i * y), pos.getZ() + (int)(pZ));
				if(worldObj.getBlockState(p) == air || worldObj.getBlockState(p) == leaf || worldObj.getBlockState(p) == water) {
					worldObj.setBlockState(p, logY);
				}
				pX += rand.nextDouble() * x * ((double)(i * y) / (double)size);
				pZ += rand.nextDouble() * z * ((double)(i * y) / (double)size);
				
				double probability = Math.max((double)(i - 10) / (size / y), 0);
				double branchSize = (((double)size / y) - (double)i) / 6 + 3;
				//branchSize = 20;
				if(rand.nextDouble() < probability) {
					double bx = rand.nextDouble() - 0.5;
					double by = (rand.nextDouble() - 0.5) / 2;
					double bz = rand.nextDouble() - 0.5;
					double bpX = 0;
					double bpY = 0;
					double bpZ = 0;
					for(int i2 = 0; i2 < (double)branchSize / y; i2++) {
						//System.out.println("bla");
						BlockPos p2 = new BlockPos(p.getX() + (int)(bpX), p.getY() + (int)(bpY), p.getZ() + (int)(bpZ));
						if(worldObj.getBlockState(p2) == air || worldObj.getBlockState(p2) == leaf || worldObj.getBlockState(p2) == water) {
							worldObj.setBlockState(p2, logY);
						}
						bpX += rand.nextDouble() * bx * 1;
						bpY += rand.nextDouble() * by * 1;
						bpZ += rand.nextDouble() * bz * 1;
						if(worldObj.getBlockState(p2.up()) == air || worldObj.getBlockState(p2.up()) == leaf || worldObj.getBlockState(p2.up()) == water) {
							double prob = (double)(i2) / ((double)branchSize / y);
							if(rand.nextDouble() < prob)
							worldObj.setBlockState(p2.up(), leaf);
						}
						if(worldObj.getBlockState(p2.down()) == air || worldObj.getBlockState(p2.down()) == leaf || worldObj.getBlockState(p2.down()) == water) {
							double prob = (double)(i2) / ((double)branchSize / y);
							if(rand.nextDouble() < prob)
							worldObj.setBlockState(p2.down(), leaf);
						}
						if(worldObj.getBlockState(p2.north()) == air || worldObj.getBlockState(p2.north()) == leaf || worldObj.getBlockState(p2.north()) == water) {
							double prob = (double)(i2) / ((double)branchSize / y);
							if(rand.nextDouble() < prob)
							worldObj.setBlockState(p2.north(), leaf);
						}
						if(worldObj.getBlockState(p2.south()) == air || worldObj.getBlockState(p2.south()) == leaf || worldObj.getBlockState(p2.south()) == water) {
							double prob = (double)(i2) / ((double)branchSize / y);
							if(rand.nextDouble() < prob)
							worldObj.setBlockState(p2.south(), leaf);
						}
						if(worldObj.getBlockState(p2.west()) == air || worldObj.getBlockState(p2.west()) == leaf || worldObj.getBlockState(p2.west()) == water) {
							double prob = (double)(i2) / ((double)branchSize / y);
							if(rand.nextDouble() < prob)
							worldObj.setBlockState(p2.west(), leaf);
						}
						if(worldObj.getBlockState(p2.east()) == air || worldObj.getBlockState(p2.east()) == leaf || worldObj.getBlockState(p2.east()) == water) {
							double prob = (double)(i2) / ((double)branchSize / y);
							if(rand.nextDouble() < prob)
							worldObj.setBlockState(p2.east(), leaf);
						}
						if(i2 + 1 > (double)branchSize / y) {
							for(int b = 0; b < rand.nextInt(5) + 3; b++) {
								if(worldObj.getBlockState(p2.down().down(b)) == air || worldObj.getBlockState(p2.down().down(b)) == leaf || worldObj.getBlockState(p2.down().down(b)) == water) {
									worldObj.setBlockState(p2.down().down(b), leaf);
								}
							}
							for(int b = 0; b < rand.nextInt(5) + 3; b++) {
								if(worldObj.getBlockState(p2.north().down(b)) == air || worldObj.getBlockState(p2.north().down(b)) == leaf || worldObj.getBlockState(p2.north().down(b)) == water) {
									worldObj.setBlockState(p2.north().down(b), leaf);
								}
							}
							for(int b = 0; b < rand.nextInt(5) + 3; b++) {
								if(worldObj.getBlockState(p2.south().down(b)) == air || worldObj.getBlockState(p2.south().down(b)) == leaf || worldObj.getBlockState(p2.south().down(b)) == water) {
									worldObj.setBlockState(p2.south().down(b), leaf);
								}
							}
							for(int b = 0; b < rand.nextInt(5) + 3; b++) {
								if(worldObj.getBlockState(p2.west().down(b)) == air || worldObj.getBlockState(p2.west().down(b)) == leaf || worldObj.getBlockState(p2.west().down(b)) == water) {
									worldObj.setBlockState(p2.west().down(b), leaf);
								}
							}
							for(int b = 0; b < rand.nextInt(5) + 3; b++) {
								if(worldObj.getBlockState(p2.east().down(b)) == air || worldObj.getBlockState(p2.east().down(b)) == leaf || worldObj.getBlockState(p2.east().down(b)) == water) {
									worldObj.setBlockState(p2.east().down(b), leaf);
								}
							}
						}
					}
				}
			}
		}
	}
}