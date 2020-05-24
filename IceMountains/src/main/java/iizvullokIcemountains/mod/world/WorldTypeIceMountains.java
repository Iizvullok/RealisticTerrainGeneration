package iizvullokIcemountains.mod.world;

import net.minecraft.init.Biomes;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.biome.BiomeProviderSingle;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.*;

public class WorldTypeIceMountains extends WorldType{

	public WorldTypeIceMountains(String name) {
		super("IceMountains");
		
	}

	@Override
	public BiomeProvider getBiomeProvider(World world) {
		return new BiomeProviderIceMountains(world);
	}
	
	@Override
	public IChunkGenerator getChunkGenerator(World world, String generatorOptions) {
		return new ChunkGeneratorIceMountains(world, world.getSeed(), world.getWorldInfo().isMapFeaturesEnabled(), generatorOptions);
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