package com.gm910.magicmod.entity.classes.demons;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Nullable;

import com.gm910.magicmod.blocks.machine.BlockPentacle;
import com.gm910.magicmod.magicdamage.DamageSourceMystic;
import com.gm910.magicmod.magicdamage.EntityDemonLightning;

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
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntitySpellcasterIllager;
import net.minecraft.entity.monster.EntityVex;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
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

public class EntityAzraelite extends EntityDemon {

	private boolean inPentacle;
	protected boolean canMove = false;
	private Map<EntityLiving, Integer> entities = new HashMap<EntityLiving, Integer>();
	public InventoryBasic inventory = new InventoryBasic("Azraelite", false, 800);
	
	public EntityAzraelite(World worldIn) {
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
        this.tasks.addTask(1, new EntityAzraelite.AICastingSpell());
        this.tasks.addTask(2, new EntityAIAvoidEntity(this, EntityVillager.class, 4.0F, 0.6D, 1.0D));
        this.tasks.addTask(2, new EntityAIAvoidEntity(this, EntityIronGolem.class, 4.0F, 0.6D, 1.0D));
        this.tasks.addTask(2, new EntityAIAvoidEntity(this, EntityAzraelite.class, 2.0F, 0.6D, 1.0D));
        this.tasks.addTask(2, new EntityAIAvoidEntity(this, EntityPlayer.class, 4.0F, 0.6D, 1.0D));
        this.tasks.addTask(4, new EntityAzraelite.AIForgeSpell());
        this.tasks.addTask(5, new EntityAzraelite.AIAttackSpell());
        this.tasks.addTask(6, new EntityAzraelite.AISummonSpell());
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
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(10.0D);
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
                int i = EntityAzraelite.this.world.getEntitiesWithinAABB(EntityVex.class, EntityAzraelite.this.getEntityBoundingBox().grow(16.0D)).size();
                return EntityAzraelite.this.rand.nextInt(8) + 1 > i;
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
                BlockPos blockpos = (new BlockPos(EntityAzraelite.this)).add(-2 + EntityAzraelite.this.rand.nextInt(5), 1, -2 + EntityAzraelite.this.rand.nextInt(5));
                EntityVex entityvex = new EntityVex(EntityAzraelite.this.world);
                entityvex.moveToBlockPosAndAngles(blockpos, 0.0F, 0.0F);
                entityvex.onInitialSpawn(EntityAzraelite.this.world.getDifficultyForLocation(blockpos), (IEntityLivingData)null);
                entityvex.setOwner(EntityAzraelite.this);
                entityvex.setBoundOrigin(blockpos);
                entityvex.setLimitedLife(20 * (30 + EntityAzraelite.this.rand.nextInt(90)));
                EntityAzraelite.this.world.spawnEntity(entityvex);
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
        if (entities.entrySet().size() > 5) {
        	entities.clear();
        }
        Set<Entry<EntityLiving, Integer>> entrySet = entities.entrySet();
        if (entrySet.size() > 0) {
        	try {
		        for (Entry<EntityLiving, Integer> en : entrySet) {
		        	if (en.getValue() <= 0) {
		        		if (!en.getKey().isDead) {
		        			if (rand.nextInt(8) <= 7) {
		        				entities.put(en.getKey(), this.rand.nextInt(800));
		        			} else {
		        				en.getKey().getEntityData().removeTag("Azraelite");
		        				entities.remove(en.getKey());
		        			}
		        		} else {
		        			
		        			entities.remove(en.getKey());
		        		}
		        	} else {
		        		if (rand.nextInt(5) < 3) {
		        			EntityLivingBase source = this;
		        			if (this.summoner != null) {
		        				System.out.println("Summoner is player");
		        				source= this.summoner;
		        			}
		        			en.getKey().attackEntityFrom(DamageSourceMystic.causeIndirectDemonDamage(this, source), 10);
		        			//System.out.println("DAMAGED " + this.getPersistentID());
		        			en.getKey().getEntityData().setUniqueId("Azraelite", this.getPersistentID());
		        			//getEntityWorld().addWeatherEffect(new EntityLightningBolt(getEntityWorld(), en.getKey().posX, en.getKey().posY, en.getKey().posZ, true));
		        		}
		        		if (en.getKey().getDistanceSq(this) > 25) {
		        			en.getKey().moveToBlockPosAndAngles(EntityAzraelite.this.getPosition().north(EntityAzraelite.this.rand.nextInt(3)).south(EntityAzraelite.this.rand.nextInt(3)).east(EntityAzraelite.this.rand.nextInt(3)).west(EntityAzraelite.this.rand.nextInt(3)), 0, 0);
		        		}
		        		entities.put(en.getKey(), en.getValue()-1);
		        	}
		        }
	        } catch (Exception e) {
	        	e.printStackTrace();
	        }
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
            return EntityAzraelite.this.getForgeTarget() != null && this.spellWarmup > 0;
        }
		
		@Override
		public void resetTask()
        {
            super.resetTask();
            EntityAzraelite.this.setForgeTarget((EntityLiving)null);
         
        }

		
		@Override
		public boolean shouldExecute() {
			
            if (EntityAzraelite.this.isSpellcasting())
            {
            	//System.out.println("isSpellcasting");
                return false;
            }
            else if (!net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(EntityAzraelite.this.world, EntityAzraelite.this))
            {
            	//System.out.println("griefing");
                return false;
            }
            else
            {
            	//System.out.println("sdfdgfhg");

            	List<EntityLiving> list = EntityAzraelite.this.world.<EntityLiving>getEntitiesWithinAABB(EntityLiving.class, EntityAzraelite.this.getEntityBoundingBox().grow(8.0D, 8.0D, 8.0D), n -> !(n instanceof EntityDemon) && n != EntityAzraelite.this);
                if (list.isEmpty())
                {
                	//System.out.println("noItems");
                    return false;
                }
                else
                {
                	EntityLiving liv = list.get(EntityAzraelite.this.rand.nextInt(list.size()));
                    EntityAzraelite.this.setForgeTarget(liv);
                    return true;
                }
            }
		}

		@Override
		public void castSpell() {
			// TODO Auto-generated method stub
			EntityLiving entity = EntityAzraelite.this.getForgeTarget();
			
			if (EntityAzraelite.this.world.isRemote) return;
			
            if (entity != null && entity.isEntityAlive())
            {
            	//System.out.println("ENTITY ITem Forge SPELL " + entity);
            	

                	entity.world.addWeatherEffect((new EntityDemonLightning(entity.world, EntityAzraelite.this, entity.posX, entity.posY - 2, entity.posZ, true, false)));
                	
                	//entity.addPotionEffect(new PotionEffect(Potion.getPotionFromResourceLocation("wither"), 400, 4));
                	
                	entities.put(entity, EntityAzraelite.this.rand.nextInt(800));
                	entity.addPotionEffect(new PotionEffect(Potion.getPotionFromResourceLocation("slowness"), 400, 4));
                	entity.moveToBlockPosAndAngles(EntityAzraelite.this.getPosition().north(EntityAzraelite.this.rand.nextInt(3)).south(EntityAzraelite.this.rand.nextInt(3)).east(EntityAzraelite.this.rand.nextInt(3)).west(EntityAzraelite.this.rand.nextInt(3)), 0, 0);
                	EntityAzraelite.this.setHealth(EntityAzraelite.this.getMaxHealth());
                	EntityAzraelite.super.setSize((int)(EntityAzraelite.this.width*1.5), (int)(EntityAzraelite.this.height*1.5));
                	EntityAzraelite.this.spawnExplosionParticle();
                	EntityAzraelite.this.curePotionEffects(new ItemStack(Items.MILK_BUCKET, 1));

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
	
	

	private EntityLiving forgeTarget;
	
    private void setForgeTarget(@Nullable EntityLiving entityCreature)
    {
        this.forgeTarget = entityCreature;
    }

    @Nullable
    private EntityLiving getForgeTarget()
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
            EntityLivingBase entitylivingbase = EntityAzraelite.this.getAttackTarget();
            double d0 = entitylivingbase.posX - EntityAzraelite.this.posX;
            double d1 = entitylivingbase.getEntityBoundingBox().minY + (double)(entitylivingbase.height / 3.0F) - EntityAzraelite.this.posY;
            double d2 = entitylivingbase.posZ - EntityAzraelite.this.posZ;
            double d3 = (double)MathHelper.sqrt(d0 * d0 + d2 * d2);
            
            EntityLargeFireball fireball = new EntityLargeFireball(world, EntityAzraelite.this, d0, d1 + d3 * 0.20000000298023224D, d2);
            fireball.explosionPower = 0;
            EntityAzraelite.this.world.spawnEntity(fireball);
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
            if (EntityAzraelite.this.getAttackTarget() != null)
            {
                EntityAzraelite.this.getLookHelper().setLookPositionWithEntity(EntityAzraelite.this.getAttackTarget(), (float)EntityAzraelite.this.getHorizontalFaceSpeed(), (float)EntityAzraelite.this.getVerticalFaceSpeed());
            }
            else if (EntityAzraelite.this.getForgeTarget() != null)
            {
                EntityAzraelite.this.getLookHelper().setLookPositionWithEntity(EntityAzraelite.this.getForgeTarget(), (float)EntityAzraelite.this.getHorizontalFaceSpeed(), (float)EntityAzraelite.this.getVerticalFaceSpeed());
            }
        }
    }


}
