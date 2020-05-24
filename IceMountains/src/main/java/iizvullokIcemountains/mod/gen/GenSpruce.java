package iizvullokIcemountains.mod.gen;

import java.util.Random;

import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockOldLeaf;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class GenSpruce {
	public static void generate(BlockPos pos, World worldObj, double maxSize, long treeSeed) {
		if(worldObj.getBlockState(pos.down()) != Blocks.DIRT.getDefaultState() && worldObj.getBlockState(pos.down()) != Blocks.GRASS.getDefaultState() && worldObj.getBlockState(pos.down()) != Blocks.DIRT.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.PODZOL)) {
			return;
		}
		Random rand = new Random();
		rand.setSeed(treeSeed);
		int size = (int)(Math.min(maxSize, 20) * (rand.nextDouble() * 0.25 + 0.75));
		for(int i = 0; i < size; i++) {
			worldObj.setBlockState(new BlockPos(pos.getX(), pos.getY() + i, pos.getZ()), Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.SPRUCE));
		}
		IBlockState logX = Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.SPRUCE).withProperty(BlockLog.LOG_AXIS, BlockLog.EnumAxis.X);
		IBlockState logZ = Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.SPRUCE).withProperty(BlockLog.LOG_AXIS, BlockLog.EnumAxis.Z);
		IBlockState leaf = Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.SPRUCE).withProperty(BlockLeaves.CHECK_DECAY, Boolean.valueOf(false));
		worldObj.setBlockState(new BlockPos(pos.getX(), pos.getY() + size, pos.getZ()), leaf);
		worldObj.setBlockState(new BlockPos(pos.getX() - 1, pos.getY() + size - 1, pos.getZ()), leaf);
		worldObj.setBlockState(new BlockPos(pos.getX() + 1, pos.getY() + size - 1, pos.getZ()), leaf);
		worldObj.setBlockState(new BlockPos(pos.getX(), pos.getY() + size - 1, pos.getZ() - 1), leaf);
		worldObj.setBlockState(new BlockPos(pos.getX(), pos.getY() + size - 1, pos.getZ() + 1), leaf);
		for(int i = size; i > 2;) {
			if(i != size) {
				int branchLength = (int)((size - i) / 4 + rand.nextDouble() * 0.7);
				double zDir = (rand.nextDouble() - 0.5) * 1.6;
				double zOffset = (zDir / Math.abs(zDir)) * rand.nextDouble() * 0.9;
				double yDir = rand.nextDouble() * 0.5 + 0.5;
				for(int j = 0; j <= branchLength; j++) {
					int x = pos.getX() - j;
					int y = pos.getY() + i - (int)(yDir * j);
					int z = pos.getZ() + (int)(zOffset + zDir * j);
					BlockPos blockpos = new BlockPos(x, y, z);
					IBlockState block = worldObj.getBlockState(blockpos);
					if(block == Blocks.AIR.getDefaultState() || block == Blocks.LEAVES.getDefaultState()) {
						worldObj.setBlockState(blockpos, logX);
						blockpos = new BlockPos(x - 1, y, z);
						if(worldObj.getBlockState(blockpos) == Blocks.AIR.getDefaultState()) {
							worldObj.setBlockState(blockpos, leaf);
						}
						blockpos = new BlockPos(x + 1, y, z);
						if(worldObj.getBlockState(blockpos) == Blocks.AIR.getDefaultState()) {
							worldObj.setBlockState(blockpos, leaf);
						}
						blockpos = new BlockPos(x, y, z - 1);
						if(worldObj.getBlockState(blockpos) == Blocks.AIR.getDefaultState()) {
							worldObj.setBlockState(blockpos, leaf);
						}
						blockpos = new BlockPos(x, y, z + 1);
						if(worldObj.getBlockState(blockpos) == Blocks.AIR.getDefaultState()) {
							worldObj.setBlockState(blockpos, leaf);
						}
					}
				}
			}
			i -= (int)(rand.nextDouble() * 2.3 + 2.4);
		}
		for(int i = size; i > 2;) {
			if(i != size) {
				int branchLength = (int)((size - i) / 4 + rand.nextDouble() * 0.7);
				double zDir = (rand.nextDouble() - 0.5) * 1.6;
				double zOffset = (zDir / Math.abs(zDir)) * rand.nextDouble() * 0.9;
				double yDir = rand.nextDouble() * 0.5 + 0.5;
				for(int j = 0; j <= branchLength; j++) {
					int x = pos.getX() + j;
					int y = pos.getY() + i - (int)(yDir * j);
					int z = pos.getZ() + (int)(zOffset + zDir * j);BlockPos blockpos = new BlockPos(x, y, z);
					IBlockState block = worldObj.getBlockState(blockpos);
					if(block == Blocks.AIR.getDefaultState() || block == Blocks.LEAVES.getDefaultState()) {
						worldObj.setBlockState(blockpos, logX);
						blockpos = new BlockPos(x - 1, y, z);
						if(worldObj.getBlockState(blockpos) == Blocks.AIR.getDefaultState()) {
							worldObj.setBlockState(blockpos, leaf);
						}
						blockpos = new BlockPos(x + 1, y, z);
						if(worldObj.getBlockState(blockpos) == Blocks.AIR.getDefaultState()) {
							worldObj.setBlockState(blockpos, leaf);
						}
						blockpos = new BlockPos(x, y, z - 1);
						if(worldObj.getBlockState(blockpos) == Blocks.AIR.getDefaultState()) {
							worldObj.setBlockState(blockpos, leaf);
						}
						blockpos = new BlockPos(x, y, z + 1);
						if(worldObj.getBlockState(blockpos) == Blocks.AIR.getDefaultState()) {
							worldObj.setBlockState(blockpos, leaf);
						}
					}
				}
			}
			i -= (int)(rand.nextDouble() * 2.3 + 2.4);
		}
		for(int i = size; i > 2;) {
			if(i != size) {
				int branchLength = (int)((size - i) / 4 + rand.nextDouble() * 0.7);
				double zDir = (rand.nextDouble() - 0.5) * 1.6;
				double zOffset = (zDir / Math.abs(zDir)) * rand.nextDouble() * 0.9;
				double yDir = rand.nextDouble() * 0.5 + 0.5;
				for(int j = 0; j <= branchLength; j++) {
					int x = pos.getX() + (int)(zOffset + zDir * j);
					int y = pos.getY() + i - (int)(yDir * j);
					int z = pos.getZ() - j;
					BlockPos blockpos = new BlockPos(x, y, z);
					IBlockState block = worldObj.getBlockState(blockpos);
					if(block == Blocks.AIR.getDefaultState() || block == Blocks.LEAVES.getDefaultState()) {
						worldObj.setBlockState(blockpos, logZ);
						blockpos = new BlockPos(x - 1, y, z);
						if(worldObj.getBlockState(blockpos) == Blocks.AIR.getDefaultState()) {
							worldObj.setBlockState(blockpos, leaf);
						}
						blockpos = new BlockPos(x + 1, y, z);
						if(worldObj.getBlockState(blockpos) == Blocks.AIR.getDefaultState()) {
							worldObj.setBlockState(blockpos, leaf);
						}
						blockpos = new BlockPos(x, y, z - 1);
						if(worldObj.getBlockState(blockpos) == Blocks.AIR.getDefaultState()) {
							worldObj.setBlockState(blockpos, leaf);
						}
						blockpos = new BlockPos(x, y, z + 1);
						if(worldObj.getBlockState(blockpos) == Blocks.AIR.getDefaultState()) {
							worldObj.setBlockState(blockpos, leaf);
						}
					}
				}
			}
			i -= (int)(rand.nextDouble() * 2.3 + 2.4);
		}
		for(int i = size; i > 2;) {
			if(i != size) {
				int branchLength = (int)((size - i) / 4 + rand.nextDouble() * 0.7);
				double zDir = (rand.nextDouble() - 0.5) * 1.6;
				double zOffset = (zDir / Math.abs(zDir)) * rand.nextDouble() * 0.9;
				double yDir = rand.nextDouble() * 0.5 + 0.5;
				for(int j = 0; j <= branchLength; j++) {
					int x = pos.getX() + (int)(zOffset + zDir * j);
					int y = pos.getY() + i - (int)(yDir * j);
					int z = pos.getZ() + j;
					BlockPos blockpos = new BlockPos(x, y, z);
					IBlockState block = worldObj.getBlockState(blockpos);
					if(block == Blocks.AIR.getDefaultState() || block == Blocks.LEAVES.getDefaultState()) {
						worldObj.setBlockState(blockpos, logZ);
						blockpos = new BlockPos(x - 1, y, z);
						if(worldObj.getBlockState(blockpos) == Blocks.AIR.getDefaultState()) {
							worldObj.setBlockState(blockpos, leaf);
						}
						blockpos = new BlockPos(x + 1, y, z);
						if(worldObj.getBlockState(blockpos) == Blocks.AIR.getDefaultState()) {
							worldObj.setBlockState(blockpos, leaf);
						}
						blockpos = new BlockPos(x, y, z - 1);
						if(worldObj.getBlockState(blockpos) == Blocks.AIR.getDefaultState()) {
							worldObj.setBlockState(blockpos, leaf);
						}
						blockpos = new BlockPos(x, y, z + 1);
						if(worldObj.getBlockState(blockpos) == Blocks.AIR.getDefaultState()) {
							worldObj.setBlockState(blockpos, leaf);
						}
					}
				}
			}
			i -= (int)(rand.nextDouble() * 2.3 + 2.4);
		}
	}
}