package com.gm910.magicmod.items;

import com.gm910.magicmod.blocks.BlockStatue;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

public class ItemStatue extends ItemBlock {
	
	public ItemStatue(BlockStatue statue) {
		super(statue);
		System.out.println(statue.getRegistryName() + " statue item created");
		this.setRegistryName(new ResourceLocation(statue.getRegistryName().getResourceDomain(), statue.getRegistryName().getResourcePath()));
	}

	
	@Override
	public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side,
			float hitX, float hitY, float hitZ, IBlockState newState) {
		return super.placeBlockAt(stack, player, world, pos, side, hitX, hitY, hitZ, newState);
	}
	
	@Override
	public boolean hasEffect(ItemStack stack) {
		// TODO Auto-generated method stub
		return true;
	}

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
    	// TODO Auto-generated method stub
    	return (new TextComponentTranslation("item.statue.name", ((BlockStatue)block).deity.getDisplayName().getFormattedText())).getFormattedText();
    	
    }


}
