package com.gm910.magicmod.magicdamage;

import com.gm910.magicmod.entity.classes.demons.EntityDemon;

import net.minecraft.init.Blocks;
import net.minecraft.world.World;

public class EntityDemonLightning extends EntitySpecialLightning {

	public final EntityDemon demon;
	
	public EntityDemonLightning(World worldIn, EntityDemon demon, double x, double y, double z,
			boolean effectOnlyIn, boolean actAsLightning) {
		super(worldIn, Blocks.FIRE, DamageSourceMystic.causeDemonDamage(demon), x, y, z, effectOnlyIn, actAsLightning);
		this.demon = demon;
	}
	
	public EntityDemon getDemon() {
		return demon;
	}

}
