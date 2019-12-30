package com.gm910.magicmod.entity.classes.demons;

import net.minecraft.entity.EntityLivingBase;

public interface IDemonWithOwner {
	public EntityLivingBase getOwner();
	public void setOwner(EntityLivingBase owner);
}
