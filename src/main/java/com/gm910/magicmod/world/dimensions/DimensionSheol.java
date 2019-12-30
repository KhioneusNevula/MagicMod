package com.gm910.magicmod.world.dimensions;

import com.gm910.magicmod.init.BiomeInit;
import com.gm910.magicmod.init.DimensionInit;
import com.gm910.magicmod.world.chunkgen.ChunkGeneratorInfernal2;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.biome.BiomeProviderSingle;
import net.minecraft.world.gen.ChunkGeneratorOverworld;
import net.minecraft.world.gen.IChunkGenerator;

public class DimensionSheol extends WorldProvider {

	public DimensionSheol() {
		this.biomeProvider = new BiomeProviderSingle(BiomeInit.SHEOL);
	}
	
	@Override
	public DimensionType getDimensionType() {
		// TODO Auto-generated method stub
		return DimensionInit.SHEOL;
	}
	
	@Override
	public BiomeProvider getBiomeProvider() {
		// TODO Auto-generated method stub
		return new BiomeProviderSingle(BiomeInit.SHEOL);
	}
	
	@Override
	public String getSaveFolder() {
		// TODO Auto-generated method stub
		return "SHEOL";
	}
	
	@Override
	public Biome getBiomeForCoords(BlockPos pos) {
		// TODO Auto-generated method stub
		return BiomeInit.SHEOL;//super.getBiomeForCoords(pos);
	}
	
	@Override
	public IChunkGenerator createChunkGenerator() {
		// TODO Auto-generated method stub
		return new ChunkGeneratorInfernal2(world, world.getSeed(), true, world.getMinecraftServer().getWorld(0).getWorldInfo().getGeneratorOptions());
	}
	
	@Override
	public boolean canRespawnHere() {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean isSurfaceWorld() {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public Vec3d getFogColor(float p_76562_1_, float p_76562_2_) {
		// TODO Auto-generated method stub
		return new Vec3d(0.2, 0, 0);
	}
	
	

}
