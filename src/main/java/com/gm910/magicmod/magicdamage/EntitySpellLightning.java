package com.gm910.magicmod.magicdamage;

import com.gm910.magicmod.magicdamage.DamageSourceMystic.SpellDamageSource;

import net.minecraft.init.Blocks;
import net.minecraft.world.World;

public class EntitySpellLightning extends EntitySpecialLightning {

	
	public EntitySpellLightning(World worldIn, SpellDamageSource spell, double x, double y, double z,
			boolean effectOnlyIn, boolean actAsLightning) {
		super(worldIn, Blocks.FROSTED_ICE, spell, x, y, z, effectOnlyIn, actAsLightning);
	}

}
