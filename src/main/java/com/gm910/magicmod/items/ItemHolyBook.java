package com.gm910.magicmod.items;


import com.gm910.magicmod.deity.util.Deity;
import com.gm910.magicmod.init.PotionInit;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.ClickEvent.Action;
import net.minecraft.world.World;

public class ItemHolyBook extends ItemBase {

	public final Deity deity;
	
	public ItemHolyBook(Deity d) {
		super("holy_book_" + d.getUnlocName().getResourcePath());
		deity = d;
	}
	
	@Override
	public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer playerIn, EntityLivingBase target,
			EnumHand hand) {
			if (!deity.isDevout(target) && deity.isDevout(playerIn)) {
				if (target instanceof EntityPlayer) {
					TextComponentTranslation tc = new TextComponentTranslation("deity.join", deity.getDisplayName().getFormattedText());
					tc.setStyle(tc.getStyle().setClickEvent(new ClickEvent(Action.RUN_COMMAND, "/deity level " + deity.getUnlocName().toString() + " add 1 @p")));
				} else {
					deity.changePointsBy(target, 1);
				}
			}
		return super.itemInteractionForEntity(stack, playerIn, target, hand);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		if (playerIn.isSneaking()) {
			
			if (deity.getPoints(playerIn) == 0) {
				deity.setPoints(playerIn, 1);
				playerIn.sendStatusMessage(new TextComponentTranslation("item.holy_book.devoted", deity.getDisplayName()), true);
				
			} else if (deity.getPoints(playerIn) < 0) {
				playerIn.sendMessage(new TextComponentTranslation("item.holy_book.enemy", deity.getNameToDisplay()));
				playerIn.addPotionEffect(PotionInit.CURSE_BOTTLES.get(deity).getEffects().get(0));
			}
			
			return ActionResult.newResult(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
		}
		return super.onItemRightClick(worldIn, playerIn, handIn);
	}
	
	@Override
	public boolean hasEffect(ItemStack stack) {
		// TODO Auto-generated method stub
		return true;
	}
	
	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		
		return (new TextComponentTranslation("item.holy_book.name", deity.getDisplayName().getFormattedText())).getFormattedText();
	}


}
