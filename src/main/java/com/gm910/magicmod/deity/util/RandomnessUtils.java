package com.gm910.magicmod.deity.util;

import java.util.Random;

import com.gm910.magicmod.proxy.CommonProxy;

import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class RandomnessUtils {

	public static void spawnParticles(EnumParticleTypes type, ServerPos pos, int iterations) {
		World world = CommonProxy.getServer().getWorld(pos.getD());
		spawnParticles(type, world, pos.getPos(), iterations);
	}
	
	public static void spawnParticles(EnumParticleTypes type, World world, BlockPos pos, int iterations) {
		for (int i = 0; i < iterations; i++) {
			double x = world.rand.nextDouble()*2-1;
			double y = world.rand.nextDouble()*2-1;
			double z = world.rand.nextDouble() * 2 -1;
			double xs = world.rand.nextDouble()*2-1;
			double ys = world.rand.nextDouble()*2-1;
			double zs = world.rand.nextDouble()*2-1;
			world.spawnParticle(type, pos.getX()+x, pos.getY()+y, pos.getZ()+z, xs, ys, zs);
		}
	}
	
	public static int nextInt(int bound) {
		return (new Random()).nextInt(bound);
	}
	
	public static boolean nextBoolean() {
		return (new Random()).nextBoolean();
	}
	
	public static double nextDouble(int scale, int subtract) {
		return ((new Random()).nextDouble()) * scale - subtract;
	}
	
	public static double nextDouble() {
		return nextDouble(1, 0);
	}
}
