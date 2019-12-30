package com.gm910.magicmod.init;

import com.gm910.magicmod.MagicMod;
import com.gm910.magicmod.command.CommandDeity;
import com.gm910.magicmod.command.CommandDimensionTeleport;

import net.minecraft.command.ICommand;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

public class CommandInit {


	public static void registerCommands(FMLServerStartingEvent event) {
		registerCommand(event, new CommandDimensionTeleport());
		registerCommand(event, new CommandDeity());
	}
	
	public static void registerCommand(FMLServerStartingEvent event, ICommand command) {
		event.registerServerCommand(command);
	}
}
