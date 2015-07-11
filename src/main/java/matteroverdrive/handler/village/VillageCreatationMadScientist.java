package matteroverdrive.handler.village;

import cpw.mods.fml.common.registry.VillagerRegistry;
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
        MapGenStructureIO.func_143031_a(MadScientistHouse.class, "ViBHMS");
    }

    @Override
    public StructureVillagePieces.PieceWeight getVillagePieceWeight(Random random, int i)
    {
        return new StructureVillagePieces.PieceWeight(MadScientistHouse.class,20, MathHelper.getRandomIntegerInRange(random, 0 + i, 2 + i));
    }

    @Override
    public Class<?> getComponentClass() {
        return MadScientistHouse.class;
    }

    @Override
    public Object buildComponent(StructureVillagePieces.PieceWeight villagePiece, StructureVillagePieces.Start startPiece, List pieces, Random random, int p1, int p2, int p3, int p4, int p5) {
        return MadScientistHouse.func_74898_a(startPiece,pieces,random,p1,p2,p3,p4,p5);
    }
}
