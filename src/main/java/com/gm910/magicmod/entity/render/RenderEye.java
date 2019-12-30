package com.gm910.magicmod.entity.render;

import javax.annotation.Nullable;

import com.gm910.magicmod.MagicMod;
import com.gm910.magicmod.entity.classes.magical.EntityEye;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderEye extends Render<EntityEye>
{
    public RenderEye(RenderManager manager)
    {
        super(manager);
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    @Nullable
    protected ResourceLocation getEntityTexture(EntityEye entity)
    {
        return new ResourceLocation(MagicMod.MODID + ":textures/block/pentacle.png");
    }
}