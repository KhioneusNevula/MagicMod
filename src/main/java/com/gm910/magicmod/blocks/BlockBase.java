package com.gm910.magicmod.blocks;

import com.gm910.magicmod.init.BlockInit;
import com.gm910.magicmod.init.ItemInit;
import com.gm910.magicmod.items.ItemStatue;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemBlock;

public class BlockBase extends Block {

	public BlockBase(String name, Material materialIn) {
		super(materialIn);
		setRegistryName(name);
		setUnlocalizedName(name);
		BlockInit.BLOCKS.add(this);
		if (!(this instanceof BlockStatue)) {
			ItemInit.ITEMS.add((new ItemBlock(this)).setRegistryName(this.getRegistryName()));
		}
	}
	
	public BlockBase(String name, Material materialIn, boolean makeItem) {
		super(materialIn);
		setRegistryName(name);
		setUnlocalizedName(name);
		BlockInit.BLOCKS.add(this);
		if (makeItem) {
			ItemInit.ITEMS.add((new ItemBlock(this)).setRegistryName(this.getRegistryName()));
		}
	}
	
}
