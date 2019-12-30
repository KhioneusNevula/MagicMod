package com.gm910.magicmod.handling;

import java.util.Random;

import com.gm910.magicmod.capabilities.villagereligion.IVillageReligion;
import com.gm910.magicmod.capabilities.villagereligion.VReligionProvider;
import com.gm910.magicmod.deity.Deities;
import com.gm910.magicmod.deity.util.Deity;
import com.gm910.magicmod.init.ItemInit;

import net.minecraft.entity.IMerchant;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.passive.EntityVillager.ITradeList;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.village.Village;

public class ClericTradeAdder implements ITradeList {

	@Override
	public void addMerchantRecipe(IMerchant merchant, MerchantRecipeList recipeList, Random random) {
		if (merchant instanceof EntityVillager) {
			EntityVillager villager = (EntityVillager) merchant;
			Deity deity = Deities.SEAR;
			if (villager.world.getVillageCollection() != null) {
				if (villager.world.getVillageCollection().getNearestVillage(villager.getPosition(), 1000) != null) {
					Village village = villager.world.getVillageCollection().getNearestVillage(villager.getPosition(), 1000);
					IVillageReligion religion = village.getCapability(VReligionProvider.REL_CAP, null);
					if (religion.getReligion() != null) {
						deity = religion.getReligion();
					}
				}
			}
			recipeList.add(new MerchantRecipe(new ItemStack(Items.EMERALD, 2), new ItemStack(Items.PAPER, 1), new ItemStack(ItemInit.ITEM_HOLY_BOOKS.get(deity), 1)));
		}
	}

}
