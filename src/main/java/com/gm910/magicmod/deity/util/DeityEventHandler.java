package com.gm910.magicmod.deity.util;

import java.util.List;
import java.util.UUID;

import com.gm910.magicmod.MagicMod;
import com.gm910.magicmod.deity.Deities;
import com.gm910.magicmod.deity.util.Deity.Quest;
import com.gm910.magicmod.deity.util.Deity.Status;
import com.gm910.magicmod.init.PotionInit;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.event.entity.player.PlayerDestroyItemEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.NameFormat;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.event.entity.player.PlayerWakeUpEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.ItemPickupEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerRespawnEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ServerTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@EventBusSubscriber
public class DeityEventHandler {
	
	public static int time = 1000;
	
	@SubscribeEvent
	public static void func(LivingUpdateEvent event) {
		
		if (!MagicMod.proxy.deities.deities.isEmpty()) {
			for (Deity d : MagicMod.proxy.deities.deities) {
				/*if (d.getTimeActive() > 0) {
					d.setTimeActive(d.getTimeActive() - 1);
				} else {
					d.dismiss();
					d.setTimeActive(800);
				}*/
				if (d.isDevout(event.getEntityLiving())) {
					//if (event.getEntity().world.getWorldTime() < 3) {
						//d.setWorship(event.getEntityLiving(), 0);
						
					//}
					d.putWorshipSubtraction(event.getEntity().getPersistentID(), d.getWorship(event.getEntityLiving()) / ((24000.0-event.getEntity().world.getWorldTime())+0.1));
					
					if (d.isPriest(event.getEntityLiving().getPersistentID()) && event.getEntityLiving() instanceof EntityLiving) {
						((EntityLiving)event.getEntityLiving()).enablePersistence();
					}
					
					
					d.onTickDevout(event);
					{
						for (Class<? extends Quest> clazz : d.getRequiredQuests()) {
							Quest q = Quest.newQuest(clazz, d);
							if (q != null) {
								if (q.prerequisiteLevels > 0) {
									if (d.getPoints(event.getEntityLiving()) >= q.prerequisiteLevels && !d.isDevoutFinishedWithQuest(event.getEntityLiving().getPersistentID(), clazz) && d.getQuests().getOrDefault(event.getEntityLiving().getPersistentID(), Quest.newQuest(clazz, d)).equals(q)) {
										d.giveQuest(event.getEntityLiving(), q);
									}
								}
							}
						}
						Quest q = d.getQuests().get(event.getEntityLiving().getUniqueID());
						if (q != null) {
							q.checkCompletion(event.getEntityLiving());
						}
					}
					if (d.getWorship(event.getEntityLiving()) > 0) {
						d.changeWorshipBy(event.getEntityLiving(), -(d.getWorshipSubtraction(event.getEntity().getPersistentID())));
					}
				} else if (d.isEnemy(event.getEntityLiving())) {
					d.onTickEnemy(event);
				} else {
					d.onTickNormal(event);
				}
				d.onAnyLivingTick(event);
				for (Quest q : d.getQuests().values()) {
					q.onEvent(event);
					
				}
				
			}
		}
	}
	
	@SubscribeEvent
	public static void func(ServerTickEvent event) {
		if (!MagicMod.proxy.deities.deities.isEmpty()) {
			for (Deity d : MagicMod.proxy.deities.deities) {
				d.onGeneralTick(event);
				for (Quest q : d.getQuests().values()) {
					q.onEvent(event);
				}
				if (time <= 0) {
					//CommonProxy.NETWORK.sendToAll(new CommonProxy.DeityMessage(MagicMod.deities().writeToNBT(new NBTTagCompound())));
					time = 1000;
				} else {
					time--;
				}
			}
		}
	}
	
	@SubscribeEvent
	public static void client(ClientTickEvent event) {
		//System.out.println("sdfghj");
		/*if (time <= 0) {
			CommonProxy.NETWORK.sendToServer(new CommonProxy.DeityMessage(MagicMod.deities().writeToNBT(new NBTTagCompound())));
			for (EntityPlayer player : CommonProxy.getServer().getPlayerList().getPlayers()) {
				System.out.println(player + ": " + MagicMod.deities().LEVIATHAN.isDevout(player));
			}
			time = 1000;
		} else {
			time--;
		}*/
		for (KeyBinding key : MagicMod.keybindings.values()) {
			if (!MagicMod.proxy.deities.deities.isEmpty()) {
				for (Deity d : MagicMod.proxy.deities.deities) {
					if (key.isPressed() && d.canAccessInventory(Minecraft.getMinecraft().player.getUniqueID())) {
						d.onKeyPressed(key, Minecraft.getMinecraft().player);
					}
					
					
				}
			}
		}
		
		World world = FMLClientHandler.instance().getWorldClient();
		EntityPlayer player = Minecraft.getMinecraft().player;
		if (world != null && player != null) {
			for (Deity deity : MagicMod.deities().deities) {
				if (deity.isDevout(Minecraft.getMinecraft().player)) {
					List<EntityLivingBase> entities = world.getEntitiesWithinAABB(EntityLivingBase.class, Minecraft.getMinecraft().player.getEntityBoundingBox().grow(10), en -> {
						
					return deity.isDevout(en) && en != Minecraft.getMinecraft().player;
					});
					if (entities.isEmpty()) continue;
					for (EntityLivingBase en : entities) {
						for (int i = 0; i < 3; i++) {
							double x = world.rand.nextDouble()*2-1;
							double y = world.rand.nextDouble()*2-1;
							double z = world.rand.nextDouble() * 2 -1;
							double xs = world.rand.nextDouble()*2-1;
							double ys = world.rand.nextDouble()*2-1;
							double zs = world.rand.nextDouble()*2-1;
							world.spawnParticle(EnumParticleTypes.CRIT, en.posX+x, en.posY+y, en.posZ+z, xs, ys, zs);
						}
					}
				}
			}
		}
		
	}
	
	@SubscribeEvent
	public static void func(NameFormat event) {
		if (!MagicMod.proxy.deities.deities.isEmpty()) {
			for (Deity d : MagicMod.proxy.deities.deities) {
				if (d.getStatus() == Status.SUPPLANTED) {
					if (d.getSupplanter().equals(event.getEntityPlayer().getUniqueID())) {
						event.setDisplayname(event.getUsername() + ", " + d.getTitle());
					}
				}
				for (Quest q : d.getQuests().values()) {
					q.onEvent(event);
				}
			}
		}
	}
	
	@SubscribeEvent
	public static void func(LivingDeathEvent event) {
		if (!MagicMod.deities().deities.isEmpty()) {
			for (Deity d : MagicMod.deities().deities) {
				if (d.isDevout(event.getEntityLiving())) {
					d.onDeathOfDevout(event);
					d.__on__death__devout(event);
				} else if (d.isEnemy(event.getEntityLiving())) {
					d.onDeathOfEnemy(event);
					d.__on__death__enemy(event);
				} else {
					d.onNormalDeath(event);
				}
				d.onDeath(event);
				if (event.getSource().getTrueSource() instanceof EntityLivingBase) {
					EntityLivingBase player = (EntityLivingBase) event.getSource().getTrueSource();
					if (d.isDevout(player)) {
						d.onDevoutKill(event);
					} else if (d.isEnemy(player)) {
						d.onEnemyKill(event);
					}
				}
				for (Quest q : d.getQuests().values()) {
					q.onEvent(event);
				}
			}
		}
	}
	
	@SubscribeEvent
	public static void func(PlayerInteractEvent.RightClickBlock event) {
		if (!MagicMod.deities().deities.isEmpty()) {
			for (Deity d : MagicMod.deities().deities) {
				if (d.isDevout(event.getEntityLiving())) {
					d.onDevoutInteract(event);
				} else if (d.isEnemy(event.getEntityLiving())) {
					d.onEnemyInteract(event);
				} else {
					d.onNormalInteract(event);
				}
				d.onInteract(event);
				for (Quest q : d.getQuests().values()) {
					q.onEvent(event);
				}
			}
		}
	}
	
	@SubscribeEvent
	public static void func(PlayerInteractEvent.RightClickEmpty event) {
		if (!MagicMod.deities().deities.isEmpty()) {
			for (Deity d : MagicMod.deities().deities) {
				if (d.isDevout(event.getEntityLiving())) {
					d.onDevoutRightClick(event);
				} else if (d.isEnemy(event.getEntityLiving())) {
					d.onEnemyRightClick(event);
				} else {
					d.onNormalRightClick(event);
				}
				d.onRightClick(event);
				for (Quest q : d.getQuests().values()) {
					q.onEvent(event);
				}
			}
		}
	}
	
	@SubscribeEvent
	public static void func(PlayerInteractEvent.RightClickItem event) {
		if (!MagicMod.deities().deities.isEmpty()) {
			for (Deity d : MagicMod.deities().deities) {
				if (d.questItems.containsValue(event.getItemStack().getItem())) {
					if (d.isDevout(event.getEntityLiving())) {
						d.onDevoutUseQuestItem(event, event.getItemStack().getItem());
					} else if (d.isEnemy(event.getEntityLiving())) {
						d.onEnemyUseQuestItem(event, event.getItemStack().getItem());
					} else {
						d.onNormalUseQuestItem(event, event.getItemStack().getItem());
					}
					d.onUseQuestItem(event, event.getItemStack().getItem());
				} else {
					if (d.isDevout(event.getEntityLiving())) {
						d.onDevoutUseItem(event);
					} else if (d.isEnemy(event.getEntityLiving())) {
						d.onEnemyUseItem(event);
					} else {
						d.onNormalUseItem(event);
					}
					d.onUseItem(event);
				}
				for (Quest q : d.getQuests().values()) {
					q.onEvent(event);
				}
			}
			
		}
	}
	
	@SubscribeEvent
	public static void func(PlayerInteractEvent.EntityInteract event) {
		if (!MagicMod.deities().deities.isEmpty()) {
			for (Deity d : MagicMod.deities().deities) {
				if (d.isDevout(event.getEntityLiving())) {
					if (event.getTarget().equals(d.getAvatar())) {
						d.onDevoutInteractDeity(event);
					}
					else if (d.getProjections().contains(event.getTarget())) {
						d.onDevoutInteractDeityProjection(event);
					}
					else {
						d.onDevoutInteractEntity(event);
					}
				} else if (d.isEnemy(event.getEntityLiving())) {
					if (event.getTarget().equals(d.getAvatar())) {
						d.onEnemyInteractDeity(event);
					}
					else {
						d.onEnemyInteractEntity(event);
					}
				} else {
					if (event.getTarget().equals(d.getAvatar())) {
						d.onNormalInteractDeity(event);
					}
					else {
						d.onNormalInteractEntity(event);
					}
				}
				if (event.getTarget().equals(d.getAvatar())) {
					d.onInteractDeity(event);
				}
				else {
					d.onInteractEntity(event);
				}
				
				for (Quest q : d.getQuests().values()) {
					q.onEvent(event);
				}
			}
		}
	}
	
	@SubscribeEvent
	public static void func(BlockEvent.EntityPlaceEvent event) {
		if (!(event.getEntity() instanceof EntityLivingBase)) return;
		EntityLivingBase entity = (EntityLivingBase)event.getEntity();
		if (!MagicMod.deities().deities.isEmpty()) {
			for (Deity d : MagicMod.deities().deities) {
				if (d.isDevout(entity)) {
					d.onDevoutPlaceBlock(event);
				} else if (d.isEnemy(entity)) {
					d.onEnemyPlaceBlock(event);
				} else {
					d.onNonDevoutBlockPlace(event);
				}
				d.onBlockPlaced(event);
				for (Quest q : d.getQuests().values()) {
					q.onEvent(event);
				}
			}
			
		}
	}
	
	@SubscribeEvent
	public static void quests(Event event) {
		if (!MagicMod.deities().deities.isEmpty()) {
			for (Deity d : MagicMod.deities().deities) {
				d.onAnyEvent(event);
				for (Quest q : d.getQuests().values()) {
					q.onEvent(event);
				}
				
			}
			
		}
		
		
	}
	
	@SubscribeEvent
	public static void blessland(LivingEvent event) {
		
		if (!MagicMod.deities().deities.isEmpty()) {
			for (Deity d : MagicMod.deities().deities) {
				EntityLivingBase entity = event.getEntityLiving();
				if (event.getEntityLiving() == null) return;
				if (d.isDevout(entity)) {
					d.onDevoutLivingEvent(event);
				} else if (d.isEnemy(entity)) {
					d.onEnemyLivingEvent(event);
				} else {
					d.onNormalLivingEvent(event);
				}
				d.onLivingEvent(event);
				if (event.getEntityLiving() == null) return;
				if (event.getEntityLiving().getPersistentID() == null) return;
				if (d.isInHolyLand(event.getEntityLiving().getPersistentID())) {
					if (d.getPoints(event.getEntityLiving()) > 0) {
						d.devoutInHolyLand(event.getEntityLiving(), event);
					} else if (d.getPoints(event.getEntityLiving()) < 0) {
						d.enemyInHolyLand(event.getEntityLiving(), event);
					} else {
						d.normalInHolyLand(event.getEntityLiving(), event);
					}
					d.inHolyLand(event.getEntityLiving(), event);
					d.removeDevoutFromHolyLand(event.getEntityLiving());
				}
				if (event.getEntityLiving().getActivePotionEffect(PotionInit.BLESSINGS.get(d)) != null) {
					d.bless(event.getEntityLiving(), event.getEntityLiving().getActivePotionEffect(PotionInit.BLESSINGS.get(d)), event);
				}
				
				if (event.getEntityLiving().getActivePotionEffect(PotionInit.CURSES.get(d)) != null) {
					d.curse(event.getEntityLiving(), event.getEntityLiving().getActivePotionEffect(PotionInit.CURSES.get(d)), event);
				}
				
				if (d.getPoints(event.getEntityLiving()) >= 0) {
					d.abilities.forEach(ability -> {
						if (d.getPoints(event.getEntityLiving()) >= ability.getLevels()) {
							ability.giveAbility(event.getEntityLiving().getPersistentID());
						}
					});
					d.getRequiredQuests().forEach(clazz -> {
						if (d.getPoints(event.getEntityLiving()) >= (Quest.newQuest(clazz, d)).prerequisiteLevels) {
							
							if (d.getQuest(event.getEntityLiving()) == null && !d.isDevoutFinishedWithQuest(event.getEntity().getPersistentID(), clazz)) {
								d.giveQuest(event.getEntityLiving(), clazz);
								
							}
							
						}
					});
				}
				
			}
			
		}
		
		
	}
	
	@SubscribeEvent
	public static void func(BlockEvent.BreakEvent event) {
		EntityLivingBase entity = event.getPlayer();
		if (!MagicMod.deities().deities.isEmpty()) {
			for (Deity d : MagicMod.deities().deities) {
				if (d.isDevout(entity)) {
					d.onDevoutBreakBlock(event);
				} else if (d.isEnemy(entity)) {
					d.onEnemyBreakBlock(event);
				} else {
					d.onNormalBreakBlock(event);
				}
				d.onBreakBlock(event);
				for (Quest q : d.getQuests().values()) {
					q.onEvent(event);
				}
			}
		}
	}
	
	@SubscribeEvent
	public static void func(PlayerDestroyItemEvent event) {
		if (!MagicMod.deities().deities.isEmpty()) {
			for (Deity d : MagicMod.deities().deities) {
				if (d.isDevout(event.getEntityLiving())) {
					d.onDevoutDestroyItem(event);
				} else if (d.isEnemy(event.getEntityLiving())) {
					d.onEnemyDestroyItem(event);
				} else {
					d.onNormalDestroyItem(event);
				}
				d.onDestroyItem(event);
				for (Quest q : d.getQuests().values()) {
					q.onEvent(event);
				}
			}
		}
	}
	
	@SubscribeEvent
	public static void func(LivingDropsEvent event) {
		if (!MagicMod.deities().deities.isEmpty()) {
			for (Deity d : MagicMod.deities().deities) {
				if (d.isDevout(event.getEntityLiving())) {
					d.onDevoutDrop(event);
				} else if (d.isEnemy(event.getEntityLiving())) {
					d.onEnemyDrop(event);
				} else {
					d.onNormalDrop(event);
				}
				d.onDrop(event);
				for (Quest q : d.getQuests().values()) {
					q.onEvent(event);
				}
			}
		}
	}
	
	@SubscribeEvent
	public static void func(LivingAttackEvent event) {
		if (!MagicMod.deities().deities.isEmpty()) {
			for (Deity d : MagicMod.deities().deities) {
				if (d.isDevout(event.getEntityLiving())) {
					d.onDevoutAttacked(event);
				} else if (d.isEnemy(event.getEntityLiving())) {
					d.onEnemyAttacked(event);
				} else {
					d.onNormalAttacked(event);
				}
				d.onAttacked(event);
				if (event.getSource().getTrueSource() instanceof EntityLivingBase) {
					if (d.isDevout((EntityLivingBase)event.getSource().getTrueSource())) {
						d.onDevoutAttack(event);
					} else if (d.isEnemy((EntityLivingBase)event.getSource().getTrueSource())) {
						d.onEnemyAttack(event);
					}
				}
				for (Quest q : d.getQuests().values()) {
					q.onEvent(event);
				}
			}
		}
	}
	
	@SubscribeEvent
	public static void func(PlayerContainerEvent event) {
		Deities deit = MagicMod.proxy.getDeities();
		if (!deit.deities.isEmpty()) {
			for (Deity d : deit.deities) {
				if (d.isDevout(event.getEntityLiving())) {
					d.onDevoutContainer(event);
				} else if (d.isEnemy(event.getEntityLiving())) {
					d.onEnemyContainer(event);
				} else {
					d.onNormalContainer(event);
				}
				d.onContainer(event);
				for (Quest q : d.getQuests().values()) {
					q.onEvent(event);
				}
			}
		}
	}
	
	@SubscribeEvent
	public static void func(PlayerWakeUpEvent event) {
		Deities deities = MagicMod.proxy.getDeities();
		int time = (int) event.getEntityPlayer().getEntityWorld().getWorldTime();
		if (!deities.deities.isEmpty()) {
			for (Deity d : deities.deities) {
				if (d.ticksTillSunrise.getValue() > 0) {

					int sleepTime = d.ticksTillSunrise.getValue();
					int diff = Math.abs(time-sleepTime);
					int worldTime = sleepTime;
					
					for (UUID uuid : d.getFavorPerPlayer().keySet()) {
						if (!d.isDevout(uuid)) continue;
						double $_$ = d.getWorship(uuid);
						
						//for (int i = 0; i < diff; i++) {
							$_$ = -$_$/(diff*((24000-worldTime) + 0.1));
							//worldTime++;
						//}
						d.changeWorshipBy(uuid, $_$);
					}
				}
				if (d.isDevout(event.getEntityLiving())) {
					d.onDevoutWake(event);
				} else if (d.isEnemy(event.getEntityLiving())) {
					d.onEnemyWake(event);
				} else {
					d.onNormalWake(event);
				}
				d.onWake(event);
				for (Quest q : d.getQuests().values()) {
					q.onEvent(event);
				}
			}
		}
	}
	
	@SideOnly(Side.SERVER)
	@SubscribeEvent
	public static void func(PlayerSleepInBedEvent event) {
		if (event.getEntity().world.isRemote) return;
		WorldServer world = (WorldServer) event.getEntityPlayer().world;
		if (!MagicMod.deities().deities.isEmpty()) {
			for (Deity d : MagicMod.deities().deities) {
				if (world.areAllPlayersAsleep()) {
					d.ticksTillSunrise.setValue((int) world.getWorldTime());
					
				}
			}
		}
	}
	
	@SideOnly(Side.SERVER)
	@SubscribeEvent
	public static void func(ItemTossEvent event) {
		if (!MagicMod.deities().deities.isEmpty()) {
			for (Deity d : MagicMod.deities().deities) {
				if (d.isDevout(event.getPlayer())) {
					d.onDevoutToss(event);
				} else if (d.isEnemy(event.getPlayer())) {
					d.onEnemyToss(event);
				} else {
					d.onNormalToss(event);
				}
				d.onToss(event);
				for (Quest q : d.getQuests().values()) {
					q.onEvent(event);
				}
			}
		}
	}
	
	
	@SubscribeEvent
	public static void func(ServerChatEvent event) {
		
		if (!MagicMod.proxy.getDeities().deities.isEmpty()) {
			for (Deity d : MagicMod.proxy.getDeities().deities) {
				if (d.isDevout(event.getPlayer())) {
					d.onDevoutChat(event);
				} else if (d.isEnemy(event.getPlayer())) {
					d.onEnemyChat(event);
				} else {
					d.onNormalChat(event);
				}
				d.onChat(event);
				for (Quest q : d.getQuests().values()) {
					q.onEvent(event);
				}
			}
		}
	}
	
	@SubscribeEvent
	public static void func(ItemCraftedEvent event) {
		if (!MagicMod.proxy.getDeities().deities.isEmpty()) {
			for (Deity d : MagicMod.proxy.getDeities().deities) {
				if (d.isDevout(event.player)) {
					d.onDevoutCraft(event);
				} else if (d.isEnemy(event.player)) {
					d.onEnemyCraft(event);
				} else {
					d.onNormalCraft(event);
				}
				d.onCraft(event);
				for (Quest q : d.getQuests().values()) {
					q.onEvent(event);
				}
			}
		}
	}
	
	@SubscribeEvent
	public static void func(ItemPickupEvent event) {
		if (!MagicMod.proxy.getDeities().deities.isEmpty()) {
			for (Deity d : MagicMod.proxy.getDeities().deities) {
				if (d.isDevout(event.player)) {
					d.onDevoutPickup(event);
				} else if (d.isEnemy(event.player)) {
					d.onEnemyPickup(event);
				} else {
					d.onNormalPickup(event);
				}
				d.onPickup(event);
				for (Quest q : d.getQuests().values()) {
					q.onEvent(event);
				}
			}
		}
	}
	
	@SubscribeEvent
	public static void constructing(EntityConstructing event) {
		if (!MagicMod.proxy.getDeities().deities.isEmpty()) {
			for (Deity d : MagicMod.proxy.getDeities().deities) {
				if (d.getNaturallyDevout().contains(event.getEntity().getClass())) {
					d.changePointsBy((EntityLivingBase)event.getEntity(), 1);
					break;
				}
			}
		}
	}
	
	@SubscribeEvent
	public static void spawn(PlayerRespawnEvent event) {
		if (!MagicMod.proxy.getDeities().deities.isEmpty()) {
			for (Deity d : MagicMod.proxy.getDeities().deities) {
				if (d.deadPlayerFavor.containsKey(event.player.getUniqueID())) {
					d.resurrectPlayer(event.player.getPersistentID());
				}
			}
		}
	}
	
	@SubscribeEvent
	public static void join(EntityJoinWorldEvent event) {
		if (event.getEntity() instanceof EntityPlayer) {
			if (event.getEntity().isEntityAlive()) {
				if (!MagicMod.deities().deities.isEmpty()) {
					for (Deity d : MagicMod.deities().deities) {
						if (d.deadPlayerFavor.containsKey(event.getEntity().getUniqueID())) {
							d.resurrectPlayer(event.getEntity().getPersistentID());
						}
					}
				}
			}
		}
	}
	
	

}
