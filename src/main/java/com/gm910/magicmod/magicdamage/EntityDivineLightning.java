package com.gm910.magicmod.magicdamage;

import com.gm910.magicmod.deity.util.Deity;

import net.minecraft.init.Blocks;
import net.minecraft.world.World;

public class EntityDivineLightning extends EntitySpecialLightning {

	public final Deity deity;
	
	public EntityDivineLightning(World worldIn, Deity deity, double x, double y, double z,
			boolean effectOnlyIn, boolean actAsLightning) {
		super(worldIn, deity.getLightningBlock(), DamageSourceMystic.castGodCurse(deity), x, y, z, effectOnlyIn, actAsLightning);
		this.deity = deity;
	}
	
	public Deity getDeity() {
		return deity;
	}

}
