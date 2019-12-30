package com.gm910.magicmod.handling.util;

import com.gm910.magicmod.deity.util.ServerPos;
import com.gm910.magicmod.handling.MagicHandler.Spell;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class MagicEvent extends Event {

	private EntityLivingBase activator;
	private ServerPos castPos;
	private World world;
	private Spell spell;
	
	public MagicEvent(EntityLivingBase activator, BlockPos castPos, Spell spell) {
		this(activator, new ServerPos(castPos, activator.dimension), activator.world, spell);
	}
	
	public MagicEvent(EntityLivingBase activator, ServerPos castPos, World world, Spell spell) {
		this.activator = activator;
		this.castPos = castPos;
		this.world = world;
		this.spell = spell;
	}
	
	public EntityLivingBase getActivator() {
		return activator;
	}
	
	public ServerPos getCastPos() {
		return castPos;
	}
	
	public World getWorld() {
		return world;
	}
	
	public Spell getSpell() {
		return spell;
	}
	
	public void setActivator(EntityLivingBase activator) {
		this.activator = activator;
	}
	
	public void setCastPos(ServerPos castPos) {
		this.castPos = castPos;
	}
	
	public void setWorld(World world) {
		this.world = world;
	}
	
	public void setSpell(Spell spell) {
		this.spell = spell;
	}
	
}
