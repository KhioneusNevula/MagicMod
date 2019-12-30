package com.gm910.magicmod.blocks;

import java.util.Random;

import com.gm910.magicmod.entity.classes.demons.EntityDemon;
import com.gm910.magicmod.magicdamage.DamageSourceMystic;

import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockHolyTile extends BlockBase {

	public BlockHolyTile(String name) {
		super(name, Material.ROCK);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void onEntityWalk(World worldIn, BlockPos pos, Entity entityIn) {
		if (entityIn instanceof EntityDemon) {
			if ((new Random()).nextInt(5) < 3) {
				entityIn.attackEntityFrom(DamageSourceMystic.HOLY, 3);
			}
		}
		super.onEntityWalk(worldIn, pos, entityIn);
	}

}
