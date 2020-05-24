package iizvullokIcemountains.mod.init;

import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.common.BiomeManager.BiomeEntry;
import net.minecraftforge.common.BiomeManager.BiomeType;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import iizvullokIcemountains.mod.biomes.BiomeIceIslands;
import iizvullokIcemountains.mod.biomes.BiomeIizIceMountains;
import net.minecraft.world.biome.Biome;

public class BiomeInit {
	public static final Biome ICE_ISLANDS = new BiomeIceIslands();
	public static final Biome ICE_MOUNTAINS = new BiomeIizIceMountains();
	
	public static void registerBiomes() {
		initBiome(ICE_ISLANDS, "Ice Islands", BiomeType.ICY, Type.SNOWY, Type.COLD, Type.MOUNTAIN);
		initBiome(ICE_MOUNTAINS, "Ice Mountians", BiomeType.COOL, Type.COLD, Type.MOUNTAIN);
	}
	
	private static Biome initBiome(Biome biome, String name, BiomeType biomeType, Type... types) {
		biome.setRegistryName(name);
		ForgeRegistries.BIOMES.register(biome);
		BiomeDictionary.addTypes(biome, types);
		BiomeManager.addBiome(biomeType, new BiomeEntry(biome, 0));
		BiomeManager.addSpawnBiome(biome);
		return biome;
	}
}
