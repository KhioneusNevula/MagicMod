package com.gm910.magicmod.init;

import com.gm910.magicmod.blocks.BlockBase;
import com.gm910.magicmod.interfaces.IMetaName;

import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;

public class BlockOres extends BlockBase implements IMetaName {

	private String name;
	private String dimension;
	
	public BlockOres(String name, String dimension) {
		super(name, Material.ROCK);
		
		this.name = name;
		this.dimension = dimension;
	}

	@Override
	public String getSpecialName(ItemStack stack) {
		// TODO Auto-generated method stub
		return null;
	}

}
