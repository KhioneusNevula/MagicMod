package com.gm910.magicmod.items;

import com.gm910.magicmod.blocks.BlockStatue;
import com.gm910.magicmod.deity.util.Deity.Status;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemGodKillerDebug extends ItemBase {

	public ItemGodKillerDebug(String name) {
		super(name);
	}
	
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand,
			EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (worldIn.getBlockState(pos).getBlock() instanceof BlockStatue) {
			((BlockStatue)worldIn.getBlockState(pos).getBlock()).deity.killDeity();
			System.out.println(((BlockStatue)worldIn.getBlockState(pos).getBlock()).deity.getStatus());
			return EnumActionResult.SUCCESS;
		}
		return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
	}

}
