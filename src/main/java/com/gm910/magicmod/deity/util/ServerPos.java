package com.gm910.magicmod.deity.util;

import com.gm910.magicmod.handling.util.TextUtil;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.common.DimensionManager;

public class ServerPos extends BlockPos {
	
	private int dimension = 0;
	private String name = "";

	public ServerPos(int x, int y, int z) {
		super(x, y, z);
		
	}
	
	public ServerPos() {
		this(0,0,0,0);
	}
	
	private ServerPos(double x, double y, double z, int d, String name) {
		this(x, y, z);
		this.name = name;
	}
	
	public ServerPos(NBTTagCompound comp) {
		this(comp.getDouble("X"), comp.getDouble("Y"), comp.getDouble("Z"), comp.getInteger("D"), comp.getString("Name"));
	}
	
	public ServerPos setName(String name) {
		return new ServerPos(this.getX(), this.getY(), this.getZ(), this.getD(), name);
	}
	
	public String getName() {
		return name;
	}
	
	public boolean equalsWithoutName(Object e) {
		if (e instanceof BlockPos) {
			return this.getPos().equals(e);
		} else if (e instanceof ServerPos) {
			return this.getPos().equals(((ServerPos) e).getPos()) && this.getD() == ((ServerPos) e).getD();
		} else {
			return false;
		}
	}
	
	@Override
	public boolean equals(Object e) {
		if (e instanceof BlockPos) {
			return this.getPos().equals(e);
		} else if (e instanceof ServerPos) {
			return this.getPos().equals(((ServerPos) e).getPos()) && this.getD() == ((ServerPos) e).getD() && this.name.equals(((ServerPos) e).getName());
		} else {
			return false;
		}
	}

	public ServerPos(double x, double y, double z)
    {
        super(x, y, z);
    }

    public ServerPos(Entity source)
    {
        super(source);
    }

    public ServerPos(Vec3d vec)
    {
        super(vec);
    }

    public ServerPos(Vec3i source)
    {
        super(source);
    }
    
    public ServerPos(int x, int y, int z, int d) {
		super(x, y, z);
		dimension = d;
	}

	public ServerPos(double x, double y, double z, int d)
    {
        super(x, y, z);
        dimension = d;
    }


    public ServerPos(Vec3d vec, int d)
    {
        super(vec);
        dimension = d;
    }

    public ServerPos(Vec3i source, int d)
    {
        super(source);
        dimension = d;
    }
    
    public ServerPos(BlockPos pos, int d) {
    	this(pos.getX(), pos.getY(), pos.getZ(), d);
    }
    
    public ServerPos(ServerPos pos, int d) {
    	this(pos.getX(), pos.getY(), pos.getZ(), d);
    }
    
    public ServerPos setDimension(int dimension) {
		return new ServerPos(this, dimension);
	}
    
    public int getDimension() {
    	return dimension;
    }
    
    public int getD() {
    	return getDimension();
    }
    
    public ServerPos setD(int d) {
    	return setDimension(d);
    }
	
    public NBTTagCompound toNBT() {
    	NBTTagCompound comp = new NBTTagCompound();
    	comp.setDouble("X", this.getX());
    	comp.setDouble("Y", this.getY());
    	comp.setDouble("Z", this.getZ());
    	comp.setInteger("D", this.getD());
    	comp.setString("Name", this.name);
    	return comp;
    }
    
    public static ServerPos fromNBT(NBTTagCompound comp) {
    	return new ServerPos(comp);
    }
    
    public static NBTTagCompound BPtoNBT(BlockPos pos) {
    	NBTTagCompound comp = new NBTTagCompound();
    	comp.setDouble("X", pos.getX());
    	comp.setDouble("Y", pos.getY());
    	comp.setDouble("Z", pos.getZ());
    	return comp;
    }
    
    public static BlockPos BPfromNBT(NBTTagCompound comp) {
    	return new BlockPos(comp.getDouble("X"), comp.getDouble("Y"), comp.getDouble("Z"));
    }
    
	public BlockPos getPos() {
		return new BlockPos(this.getX(), this.getY(), this.getZ());
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return toString(false);
	}
	
	public String toString(boolean useDimensionName) {
		return (this.name.isEmpty() ? "" : this.name + ": ") + "{" + this.getX() + ", " + this.getY() + ", " + this.getZ() + "}, " + TextUtil.translation("sp.dim") +" " 
	+ (useDimensionName ? getDimensionName(this.getD()) : this.getD());
	}
    
	public static String getDimensionName(int dimension) {
		return DimensionManager.getProviderType(dimension).getName();
	}


}
