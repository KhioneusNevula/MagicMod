package com.gm910.magicmod.entity.classes.demons;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IDemonBoundPos {

	public void setBoundOrigin(BlockPos origin);
	public BlockPos getBoundOrigin();
	public World getWorld();
	public void setBoundWorld(World world);
}
