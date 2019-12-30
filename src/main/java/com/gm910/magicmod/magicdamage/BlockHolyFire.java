package com.gm910.magicmod.magicdamage;

import com.gm910.magicmod.blocks.BlockBase;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockHolyFire extends BlockBase {

    
	public BlockHolyFire(String name)
    {
		//BlockFire
        super(name, Material.FIRE, false);
    }
	
	@Override
	public boolean isCollidable() {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
		entityIn.attackEntityFrom(DamageSourceMystic.HOLY, 1);
		super.onEntityCollidedWithBlock(worldIn, pos, state, entityIn);
	}
	
}
