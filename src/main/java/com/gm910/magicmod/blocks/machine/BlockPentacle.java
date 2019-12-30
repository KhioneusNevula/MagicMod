package com.gm910.magicmod.blocks.machine;

import java.util.Random;
import java.util.UUID;

import com.gm910.magicmod.blocks.BlockBase;
import com.gm910.magicmod.blocks.te.TileEntityPentacle;
import com.gm910.magicmod.init.BlockInit;
import com.gm910.magicmod.proxy.CommonProxy;

import net.minecraft.block.Block;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockPentacle extends BlockBase {

	public BlockPentacle(String name) {
		super(name, Material.WOOD);
		setLightLevel(1.0f);
		this.blockHardness = 25;
		this.blockResistance = 99;
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {
		for (int j = 0; j < 1000; ++j)
        {
            double d3 = (double)pos.getX() + worldIn.rand.nextDouble(); //* 0.10000000149011612D;
            double d8 = (double)pos.getY() + (worldIn.rand.nextInt(7) > 2 ? -1 : 1)*worldIn.rand.nextDouble() * 500;
            double d13 = (double)pos.getZ() + worldIn.rand.nextDouble();
            worldIn.spawnParticle(EnumParticleTypes.ENCHANTMENT_TABLE, d3, d8, d13, 0.0D, 0.1D, 0.0D);
        }
	}
	

	
	@Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
		
		for (int x = -1; x <= 1; x++) {
			for (int z = -1; z<=1;z++) {
				BlockPos pos1 = new BlockPos(pos.getX()+x, pos.getY(), pos.getZ()+z);
				if (!(worldIn.isAirBlock(pos1) || worldIn.getBlockState(pos1).getBlock().isReplaceable(worldIn, pos1))) {
					return false;
				}
				if (!worldIn.getBlockState(pos1.down()).isTopSolid()) {
					return false;
				}
			}
		}
		
		return true;
	}
	
	@Override
	public EnumPushReaction getMobilityFlag(IBlockState state)
    {
        return EnumPushReaction.IGNORE;
    }
	
	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face)
    {
		if (face == EnumFacing.DOWN) {
			return BlockFaceShape.SOLID;
		}
        return BlockFaceShape.UNDEFINED;
    }


    /**
     * Used to determine ambient occlusion and culling when rebuilding chunks for render
    */
	@Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

	@Override
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    /**
     * Determines if an entity can path through this block
     */
	@Override
    public boolean isPassable(IBlockAccess worldIn, BlockPos pos)
    {
        return true;
    }

    /**
     * Return true if an entity can be spawned inside the block (used to get the player's bed spawn location)
     */
	@Override
    public boolean canSpawnInBlock()
    {
        return false;
    }

	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
		// TODO Auto-generated method stub
		//System.out.println("sdfg");
		for (int x = -1; x <= 1; x++) {
			for (int z = -1; z<=1;z++) {
				BlockPos pos1 = new BlockPos(pos.getX()+x, pos.getY(), pos.getZ()+z);
				if (x == 0 && z == 0) continue;
				if (!(worldIn.getBlockState(pos1).getBlock() instanceof BlockPentaExtend)) {
					worldIn.setBlockToAir(pos);
				}
				/*if (!(worldIn.getBlockState(pos).isTopSolid() || worldIn.getBlockState(pos).getBlock() instanceof BlockFence)) {
					return false;
				}*/
			}
		}
	    super.neighborChanged(state, worldIn, pos, blockIn, fromPos);
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		
		TileEntityPentacle penta = ((TileEntityPentacle)worldIn.getTileEntity(pos));
		UUID cast = (penta.demon_orb != null ? (penta.demon_orb.hasTagCompound() ? penta.demon_orb.getTagCompound().getUniqueId("Entity") : null) : null);
		Entity en = null;
		if (cast != null) {
			en = CommonProxy.getServer().getEntityFromUuid(cast);
		}
		playerIn.sendStatusMessage(new TextComponentTranslation("pentacle.statusmessage", ((TileEntityPentacle)worldIn.getTileEntity(pos)).getEnergyStored(), (en != null ? en.getDisplayName().getFormattedText() : (new TextComponentTranslation("p.none")).getFormattedText())), true);
		
		return false;
	}
	
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer,
			ItemStack stack) {
		for (int x = -1; x <= 1; x++) {
			for (int z = -1; z<=1;z++) {
				BlockPos pos1 = new BlockPos(pos.getX()+x, pos.getY(), pos.getZ()+z);
				if (x == 0 && z == 0) continue;
				worldIn.setBlockState(pos1, BlockInit.PENTA_EXTEND.getDefaultState());
			}
		}
	}
	
	@Override
	public boolean hasTileEntity(IBlockState state) {
		// TODO Auto-generated method stub
		return true;
	}
	
	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		// TODO Auto-generated method stub
		return new TileEntityPentacle();
	}
	
	
	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		TileEntityPentacle te = (TileEntityPentacle)worldIn.getTileEntity(pos);
		if (te.demon != null) {
			te.demon.setInPentacle(false);
			te.demonDismiss();

			te.demon = null;

		}
		if (te.getOrb_render() != null) {
			te.getOrb_render().setDead();
			te.orb_render = null;
		}
		
		super.breakBlock(worldIn, pos, state);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer() {
		// TODO Auto-generated method stub
		return BlockRenderLayer.CUTOUT;
	}
	
	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
		// TODO Auto-generated method stub
		return NULL_AABB;
	}
	
}
