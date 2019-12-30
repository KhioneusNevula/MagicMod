package com.gm910.magicmod.deity.deities;

import java.util.ArrayList;

import com.gm910.magicmod.deity.entities.EntityDeity;
import com.gm910.magicmod.deity.util.Deity;
import com.gm910.magicmod.deity.util.ServerPos;

import net.minecraft.block.BlockStone;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;

public class DeityVillager extends Deity {

	public DeityVillager(ResourceLocation rl, String displayName, boolean warlike, boolean cvf, ItemStack...required) {
		super(rl, displayName, warlike, cvf, "civilization", required);
		// TODO Auto-generated constructor stub
	}
	
	public DeityVillager(ResourceLocation rl, String displayName, boolean warlike, boolean cvf) {
		this(rl, displayName, warlike, cvf, new ItemStack[0]);
		// TODO Auto-generated constructor stub
	}

	
	
	private boolean checkAltar(RightClickBlock event) {
		if (event.getWorld().isRemote) return false;
		int northsouth = 0;
		if (event.getWorld().getBlockState(event.getPos()).getBlock().equals(Blocks.TORCH) && event.getWorld().getBlockState(event.getPos().down()).getBlock().getRegistryName().getResourcePath().contains("cobblestone")) {
			if (event.getWorld().getBlockState(event.getPos().north().down()).getBlock().equals(Blocks.STONE_STAIRS) != event.getWorld().getBlockState(event.getPos().south().down()).getBlock().equals(Blocks.STONE_STAIRS)) {
				northsouth = 1;
			} else if (event.getWorld().getBlockState(event.getPos().east().down()).getBlock().equals(Blocks.STONE_STAIRS) != event.getWorld().getBlockState(event.getPos().west().down()).getBlock().equals(Blocks.STONE_STAIRS)) {
				northsouth = 2;
			}
		}
		
		if (northsouth == 1) {
			if (event.getWorld().getBlockState(event.getPos().east().down()).getBlock().getRegistryName().getResourcePath().contains("cobblestone") && event.getWorld().getBlockState(event.getPos().west().down()).getBlock().getRegistryName().getResourcePath().contains("cobblestone")) {
				if (event.getWorld().getBlockState(event.getPos().east()).getBlock().equals(Blocks.STONE_STAIRS) && event.getWorld().getBlockState(event.getPos().west()).getBlock().equals(Blocks.STONE_STAIRS)) {
					return true;
				}
			}
		} else if (northsouth == 2){
			if (event.getWorld().getBlockState(event.getPos().north().down()).getBlock().getRegistryName().getResourcePath().contains("cobblestone") && event.getWorld().getBlockState(event.getPos().south().down()).getBlock().getRegistryName().getResourcePath().contains("cobblestone")) {
				if (event.getWorld().getBlockState(event.getPos().north()).getBlock().equals(Blocks.STONE_STAIRS) && event.getWorld().getBlockState(event.getPos().south()).getBlock().equals(Blocks.STONE_STAIRS)) {
					return true;
				}
			}
		}
		return false;
	}
	
	@Override
	public void onNormalInteract(RightClickBlock event) {
		if (checkAltar(event)) {
			this.changePointsBy(event.getEntityPlayer(), 2);
		}
	}

	@Override
	public void initAI(EntityDeity avatar) {
		// TODO Auto-generated method stub
		
	}

}
