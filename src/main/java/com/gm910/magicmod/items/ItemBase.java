package com.gm910.magicmod.items;

import com.gm910.magicmod.MagicMod;
import com.gm910.magicmod.init.ItemInit;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class ItemBase extends Item {
	
	private boolean hasEffect;

	public ItemBase(String name) {
		setUnlocalizedName(name);
		setRegistryName(new ResourceLocation(MagicMod.MODID, name));
		ItemInit.ITEMS.add(this);
		setCreativeTab(CreativeTabs.MISC);
		hasEffect = false;
	}
	
	public ItemBase setHasEffect(boolean hasEffect) {
		this.hasEffect = hasEffect;
		return this;
	}
	
	@Override
	public boolean hasEffect(ItemStack stack) {
		// TODO Auto-generated method stub
		return hasEffect;
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		return super.onItemRightClick(worldIn, playerIn, handIn);
	}

}
