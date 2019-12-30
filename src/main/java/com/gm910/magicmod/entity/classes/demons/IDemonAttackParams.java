package com.gm910.magicmod.entity.classes.demons;

import net.minecraft.entity.EntityLivingBase;

public interface IDemonAttackParams {

	public boolean shouldAttack(EntityLivingBase attacker, EntityLivingBase owner);
}
