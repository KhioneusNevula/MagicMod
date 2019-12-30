package com.gm910.magicmod.init;


import java.util.ArrayList;

import com.gm910.magicmod.MagicMod;
import com.gm910.magicmod.world.biome.BiomeSheol;
import com.gm910.magicmod.world.biome.BiomeTyphon;

import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.common.BiomeManager.BiomeEntry;
import net.minecraftforge.common.BiomeManager.BiomeType;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class BiomeInit {
	
	public static Biome SHEOL = new BiomeSheol();
	public static Biome TYPHON = new BiomeTyphon();
	public static final ArrayList<Biome> BIOMES = new ArrayList<Biome>();
	
	public static void registerBiomes() {
		SHEOL = initBiome(SHEOL, "Sheol", BiomeType.DESERT, 0, Type.MAGICAL);
		TYPHON = initBiome(TYPHON, "Typhon", BiomeType.COOL, 0, Type.WATER, Type.WET, Type.OCEAN, Type.MAGICAL);
	}
	
	public static Biome initBiome(Biome biome, String name, BiomeType biomeType, int weight, Type...type) {
		biome.setRegistryName(MagicMod.MODID, name);
		ForgeRegistries.BIOMES.register(biome);
		System.out.println("BIOME " + name + " REGISTERRRRRRRRRRRRRRRED");
		BiomeDictionary.addTypes(biome, type);
		BiomeManager.addBiome(biomeType, new BiomeEntry(biome, weight));
		BiomeManager.addSpawnBiome(biome);
		System.out.println("BIOME ADDDDDDDDDDDDDDDDDDDDDDDED");
		BIOMES.add(biome);
		return biome;
	}
}
