package com.gm910.magicmod.items;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemBloodOrb extends ItemBase {

	public ItemBloodOrb(String name) {
		super(name);
		
	}
	
	@Override
	public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) {
		
		return super.onLeftClickEntity(stack, player, entity);
	}
	
	@Override
	public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer playerIn, EntityLivingBase entity,
			EnumHand hand) {
		if (stack.hasTagCompound()) {
			return false;
		}
		if (entity instanceof EntityLivingBase) {
			NBTTagCompound nbt = new NBTTagCompound();
			nbt.setUniqueId("Entity", entity.getPersistentID());
			stack.setTagCompound(nbt);
		}
		return true;
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		ItemStack stack = playerIn.getHeldItem(handIn);
		if (playerIn.isSneaking()) {
			if (stack.hasTagCompound()) return new ActionResult<ItemStack>(EnumActionResult.PASS, playerIn.getHeldItem(handIn));
			NBTTagCompound nbt = new NBTTagCompound();
			nbt.setUniqueId("Entity", playerIn.getUniqueID());
			stack.setTagCompound(nbt);
			return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
		}
		return super.onItemRightClick(worldIn, playerIn, handIn);
	}

}
