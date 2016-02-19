package matteroverdrive.commands;

import net.minecraft.entity.Entity;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;

/**
 * Created by Simeon on 2/5/2016.
 */
public class AbsoluteDimensionTeleporter extends Teleporter
{
    WorldServer worldServer;

    public AbsoluteDimensionTeleporter(WorldServer worldIn)
    {
        super(worldIn);
        this.worldServer = worldIn;
    }

    @Override
    public void placeInPortal(Entity entityIn, float rotationYaw)
    {
        entityIn.setLocationAndAngles(entityIn.posX,entityIn.posY,entityIn.posZ, entityIn.rotationYaw, 0.0F);
        entityIn.motionX = entityIn.motionY = entityIn.motionZ = 0.0D;
    }
}
