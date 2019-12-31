package com.gm910.magicmod.deity.entities;

import com.gm910.magicmod.MagicMod;
import com.gm910.magicmod.deity.Deities;
import com.gm910.magicmod.deity.util.Deity;
import com.gm910.magicmod.deity.util.ServerPos;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class EntityDeityProjection extends EntityLiving {

	private Deity deity;
	private EntityLivingBase summoner;
	private BlockPos bound;
	private int gracePeriod;
	
	public EntityDeityProjection(World worldIn, Deity deity) {
		this(worldIn);
		this.deity = deity;
		this.summoner = worldIn.getClosestPlayer(posX, posY, posZ, 1000, false);
		System.out.println(deity);
	}
	
	
	public EntityDeityProjection(World w) {
		super(w);
		setSize(1, 1);
		gracePeriod = 0;
	}
	
	
	public BlockPos getBound() {
		return bound;
	}
	public void setBound(BlockPos bound) {
		this.bound = bound;
	}
	
	public EntityLivingBase getSummoner() {
		return summoner;
	}
	
	public void setSummoner(EntityLivingBase summoner) {
		this.summoner = summoner;
	}
	
	@Override
	public boolean isInvisibleToPlayer(EntityPlayer player) {
		if (this.deity != null) {
			if (this.deity.isDevout(player)) {
				return false;
			}
		}
		return true;
	}
	
	@Override
	public void onLivingUpdate() {
		if (gracePeriod >= 8) {
			if (this.deity == null) {
				System.out.println("nodeity");
				this.isDead = true;
			}
			if (this.summoner == null) {
				System.out.println("sumnull");
				this.isDead = true; 
			}
			else {
				if (summoner.isDead) {
					System.out.println("sumdead");
					this.isDead = true;
				}
				if (this.dimension != this.summoner.dimension || this.getDistanceSq(summoner) > 625) {
					System.out.println("sumfar");
					this.isDead = true;
				}
			}
			
			if (bound != null) {
				this.setPosition(bound.getX(), bound.getY(), bound.getZ());
			}
			
			if (this.isDead && this.deity != null) {
				this.deity.dismissProjection(this);
			}
			//Minecraft.getMinecraft().setRenderViewEntity(this);
	
			super.onLivingUpdate();
		} else {
			gracePeriod++;
		}
	}
	
	@Override
	public boolean isEntityInvulnerable(DamageSource source) {
		// TODO Auto-generated method stub
		return true;
	}
	
	@Override
	public void setDead() {
		if (deity != null) {
			deity.dismissProjection(this);
		}
		//Minecraft.getMinecraft().setRenderViewEntity(Minecraft.getMinecraft().player);
		super.setDead();
	}
	
	@Override
	public void onDeath(DamageSource cause) {
		
		super.onDeath(cause);
		if (deity != null) {
			deity.dismissProjection(this);
		}
	}

	@Override
	public boolean hasNoGravity() {
		// TODO Auto-generated method stub
		return true;
	}
	@Override
	public boolean isNoDespawnRequired() {
		// TODO Auto-generated method stub
		return true;
	}
	
	public void setDeity(Deity deity) {
		this.deity = deity;

	}
	
	public Deity getDeity() {
		return deity;
	}
	
	@Override
	public NBTTagCompound serializeNBT() {
		NBTTagCompound nbt = super.serializeNBT();
		if (deity != null) {
			nbt.setString("Deity", this.deity.toString());
		}
		if (summoner != null) { 
			nbt.setUniqueId("Summoner", this.summoner.getUniqueID());
		}
		
		if (bound != null) { 
			nbt.setTag("Bound", ServerPos.BPtoNBT(bound));
		}
		
		return super.serializeNBT();
	}
	
	@Override
	public boolean hitByEntity(Entity entityIn) {
		if (entityIn.isEntityEqual(summoner)) {
			if (this.deity != null) {
				this.deity.dismissProjection(this);
			}
		}

		return super.hitByEntity(entityIn);
	}
	
	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		if (nbt.hasKey("Deity")) {
			this.deity = MagicMod.deities().fromString(nbt.getString("Deity"));
		}
		
		if (nbt.hasKey("Summoner")) {
			this.summoner = (EntityLivingBase)FMLCommonHandler.instance().getMinecraftServerInstance().getEntityFromUuid(nbt.getUniqueId("Summoner"));
		}
		
		if (nbt.hasKey("Bound")) {
			this.bound = ServerPos.BPfromNBT(nbt.getCompoundTag("Bound"));
		}
		
		super.deserializeNBT(nbt);
	}
	
	@Override
	protected void initEntityAI() {
	}

}
