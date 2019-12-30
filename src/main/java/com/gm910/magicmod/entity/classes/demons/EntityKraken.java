package com.gm910.magicmod.entity.classes.demons;

import java.util.UUID;

import com.gm910.magicmod.magicdamage.DamageSourceMystic;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAITarget;
import net.minecraft.world.World;

public class EntityKraken extends EntityDemon implements IDemonWithOwner, IDemonAttackParams {

	private UUID owner;
	
	public EntityKraken(World worldIn) {
		super(worldIn);
		this.setSize(1, 2);
	}

	@Override
	protected void initEntityAI() {
		this.targetTasks.addTask(1, new AIDemonOwnerHurtByTarget(this));
		this.targetTasks.addTask(2, new AIDemonOwnerHurtTarget(this));
		this.tasks.addTask(1, new EntityAIKrakenTentacleAttack(this));
	}

	@Override
	public EntityLivingBase getOwner() {
		if (world.isRemote) return null;
		if (owner == null) return null;
		return (EntityLivingBase)world.getMinecraftServer().getEntityFromUuid(owner);
	}
	
	@Override
	public void applyEntityAttributes() {
		
        //this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(10.0f);
		super.applyEntityAttributes();
	}

	@Override
	public void setOwner(EntityLivingBase owner) {
		this.owner = owner.getPersistentID();
	}
	
	@Override
	public boolean shouldAttack(EntityLivingBase attacker, EntityLivingBase owner) {
		// TODO Auto-generated method stub
		return !(attacker instanceof EntityDemon && owner != null);
	}
	
	public static class AIDemonOwnerHurtByTarget extends EntityAITarget {

		public EntityDemon theDemon;
		public IDemonWithOwner withOwner;
		public EntityLivingBase attacker;
		private int timestamp;
		
		/**
		 * creature must be demonwith owner
		 * @param creature
		 */
		public AIDemonOwnerHurtByTarget(EntityDemon creature) {
			super(creature, false, true);
			if (!(creature instanceof IDemonWithOwner)) {
				throw new IllegalArgumentException("Creature must be demonwithowner");
			}
			this.theDemon = creature;
			this.withOwner = (IDemonWithOwner)creature;
			this.setMutexBits(1);
		}

	    /**
	     * Returns whether the EntityAIBase should begin execution.
	     */
	    public boolean shouldExecute()
	    {

	        if (this.withOwner.getOwner() == null)
	        {
	            return false;
	        }
	        else
	        {
	            EntityLivingBase entitylivingbase = this.withOwner.getOwner();

	            
	                this.attacker = entitylivingbase.getRevengeTarget();
	                int i = entitylivingbase.getRevengeTimer();
	                return i != this.timestamp && this.isSuitableTarget(this.attacker, false) && (theDemon instanceof IDemonAttackParams ? ((IDemonAttackParams)theDemon).shouldAttack(attacker, entitylivingbase) : true);
	            }
	    }

	    /**
	     * Execute a one shot task or start executing a continuous task
	     */
	    public void startExecuting()
	    {
	    	
	        this.taskOwner.setAttackTarget(this.attacker);
	        EntityLivingBase entitylivingbase = this.withOwner.getOwner();

	        if (entitylivingbase != null)
	        {
	            this.timestamp = entitylivingbase.getRevengeTimer();
	        }

	        super.startExecuting();
	    }
		
	}
	
	public static class AIDemonOwnerHurtTarget extends EntityAITarget {

		public EntityDemon theDemon;
		public IDemonWithOwner withOwner;
		public EntityLivingBase attacker;
		private int timestamp;
		
		/**
		 * creature must be instanceof demonWithOwner
		 * @param creature
		 */
		public AIDemonOwnerHurtTarget(EntityDemon creature) {
			super(creature, false, true);
			if (!(creature instanceof IDemonWithOwner)) {
				throw new IllegalArgumentException("Creature must be demonwithowner");
			}
			this.theDemon = creature;
			this.withOwner = (IDemonWithOwner)creature;
			this.setMutexBits(1);
		}

	    /**
	     * Returns whether the EntityAIBase should begin execution.
	     */
	    public boolean shouldExecute()
	    {
	        if (this.withOwner.getOwner() == null)
	        {
	            return false;
	        }
	        else
	        {
	            EntityLivingBase entitylivingbase = this.withOwner.getOwner();

	            
	                this.attacker = entitylivingbase.getLastAttackedEntity();
	                int i = entitylivingbase.getLastAttackedEntityTime();
	                return i != this.timestamp && this.isSuitableTarget(this.attacker, false) && (theDemon instanceof IDemonAttackParams ? ((IDemonAttackParams)theDemon).shouldAttack(attacker, entitylivingbase) : true);
	        }
	    }

	    /**
	     * Execute a one shot task or start executing a continuous task
	     */
	    public void startExecuting()
	    {
	        this.taskOwner.setAttackTarget(this.attacker);
	        EntityLivingBase entitylivingbase = this.withOwner.getOwner();

	        if (entitylivingbase != null)
	        {
	            this.timestamp = entitylivingbase.getLastAttackedEntityTime();
	        }

	        super.startExecuting();
	    }
		
	}

	public static class EntityAIKrakenTentacleAttack extends EntityAIBase
	{
	    EntityDemon kraken;

	    EntityLivingBase grabTarget;


	    public EntityAIKrakenTentacleAttack(EntityDemon krak)
	    {
	        this.kraken = krak;
	        this.setMutexBits(5);
	    }

	    /**
	     * Returns whether the EntityAIBase should begin execution.
	     */
	    public boolean shouldExecute()
	    {
	        this.grabTarget = this.kraken.getAttackTarget();

	        if (this.grabTarget == null)
	        {
	            return false;
	        }
	        else
	        {
	            double d0 = this.kraken.getDistanceSq(this.grabTarget);
	            if (d0 <= 100) {
	            	return true;
	            }
	            return false;
	        }
	    }

	    /**
	     * Returns whether an in-progress EntityAIBase should continue executing
	     */
	    public boolean shouldContinueExecuting()
	    {
	        return shouldExecute();
	    }

	    /**
	     * Execute a one shot task or start executing a continuous task
	     */
	    public void startExecuting()
	    {
	        double d0 = this.grabTarget.posX - this.kraken.posX;
	        double d1 = this.grabTarget.posZ - this.kraken.posZ;
	        this.grabTarget.move(MoverType.SELF, kraken.posX+kraken.getRNG().nextInt(10), kraken.posY+kraken.getRNG().nextInt(10), kraken.posZ+kraken.getRNG().nextInt(10));
	        kraken.setLeashHolder(grabTarget, false);
	        if (kraken.getRNG().nextInt(100) < 5) {
	        	if (this.kraken instanceof IDemonWithOwner) {
	        		IDemonWithOwner dwo = (IDemonWithOwner) this.kraken;
	        		if (dwo.getOwner() != null) {
	        			grabTarget.attackEntityFrom(DamageSourceMystic.causeIndirectDemonDamage((EntityDemon)kraken, dwo.getOwner()), (float)10);//kraken.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue());
	        			
	        		}
	        		grabTarget.attackEntityAsMob(kraken);
	        	}
	        }
	    }
	}

}
