package com.gm910.magicmod.blocks.te;

import java.util.ArrayList;
import java.util.List;

import com.gm910.magicmod.MagicMod;
import com.gm910.magicmod.blocks.machine.BlockPentaExtend;
import com.gm910.magicmod.blocks.machine.CustomEnergyStorage;
import com.gm910.magicmod.blocks.machine.IMagicEnergy;
import com.gm910.magicmod.entity.classes.demons.EntityDemon;
import com.gm910.magicmod.handling.MagicHandler;
import com.gm910.magicmod.init.BlockInit;
import com.gm910.magicmod.init.DimensionInit;
import com.gm910.magicmod.items.ItemBloodOrb;
import com.gm910.magicmod.magicdamage.EntityDemonLightning;
import com.gm910.magicmod.proxy.CommonProxy;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;

public class TileEntityPentacle extends TileEntity implements ITickable, IMagicEnergy {

	public EntityDemon demon = null;
	private String incantation = "";
	public ItemStack demon_orb = null;
	//public ItemStackHandler stack = new ItemStackHandler(1);
	public EntityItem orb_render = null;
	public static String DISMISS_IMP = "amando";
	public static String SUMMON_IMP = "voco";
	public int cooling = 500;
	
	public CustomEnergyStorage storage = new CustomEnergyStorage(10000);
	
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound data) {
        super.writeToNBT(data);
        if (demon_orb != null) data.setTag("Orb", demon_orb.serializeNBT());
        if (!incantation.isEmpty()) data.setString("Incantation", incantation);
        storage.writeToNBT(data);
		return data;
    }

    @Override
    public void readFromNBT(NBTTagCompound data) {
    	if (data.hasKey("Orb")) {
    		demon_orb = new ItemStack(data.getCompoundTag("Orb"));
    	}
    	if (data.hasKey("Incantation")) {
    		incantation = data.getString("Incantation");
    	}
    	storage.readFromNBT(data);
    	super.readFromNBT(data);

    }

	

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (capability == CapabilityEnergy.ENERGY) return (T)this.storage;
		//if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) return (T)this.handler;
		return super.getCapability(capability, facing);
	}
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		// TODO Auto-generated method stub
		if (capability == CapabilityEnergy.ENERGY) return true;
		//if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) return true;
		return super.hasCapability(capability, facing);
	}


	@Override
	public void update() {
		
		
			if (world.provider.getDimension() == DimensionInit.DIMENSION_SHEOL) {
				for (int x = -1; x <= 1; x++) {
					for (int z = -1; z <= 1; z++) {
						//world.setBlockToAir(new BlockPos(this.getPos().getX() + x, this.getPos().getY(), this.getPos().getZ()+z));
					}
				}
				return;
			}
			
			List<Entity> list = getWorld().getEntitiesWithinAABB(Entity.class, getWorld().getBlockState(getPos()).getSelectedBoundingBox(getWorld(), getPos()).grow(1, 2, 1), e -> e != demon);
			ArrayList<EntityItem> items = new ArrayList<EntityItem>();
			if (!list.isEmpty()) {
				for (Entity e : list) {
					if (e instanceof EntityLivingBase) {
						if (e instanceof EntityDemon) {
							if (demon == null) {
									demon = (EntityDemon)e;
									((EntityDemon)e).addPotionEffect(new PotionEffect(Potion.getPotionFromResourceLocation("slowness"), 2, 255));
									e.setVelocity(0, -10, 0);
									e.setPosition(getPos().getX()+0.5, e.posY, getPos().getZ()+0.5);
									((EntityDemon) e).setInPentacle(true);
							}
						} 
						else {/*
							e.motionX *= -1;
							e.motionX -= ((new Random()).nextBoolean()) ? 0.6 : -0.6;
							e.motionZ *= -1;
							e.motionZ -= ((new Random()).nextBoolean()) ? 0.6 : -0.6;
							e.motionY *= -2;
							e.setFire(4);
							*/
							if (demon != null)
							((EntityLivingBase) e).knockBack(e, 0.5f, 1, 1);
						}
					} else {
						if (e instanceof EntityItem) {
							/*
							if (((EntityItem)e).getItem().getItem() instanceof ItemBloodOrb) {
								if (((EntityItem)e).getItem().hasTagCompound()) {
									if (incantation.equalsIgnoreCase("ego")) {
										this.demon_orb = ((EntityItem) e).getItem();
										getWorld().addWeatherEffect(new EntityLightningBolt(getWorld(), e.posX, e.posY, e.posZ, true));
										e.setDead();
									}
								}
							} else*/ //{
								//System.out.println("Found " + e);
								items.add((EntityItem)e);
							//}
						}
					}
				}
				
				
			}
			
			ArrayList<IInventory> invs = new ArrayList<IInventory>();
			
			
			for (int x = -2; x <= 2; x++) {
				for (int y = -2; y <= 2; y++) {
					for (int z = -2; z <= 2; z++) {
						BlockPos bPos = pos.add(x, y, z);
						if (world.getTileEntity(bPos) == this || world.getBlockState(bPos).getBlock() == BlockInit.PENTA_EXTEND) {
							continue;
						}
						if (world.getTileEntity(bPos) instanceof IInventory) {
							invs.add((IInventory) world.getTileEntity(bPos));
							for (int i = 0; i < 100 /*((IInventory)world.getTileEntity(bPos)).getSizeInventory()*/; i++) {
								/*items.add(new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), ((IInventory)world.getTileEntity(bPos)).getStackInSlot(i)));
								invs.add((IInventory) world.getTileEntity(bPos));
								*/
								
								if (world.isRemote) world.spawnParticle(EnumParticleTypes.ENCHANTMENT_TABLE, 
										bPos.getX() + world.rand.nextDouble()*2-1, 
										bPos.getY() + world.rand.nextDouble()*2-1, 
										bPos.getZ() + world.rand.nextDouble() * 2-1, 
										world.rand.nextDouble()*2-1, 
										world.rand.nextDouble()*2-1, 
										world.rand.nextDouble()*2-1, 
										world.rand.nextInt(255), 
										world.rand.nextInt(255), 
										world.rand.nextInt(255));
							}
							
						}
						
					}
				}
			}
			
			ArrayList<ItemStack> stacks = new ArrayList<ItemStack>();
			items.forEach(d -> stacks.add(d.getItem()));
			invs.forEach(inv -> {
				for (int i = 0; i < inv.getSizeInventory(); i++) {
					if (inv.getStackInSlot(i) != null) {
						stacks.add(inv.getStackInSlot(i));
					}
				}
			});
			
			//System.out.println(incantation);
			incantation = TextFormatting.getTextWithoutFormattingCodes(incantation);
			//System.out.println(incantation);
			if (!incantation.isEmpty() && cooling <= 0) {
				cooling = 500;
				System.out.println(incantation);
				if (demon_orb != null) {
					
					if (demon_orb.hasTagCompound()) {
						EntityLivingBase activator = (EntityLivingBase)CommonProxy.getServer().getEntityFromUuid(demon_orb.getTagCompound().getUniqueId("Entity"));
						if (activator != null) {
								System.out.println("Running " + incantation + " spell with " + stacks.toString());
								if (MagicHandler.Spell.runByIncantation(incantation, this.getPos(), activator, world, items, invs)) {
									System.out.println("sdfdg");
									/*if (world.isRemote)*/ for (int x = 0; x < 100; x++) world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, 
											pos.getX() + world.rand.nextDouble()*2-1, 
											pos.getY() + world.rand.nextDouble()*2-1, 
											pos.getZ() + world.rand.nextDouble() * 2-1, 
											world.rand.nextDouble()*2-1, 
											world.rand.nextDouble()*2-1, 
											world.rand.nextDouble()*2-1, 
											world.rand.nextInt(255), 
											world.rand.nextInt(255), 
											world.rand.nextInt(255));
									
									for (int x = -1; x <= 1; x++) {
										for (int z = -1; z<= 1;z++) {
											if (world.getBlockState(pos).getBlock() == BlockInit.PENTA_EXTEND) {
												world.setBlockState(pos, world.getBlockState(pos).withProperty(BlockPentaExtend.POWER_TIME, 30));
											}
										}
									}
									incantation = "";
									items.clear();
									stacks.clear();
									invs.clear();
									list.clear();
									
								}
								
						}
					}
				} else {
					
					boolean c = false;
					if (!stacks.isEmpty()) {
						for (ItemStack en : stacks) {
							System.out.println(en + "\n" + (incantation.equals(MagicMod.magic().BLOOD_ORB.getIncantation())));
							if (en.getItem() instanceof ItemBloodOrb && incantation.equals(MagicMod.magic().BLOOD_ORB.getIncantation())) {
								System.out.println("Running daemon ego");
								MagicMod.magic().BLOOD_ORB.runSpell(world, this.getPos(), null, stacks);
								//this.setDemon_orb(en.copy());
								
								c = true;
								this.setIncantation("");
							}
						}
					}
					
					if (!c) {
						if (!world.getPlayers(EntityPlayer.class, Player -> Player.getDistanceSq(pos) < 100).isEmpty()) {
							for (EntityPlayer p : world.getPlayers(EntityPlayer.class, Player -> Player.getDistanceSq(pos) < 100)) {
								p.sendMessage(new TextComponentString("No blood orb is present to designate caster of spell"));
							}
						}
					}
				}
				this.setIncantation("");
			} else {
				cooling--;
				//CommonProxy.getServer().getPlayerList().sendMessage(new TextComponentString(incantation));
			}
			
			
			if (demon != null && !demon.canMove()) {
				demon.setVelocity(0, -10, 0);
				demon.setPosition(getPos().getX()+0.5, demon.posY, getPos().getZ()+0.5);
				demon.setInPentacle(true);
				
			}
			/*if (incantation.equalsIgnoreCase("amando")) {
				demonDismiss();
			}
			
			if (incantation.equalsIgnoreCase("destruo")) {
				demonDismiss();
				for (int x = -1; x <= 1; x++) {
					for (int z = -1; z <= 1; z++) {
						world.setBlockToAir(new BlockPos(this.getPos().getX() + x, this.getPos().getY(), this.getPos().getZ()+z));
					}
				}
				return;
			}*/
			
			if (demon != null) {
				if (demon.isDead) {
				demon = null;
				incantation = "";
				}
				for (EntityPlayer p : getWorld().playerEntities) {
					if (p.getRenderBoundingBox().intersects(getWorld().getBlockState(getPos()).getSelectedBoundingBox(getWorld(), getPos()).grow(1, 2, 1))) {
						p.knockBack(p, 0.3f, 2, 2);
					}
				}
			}
			
			
		if (world.isRemote) {
			if (demon_orb != null && (orb_render == null ? true : (orb_render.isDead))) {
				orb_render = new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), demon_orb);
				orb_render.setInfinitePickupDelay();
				System.out.println("sf");
				orb_render.setNoDespawn();
				world.spawnEntity(orb_render);
			}
		}
	}

	
	public void setIncantation(String activated) {
		this.incantation = activated;
	}
	
	public String getIncantation() {
		return incantation;
	}
	
	public ItemStack getDemon_orb() {
		return demon_orb;
	}
	
	public void setDemon_orb(ItemStack demon_orb) {
		this.demon_orb = demon_orb;
	}
	
	public EntityItem getOrb_render() {
		return orb_render;
	}
	public void setOrb_render(EntityItem orb_render) {
		this.orb_render = orb_render;
	}
	
	public void demonDismiss() {
		if (demon != null) {
			getWorld().addWeatherEffect(new EntityDemonLightning(getWorld(), demon, this.getPos().getX(), this.getPos().getY(), this.getPos().getZ(), true, false));
			demon.setDead();
			demon = null;
			EntityItem it = new EntityItem(getWorld(), getPos().getX(), getPos().getY() + 2, getPos().getZ(), demon_orb);
			getWorld().spawnEntity(it);
		}
		
		incantation = "";
		
	}

	@Override
	public int receiveEnergy(int en) {
		
		return storage.receiveEnergy(en, false);
	}

	@Override
	public int extractEnergy(int en) {
		// TODO Auto-generated method stub
		return storage.extractEnergy(en, false);
	}

	@Override
	public int getEnergyStored() {
		// TODO Auto-generated method stub
		return storage.getEnergyStored();
	}

	@Override
	public int getMaxEnergyStored() {
		// TODO Auto-generated method stub
		return storage.getMaxEnergyStored();
	}

}
