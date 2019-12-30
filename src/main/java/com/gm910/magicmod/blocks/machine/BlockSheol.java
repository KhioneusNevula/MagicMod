package com.gm910.magicmod.blocks.machine;

import java.util.HashMap;
import java.util.Map;

import com.gm910.magicmod.MagicMod;
import com.gm910.magicmod.MagicMod.Priority;
import com.gm910.magicmod.blocks.BlockBase;
import com.gm910.magicmod.init.DimensionInit;
import com.gm910.magicmod.world.Teleport;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.Ticket;
import net.minecraftforge.common.ForgeChunkManager.Type;

public class BlockSheol extends BlockBase {
	
	public BlockSheol(String name) {
		super(name, Material.ROCK);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		// TODO Auto-generated method stub
		return rightClick(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
	}

	public boolean rightClick(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (worldIn.isRemote) return false;
		if (playerIn.dimension != DimensionInit.DIMENSION_SHEOL) {
			if (worldIn.getTileEntity(pos) instanceof TileEntityTeleSheol ? ((TileEntityTeleSheol)worldIn.getTileEntity(pos)).worldSheol != null : false)
				((TileEntityTeleSheol)worldIn.getTileEntity(pos)).worldSheol.setBlockState(playerIn.getPosition().down(), Blocks.STONE.getDefaultState());
				Teleport.teleportToDimension(playerIn, DimensionInit.DIMENSION_SHEOL, playerIn.getPosition().getX(), playerIn.getPosition().getY(), playerIn.getPosition().getZ());
			return true;
		} else {
			Teleport.teleportToDimension(playerIn, playerIn.getSpawnDimension(), playerIn.posX, 100, playerIn.posZ);
		}
		return false;
	}
	
	@Override
	public boolean hasTileEntity(IBlockState state) {
		// TODO Auto-generated method stub
		return true;
	}
	
	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		// TODO Auto-generated method stub
		return new TileEntityTeleSheol();
	}
	
	@Override
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
		if (worldIn.isRemote) return;
		System.out.println("Hi");
		int dim = worldIn.provider.getDimension() == DimensionInit.DIMENSION_SHEOL ? 0 : DimensionInit.DIMENSION_SHEOL;
		World world = worldIn.getMinecraftServer().getWorld(dim);
		if (world.isAirBlock(pos)) {
			world.setBlockState(pos, state);
		} 
		//tickets.put(pos, ForgeChunkManager.requestTicket(MagicMod.instance, worldIn.getMinecraftServer().getWorld(DimensionInit.DIMENSION_SHEOL), ForgeChunkManager.Type.NORMAL));
		super.onBlockAdded(worldIn, pos, state);
	}
	
	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		if (!worldIn.isRemote) {
			worldIn.getMinecraftServer().getWorld(DimensionInit.DIMENSION_SHEOL).setBlockToAir(pos);
		}
		super.breakBlock(worldIn, pos, state);
	}

	public static class TileEntityTeleSheol extends TileEntity implements ITickable {
		public Ticket tick = null;
		public World worldSheol = null;
		public TileEntityTeleSheol() {
			super();
		}
		@Override
		public void update() {
			if (world.isRemote) return;
			if (worldSheol != null) {
				DimensionManager.keepDimensionLoaded(worldSheol.provider.getDimension(), true);
				worldSheol.getChunkFromBlockCoords(pos);
			}
			if (world.getClosestPlayer(pos.getX(), pos.getY(), pos.getZ(), 10, false) != null && world.provider.getDimension() != DimensionInit.DIMENSION_SHEOL) {
				worldSheol = world.getMinecraftServer().getWorld(DimensionInit.DIMENSION_SHEOL);
				DimensionManager.keepDimensionLoaded(worldSheol.provider.getDimension(), true);
				worldSheol.getChunkFromBlockCoords(pos);
				/*Ticket t = MagicMod.requestTicket(Priority.EH, world.getMinecraftServer().getWorld(DimensionInit.DIMENSION_SHEOL), Type.NORMAL);
				if (t != null) {
					System.out.println("sdfdgfhj");
					ForgeChunkManager.forceChunk(t, world.getChunkFromBlockCoords(pos).getPos());
					tick = t;
				} else {
					System.out.println("zxvb");
				}*/
			} else {
				if (worldSheol != null) {
					DimensionManager.keepDimensionLoaded(worldSheol.provider.getDimension(), false);
					worldSheol = null;
					/*if (tick != null) { 
						System.out.println("DSfdgh");
						MagicMod.releaseTicket(tick);
						tick = null;
					}*/
				}
			}
		}
		
		/*
		@Override
		public void update() {
			World worldIn = world;
			if (worldIn.isRemote) return;
			if (worldIn.getClosestPlayer(pos.getX(), pos.getY(), pos.getZ(), 10, false) != null && worldIn.provider.getDimension() != DimensionInit.DIMENSION_SHEOL) {
				if (tick == null) {
					Ticket t = MagicMod.requestTicket(Priority.EH, worldIn.getMinecraftServer().getWorld(DimensionInit.DIMENSION_SHEOL), Type.NORMAL);
					if (t != null) {
						System.out.println("sdfdgfhj");
						ForgeChunkManager.forceChunk(t, worldIn.getChunkFromBlockCoords(pos).getPos());
						tick = t;
					} else {
						System.out.println("zxvb");
					}
				}
			} else {
				if (tick != null) {
					System.out.println("DSfdgh");
					MagicMod.releaseTicket(tick);
					tick = null;
				}
			}
		}*/
		
		
	}
	
}
