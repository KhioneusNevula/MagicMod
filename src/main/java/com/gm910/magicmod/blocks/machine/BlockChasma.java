package com.gm910.magicmod.blocks.machine;

import net.minecraft.block.Block;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving.SpawnPlacementType;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockChasma extends Block {

	public BlockChasma(String name) {
		super(Material.BARRIER);
	}
	
	@Override
	public boolean hasTileEntity(IBlockState state) {
		// TODO Auto-generated method stub
		return true;
	}
	
	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		// TODO Auto-generated method stub
		return new TileEntityChasma();
	}
	
	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
		// TODO Auto-generated method stub
		return NULL_AABB;
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		// TODO Auto-generated method stub
		return super.getBoundingBox(state, source, pos).shrink(1);
	}
	
	@Override
	public boolean isPassable(IBlockAccess worldIn, BlockPos pos) {
		// TODO Auto-generated method stub
		return true;
	}
	
	@Override
	public boolean canSpawnInBlock() {
		// TODO Auto-generated method stub
		return true;
	}
	
	@Override
	public boolean canCreatureSpawn(IBlockState state, IBlockAccess world, BlockPos pos, SpawnPlacementType type) {
		// TODO Auto-generated method stub
		return true;
	}
	
	@Override
	public boolean canEntitySpawn(IBlockState state, Entity entityIn) {
		// TODO Auto-generated method stub
		return true;
	}
	
	
	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face)
    {
        return BlockFaceShape.UNDEFINED;
    }


    /**
     * Used to determine ambient occlusion and culling when rebuilding chunks for render
    */
	@Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

	@Override
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }
	
	@Override
	public EnumPushReaction getMobilityFlag(IBlockState state)
    {
        return EnumPushReaction.IGNORE;
    }
	
	@SideOnly(Side.CLIENT)
	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		// TODO Auto-generated method stub
		return super.getRenderType(state);
	}
	
	public static class TileEntityChasma extends TileEntity implements ITickable {

		public int ticksLeft=200;
		public NBTTagCompound formerblock = new NBTTagCompound();
		
		public TileEntityChasma() {
			formerblock = new NBTTagCompound();
			formerblock.setInteger("Block", 0);
			formerblock.setInteger("Meta", 0);
		}
		
		@Override
		public void update() {
			if (ticksLeft < 1) {
				this.world.setBlockState(pos, Block.getBlockById(formerblock.getInteger("Block")).getStateFromMeta(formerblock.getInteger("Meta")));
				//System.out.println(Block.REGISTRY.getObject(new ResourceLocation(formerblock.getString("Block"))));
				if (formerblock.hasKey("TE")) {
					this.world.setTileEntity(pos, TileEntity.create(this.world, formerblock.getCompoundTag("TE")));
				}
			} else {
				ticksLeft--;
				//System.out.println(ticksLeft);
			}
			
		}

	}


}
