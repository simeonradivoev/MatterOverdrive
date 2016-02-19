package matteroverdrive.handler.village;

import net.minecraft.util.EnumFacing;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraftforge.fml.common.registry.VillagerRegistry;
import matteroverdrive.world.MadScientistHouse;
import net.minecraft.util.MathHelper;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraft.world.gen.structure.StructureVillagePieces;

import java.util.List;
import java.util.Random;

/**
 * Created by Simeon on 5/30/2015.
 */
public class VillageCreatationMadScientist implements VillagerRegistry.IVillageCreationHandler
{
    public VillageCreatationMadScientist()
    {
        MapGenStructureIO.registerStructureComponent(MadScientistHouse.class, "ViBHMS");
    }

    @Override
    public StructureVillagePieces.PieceWeight getVillagePieceWeight(Random random, int i)
    {
        return new StructureVillagePieces.PieceWeight(MadScientistHouse.class,20, MathHelper.getRandomIntegerInRange(random, i, 2 + i));
    }

    @Override
    public Class<?> getComponentClass() {
        return MadScientistHouse.class;
    }

    @Override
    public StructureVillagePieces.Village buildComponent(StructureVillagePieces.PieceWeight villagePiece, StructureVillagePieces.Start startPiece, List<StructureComponent> pieces, Random random, int p1, int p2, int p3, EnumFacing facing, int p5)
    {
        return MadScientistHouse.func_74898_a(startPiece,pieces,random,p1,p2,p3,facing,p5);
    }
}
