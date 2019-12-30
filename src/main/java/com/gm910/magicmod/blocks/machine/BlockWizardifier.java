package com.gm910.magicmod.blocks.machine;

import java.util.List;
import java.util.Random;

import com.gm910.magicmod.blocks.BlockBase;
import com.gm910.magicmod.capabilities.wizard.IWizard;
import com.gm910.magicmod.capabilities.wizard.WizardProvider;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockWizardifier extends BlockBase {

	
	public BlockWizardifier(String name, Material materialIn) {
		super(name, materialIn);
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		System.out.println("Wizardifier");
		playerIn.getCapability(WizardProvider.WIZ_CAP, null).setIsWizard(true);
		if (worldIn.isRemote) return false;
			List<Entity> list = worldIn.getEntitiesWithinAABB(EntityLiving.class, this.getBoundingBox(worldIn.getBlockState(pos), worldIn, pos).grow(1, 2, 1));
			if (list.isEmpty()) return false;
			for (Entity e : list) {
				if (e.hasCapability(WizardProvider.WIZ_CAP, null)) {
					IWizard wiz = e.getCapability(WizardProvider.WIZ_CAP, null);
					wiz.setIsWizard(true);
					
				}
			}
		return true;
	}
	
	
}
