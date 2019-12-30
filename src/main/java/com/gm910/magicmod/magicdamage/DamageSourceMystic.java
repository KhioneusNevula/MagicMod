package com.gm910.magicmod.magicdamage;

import com.gm910.magicmod.deity.entities.EntityDeity;
import com.gm910.magicmod.deity.util.Deity;
import com.gm910.magicmod.entity.classes.demons.EntityDemon;
import com.gm910.magicmod.handling.MagicHandler.Spell;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;

public class DamageSourceMystic extends DamageSource {
	
	private boolean isHoly;
	private boolean isDemonic;
	
	//public static final DamageSource GOD_CURSE = (new DamageSource("godCurse")).setDamageBypassesArmor().setDamageIsAbsolute().setMagicDamage();
	public static final DamageSource HELLISH = (new DamageSourceCustomDeathMessage("hellish", "damage.hellish", false, true)).setFireDamage().setMagicDamage().setDamageIsAbsolute().setDamageBypassesArmor();
	public static final DamageSource HOLY = (new DamageSourceCustomDeathMessage("generic_holy", "damage.holy", true, false)).setDamageIsAbsolute().setDamageBypassesArmor().setMagicDamage();
	public static final DamageSource GENERIC_SPELL = (new DamageSourceCustomDeathMessage("genericSpell", "damage.generic_spell", false, false)).setDamageIsAbsolute().setDamageBypassesArmor().setMagicDamage();

	public DamageSourceMystic(String damageTypeIn, boolean isHoly, boolean isDemonic) {
		super(damageTypeIn);
		this.isHoly = isHoly;
		this.isDemonic = isDemonic;
		setDamageBypassesArmor();
	}
	
	public boolean isHoly() {
		return isHoly;
	}
	
	public boolean isDemonic() {
		return isDemonic;
	}
	
	@Override
	public boolean isMagicDamage() {
		// TODO Auto-generated method stub
		return true;
	}
	
	@Override
	public boolean isDamageAbsolute() {
		// TODO Auto-generated method stub
		return true;
	}
	
	public static DeityDamageSource castGodCurse(Deity deity) {
		
		return new DeityDamageSource("godCurse", deity);
	}
	
	public static boolean isHoly(DamageSource damage) {
		if (damage instanceof DamageSourceMystic) return ((DamageSourceMystic) damage).isHoly();
		return false;
	}
	
	public static boolean isDemonic(DamageSource damage) {
		if (damage instanceof DamageSourceMystic) return ((DamageSourceMystic) damage).isDemonic();
		if (damage instanceof GhostDamageSource) return true;
		return false;
	}

	
	public static DeityDamageSource castGodCurse(Deity deity, EntityDeity entity) {
		return new DeityDamageSource("godCurse", deity, entity);
	}
	
	public static DeityDamageSource causeHolyDamageFrom(Deity deity) {
		return new DeityDamageSource("godHoliness", deity);
	}
	
	public static DeityDamageSource causeHolyDamageFrom(Deity deity, EntityDeity entity) {
		return new DeityDamageSource("godHoliness", deity, entity);
	}
	
	public static DemonDamageSource causeDemonDamage(EntityDemon entityIn) {
		return new DemonDamageSource("demon", entityIn);
	}
	
	public static DemonDamageSource causeIndirectDemonDamage(EntityDemon entityIn, EntityLivingBase summoner) {
		return new DemonDamageSource("demonIndirect", entityIn, summoner);
	}
	
	public static GhostDamageSource causeGhostDamage(EntityLiving entityIn) {
		return new GhostDamageSource("ghostIndirect", entityIn);
	}
	
	public static SpellDamageSource castSpell(EntityLivingBase caster, BlockPos location, Spell spell) {
		
		return new SpellDamageSource("spell", caster, location, spell);
	}
	
	public static SpellDamageSource castIndirectSpell(EntityLivingBase caster, Entity proxy, BlockPos location, Spell spell) {
		
		return new SpellDamageSource("spellIndirect", caster, proxy, spell);
	}
	
	public static SpellDamageSource causeReboundSpellDamage(EntityLivingBase caster, BlockPos location, Spell spell) {
		
		return (new SpellDamageSource("magicalRebound", caster, location, spell)).setIsRebound();
	}
	
	public static SpellDamageSource causeIndirectReboundSpellDamage(EntityLivingBase caster, Entity proxy, Spell spell) {
		
		return (new SpellDamageSource("magicalReboundIndirect", caster, proxy, spell)).setIsRebound();
	}

	
	public static class DeityDamageSource extends DamageSourceMystic {
		
		public final Deity deity;
		public EntityLivingBase entity;

		public DeityDamageSource(String name, Deity deity) {
			super(name, true, false);
			this.deity = deity;
			entity = deity.getAvatar();
			setDamageBypassesArmor();
			setDamageIsAbsolute();
		}
		
		public DeityDamageSource(String name, Deity deity, EntityDeity source) {
			this(name, deity);
			this.entity = source;
		}
		
		@Override
		public Entity getImmediateSource() {
			// TODO Auto-generated method stub
			return this.entity;
		}
		
		@Override
		public Vec3d getDamageLocation() {
			return entity.getPositionVector();
		}
		
		@Override
		public boolean isMagicDamage() {
			// TODO Auto-generated method stub
			return true;
		}
		
		public Deity getDeity() {
			return deity;
		}
		
		@Override
		public ITextComponent getDeathMessage(EntityLivingBase base) {
			
			return new TextComponentTranslation("damage.godcurse", base.getDisplayName().getFormattedText(), deity.pro.getNoun(), deity.getDisplayName());
		}
		
		@Override
		public boolean isUnblockable() {
			// TODO Auto-generated method stub
			return true;
		}
		
	}
	
	public static class GhostDamageSource extends DamageSourceMystic {
		
		public EntityLiving ghost;

		public GhostDamageSource(String damageTypeIn, EntityLiving damageSourceEntityIn) {
			super(damageTypeIn, false, true);
			setDamageBypassesArmor();
			ghost = damageSourceEntityIn;
			
		}

		@Override
		public ITextComponent getDeathMessage(EntityLivingBase entityLivingBaseIn) {
			
			return new TextComponentTranslation("damage.ghost", entityLivingBaseIn.getDisplayName().getFormattedText(), this.getImmediateSource().getDisplayName().getFormattedText());
		}
		
		@Override
		public boolean isMagicDamage() {
			
			return true;
		}

		
		@Override
		public boolean isDamageAbsolute() {
			// TODO Auto-generated method stub
			return true;
		}
		
		@Override
		public Entity getTrueSource() {
			// TODO Auto-generated method stub
			return ghost;
		}
		
		@Override
		public Entity getImmediateSource() {
			// TODO Auto-generated method stub
			return ghost;
		}
		
		@Override
		public Vec3d getDamageLocation() {
			// TODO Auto-generated method stub
			return ghost.getPositionVector();
		}
		
	}
	
	public static class DemonDamageSource extends DamageSourceMystic {
		
		public EntityDemon demon;
		public EntityLivingBase summoner;
		private boolean isThornsDamage;

		public DemonDamageSource(String damageTypeIn, EntityDemon damageSourceEntityIn) {
			super(damageTypeIn, false, true);
			setDamageBypassesArmor();
			demon = damageSourceEntityIn;
			summoner = damageSourceEntityIn;
		}
		
		public DemonDamageSource setIsThornsDamage()
	    {
	        this.isThornsDamage = true;
	        return this;
	    }

	    public boolean getIsThornsDamage()
	    {
	        return this.isThornsDamage;
	    }

		
		public DemonDamageSource(String name, EntityDemon indirect, EntityLivingBase summoner) {
			super(name, false, true);
			demon = indirect;
			this.summoner = summoner;
		}
		
		@Override
		public ITextComponent getDeathMessage(EntityLivingBase entityLivingBaseIn) {
			
			return new TextComponentTranslation("damage.demon", entityLivingBaseIn.getDisplayName().getFormattedText(), this.getImmediateSource().getDisplayName().getFormattedText());
		}
		
		@Override
		public boolean isMagicDamage() {
			
			return true;
		}
		
		@Override
		public boolean isFireDamage() {
			// TODO Auto-generated method stub
			return true;
		}
		
		@Override
		public boolean isDamageAbsolute() {
			// TODO Auto-generated method stub
			return true;
		}
		
		@Override
		public Entity getTrueSource() {
			// TODO Auto-generated method stub
			return summoner;
		}
		
		@Override
		public Entity getImmediateSource() {
			// TODO Auto-generated method stub
			return demon;
		}
		
		@Override
		public Vec3d getDamageLocation() {
			// TODO Auto-generated method stub
			return demon.getPositionVector();
		}
		
	}
	
	public static class SpellDamageSource extends DamageSourceMystic {

		public final EntityLivingBase caster;
		public final Entity indirectSource;
		public final Spell spell;
		public final Vec3d location;
		private boolean isRebound= false;
		private boolean isThornsDamage;
		
		public SpellDamageSource(String damageTypeIn, EntityLivingBase caster, BlockPos location, Spell spell) {
			super(damageTypeIn, false, false);
			this.caster = caster;
			this.spell = spell;
			this.indirectSource = caster;
			this.location = new Vec3d(location.getX(), location.getY(), location.getZ());
		}
		
		public SpellDamageSource(String name, EntityLivingBase caster, Entity indirect, Spell spell) {
			super(name, false, false);
			this.caster = caster;
			this.spell = spell;
			this.indirectSource = indirect;
			this.location = indirect.getPositionVector();
		}

		
		public SpellDamageSource setIsThornsDamage()
	    {
	        this.isThornsDamage = true;
	        return this;
	    }

	    public boolean getIsThornsDamage()
	    {
	        return this.isThornsDamage;
	    }

		
		@Override
		public Entity getImmediateSource() {
			
			return indirectSource;
		}
		
		@Override
		public Vec3d getDamageLocation() {
			// TODO Auto-generated method stub
			return location;
		}
		
		public Spell getSpell() {
			return spell;
		}
		
		@Override
		public Entity getTrueSource() {
			// TODO Auto-generated method stub
			return caster;
		}
		
		@Override
		public ITextComponent getDeathMessage(EntityLivingBase entityLivingBaseIn) {
			// TODO Auto-generated method stub
			if (!isRebound) {
				return new TextComponentTranslation("damage.spell", entityLivingBaseIn.getDisplayName().getFormattedText());
			} else {
				return new TextComponentTranslation("damage.rebound", entityLivingBaseIn.getDisplayName().getFormattedText());
			}
		}
		
		@Override
		public boolean isDamageAbsolute() {
			// TODO Auto-generated method stub
			return true;
		}
		
		@Override
		public boolean isMagicDamage() {
			// TODO Auto-generated method stub
			return true;
		}
		
		public boolean isRebound() {
			return isRebound;
		}
		
		public SpellDamageSource setIsRebound() {
			this.isRebound = true;
			return this;
		}
		
	}
	
	public static class DamageSourceCustomDeathMessage extends DamageSourceMystic {
		
		public final String message;

		/**
		 * use [NAME] for entity name
		 * @param damageTypeIn
		 * @param message
		 */
		public DamageSourceCustomDeathMessage(String damageTypeIn, String message, boolean isHoly, boolean isDemonic) {
			super(damageTypeIn, isHoly, isDemonic);
			this.message = message;
		}
		
		@Override
		public ITextComponent getDeathMessage(EntityLivingBase entityLivingBaseIn) {
			
			return new TextComponentTranslation(message, entityLivingBaseIn.getDisplayName().getFormattedText());
		}
		
	}

}
