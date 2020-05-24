package iizvullokIcemountains.mod.world;

import iizvullokIcemountains.mod.init.BiomeInit;
import net.minecraft.init.Biomes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.biome.BiomeProviderSingle;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.*;

public class WorldTypeIceIslands extends WorldType{

	public WorldTypeIceIslands(String name) {
		super("IceIslands");
	}

	@Override
	public BiomeProvider getBiomeProvider(World world) {
		return new BiomeProviderIceIslands(world);
	}
	
	@Override
	public IChunkGenerator getChunkGenerator(World world, String generatorOptions) {
		return new ChunkGeneratorIceIslands(world, false, world.getSeed());
	}
	
	@Override
	public int getMinimumSpawnHeight(World world)
    {
        return 0;
    }

	@Override
    public double getHorizon(World world)
    {
        return 0;
    }

	@Override
    public double voidFadeMagnitude()
    {
        return 8;
    }

	@Override
    public boolean handleSlimeSpawnReduction(java.util.Random random, World world)
    {
        return true;
    }
}