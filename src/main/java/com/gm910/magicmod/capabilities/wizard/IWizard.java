package com.gm910.magicmod.capabilities.wizard;

import net.minecraft.entity.player.EntityPlayer;

public interface IWizard {

	public static final double FULL_AMOUNT = 40000;
	public static enum Spell {
		OFENSIA,
		CONJURA_ONIRA,
		SHIELT;
		
		public static Spell fromString(String spell) {
			for (Spell s : values()) {
				if (spell.equalsIgnoreCase(s.toString())) {
					return s;
				}
			}
			return null;
		}
		
	}
	
	public void castSpell(EntityPlayer player, Spell spell, String...parameters);
	public void castSpell(EntityPlayer player);
	public double getMagicPower();
	public void setMagicPower(double power);
	public void decrementMagicPower(double amount);
	public void incrementMagicPower(double amount);
	public void fill();
	public void empty();
	public void setFullAmountTo(double amount);
	public double getFullAmount();
	public Spell getActiveSpell();
	public void setActiveSpell(Spell spell);
	public boolean isWizard();
	public void setIsWizard(boolean isWizard);
	
}
