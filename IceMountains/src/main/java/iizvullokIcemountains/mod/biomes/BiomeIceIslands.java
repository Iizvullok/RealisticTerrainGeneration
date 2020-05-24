package iizvullokIcemountains.mod.biomes;

import iizvullokIcemountains.mod.init.BlockInit;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.SpawnListEntry;
import net.minecraft.world.gen.feature.WorldGenMinable;

public class BiomeIceIslands extends Biome {

	public BiomeIceIslands() {
		super(new BiomeProperties("Ice Islands").setBaseHeight(5.0f).setHeightVariation(2f).setTemperature(-3f).setSnowEnabled().setRainfall(5));
		this.decorator.treesPerChunk = 5;
	}
}