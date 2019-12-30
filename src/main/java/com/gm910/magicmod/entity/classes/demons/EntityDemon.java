package com.gm910.magicmod.entity.classes.demons;


import javax.annotation.Nullable;

import com.gm910.magicmod.blocks.machine.BlockPentacle;
import com.gm910.magicmod.magicdamage.DamageSourceMystic;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntitySpellcasterIllager;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public abstract class EntityDemon extends EntitySpellcasterIllager {

	protected boolean inPentacle;
	protected boolean canMove = false;
	protected boolean hasOwner = false;
	protected EntityItem item = null;
	protected boolean hasItem = false;
	protected EntityLivingBase summoner = null;
	
	public EntityDemon(World worldIn) {
		super(worldIn);
		isImmuneToFire = true;
		
		enablePersistence();
		
	}
	
	public EntityLivingBase getSummoner() {
		return summoner;
	}
	public void setSummoner(EntityLivingBase summoner) {
		this.summoner = summoner;
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float amount) {
		if (source.isFireDamage() || source.isExplosion() || DamageSourceMystic.isDemonic(source)) {
			return false;
		} 
		else if (DamageSourceMystic.isHoly(source)) {
			return super.attackEntityFrom(source, 10+amount);
		}
		return super.attackEntityFrom(source, amount);
	}
	
	
	@Override
	public boolean isImmuneToExplosions() {
		// TODO Auto-generated method stub
		return true;
	}
	
	public boolean canMove() {
		return canMove;
	}
	
	protected void setCanMove(boolean canMove) {
		this.canMove = canMove;
	}
	
	public EntityItem getItem() {
		return item;
	}
	
	public void setItem(EntityItem item) {
		this.item = item;
		if (item != null) hasItem = true;
	}
	
	
	
	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		
		super.writeEntityToNBT(compound);
		
	}
	
	@Override
	public void setAttackTarget(EntityLivingBase entitylivingbaseIn) {
		super.setAttackTarget(entitylivingbaseIn);
	}
	
	@Override
	public EntityLivingBase getAttackTarget() {
		
		return super.getAttackTarget();
	}
	
	
	/*@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	protected void initEntityAI() {
		super.initEntityAI();
		this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityDemon.AICastingSpell());
        this.tasks.addTask(2, new EntityAIAvoidEntity(this, EntityVillager.class, 4.0F, 0.6D, 1.0D));
        this.tasks.addTask(2, new EntityAIAvoidEntity(this, EntityIronGolem.class, 4.0F, 0.6D, 1.0D));
        this.tasks.addTask(2, new EntityAIAvoidEntity(this, EntityDemon.class, 2.0F, 0.6D, 1.0D));
        this.tasks.addTask(2, new EntityAIAvoidEntity(this, EntityPlayer.class, 4.0F, 0.6D, 1.0D));
        this.tasks.addTask(4, new EntityDemon.AIForgeSpell());
        this.tasks.addTask(5, new EntityDemon.AIAttackSpell());
        this.tasks.addTask(6, new EntityDemon.AISummonSpell());
        this.tasks.addTask(8, new EntityAIWander(this, 0.6D));
        this.tasks.addTask(9, new EntityAIWatchClosest(this, EntityPlayer.class, 3.0F, 1.0F));
        this.tasks.addTask(10, new EntityAIWatchClosest(this, EntityLiving.class, 8.0F));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true, new Class[0]));
        this.targetTasks.addTask(2, (new EntityAINearestAttackableTarget(this, EntityPlayer.class, true)).setUnseenMemoryTicks(300));
        this.targetTasks.addTask(3, (new EntityAINearestAttackableTarget(this, EntityVillager.class, false)).setUnseenMemoryTicks(300));
        this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityIronGolem.class, false));
        this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityDemon.class, false));
	}*/
	@Override
	protected abstract void initEntityAI();
	
	public void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(1.0D);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(12.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(34.0D);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(10.0f);
        
    }
	
	
	@Override
	protected SoundEvent getSpellSound() {
		// TODO Auto-generated method stub
		return SoundEvents.ENTITY_GENERIC_EXPLODE;
	}
	
	
	
	@Override
	public float getEyeHeight() {
		// TODO Auto-generated method stub
		return 1.62f;
	}
	
	public ResourceLocation getLootTable()
    {
        return LootTableList.ENTITIES_GHAST;
    }
	
	public void updateAITasks()
    {
        super.updateAITasks();
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        super.onUpdate();
        if (isInWater()) {
        	if (this.rand.nextInt(10) < 2) {
        		this.damageEntity(DamageSourceMystic.HOLY, 1);
        	}
        }
        if (item != null) {
        	this.setPositionAndRotation(item.posX, item.posY, item.posZ, item.rotationYaw, item.rotationPitch);
        	this.setEntityInvulnerable(true);
        	this.setInvisible(true);
        } else {
        	this.setEntityInvulnerable(false);
        	this.setInvisible(false);
        }
    }
    
    public void setInPentacle(boolean inPentacle) {
		this.inPentacle = inPentacle;
	}
    
    public boolean isInPentacle() {
		return inPentacle;
	}
    
    @Override
    public void onLivingUpdate() {
    	// TODO Auto-generated method stub
    	super.onLivingUpdate();
    	if (item != null) {
    		inPentacle = true;
    		this.setInvisible(true);
    		this.addPotionEffect(new PotionEffect(Potion.getPotionFromResourceLocation("invisibility"), 100));
    		if (item.isDead) this.setDead();
    	} else {
    		if (hasItem == true)
    			this.setDead();
    		inPentacle = false;
    	}
    	if (this.getEntityWorld() != null && this.getPosition() != null)
			if (this.getEntityWorld().getBlockState(this.getPosition()) != null)
				if (this.getEntityWorld().getBlockState(this.getPosition()).getBlock() != null)
					if (!(this.getEntityWorld().getBlockState(this.getPosition()).getBlock() instanceof BlockPentacle)) {
						if (item == null)
						inPentacle = false;
			        }
    	if (inPentacle) {
    		this.setEntityInvulnerable(true);
    	} else {
    		this.setEntityInvulnerable(false);
    	}
    	
    }
    
    public SoundEvent getAmbientSound()
    {
        return SoundEvents.BLOCK_FIRE_AMBIENT;
    }

    public SoundEvent getDeathSound()
    {
        return SoundEvents.ENTITY_LIGHTNING_THUNDER;
    }

    public SoundEvent getHurtSound(DamageSource damageSourceIn)
    {
        return SoundEvents.ENTITY_POLAR_BEAR_WARNING;
    }
    
    private EntityItem forgeTarget;
	
    private void setForgeTarget(@Nullable EntityItem entityItem)
    {
        this.forgeTarget = entityItem;
    }

    @Nullable
    private EntityItem getForgeTarget()
    {
        return this.forgeTarget;
    }

    protected class AIAttackSpell extends EntitySpellcasterIllager.AIUseSpell
    {
        AIAttackSpell()
        {
            super();
        }

        public int getCastingTime()
        {
            return 40;
        }

        @Override
        public int getCastingInterval()
        {
            return 100;
        }

        public void castSpell()
        {
            EntityLivingBase entitylivingbase = EntityDemon.this.getAttackTarget();
            double d0 = entitylivingbase.posX - EntityDemon.this.posX;
            double d1 = entitylivingbase.getEntityBoundingBox().minY + (double)(entitylivingbase.height / 3.0F) - EntityDemon.this.posY;
            double d2 = entitylivingbase.posZ - EntityDemon.this.posZ;
            double d3 = (double)MathHelper.sqrt(d0 * d0 + d2 * d2);
            EntityLivingBase owner = EntityDemon.this;
            if (EntityDemon.this instanceof IDemonWithOwner) {
            	if (((IDemonWithOwner)EntityDemon.this).getOwner() != null) {
            		owner = ((IDemonWithOwner)EntityDemon.this).getOwner();
            	}
            }
            EntityLargeFireball fireball = new EntityLargeFireball(world, owner, d0, d1 + d3 * 0.20000000298023224D, d2);
            fireball.setPosition(EntityDemon.this.posX, EntityDemon.this.posY, EntityDemon.this.posZ);
            fireball.explosionPower = 0;
            EntityDemon.this.world.spawnEntity(fireball);
            /*
            double d0 = Math.min(entitylivingbase.posY, EntityDemon.this.posY);
            double d1 = Math.max(entitylivingbase.posY, EntityDemon.this.posY) + 1.0D;
            float f = (float)MathHelper.atan2(entitylivingbase.posZ - EntityDemon.this.posZ, entitylivingbase.posX - EntityDemon.this.posX);

            if (EntityDemon.this.getDistanceSq(entitylivingbase) < 9.0D)
            {
                for (int i = 0; i < 5; ++i)
                {
                    float f1 = f + (float)i * (float)Math.PI * 0.4F;
                    this.spawnFangs(EntityDemon.this.posX + (double)MathHelper.cos(f1) * 1.5D, EntityDemon.this.posZ + (double)MathHelper.sin(f1) * 1.5D, d0, d1, f1, 0);
                }

                for (int k = 0; k < 8; ++k)
                {
                    float f2 = f + (float)k * (float)Math.PI * 2.0F / 8.0F + ((float)Math.PI * 2F / 5F);
                    this.spawnFangs(EntityDemon.this.posX + (double)MathHelper.cos(f2) * 2.5D, EntityDemon.this.posZ + (double)MathHelper.sin(f2) * 2.5D, d0, d1, f2, 3);
                }
            }
            else
            {
                for (int l = 0; l < 16; ++l)
                {
                    double d2 = 1.25D * (double)(l + 1);
                    int j = 1 * l;
                    this.spawnFangs(EntityDemon.this.posX + (double)MathHelper.cos(f) * d2, EntityDemon.this.posZ + (double)MathHelper.sin(f) * d2, d0, d1, f, j);
                }
            }*/
        }

		@Override
		public SoundEvent getSpellPrepareSound() {
			// TODO Auto-generated method stub
			return SoundEvents.BLOCK_PORTAL_AMBIENT;
		}

		@Override
		public boolean shouldExecute() {
			if (inPentacle) return false;
			return super.shouldExecute();
		}
		
		@Override
		public SpellType getSpellType() {
			// TODO Auto-generated method stub
			return SpellType.FANGS;
		}
/*
        private void spawnFangs(double p_190876_1_, double p_190876_3_, double p_190876_5_, double p_190876_7_, float p_190876_9_, int p_190876_10_)
        {
            BlockPos blockpos = new BlockPos(p_190876_1_, p_190876_7_, p_190876_3_);
            boolean flag = false;
            double d0 = 0.0D;

            while (true)
            {
                if (!EntityDemon.this.world.isBlockNormalCube(blockpos, true) && EntityDemon.this.world.isBlockNormalCube(blockpos.down(), true))
                {
                    if (!EntityDemon.this.world.isAirBlock(blockpos))
                    {
                        IBlockState iblockstate = EntityDemon.this.world.getBlockState(blockpos);
                        AxisAlignedBB axisalignedbb = iblockstate.getCollisionBoundingBox(EntityDemon.this.world, blockpos);

                        if (axisalignedbb != null)
                        {
                            d0 = axisalignedbb.maxY;
                        }
                    }

                    flag = true;
                    break;
                }

                blockpos = blockpos.down();

                if (blockpos.getY() < MathHelper.floor(p_190876_5_) - 1)
                {
                    break;
                }
            }

            if (flag)
            {
                EntityDemonFangs EntityDemonfangs = new EntityDemonFangs(EntityDemon.this.world, p_190876_1_, (double)blockpos.getY() + d0, p_190876_3_, p_190876_9_, p_190876_10_, EntityDemon.this);
                EntityDemon.this.world.spawnEntity(EntityDemonfangs);
            }
            */
        }
    protected class AICastingSpell extends EntitySpellcasterIllager.AICastingApell
    {
        AICastingSpell()
        {
            super();
        }

        /**
         * Keep ticking a continuous task that has already been started
         */
        public void updateTask()
        {
            if (EntityDemon.this.getAttackTarget() != null)
            {
                EntityDemon.this.getLookHelper().setLookPositionWithEntity(EntityDemon.this.getAttackTarget(), (float)EntityDemon.this.getHorizontalFaceSpeed(), (float)EntityDemon.this.getVerticalFaceSpeed());
            }
            else if (EntityDemon.this.getForgeTarget() != null)
            {
                EntityDemon.this.getLookHelper().setLookPositionWithEntity(EntityDemon.this.getForgeTarget(), (float)EntityDemon.this.getHorizontalFaceSpeed(), (float)EntityDemon.this.getVerticalFaceSpeed());
            }
        }
    }

}
