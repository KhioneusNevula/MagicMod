package com.gm910.magicmod.handling;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import com.gm910.magicmod.blocks.te.TileEntityPentacle;
import com.gm910.magicmod.capabilities.ghosts.GhostProvider;
import com.gm910.magicmod.capabilities.ghosts.IGhost;
import com.gm910.magicmod.capabilities.souls.ISouls;
import com.gm910.magicmod.capabilities.souls.SoulProvider;
import com.gm910.magicmod.capabilities.souls.Souls;
import com.gm910.magicmod.capabilities.villagereligion.IVillageReligion;
import com.gm910.magicmod.capabilities.villagereligion.VReligionProvider;
import com.gm910.magicmod.deity.Deities;
import com.gm910.magicmod.deity.capability.DeityDataProvider;
import com.gm910.magicmod.deity.capability.IDeityData;
import com.gm910.magicmod.deity.util.Deity;
import com.gm910.magicmod.entity.classes.demons.EntityAzraelite;
import com.gm910.magicmod.entity.classes.demons.EntityDemon;
import com.gm910.magicmod.entity.classes.demons.EntityImp;
import com.gm910.magicmod.handling.MagicHandler.Spell;
import com.gm910.magicmod.init.BlockInit;
import com.gm910.magicmod.init.DimensionInit;
import com.gm910.magicmod.items.ItemDemonOrb;
import com.gm910.magicmod.magicdamage.EntityDemonLightning;
import com.gm910.magicmod.world.Teleport;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAITasks.EntityAITaskEntry;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.village.Village;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerChangedDimensionEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

@EventBusSubscriber
public class EventHandler {
	
	public static Map<UUID, UUID> azraelism = new HashMap<UUID, UUID>();
	public static Map<UUID, ArrayList<NBTTagCompound>> shouldSpawnDead = new HashMap<UUID, ArrayList<NBTTagCompound>>();
	public static Random rand = new Random();
	
	@SubscribeEvent
	public static void chat(ServerChatEvent event) {
		//System.out.println(event.getPlayer().getCapability(WizardProvider.WIZ_CAP, null).isWizard() + " :: ");
		/*if (event.getPlayer().getCapability(WizardProvider.WIZ_CAP, null).isWizard()) {
			if (Spell.fromString(event.getMessage()) != null) {
				System.out.println(Spell.fromString(event.getMessage()) + "USEDDDDD");
				event.setComponent(new TextComponentString(TextFormatting.GRAY + event.getMessage().toUpperCase()));
				event.getPlayer().getCapability(WizardProvider.WIZ_CAP, null).castSpell(event.getPlayer(), Spell.fromString(event.getMessage()));
			}
		}*/
		EntityPlayer player = event.getPlayer();
		if (event.getMessage().substring(0, 7).equalsIgnoreCase("cobalum")) {
			if (event.getMessage().substring(8).equalsIgnoreCase(TileEntityPentacle.DISMISS_IMP)) {
				List<EntityImp> lis = event.getPlayer().getEntityWorld().getEntities(EntityImp.class, n -> n.getOwner() == ((EntityLivingBase)event.getPlayer()));
				for (EntityImp im : lis) {
					TileEntityPentacle te = (TileEntityPentacle)FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(im.getWorld().provider.getDimension()).getTileEntity(im.getBoundOrigin());
					if (te != null) {
						te.demonDismiss();
					}
					im.setDead();
				}
				
			
			} else if (event.getMessage().substring(8).equalsIgnoreCase(TileEntityPentacle.SUMMON_IMP)) {
				List<EntityImp> lis = event.getPlayer().getEntityWorld().getEntities(EntityImp.class, n -> n.getOwner() == ((EntityLivingBase)event.getPlayer()));
				for (EntityImp im : lis) {
					im.setPosition(event.getPlayer().posX + event.getPlayer().getLookVec().x, event.getPlayer().posY + event.getPlayer().getLookVec().y, event.getPlayer().posZ + event.getPlayer().getLookVec().z);
				}
			}
		} else {
			
			World world = event.getPlayer().getEntityWorld();
			RayTraceResult plac = rayTrace(world, player, true);
			
			
			if (plac.typeOfHit == RayTraceResult.Type.BLOCK) {
				System.out.println("Hit: "+ plac.typeOfHit);
				if (!world.isRemote) {
					BlockPos pos = plac.getBlockPos();//.offset(player.getHorizontalFacing());
					System.out.println(pos);
					if (world.getBlockState(pos).getBlock().equals(BlockInit.PENTACLE) || 
							world.getBlockState(pos).getBlock().equals(BlockInit.PENTA_EXTEND)) {
						

						if (FMLClientHandler.instance().getWorldClient().provider.getDimension() == player.dimension) {
							for (int i = 0; i < 20; i++) {
								FMLClientHandler.instance().getWorldClient().spawnParticle(EnumParticleTypes.LAVA, false, pos.getX()+0.5, pos.getY()+0.5, pos.getZ()+0.5, player.getRNG().nextDouble()-1, player.getRNG().nextDouble()-1, player.getRNG().nextDouble()-1);
							}
							
						}
						
						if (world.getBlockState(pos).getBlock().equals(BlockInit.PENTA_EXTEND)) {
							for (int x = -1; x <= 1; x++)
									for (int z = -1; z <= 1; z++) {
										BlockPos pospen = pos.add(x, 0, z);
										if (world.getBlockState(pospen).getBlock().equals(BlockInit.PENTACLE)) {
											
											pos = pospen;
											break;
										}
									}
						}
						System.out.println("Found Pentacle: " + event.getMessage().toLowerCase());
						((TileEntityPentacle)world.getTileEntity(pos)).setIncantation(event.getMessage().toLowerCase());
						for (Spell spell : MagicHandler.Spell.spells) {
							if (spell.incantation.equalsIgnoreCase(event.getMessage())) {
								
								event.setComponent(new TextComponentString(spell.getFormatted()));
							}
						}
					} else {
						System.out.println("Not penta/penta_extend");
					}
				} else {
					System.out.println("Remote World");
				}
			} else {
				System.out.println("T: "+ plac.typeOfHit);
			}
			
			
		}
	}
	
	public static RayTraceResult rayTrace(World worldIn, EntityPlayer playerIn, boolean useLiquids)
    {
        float f = playerIn.rotationPitch;
        float f1 = playerIn.rotationYaw;
        double d0 = playerIn.posX;
        double d1 = playerIn.posY + (double)playerIn.getEyeHeight();
        double d2 = playerIn.posZ;
        Vec3d vec3d = new Vec3d(d0, d1, d2);
        float f2 = MathHelper.cos(-f1 * 0.017453292F - (float)Math.PI);
        float f3 = MathHelper.sin(-f1 * 0.017453292F - (float)Math.PI);
        float f4 = -MathHelper.cos(-f * 0.017453292F);
        float f5 = MathHelper.sin(-f * 0.017453292F);
        float f6 = f3 * f4;
        float f7 = f2 * f4;
        double d3 = playerIn.getEntityAttribute(EntityPlayer.REACH_DISTANCE).getAttributeValue();
        Vec3d vec3d1 = vec3d.addVector((double)f6 * d3, (double)f5 * d3, (double)f7 * d3);
        return worldIn.rayTraceBlocks(vec3d, vec3d1, useLiquids, !useLiquids, true);
    }
	
	@SubscribeEvent
	public static void update(PlayerTickEvent event) {
		
		if (!event.player.getEntityWorld().isRemote) {
			deadSouls(event);
		}
	}
	
	public static void deadSouls(PlayerTickEvent event) {
		if (event.player.hasCapability(SoulProvider.SOUL_CAP, null)) {
			ISouls souls = event.player.getCapability(SoulProvider.SOUL_CAP, null);
			if (shouldSpawnDead.get(event.player.getUniqueID()) != null && event.player.dimension == DimensionInit.DIMENSION_SHEOL) {
				if (shouldSpawnDead.get(event.player.getUniqueID()).size() > 0) {
					ArrayList<NBTTagCompound> list = shouldSpawnDead.get(event.player.getUniqueID());
					EntityLiving e = (EntityLiving)EntityList.createEntityFromNBT(list.get(0), event.player.world);
					
					if (e != null) {
						e.setHealth(e.getMaxHealth());
						e.setGlowing(true);
						e.getCapability(GhostProvider.GHOST_CAP, null).setGhost(true).setEntity(e).setKiller(event.player.getPersistentID());
						/*e.setRevengeTarget(event.player);
						e.getEntityData().setBoolean("IsSoul", true);
						e.getEntityData().setUniqueId("PlayerKiller", event.player.getUniqueID());*/
						e.setPosition(event.player.posX + (rand.nextBoolean() ? -1 : 1)*rand.nextInt(5), event.player.posY + rand.nextInt(5), event.player.posZ + (rand.nextBoolean() ? -1 : 1)*rand.nextInt(5));
						e.tasks.addTask(0, new IGhost.AIGhostAttack(e, 0.4f));
						
						event.player.world.spawnEntity(e);
					}
					list.remove(0);
				} else {
					shouldSpawnDead.remove(event.player.getUniqueID());
				}
			} else {
				shouldSpawnDead.remove(event.player.getUniqueID());
			}
		}
	}

	@SubscribeEvent
	public static void update(LivingUpdateEvent event) {
		if (event.getEntity().world.isRemote) return;
		if (event.getEntity() instanceof EntityPlayer || event.getEntity() instanceof EntityDemon) return;
		if (event.getEntity() instanceof EntityVillager) {
			List<Village> villages = event.getEntity().world.getVillageCollection().getVillageList();
			for (Village village : villages) {
				IVillageReligion religion = village.getCapability(VReligionProvider.REL_CAP, null);
				if (religion.getVillage() == null) {
					religion.setVillage(village);
				}
				
				
				
				if (religion.getDimension() == -1) {
					Field f = null;
					try {
						f = Village.class.getDeclaredField("world");
					} catch (NoSuchFieldException e1) {
		
						e1.printStackTrace();
					} catch (SecurityException e1) {
		
						e1.printStackTrace();
					}
					if (f != null) {
						f.setAccessible(true);
						try {
							World world = ((World)f.get(village));
							religion.setDimension(world.provider.getDimension());
						} catch (IllegalArgumentException e) {
								
							e.printStackTrace();
						} catch (IllegalAccessException e) {
								
							e.printStackTrace();
						}
					}
				}
				
				if (religion.hasBeenVisited()) continue;
				
				if (religion.getPriest(event.getEntity().world.getMinecraftServer()) == null 
						? true 
						: religion.getPriest(event.getEntity().world.getMinecraftServer()).isDead) 
				{
					
					//System.out.println("Priestless village");
					Deity d = Deities.deities.get(event.getEntityLiving().getRNG().nextInt(Deities.deities.size()));
					while (!d.canVillageFollow) {
						d = Deities.deities.get(event.getEntityLiving().getRNG().nextInt(Deities.deities.size()));
					}
					List<EntityLivingBase> villagers = event.getEntity().world.<EntityLivingBase>getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB((double)(village.getCenter().getX() - village.getVillageRadius()), (double)(village.getCenter().getY() - 4), (double)(village.getCenter().getZ() - village.getVillageRadius()), (double)(village.getCenter().getX() + village.getVillageRadius()), (double)(village.getCenter().getY() + 4), (double)(village.getCenter().getZ() + village.getVillageRadius())));
					if (!villagers.isEmpty()) {
						boolean hasPriest = false;
						BlockPos pos = null;
						for (EntityLivingBase entity : villagers) {
							
							pos = entity.getPosition();
							if (entity instanceof EntityVillager) {
								EntityVillager villager = (EntityVillager)entity;
								if (villager.getProfession() == 2) {
									//System.out.println("Found cleric");
									hasPriest = true;
									boolean shouldReligion = true;
									religion.setPriest(villager);
									for (Deity deit : Deities.deities) {
										if (deit.isDevout(villager)) {
											shouldReligion = false;
											if (religion.getReligion() == null) {
												religion.setReligion(deit);
											}
											boolean add = true;
											for (EntityAITaskEntry entr : villager.tasks.taskEntries) {
												if (entr.action instanceof Deity.AIWorshipDeity) {
													add = false;
												}
											}
											
											if (add) {villager.tasks.addTask(0, new Deity.AIWorshipDeity(villager, deit));}
											break;
										}
									}
									if (shouldReligion) {
										
										
										boolean add = true;
										for (EntityAITaskEntry entr : villager.tasks.taskEntries) {
											if (entr.action instanceof Deity.AIWorshipDeity) {
												add = false;
											}
										}

										if (religion.getReligion() == null) {
											religion.setReligion(d);
											d.setPoints(villager, 1);
											if (add) {
												System.out.println("Added worship deity ai");
												villager.tasks.addTask(0, new Deity.AIWorshipDeity(villager, d));}
										} else {
											religion.getReligion().setPoints(villager, 1);
											if (add) {
												System.out.println("Added worship deity ai");
												villager.tasks.addTask(0, new Deity.AIWorshipDeity(villager, religion.getReligion()));}
										}
									}
								}
							}
							if (religion.getReligion() != null) {
								if (!religion.getReligion().isDevout(entity)) {
									religion.getReligion().setPoints(entity, 1);
								}
							}
						}
						if (!hasPriest) {
							EntityVillager vill = new EntityVillager(event.getEntity().world);
							vill.setPositionAndUpdate(pos.getX(), pos.getY(), pos.getZ());
							vill.setProfession(2);
							d.setPoints(vill, 1);
							religion.setPriest(vill);
							religion.setReligion(d);
							System.out.println("Added worship deity ai");
							vill.tasks.addTask(0, new Deity.AIWorshipDeity(vill, d));
							event.getEntity().world.spawnEntity(vill);
							
						}
					}
				} else {
					List<EntityLivingBase> villagers = event.getEntity().world.<EntityLivingBase>getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB((double)(village.getCenter().getX() - village.getVillageRadius()), (double)(village.getCenter().getY() - 4), (double)(village.getCenter().getZ() - village.getVillageRadius()), (double)(village.getCenter().getX() + village.getVillageRadius()), (double)(village.getCenter().getY() + 4), (double)(village.getCenter().getZ() + village.getVillageRadius())));
					for (EntityLivingBase b : villagers) {
						if (!(b instanceof IMob || b instanceof EntityPlayer) && !(religion.getReligion().isDevout(b))) {
							religion.getReligion().setPoints(b, 1);
						}
					}
				}
				religion.setHasBeenVisited(true);
			} //end of village block
		}
		
		if (!event.getEntityLiving().getEntityData().getUniqueId("Azraelite").equals(new UUID(0, 0))) {
			//System.out.println(((EntityLiving)event.getEntityLiving()).getUniqueID() + ":" + event.getEntityLiving().getEntityData().getUniqueId("Azraelite"));
			azraelism.put(((EntityLiving)event.getEntityLiving()).getUniqueID(), event.getEntityLiving().getEntityData().getUniqueId("Azraelite"));
		}
		/*if (event.getEntityLiving().getEntityData().getBoolean("IsSoul")) {
			EntityPlayer play = event.getEntity().world.getMinecraftServer().getPlayerList().getPlayerByUUID(event.getEntityLiving().getEntityData().getUniqueId("PlayerKiller"));
			if (play != null) {
				event.getEntityLiving().setRevengeTarget(play);
				
				if (event.getEntityLiving() instanceof EntityLiving) {
					((EntityLiving)event.getEntityLiving()).setAttackTarget(play);
				}
				if (event.getEntityLiving().getDistanceSq(play) > 100) {
					event.getEntityLiving().setPosition(play.posX + (rand.nextBoolean() ? 1 : -1)*rand.nextInt(6), play.posY + rand.nextInt(6), play.posZ + (rand.nextBoolean() ? 1 : -1)*rand.nextInt(6));
				}
			}
		}*/
		if (event.getEntity().dimension == DimensionInit.DIMENSION_SHEOL) {
			if (event.getEntity().world.rand.nextInt(100) < 4) {
				event.getEntity().world.spawnEntity(new EntityTNTPrimed(event.getEntity().world, event.getEntity().posX, 300, event.getEntity().posZ, null));
			}
		}
		//System.out.println(event.getEntityLiving().getEntityData() != null ? event.getEntityLiving().getEntityData().getUniqueId("Azraelite") : "hh");
	}

	@SubscribeEvent
	public static void load(WorldEvent.Load event) {
		if (event.getWorld().isRemote) return;
		World ov = event.getWorld().getMinecraftServer().getWorld(0);
		IDeityData dat = ov.getCapability(DeityDataProvider.DEITY_CAP, null);
		for (Deity deity : Deities.deities) {
			deity.server = event.getWorld().getMinecraftServer();
		}
		if (!dat.afterWorldFirstLoad()) {
			Deities.resetDeities();
			dat.setWorldFirstLoad(true);
		}
	}
	
	@SubscribeEvent
	public static void load(WorldEvent.Unload event) {
		if (event.getWorld().isRemote) return;
		World ov = event.getWorld().getMinecraftServer().getWorld(0);
		IDeityData dat = ov.getCapability(DeityDataProvider.DEITY_CAP, null);
		//dat.saveDeityData();
	}
	
	@SubscribeEvent
	//@SideOnly(Side.SERVER)
	public static void drops(LivingDropsEvent event) {
		if (event.getEntity().world.isRemote) return;
		System.out.println("livingDrop bnfs");
		/*if (event.getEntity().getEntityData().getBoolean("IsSoul")) {
			System.out.println("isSoulisSoul");
			event.getDrops().clear();
			
			event.getDrops().add(new EntityItem(event.getEntity().world, event.getEntity().posX, event.getEntity().posY, event.getEntity().posZ, ItemSoul.setSoul(event.getEntityLiving(), new ItemStack(ItemInit.SOUL, 1))));
			//event.setCanceled(true);
		}*/
		if (event.getEntity() instanceof EntityLiving) {
			System.out.println("Alive");
			EntityLiving entity = (EntityLiving)event.getEntity();
			if (azraelism.containsKey(entity.getUniqueID())) {
					System.out.println("ofAzraelite " + azraelism.get(entity.getUniqueID()));
					EntityAzraelite attacker = (EntityAzraelite)(entity.getEntityWorld().getMinecraftServer().getEntityFromUuid(azraelism.get(entity.getUniqueID())));
					if (attacker == null) {
						System.out.println("What the hell is going on");
						return;
					}
					for (EntityItem it : event.getDrops()) {
						if (it == null) {
							System.out.println("It = null");
							continue;
						}
						if (it.getItem() == null) {
							System.out.println("It.getItem = null");
							continue;
						}
						attacker.inventory.addItem(it.getItem());
						it.setDead();
					}
				azraelism.clear();
			}
		}
	}
	
	@SubscribeEvent
	//@SideOnly(Side.SERVER)
	public static void entity(ItemTossEvent event) {
		if (event.getEntity().world.isRemote) return;
		System.out.println("itemToss" + event.getEntityItem().getItem().getTagCompound() != null);
		if (event.getEntityItem().getItem().getItem() instanceof ItemDemonOrb && event.getEntityItem().getItem().hasTagCompound()) {
			if (event.getEntityItem().getItem().getTagCompound().hasKey("Demon")) {
				Entity e = EntityList.createEntityFromNBT(event.getEntityItem().getItem().getTagCompound().getCompoundTag("Demon"), event.getEntityItem().world);
				if (e instanceof EntityDemon) {
					e.setPosition(event.getEntityItem().posX, event.getEntityItem().posY, event.getEntityItem().posZ);
					e.setInvisible(true);
					((EntityDemon) e).addPotionEffect(new PotionEffect(Potion.getPotionFromResourceLocation("invisibility"), 10000));
					((EntityDemon)e).setItem(event.getEntityItem());
					event.getEntityItem().world.spawnEntity(e);
					e.setInvisible(true);
					((EntityDemon) e).addPotionEffect(new PotionEffect(Potion.getPotionFromResourceLocation("invisibility"), 10000));
				}
			}
		}
	}
	
	@SubscribeEvent
	public static void death(LivingDeathEvent event) {
		if (event.getEntity().world.isRemote) return;
		EntityLivingBase entity = event.getEntityLiving();
		System.out.println("livingdeath: " + event.getSource().getTrueSource());
		if ((event.getSource().getTrueSource() instanceof EntityPlayer) && event.getEntity().dimension != DimensionInit.DIMENSION_SHEOL && event.getEntity() instanceof EntityLiving) {
			System.out.println("playerkill");
			EntityPlayer player = (EntityPlayer)event.getSource().getTrueSource();
			if (player.hasCapability(SoulProvider.SOUL_CAP, null)) {
				ISouls souls = player.getCapability(SoulProvider.SOUL_CAP, null);
				ArrayList<UUID> uuids = new ArrayList<UUID>();
				if (!souls.getSouls((WorldServer)player.world).isEmpty()) {
					for (EntityLivingBase tag : souls.getSouls((WorldServer)player.world)) {
						uuids.add(tag.getUniqueID());
					}
				}
				if (!uuids.contains(entity.getUniqueID())) {
					System.out.println(entity);
					entity.setHealth(entity.getMaxHealth());
					entity.curePotionEffects(new ItemStack(Items.MILK_BUCKET, 1));
					souls.addSoul(entity);
					if (souls.getTagList().size() > Souls.MAX_SOULS) {
						souls.getTagList().remove(0);
					}
					entity.setHealth(0);
					
				}
			}
		}
		/*if (entity.getEntityData().getBoolean("IsSoul")) {
			EntityPlayer play = entity.world.getMinecraftServer().getPlayerList().getPlayerByUUID(entity.getEntityData().getUniqueId("PlayerKiller"));
			if (play != null) {
				ISouls souls = play.getCapability(SoulProvider.SOUL_CAP, null);
				souls.removeSoul((WorldServer)entity.world, entity);
			}
		}*/
		
		
			EntityLivingBase entityBase = event.getEntityLiving();
			for (WorldServer w : entityBase.world.getMinecraftServer().worlds) {
				List<EntityDemon> demons = w.getEntities(EntityDemon.class, dem -> (dem.getSummoner() != null ? dem.getSummoner().isEntityEqual(entityBase) : false));
				demons.forEach(demon -> {
					demon.spawnExplosionParticle();
					demon.playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, 5, 1);
					demon.world.addWeatherEffect(new EntityDemonLightning(demon.world, demon, demon.posX, demon.posY, demon.posZ, true, false));
					demon.setDead();
				});
				
			}
		
	}
	
	@SubscribeEvent
	public static void dimension(PlayerChangedDimensionEvent event) {
		if (event.player.world.isRemote) {
			if (event.fromDim == DimensionInit.DIMENSION_SHEOL) {
				for (int j = 0; j < 1000; ++j)
		        {
		            double d3 = (double)event.player.getPosition().getX() + event.player.world.rand.nextDouble(); //* 0.10000000149011612D;
		            double d8 = (double)event.player.getPosition().getY() + (event.player.world.rand.nextInt(7) > 2 ? -1 : 1)*event.player.world.rand.nextDouble() * 500;
		            double d13 = (double)event.player.getPosition().getZ() + event.player.world.rand.nextDouble();
		            event.player.world.spawnParticle(EnumParticleTypes.LAVA, d3, d8, d13, 0.0D, 0.1D, 0.0D);
		        }
			}
			return;
		}
		System.out.println("Dimension change to" + event.toDim + " from " + event.fromDim + ": " + event.player);
		if (event.toDim == DimensionInit.DIMENSION_SHEOL) {
			if (event.player.hasCapability(SoulProvider.SOUL_CAP, null)) {
				ISouls souls = event.player.getCapability(SoulProvider.SOUL_CAP, null);
				if (!souls.getTagList().isEmpty()) {
					shouldSpawnDead.put(event.player.getUniqueID(), souls.getTagList());
				}
			}
		} else if (event.fromDim == DimensionInit.DIMENSION_SHEOL) {
			if (event.player.hasCapability(SoulProvider.SOUL_CAP, null)) {
				ISouls souls = event.player.getCapability(SoulProvider.SOUL_CAP, null);
				if (!souls.getTagList().isEmpty()) {
					for (EntityLivingBase b : souls.getSouls(((WorldServer)event.player.world))) {
						b.setDead();
					}
				}
			}
			
		}
	}
	
	@SubscribeEvent
	public static void thingHurt(LivingHurtEvent event) {
		if (event.getEntity().world.isRemote) return;
		EntityLivingBase entity = event.getEntityLiving();
		World world = entity.world;
		if (event.getSource().equals(DamageSource.OUT_OF_WORLD) && !entity.isDead) {
			if (entity.dimension == 0) {
				Teleport.teleportToDimension(entity, DimensionInit.DIMENSION_SHEOL, entity.posX, 300, entity.posZ);
			} else if (entity.dimension == DimensionInit.DIMENSION_SHEOL) {
				Teleport.teleportToDimension(entity, 0, entity.posX, 300, entity.posZ);
			}
		}
	}
	
	@SubscribeEvent
	public static void construct(EntityConstructing event) {
		Entity en = event.getEntity();
		
	}

	/*@SubscribeEvent
	public static void render(RenderLivingEvent<EntityLiving> event) {
	    
	}*/
	
}
