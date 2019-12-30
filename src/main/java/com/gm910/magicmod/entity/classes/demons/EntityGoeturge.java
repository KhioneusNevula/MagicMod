package com.gm910.magicmod.entity.classes.demons;


import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import com.gm910.magicmod.blocks.machine.BlockPentacle;
import com.gm910.magicmod.items.ItemDemonOrb;
import com.gm910.magicmod.magicdamage.EntityDemonLightning;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntitySpellcasterIllager;
import net.minecraft.entity.monster.EntityVex;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntityGoeturge extends EntityDemon {

	private boolean inPentacle;
	protected boolean canMove = false;
	
	public EntityGoeturge(World worldIn) {
		super(worldIn);
		
	}
	
	public boolean canMove() {
		return canMove;
	}
	
	protected void setCanMove(boolean canMove) {
		this.canMove = canMove;
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
        this.tasks.addTask(1, new EntityGoeturge.AICastingSpell());
        this.tasks.addTask(2, new EntityAIAvoidEntity(this, EntityVillager.class, 4.0F, 0.6D, 1.0D));
        this.tasks.addTask(2, new EntityAIAvoidEntity(this, EntityIronGolem.class, 4.0F, 0.6D, 1.0D));
        this.tasks.addTask(2, new EntityAIAvoidEntity(this, EntityGoeturge.class, 2.0F, 0.6D, 1.0D));
        this.tasks.addTask(2, new EntityAIAvoidEntity(this, EntityPlayer.class, 4.0F, 0.6D, 1.0D));
        this.tasks.addTask(4, new EntityGoeturge.AIForgeSpell());
        this.tasks.addTask(5, new EntityGoeturge.AIAttackSpell());
        this.tasks.addTask(6, new EntityGoeturge.AISummonSpell());
        this.tasks.addTask(8, new EntityAIWander(this, 0.6D));
        this.tasks.addTask(9, new EntityAIWatchClosest(this, EntityPlayer.class, 3.0F, 1.0F));
        this.tasks.addTask(10, new EntityAIWatchClosest(this, EntityLiving.class, 8.0F));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true, new Class[0]));
        this.targetTasks.addTask(2, (new EntityAINearestAttackableTarget(this, EntityPlayer.class, true)).setUnseenMemoryTicks(300));
        this.targetTasks.addTask(3, (new EntityAINearestAttackableTarget(this, EntityVillager.class, false)).setUnseenMemoryTicks(300));
        this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityIronGolem.class, false));
        this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityDemon.class, false));
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
            } else if (inPentacle) {
            	return false;
            }
            else
            {
                int i = EntityGoeturge.this.world.getEntitiesWithinAABB(EntityVex.class, EntityGoeturge.this.getEntityBoundingBox().grow(16.0D)).size();
                int b = EntityGoeturge.this.world.getEntitiesWithinAABB(EntityImp.class, EntityGoeturge.this.getEntityBoundingBox().grow(16.0D)).size();
                
                return EntityGoeturge.this.rand.nextInt(8) + 1 > i && EntityGoeturge.this.rand.nextInt(8) > b;
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
            for (int i = 0; i < 3; ++i)
            {
                BlockPos blockpos = (new BlockPos(EntityGoeturge.this)).add(-2 + EntityGoeturge.this.rand.nextInt(5), 1, -2 + EntityGoeturge.this.rand.nextInt(5));
                if ((new Random()).nextBoolean()) {
	                EntityVex entityvex = new EntityVex(EntityGoeturge.this.world);
	                entityvex.moveToBlockPosAndAngles(blockpos, 0.0F, 0.0F);
	                entityvex.onInitialSpawn(EntityGoeturge.this.world.getDifficultyForLocation(blockpos), (IEntityLivingData)null);
	                entityvex.setOwner(EntityGoeturge.this);
	                entityvex.setBoundOrigin(blockpos);
	                entityvex.setLimitedLife(20 * (30 + EntityGoeturge.this.rand.nextInt(90)));
	                EntityGoeturge.this.world.spawnEntity(entityvex);
                } else {
                	EntityImp entityvex = new EntityImp(EntityGoeturge.this.world);
	                entityvex.moveToBlockPosAndAngles(blockpos, 0.0F, 0.0F);
	                entityvex.onInitialSpawn(EntityGoeturge.this.world.getDifficultyForLocation(blockpos), (IEntityLivingData)null);
	                entityvex.setOwner(EntityGoeturge.this);
	                entityvex.setBoundOrigin(blockpos);
	                entityvex.setLimitedLife(20 * (30 + EntityGoeturge.this.rand.nextInt(90)));
	                EntityGoeturge.this.world.spawnEntity(entityvex);
                }
            }
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
    	if (this.getEntityWorld() != null && this.getPosition() != null)
			if (this.getEntityWorld().getBlockState(this.getPosition()) != null)
				if (this.getEntityWorld().getBlockState(this.getPosition()).getBlock() != null)
					if (!(this.getEntityWorld().getBlockState(this.getPosition()).getBlock() instanceof BlockPentacle)) {
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
            return EntityGoeturge.this.getForgeTarget() != null && this.spellWarmup > 0;
        }
		
		@Override
		public void resetTask()
        {
            super.resetTask();
            EntityGoeturge.this.setForgeTarget((EntityItem)null);
         
        }

		
		@Override
		public boolean shouldExecute() {
			
            if (EntityGoeturge.this.isSpellcasting())
            {
            	//System.out.println("isSpellcasting");
                return false;
            }
            else if (!net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(EntityGoeturge.this.world, EntityGoeturge.this))
            {
            	//System.out.println("griefing");
                return false;
            }
            else
            {
            	//System.out.println("sdfdgfhg");

            	List<EntityItem> list = EntityGoeturge.this.world.<EntityItem>getEntitiesWithinAABB(EntityItem.class, EntityGoeturge.this.getEntityBoundingBox().grow(2.0D, 2.0D, 2.0D), it -> !(it.getItem().getItem() instanceof ItemDemonOrb));

                if (list.isEmpty())
                {
                	//System.out.println("noItems");
                    return false;
                }
                else
                {
                    EntityGoeturge.this.setForgeTarget(list.get(EntityGoeturge.this.rand.nextInt(list.size())));
                    return true;
                }
            }
		}

		@Override
		public void castSpell() {
			// TODO Auto-generated method stub
			EntityItem entity = EntityGoeturge.this.getForgeTarget();
			
			if (EntityGoeturge.this.world.isRemote) return;
			
            if (entity != null && entity.isEntityAlive())
            {
            	//System.out.println("ENTITY ITem Forge SPELL " + entity);
            	

                	entity.world.addWeatherEffect(new EntityDemonLightning(entity.world, EntityGoeturge.this, entity.posX, entity.posY - 2, entity.posZ, true, false));
                	int num = entity.getItem().getCount();
                	ItemStack stac = entity.getItem();
                	EntityPlayer p = EntityGoeturge.this.getEntityWorld().getClosestPlayerToEntity(EntityGoeturge.this, 40);
                	List<EnchantmentData> list = EnchantmentHelper.buildEnchantmentList(new Random(), stac, p == null ? 20 : p.experienceLevel, false);
                	if (!list.isEmpty()) {
                		System.out.println("Enchs");
                		for (EnchantmentData d : list) {
                			boolean yes = true;
                			for (int i = 0; i < stac.getEnchantmentTagList().tagCount(); i++) {
                				NBTTagCompound tg = stac.getEnchantmentTagList().getCompoundTagAt(i);
                				if (tg.getShort("id") == Enchantment.getEnchantmentID(d.enchantment)) {
                					yes = false;
                				}
                			}
                			if (yes) stac.addEnchantment(d.enchantment, d.enchantmentLevel);
                		}
                	}
                	stac.setCount(num);
                	entity.setItem(stac);
                	EntityGoeturge.this.setHealth(EntityGoeturge.this.getMaxHealth());
                	EntityGoeturge.super.setSize((int)(EntityGoeturge.this.width*1.5), (int)(EntityGoeturge.this.height*1.5));
                	EntityGoeturge.this.spawnExplosionParticle();
                	EntityGoeturge.this.curePotionEffects(new ItemStack(Items.MILK_BUCKET, 1));

            }
		}

		public int getCastWarmupTime()
        {
            return 40;
        }

        public int getCastingTime()
        {
            return 60;
        }

        public int getCastingInterval()
        {
            return 140;
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
            EntityLivingBase entitylivingbase = EntityGoeturge.this.getAttackTarget();
            double d0 = entitylivingbase.posX - EntityGoeturge.this.posX;
            double d1 = entitylivingbase.getEntityBoundingBox().minY + (double)(entitylivingbase.height / 3.0F) - EntityGoeturge.this.posY;
            double d2 = entitylivingbase.posZ - EntityGoeturge.this.posZ;
            double d3 = (double)MathHelper.sqrt(d0 * d0 + d2 * d2);
            ItemStack pot = new ItemStack(Items.SPLASH_POTION, 1);
            PotionUtils.addPotionToItemStack(pot, PotionType.REGISTRY.getRandomObject(new Random()));
            EntityPotion fireball = new EntityPotion(world, EntityGoeturge.this, pot);
            
            fireball.setVelocity(d0, d1 + d3 * 0.20000000298023224D, d2);
            EntityGoeturge.this.world.spawnEntity(fireball);
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
            if (EntityGoeturge.this.getAttackTarget() != null)
            {
                EntityGoeturge.this.getLookHelper().setLookPositionWithEntity(EntityGoeturge.this.getAttackTarget(), (float)EntityGoeturge.this.getHorizontalFaceSpeed(), (float)EntityGoeturge.this.getVerticalFaceSpeed());
            }
            else if (EntityGoeturge.this.getForgeTarget() != null)
            {
                EntityGoeturge.this.getLookHelper().setLookPositionWithEntity(EntityGoeturge.this.getForgeTarget(), (float)EntityGoeturge.this.getHorizontalFaceSpeed(), (float)EntityGoeturge.this.getVerticalFaceSpeed());
            }
        }
    }


}
