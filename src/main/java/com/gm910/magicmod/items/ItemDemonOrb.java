package com.gm910.magicmod.items;

import com.gm910.magicmod.entity.classes.demons.EntityDemon;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemDemonOrb extends ItemBase {

	public ItemDemonOrb(String name) {
		super(name);
	}
	
	@Override
	public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) {
		
		return super.onLeftClickEntity(stack, player, entity);
	}
	
	@Override
	public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer playerIn, EntityLivingBase entity,
			EnumHand hand) {
		if (entity instanceof EntityDemon) {
			
			if (stack.getTagCompound() == null) {
				System.out.println("hiii");
				NBTTagCompound tag = (new NBTTagCompound());
				tag.setTag("Demon", entity.serializeNBT());
				playerIn.getHeldItem(hand).setTagCompound(tag);
				entity.setDead();
				
			}
			
			return true;
		}
		return false;
	}
	
	@Override
	public int getEntityLifespan(ItemStack itemStack, World world) {
		// TODO Auto-generated method stub
		return 1000;
	}

	
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand,
			EnumFacing facing, float hitX, float hitY, float hitZ) {
		ItemStack stack = player.getHeldItem(hand);
		if (player.isSneaking()) {
			if (stack.getTagCompound() != null) {
				if (stack.getTagCompound().hasKey("Demon")) {
					Entity d = EntityList.createEntityFromNBT(stack.getTagCompound().getCompoundTag("Demon"), worldIn);
		
					if (d != null) {
						d.setPosition(pos.getX()+hitX, pos.getY()+hitY, pos.getZ()+hitZ);
						worldIn.spawnEntity(d);
						stack.setTagCompound(null);
					}
				}
			} else {
				System.out.println("null");
			}
		}
		return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
	}
	
	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		return super.getItemStackDisplayName(stack);
	}
	
	@Override
	public boolean onEntityItemUpdate(EntityItem entityItem) {
		ItemStack stack = entityItem.getItem();
		entityItem.setEntityInvulnerable(true);
		for (int j = 0; j < 200; ++j)
        {
            double d3 = (double)entityItem.posX + entityItem.world.rand.nextDouble(); //* 0.10000000149011612D;
            double d8 = (double)entityItem.posY + (entityItem.world.rand.nextInt(7) > 2 ? -1 : 1)*entityItem.world.rand.nextDouble() * 500;
            double d13 = (double)entityItem.posZ + entityItem.world.rand.nextDouble();
            entityItem.world.spawnParticle(EnumParticleTypes.CRIT, d3, d8, d13, 0.0D, 0.1D, 0.0D);
        }
		return super.onEntityItemUpdate(entityItem);
	}
	
	@Override
	public boolean onDroppedByPlayer(ItemStack item, EntityPlayer player) {
		
		return super.onDroppedByPlayer(item, player);
	}
	
	
}
