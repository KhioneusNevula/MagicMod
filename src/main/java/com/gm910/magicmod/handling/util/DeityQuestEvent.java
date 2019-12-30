package com.gm910.magicmod.handling.util;

import com.gm910.magicmod.deity.util.Deity;
import com.gm910.magicmod.deity.util.Deity.Quest;
import com.gm910.magicmod.deity.util.ServerPos;
import com.gm910.magicmod.handling.MagicHandler.Spell;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.Event;

public class DeityQuestEvent extends Event {

	private EntityLivingBase completer;
	private Deity.Quest quest;
	private Deity deity;
	
	public DeityQuestEvent(EntityLivingBase activator, Quest quest, Deity deity) {
		completer = activator;
		this.quest = quest;
		this.deity = deity;
	}
	
	public EntityLivingBase getCompleter() {
		return completer;
	}
	public Deity getDeity() {
		return deity;
	}
	public Deity.Quest getQuest() {
		return quest;
	}
}
