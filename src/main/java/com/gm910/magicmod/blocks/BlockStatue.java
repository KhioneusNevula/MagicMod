package com.gm910.magicmod.blocks;

import java.util.Random;
import java.util.UUID;

import com.gm910.magicmod.MagicMod;
import com.gm910.magicmod.deity.Deities;
import com.gm910.magicmod.deity.util.Deity;
import com.gm910.magicmod.deity.util.ServerPos;
import com.gm910.magicmod.init.ItemInit;
import com.gm910.magicmod.items.ItemStatue;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockStatue extends BlockBase {
	public final Deity deity;
	public static enum EnumTypeStatue implements IStringSerializable
    {
        NORMAL("normal", 0),
        VILLAGE("village", 1);

        private final String name;
        private final int val;

        private EnumTypeStatue(String name, int val)
        {
            this.name = name;
            this.val = val;
        }
        
        public static EnumTypeStatue fromMeta(int meta) {
        	for (EnumTypeStatue stat : values()) {
        		if (stat.val == meta) return stat;
        	}
        	return NORMAL;
        }

        public String toString()
        {
            return this.name;
        }

        public String getName()
        {
            return this.name;
        }
        
        public int getVal() {
			return val;
		}
        
    }
	public static final PropertyEnum<EnumTypeStatue> TYPE = PropertyEnum.create("type", EnumTypeStatue.class);
	
	public BlockStatue(Deity d) {
		super("statue_" + d.getUnlocName().getResourcePath(), Material.ROCK);
		System.out.println(d.getDisplayName() + " statue block created");
		deity = d;
		
		this.setDefaultState(this.blockState.getBaseState().withProperty(TYPE, EnumTypeStatue.NORMAL));
		ItemStatue is = new ItemStatue(this);
		ItemInit.ITEM_STATUES.put(d, is);
		ItemInit.ITEMS.add(is);
		
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		// TODO Auto-generated method stub
		return new BlockStateContainer(this, TYPE);
	}

	
	@Override
	public int getMetaFromState(IBlockState state) {
		
		return state.getValue(TYPE).getVal();
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(TYPE, EnumTypeStatue.fromMeta(meta));
	}

	
	@Override
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
		if (state.getValue(TYPE) == EnumTypeStatue.VILLAGE) {
			deity.getVillageStatues().add(new ServerPos(pos, worldIn.provider.getDimension()));
		}
		deity.getStatues().add(new ServerPos(pos, worldIn.provider.getDimension()));
		deity.onStatueAdded(worldIn, pos, state);
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		// TODO Auto-generated method stub
		return deity.onStatueActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
	}
	
	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		deity.onBreakStatuePre(worldIn, pos, state);
		
		super.breakBlock(worldIn, pos, state);
		deity.onBreakStatuePost(worldIn, pos, state);
		deity.getStatues().remove(new ServerPos(pos, worldIn.provider.getDimension()));
		if (state.getValue(TYPE) == EnumTypeStatue.VILLAGE) {
			deity.getVillageStatues().remove(new ServerPos(pos, worldIn.provider.getDimension()));
		}
	}
	
	@Override
	public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
		if (world.getTileEntity(pos) == null) return 0;
		if (!(world.getTileEntity(pos) instanceof TileEntityStatue)) return 0;
		if (((TileEntityStatue)world.getTileEntity(pos)).placer == null) return 0;
		int lv = (int)(this.deity.getWorship(((TileEntityStatue)world.getTileEntity(pos)).placer) / 2.0);
		lv = lv > 15 ? 15 : lv;
		//System.out.println(lv);
		this.lightValue = lv;
		return lv;
	}
	
	@Override
	public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state) {
		deity.playerBreakStatuePost(worldIn, pos, state);
	}
	
	@Override
	public void onBlockClicked(World worldIn, BlockPos pos, EntityPlayer playerIn) {
		deity.onPlayerHitStatue(worldIn, pos, playerIn);
	}
	
	@Override
	public void onBlockExploded(World world, BlockPos pos, Explosion explosion) {
		deity.statueExplodePre(world, pos, explosion);
		super.onBlockExploded(world, pos, explosion);
	}
	
	@Override
	public void onBlockDestroyedByExplosion(World worldIn, BlockPos pos, Explosion explosionIn) {
		deity.statueExplodePost(worldIn, pos, explosionIn);
	}
	
	@Override
	public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player) {
		deity.playerBreakStatuePre(worldIn, pos, state, player);
	}
	
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer,
			ItemStack stack) {
		if (worldIn.getTileEntity(pos) != null && stack.getSubCompound("BlockEntityTag") == null) {
			((TileEntityStatue)worldIn.getTileEntity(pos)).setPlacer(placer.getPersistentID());
		}
		deity.afterPlayerPlaceStatue(worldIn, pos, state, placer, stack);
	}
	
	@Override
	public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY,
			float hitZ, int meta, EntityLivingBase placer) {
		if (worldIn.getTileEntity(pos) != null) {
			((TileEntityStatue)worldIn.getTileEntity(pos)).setPlacer(placer.getPersistentID());
		} else {
			TileEntityStatue te = new TileEntityStatue(deity);
			te.setPlacer(placer.getPersistentID());
			worldIn.setTileEntity(pos, te);
		}

		return super.getStateForPlacement(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer);
	}
	
	@Override
	public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
		
		deity.entityCollideWithStatue(worldIn, pos, state, entityIn);
	}
	
	@Override
	public boolean canConnectRedstone(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
		// TODO Auto-generated method stub
		return deity.isStatueRedstone(state, world, pos, side);
	}
	
	@Override
	public boolean hasTileEntity(IBlockState state) {
		// TODO Auto-generated method stub
		return true;
	}
	
	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileEntityStatue(deity);
	}
	
	@Override
	public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor) {
		deity.onStatueNeighborChange(world, pos, neighbor);
	}
	
	@Override
	public boolean canBeConnectedTo(IBlockAccess world, BlockPos pos, EnumFacing facing) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
		// TODO Auto-generated method stub
		return BlockFaceShape.UNDEFINED;
	}
	
	@Override
	public float getEnchantPowerBonus(World world, BlockPos pos) {
		return deity.statueEnchantPower(world, pos);
	}
	
	public static class TileEntityStatue extends TileEntity implements ITickable {

		public Deity deity;
		public UUID placer;
		public BlockPos village;
		
		public TileEntityStatue(Deity deity) {
			this.deity = deity;
		}
		
		public UUID getPlacer() {
			return placer;
		}
		
		public void setVillage(BlockPos village) {
			this.village = village;
		}
		
		public BlockPos getVillage() {
			return village;
		}
		
		public void setPlacer(UUID placer) {
			this.placer = placer;
		}
		
		public EnumTypeStatue getType() {
			if (this.world.getBlockState(pos).getBlock() instanceof BlockStatue) {
				return this.world.getBlockState(pos).getValue(TYPE);
			}
			return EnumTypeStatue.NORMAL;
		}
		
		public TileEntityStatue() {
			this.deity = MagicMod.deities().deities.get((new Random()).nextInt(MagicMod.deities().deities.size()));
		}
		
		public Deity getDeity() {
			return deity;
		}
		public void setDeity(Deity deity) {
			this.deity = deity;
		}
		
		@Override
		public void update() {
			this.deity = world.getBlockState(pos).getBlock() instanceof BlockStatue ? ((BlockStatue)world.getBlockState(pos).getBlock()).deity : MagicMod.deities().LEVIATHAN;
			if (world == null) {
				System.out.println("blockstatue noworld");
				return;
			}
			if (pos == null) {
				System.out.println("blockstatue nopos");
				return;
			}
			deity.onStatueTick(world, pos, world.getBlockState(pos), this);
		}
		
		@Override
		public void readFromNBT(NBTTagCompound compound) {
			// TODO Auto-generated method stub
			super.readFromNBT(compound);
			this.placer = compound.getUniqueId("Placer");
			this.deity = MagicMod.deities().fromString(compound.getString("Deity"));
		}
		
		@Override
		public NBTTagCompound writeToNBT(NBTTagCompound compound) {
			compound = super.writeToNBT(compound);
			if (placer != null) {
				compound.setUniqueId("Placer", placer);
			}
			compound.setString("Deity", deity.toString());
			return compound;
		}
		
	}

}
