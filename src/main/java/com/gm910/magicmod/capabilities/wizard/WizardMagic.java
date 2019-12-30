package com.gm910.magicmod.capabilities.wizard;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class WizardMagic implements IWizard {

	private double fullAmount = FULL_AMOUNT;
	private double magicPower = FULL_AMOUNT;
	private Spell activeSpell = Spell.OFENSIA;
	private boolean isWizard = false;

	@Override
	public void castSpell(EntityPlayer player, Spell spell, String... parameters) {
		
	}

	@Override
	public double getMagicPower() {
		// TODO Auto-generated method stub
		return magicPower;
	}

	@Override
	public void setMagicPower(double power) {
		if (power > fullAmount) {
			magicPower = fullAmount;
		} else if (power < 0) {
			magicPower = 0;
		}
		else magicPower = power;
	}

	@Override
	public void decrementMagicPower(double amount) {
		setMagicPower(getMagicPower() - amount);
	}

	@Override
	public void incrementMagicPower(double amount) {
		setMagicPower(getMagicPower() + amount);
	}

	@Override
	public void fill() {
		magicPower = fullAmount;
	}

	@Override
	public void empty() {
		magicPower = 0;
	}

	@Override
	public void setFullAmountTo(double amount) {
		fullAmount = amount;
	}

	@Override
	public void castSpell(EntityPlayer player) {
		Spell spell = activeSpell;
		if (spell.equals(Spell.CONJURA_ONIRA)) {
			player.addItemStackToInventory(new ItemStack(Items.APPLE, 1));
		}
		
	}

	@Override
	public double getFullAmount() {
		// TODO Auto-generated method stub
		return fullAmount;
	}

	@Override
	public Spell getActiveSpell() {
		// TODO Auto-generated method stub
		return activeSpell;
	}

	@Override
	public void setActiveSpell(Spell spell) {
		activeSpell = spell;
	}

	@Override
	public boolean isWizard() {
		return isWizard;
	}

	@Override
	public void setIsWizard(boolean isWizard) {
		if (isWizard) System.out.println("Someone has turned into a wizard"); else System.out.println("Someone has turned into a nonmagical");
		this.isWizard = isWizard;
	}

}
