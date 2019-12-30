package com.gm910.magicmod.entity.classes.demons;


import java.util.List;

import javax.annotation.Nullable;

import com.gm910.magicmod.blocks.machine.BlockPentacle;
import com.gm910.magicmod.entity.classes.magical.EntityHellPortal;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntitySpellcasterIllager;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntityGatekeeper extends EntityDemon {

	private boolean inPentacle;
	protected boolean canMove = false;
	private EntityItem item = null;
	private boolean hasItem = false;
	
	public EntityGatekeeper(World worldIn) {
		super(worldIn);
		//if (worldIn.provider.getDimension() == DimensionInit.DIMENSION_SHEOL) this.setDead();
		isImmuneToFire = true;
		
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
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	protected void initEntityAI() {
		this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityGatekeeper.AICastingSpell());
        this.tasks.addTask(2, new EntityAIAvoidEntity(this, EntityVillager.class, 4.0F, 0.6D, 1.0D));
        this.tasks.addTask(2, new EntityAIAvoidEntity(this, EntityIronGolem.class, 4.0F, 0.6D, 1.0D));
        this.tasks.addTask(2, new EntityAIAvoidEntity(this, EntityGatekeeper.class, 2.0F, 0.6D, 1.0D));
        this.tasks.addTask(2, new EntityAIAvoidEntity(this, EntityPlayer.class, 4.0F, 0.6D, 1.0D));
        this.tasks.addTask(4, new EntityGatekeeper.AIForgeSpell());
        this.tasks.addTask(5, new EntityGatekeeper.AIAttackSpell());
        this.tasks.addTask(6, new EntityGatekeeper.AISummonSpell());
        this.tasks.addTask(8, new EntityAIWander(this, 0.6D));
        this.tasks.addTask(9, new EntityAIWatchClosest(this, EntityPlayer.class, 3.0F, 1.0F));
        this.tasks.addTask(10, new EntityAIWatchClosest(this, EntityLiving.class, 8.0F));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true, new Class[0]));
        this.targetTasks.addTask(2, (new EntityAINearestAttackableTarget(this, EntityPlayer.class, true)).setUnseenMemoryTicks(300));
        this.targetTasks.addTask(3, (new EntityAINearestAttackableTarget(this, EntityVillager.class, false)).setUnseenMemoryTicks(300));
        this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityIronGolem.class, false));
        this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityGatekeeper.class, false));
	}
	
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
	
	public class AISummonSpell extends AIUseSpell {

		private AISummonSpell()
        {
            super();
        }

        /**
         * Returns whether the EntityAIBase should begin execution.
         */
        public boolean shouldExecute()
        {
            if (!super.shouldExecute())
            {
                return false;
            }
            else
            {
                int i = EntityGatekeeper.this.world.getEntitiesWithinAABB(EntityHellPortal.class, EntityGatekeeper.this.getEntityBoundingBox().grow(16.0D)).size();
                return EntityGatekeeper.this.rand.nextInt(1) + 1 > i;
            }
        }

        public int getCastingTime()
        {
            return 100;
        }

        public int getCastingInterval()
        {
            return 340;
        }

        public void castSpell()
        {
             BlockPos blockpos = (new BlockPos(EntityGatekeeper.this.getAttackTarget())).add(-2 + EntityGatekeeper.this.rand.nextInt(5), 0, -2 + EntityGatekeeper.this.rand.nextInt(5));
             EntityHellPortal hell = new EntityHellPortal(EntityGatekeeper.this.world, blockpos.getX(), blockpos.getY(), blockpos.getZ(), EntityGatekeeper.this);
             EntityGatekeeper.this.world.spawnEntity(hell);
        }

        public SoundEvent getSpellPrepareSound()
        {
            return SoundEvents.ENTITY_ENDERDRAGON_DEATH;
        }

        public EntitySpellcasterIllager.SpellType getSpellType()
        {
            return EntitySpellcasterIllager.SpellType.SUMMON_VEX;
        }

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
	
	public class AIForgeSpell extends AIUseSpell {

		public AIForgeSpell () {
			super();
		}
		
		@Override
		public boolean shouldContinueExecuting()
        {
            return EntityGatekeeper.this.getForgeTarget() != null && this.spellWarmup > 0;
        }
		
		@Override
		public void resetTask()
        {
            super.resetTask();
            EntityGatekeeper.this.setForgeTarget((EntityPlayer)null);
         
        }

		
		@Override
		public boolean shouldExecute() {
			
            if (EntityGatekeeper.this.isSpellcasting())
            {
            	//System.out.println("isSpellcasting");
                return false;
            }
            else if (!net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(EntityGatekeeper.this.world, EntityGatekeeper.this))
            {
            	//System.out.println("griefing");
                return false;
            }
            else
            {
            	//System.out.println("sdfdgfhg");

            	List<EntityPlayer> list = EntityGatekeeper.this.world.<EntityPlayer>getEntitiesWithinAABB(EntityPlayer.class, EntityGatekeeper.this.getEntityBoundingBox().grow(2.0D, 2.0D, 2.0D), player -> player.getHeldItemMainhand().getItem() instanceof ItemSword);

                if (list.isEmpty())
                {
                	//System.out.println("noItems");
                    return false;
                }
                else
                {
                    EntityGatekeeper.this.setForgeTarget(list.get(EntityGatekeeper.this.rand.nextInt(list.size())));
                    return true;
                }
            }
		}

		@Override
		public void castSpell() {
			// TODO Auto-generated method stub
			EntityPlayer entity = EntityGatekeeper.this.getForgeTarget();
			
			if (EntityGatekeeper.this.world.isRemote) return;
			
            if (entity != null && entity.isEntityAlive())
            {
            	//System.out.println("ENTITY ITem Forge SPELL " + entity);
            	

                	entity.world.addWeatherEffect(new EntityLightningBolt(entity.world, entity.posX, entity.posY - 2, entity.posZ, true));
                	EntityHellPortal port = new EntityHellPortal(world, entity.posX, entity.posY, entity.posZ, EntityGatekeeper.this);
                	world.spawnEntity(port);
                	EntityGatekeeper.this.setHealth(EntityGatekeeper.this.getMaxHealth());
                	EntityGatekeeper.super.setSize((int)(EntityGatekeeper.this.width*1.5), (int)(EntityGatekeeper.this.height*1.5));
                	EntityGatekeeper.this.spawnExplosionParticle();
                	EntityGatekeeper.this.curePotionEffects(new ItemStack(Items.MILK_BUCKET, 1));

            }
		}

		public int getCastWarmupTime()
        {
            return 5;
        }

        public int getCastingTime()
        {
            return 5;
        }

        public int getCastingInterval()
        {
            return 10;
        }

        public SoundEvent getSpellPrepareSound()
        {
            return SoundEvents.BLOCK_LAVA_AMBIENT;
        }

        public EntitySpellcasterIllager.SpellType getSpellType()
        {
            return EntitySpellcasterIllager.SpellType.WOLOLO;
        }

	}
	
	

	private EntityPlayer forgeTarget;
	
    private void setForgeTarget(@Nullable EntityPlayer entityItem)
    {
        this.forgeTarget = entityItem;
    }

    @Nullable
    private EntityPlayer getForgeTarget()
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
            EntityLivingBase entitylivingbase = EntityGatekeeper.this.getAttackTarget();
            double d0 = entitylivingbase.posX - EntityGatekeeper.this.posX;
            double d1 = entitylivingbase.getEntityBoundingBox().minY + (double)(entitylivingbase.height / 3.0F) - EntityGatekeeper.this.posY;
            double d2 = entitylivingbase.posZ - EntityGatekeeper.this.posZ;
            double d3 = (double)MathHelper.sqrt(d0 * d0 + d2 * d2);
            EntityLargeFireball fireball = new EntityLargeFireball(world, EntityGatekeeper.this, d0, d1 + d3 * 0.20000000298023224D, d2);
            fireball.explosionPower = 0;
            EntityGatekeeper.this.world.spawnEntity(fireball);
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
            if (EntityGatekeeper.this.getAttackTarget() != null)
            {
                EntityGatekeeper.this.getLookHelper().setLookPositionWithEntity(EntityGatekeeper.this.getAttackTarget(), (float)EntityGatekeeper.this.getHorizontalFaceSpeed(), (float)EntityGatekeeper.this.getVerticalFaceSpeed());
            }
            else if (EntityGatekeeper.this.getForgeTarget() != null)
            {
                EntityGatekeeper.this.getLookHelper().setLookPositionWithEntity(EntityGatekeeper.this.getForgeTarget(), (float)EntityGatekeeper.this.getHorizontalFaceSpeed(), (float)EntityGatekeeper.this.getVerticalFaceSpeed());
            }
        }
    }


}
