package com.gm910.magicmod.world.biome;

import java.util.Random;

import com.gm910.magicmod.entity.classes.demons.EntityAzraelite;
import com.gm910.magicmod.entity.classes.demons.EntityCacodemon;
import com.gm910.magicmod.entity.classes.demons.EntityForgeDemon;
import com.gm910.magicmod.entity.classes.demons.EntityGoeturge;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.feature.WorldGenMinable;

public class BiomeSheol extends Biome {

	public BiomeSheol() {
		super(new BiomeProperties("Sheol").setBaseHeight(0.4F).setHeightVariation(3F).setTemperature(2.0F).setRainfall(0.0F).setRainDisabled().setWaterColor(12268084));
		
		topBlock = Blocks.COAL_BLOCK.getDefaultState();
		fillerBlock = Blocks.MAGMA.getDefaultState();
		
		
		this.decorator.coalGen = new WorldGenMinable(Blocks.LAVA.getDefaultState(), 10);
		this.decorator.treesPerChunk = 0;
		
		this.spawnableCaveCreatureList.clear();
		this.spawnableCreatureList.clear();
		this.spawnableMonsterList.clear();
		this.spawnableWaterCreatureList.clear();
		
		this.flowers.clear();
		this.addFlower(Blocks.FIRE.getDefaultState(), 10);
		
		this.spawnableCreatureList.add(new SpawnListEntry(EntityForgeDemon.class, 80, 1, 5));
		this.spawnableCreatureList.add(new SpawnListEntry(EntityCacodemon.class, 80, 1, 5));
		this.spawnableCreatureList.add(new SpawnListEntry(EntityGoeturge.class, 80, 1, 5));
		this.spawnableCreatureList.add(new SpawnListEntry(EntityAzraelite.class, 80, 1, 5));
	}
	
	@Override
	public boolean canRain() {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public int getSkyColorByTemp(float currentTemperature) {
		
		return 0x770000;
	}
	
	
	
	@Override
	public int getFoliageColorAtPos(BlockPos pos) {
		// TODO Auto-generated method stub
		return (new Random()).nextInt(0x770000);
	}
	@Override
	public int getGrassColorAtPos(BlockPos pos) {
		
		return 0x770000;//(new Random()).nextInt(0xFFFFFF);
	}

	@Override
	public int getWaterColorMultiplier() {
		// TODO Auto-generated method stub
		return 0x770000;
	}
	
	@Override
	public void genTerrainBlocks(World worldIn, Random rand, ChunkPrimer chunkPrimerIn, int x, int z, double noiseVal) {
		int i = worldIn.getSeaLevel();
        IBlockState iblockstate = this.topBlock;
        IBlockState iblockstate1 = this.fillerBlock;
        int j = -1;
        int k = (int)(noiseVal / 3.0D + 3.0D + rand.nextDouble() * 0.25D);
        int l = x & 15;
        int i1 = z & 15;
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

        for (int j1 = 255; j1 >= 0; --j1)
        {
            if (j1 <= rand.nextInt(5))
            {
                chunkPrimerIn.setBlockState(i1, j1, l, AIR);
            }
            else
            {
                IBlockState iblockstate2 = chunkPrimerIn.getBlockState(i1, j1, l);

                if (iblockstate2.getMaterial() == Material.AIR)
                {
                    j = -1;
                }
                else if (iblockstate2.getBlock() == Blocks.STONE)
                {
                	
                    if (j == -1)
                    {
                        if (k <= 0)
                        {
                            iblockstate = Blocks.LAVA.getDefaultState();
                            iblockstate1 = Blocks.LAVA.getDefaultState();
                        }
                        else if (j1 >= i - 4 && j1 <= i + 1)
                        {
                            iblockstate = this.topBlock;
                            iblockstate1 = this.fillerBlock;
                        }

                        if (j1 < i && (iblockstate == null || iblockstate.getMaterial() == Material.AIR))
                        {
                            if (this.getTemperature(blockpos$mutableblockpos.setPos(x, j1, z)) < 1.2F)
                            {
                                iblockstate = Blocks.FIRE.getDefaultState();
                            }
                            else
                            {
                                iblockstate = Blocks.LAVA.getDefaultState();
                            }
                        }

                        j = k;

                        if (j1 >= i - 1)
                        {
                            chunkPrimerIn.setBlockState(i1, j1, l, iblockstate);
                        }
                        else if (j1 < i - 7 - k)
                        {
                            iblockstate = AIR;
                            iblockstate1 = AIR;
                            chunkPrimerIn.setBlockState(i1, j1, l, Blocks.AIR.getDefaultState());
                        }
                        else
                        {
                            chunkPrimerIn.setBlockState(i1, j1, l, iblockstate1);
                        }
                    }
                    else if (j > 0)
                    {
                        --j;
                        chunkPrimerIn.setBlockState(i1, j1, l, iblockstate1);

                        if (j == 0 && iblockstate1.getBlock() == Blocks.STONE && k > 1)
                        {
                            j = rand.nextInt(4) + Math.max(0, j1 - 63);
                            iblockstate1 = Blocks.LAVA.getDefaultState();
                        }
                    }
                    else chunkPrimerIn.setBlockState(i1, j1, l, Blocks.LAVA.getDefaultState());
                }
            }
        }
	}
	
}
