package com.gm910.magicmod.items;

import com.gm910.magicmod.blocks.machine.BlockChasma;
import com.gm910.magicmod.blocks.machine.BlockChasma.TileEntityChasma;
import com.gm910.magicmod.init.BlockInit;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemHoler extends ItemBase {

	public ItemHoler(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos oldpos, EnumHand hand,
			EnumFacing facing, float hitX, float hitY, float hitZ) {
		NBTTagCompound com = new NBTTagCompound();
		BlockPos pos = null;
		for (int x = -1; x <= 1; x++) {
			for (int z = -1; z <= 1; z++) {
				for (int y = -1; y <= 1; y++) {
					pos = oldpos.north(z).east(x).up(y);
					if (worldIn.getBlockState(pos).getBlock() instanceof BlockChasma) {
						continue;
					}
					com.setInteger("Block", worldIn.getBlockState(pos).getBlock().getIdFromBlock(worldIn.getBlockState(pos).getBlock()));
					com.setInteger("Meta", worldIn.getBlockState(pos).getBlock().getMetaFromState(worldIn.getBlockState(pos)));
					if (worldIn.getTileEntity(pos) != null) {
						com.setTag("TE", worldIn.getTileEntity(pos).serializeNBT());
					}
					System.out.println(com);
					worldIn.setBlockState(pos, BlockInit.CHASMA.getDefaultState());
					TileEntityChasma te = new TileEntityChasma();
					te.formerblock = com;
					worldIn.setTileEntity(pos, te);
					((TileEntityChasma)worldIn.getTileEntity(pos)).formerblock = com;
				}
			}
		}
		return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
	}
}
