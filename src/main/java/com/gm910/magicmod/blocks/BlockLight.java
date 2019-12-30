package com.gm910.magicmod.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@EventBusSubscriber
public class BlockLight extends BlockBase {
	
	public BlockLight(String name) {
		super(name, Material.AIR, false);
		setDefaultState(blockState.getBaseState());
        setTickRandomly(false);
        setLightLevel(1.0F);
	}
	
	@Override
	public AxisAlignedBB getSelectedBoundingBox(IBlockState state, World worldIn, BlockPos pos) {
		
		return super.getSelectedBoundingBox(state, worldIn, pos).shrink(0.5);
	}
	
	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
		
		return NULL_AABB;
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state) {
		
		return false;
	}
	
	@Override
	public boolean isFullCube(IBlockState state) {
		
		return false;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer() {
		
		return BlockRenderLayer.CUTOUT;
	}
	
	@Override
	public BlockStateContainer createBlockState() {
		
		return new BlockStateContainer(this);
	}
	
	@Override
	public boolean hasTileEntity(IBlockState state) {
		
		return true;
	}
	
	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		TileEntityLightSource source = new TileEntityLightSource();
		
		return source;
	}
	
	public static class TileEntityLightSource extends TileEntity implements ITickable {

		public Entity attachedEntity;
		public int gracePeriod = 5;
		
		public TileEntityLightSource() {
			gracePeriod = 5;
		}
		
		@Override
		public void update() {
			if (gracePeriod > 0) {
				gracePeriod--;
			} else {
				if (attachedEntity == null) {
					world.setBlockToAir(pos);
					return;
				} else {
					if (this.pos.distanceSq(attachedEntity.getPosition()) > 1) {
						
					}
				}
			}
		}
		
		public void setAttachedEntity(Entity attachedEntity) {
			this.attachedEntity = attachedEntity;
		}
		public Entity getAttachedEntity() {
			return attachedEntity;
		}
		
	}
}
