package com.gm910.magicmod.deity.util;

import java.util.UUID;

import com.mojang.authlib.GameProfile;

import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayer;

public class FalsePlayer extends FakePlayer {

	public FalsePlayer(WorldServer world) {
		super(world, new GameProfile(UUID.randomUUID(), "PlayerFake1"));
		
	}

}
