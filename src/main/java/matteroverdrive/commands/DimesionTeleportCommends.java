package matteroverdrive.commands;

import net.minecraft.block.state.IBlockState;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;

import java.util.List;

/**
 * Created by Simeon on 2/5/2016.
 */
public class DimesionTeleportCommends extends CommandBase
{
	@Override
	public String getCommandName()
	{
		return "tpx";
	}

	@Override
	public String getCommandUsage(ICommandSender sender)
	{
		return "tpx <x> <y> <z> <dim>";
	}

	public int getRequiredPermissionLevel()
	{
		return 2;
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
	{
		if (args.length < 1)
		{
			throw new WrongUsageException("commands.tp.usage", new Object[0]);
		}
		else
		{
			int i = 0;
			Entity entity;

			if (args.length != 2 && args.length != 5 && args.length != 7)
			{
				entity = getCommandSenderAsPlayer(sender);
			}
			else
			{
				entity = getEntity(server, sender, args[0]);
				i = 1;
			}

            /*if (args.length != 1 && args.length != 2)
			{
                if (args.length < i + 3)
                {
                    throw new WrongUsageException("commands.tp.usage", new Object[0]);
                }
                else if (entity.worldObj != null)
                {
                    int lvt_5_2_ = i + 1;
                    CommandBase.CoordinateArg commandbase$coordinatearg = parseCoordinate(entity.posX, args[i], true);
                    CommandBase.CoordinateArg commandbase$coordinatearg1 = parseCoordinate(entity.posY, args[lvt_5_2_++], 0, 0, false);
                    CommandBase.CoordinateArg commandbase$coordinatearg2 = parseCoordinate(entity.posZ, args[lvt_5_2_++], true);
                    int dimension = args.length > lvt_5_2_ ? parseInt(args[lvt_5_2_++]) : entity.dimension;
                    CommandBase.CoordinateArg commandbase$coordinatearg3 = parseCoordinate((double)entity.rotationYaw, args.length > lvt_5_2_ ? args[lvt_5_2_++] : "~", false);
                    CommandBase.CoordinateArg commandbase$coordinatearg4 = parseCoordinate((double)entity.rotationPitch, args.length > lvt_5_2_ ? args[lvt_5_2_] : "~", false);

                    if (entity instanceof EntityPlayerMP)
                    {
                        Set<SPacketPlayerPosLook.EnumFlags> set = EnumSet.<SPacketPlayerPosLook.EnumFlags>noneOf(SPacketPlayerPosLook.EnumFlags.class);

                        if (commandbase$coordinatearg.func_179630_c())
                        {
                            set.add(SPacketPlayerPosLook.EnumFlags.X);
                        }

                        if (commandbase$coordinatearg1.func_179630_c())
                        {
                            set.add(SPacketPlayerPosLook.EnumFlags.Y);
                        }

                        if (commandbase$coordinatearg2.func_179630_c())
                        {
                            set.add(SPacketPlayerPosLook.EnumFlags.Z);
                        }

                        if (commandbase$coordinatearg4.func_179630_c())
                        {
                            set.add(SPacketPlayerPosLook.EnumFlags.X_ROT);
                        }

                        if (commandbase$coordinatearg3.func_179630_c())
                        {
                            set.add(SPacketPlayerPosLook.EnumFlags.Y_ROT);
                        }

                        float f = (float)commandbase$coordinatearg3.func_179629_b();

                        if (!commandbase$coordinatearg3.func_179630_c())
                        {
                            f = MathHelper.wrapAngleTo180_float(f);
                        }

                        float f1 = (float)commandbase$coordinatearg4.func_179629_b();

                        if (!commandbase$coordinatearg4.func_179630_c())
                        {
                            f1 = MathHelper.wrapAngleTo180_float(f1);
                        }

                        if (f1 > 90.0F || f1 < -90.0F)
                        {
                            f1 = MathHelper.wrapAngleTo180_float(180.0F - f1);
                            f = MathHelper.wrapAngleTo180_float(f + 180.0F);
                        }

                        entity.mountEntity((Entity)null);
                        ((EntityPlayerMP)entity).playerNetServerHandler.setPlayerLocation(commandbase$coordinatearg.func_179629_b(), commandbase$coordinatearg1.func_179629_b(), commandbase$coordinatearg2.func_179629_b(), f, f1, set);
                        entity.setRotationYawHead(f);
                        if (dimension != entity.dimension)
                            travelToDimension((EntityPlayerMP)entity,dimension);
                    }
                    else
                    {
                        float f2 = (float)MathHelper.wrapAngleTo180_double(commandbase$coordinatearg3.func_179628_a());
                        float f3 = (float)MathHelper.wrapAngleTo180_double(commandbase$coordinatearg4.func_179628_a());

                        if (f3 > 90.0F || f3 < -90.0F)
                        {
                            f3 = MathHelper.wrapAngleTo180_float(180.0F - f3);
                            f2 = MathHelper.wrapAngleTo180_float(f2 + 180.0F);
                        }

                        entity.setLocationAndAngles(commandbase$coordinatearg.func_179628_a(), commandbase$coordinatearg1.func_179628_a(), commandbase$coordinatearg2.func_179628_a(), f2, f3);
                        entity.setRotationYawHead(f2);
                        //if (dimension != entity.dimension)
                            //travelToDimension((EntityPlayerMP)entity,dimension);
                    }

                    notifyOperators(sender, this, "commands.tp.success.coordinates", new Object[] {entity.getSpaceBodyName(), Double.valueOf(commandbase$coordinatearg.func_179628_a()), Double.valueOf(commandbase$coordinatearg1.func_179628_a()), Double.valueOf(commandbase$coordinatearg2.func_179628_a())});
                }
            }
            else
            {
                Entity entity1 = func_175768_b(sender, args[args.length - 1]);

                if (entity1.worldObj != entity.worldObj)
                {
                    throw new CommandException("commands.tp.notSameDimension", new Object[0]);
                }
                else
                {
                    entity.dismountRidingEntity();

                    if (entity instanceof EntityPlayerMP)
                    {
                        ((EntityPlayerMP)entity).playerNetServerHandler.setPlayerLocation(entity1.posX, entity1.posY, entity1.posZ, entity1.rotationYaw, entity1.rotationPitch);
                    }
                    else
                    {
                        entity.setLocationAndAngles(entity1.posX, entity1.posY, entity1.posZ, entity1.rotationYaw, entity1.rotationPitch);
                    }

                    notifyOperators(sender, this, "commands.tp.success", new Object[] {entity.getSpaceBodyName(), entity1.getSpaceBodyName()});
                }
            }*/
		}
	}

	public void travelToDimension(EntityPlayerMP theEntity, int dimensionId)
	{
		if (!net.minecraftforge.common.ForgeHooks.onTravelToDimension(theEntity, dimensionId))
		{
			return;
		}

		WorldServer fromWorld = theEntity.mcServer.worldServerForDimension(theEntity.dimension);
		WorldServer toWorld = theEntity.mcServer.worldServerForDimension(dimensionId);

		if (theEntity.dimension == 1 && dimensionId == 1)
		{
			//theEntity.triggerAchievement(AchievementList.theEnd2);
			//theEntity.worldObj.removeEntity(theEntity);
			//theEntity.playerNetServerHandler.sendPacket(new S2BPacketChangeGameState(4, 0.0F));
		}
		else
		{
			if (theEntity.dimension == 0 && dimensionId == 1)
			{
				//theEntity.triggerAchievement(AchievementList.theEnd);
				BlockPos blockpos = theEntity.mcServer.worldServerForDimension(dimensionId).getSpawnCoordinate();

				if (blockpos != null)
				{
					theEntity.playerNetServerHandler.setPlayerLocation((double)blockpos.getX(), (double)blockpos.getY(), (double)blockpos.getZ(), 0.0F, 0.0F);
				}

				dimensionId = 1;
			}
			else
			{
				//theEntity.triggerAchievement(AchievementList.portal);
			}

			IBlockState[][][] states = new IBlockState[8][8][8];
			NBTTagCompound[][][] theEntitiesData = new NBTTagCompound[8][8][8];

			for (int x = 0; x < 8; x++)
			{
				for (int y = 0; y < 8; y++)
				{
					for (int z = 0; z < 8; z++)
					{
						BlockPos pos = new BlockPos(theEntity.posX + x - 4, theEntity.posY + y - 4, theEntity.posZ + z - 4);
						TileEntity tileEntity = fromWorld.getTileEntity(pos);
						if (tileEntity != null)
						{
							theEntitiesData[x][y][z] = new NBTTagCompound();
							tileEntity.writeToNBT(theEntitiesData[x][y][z]);
						}
						fromWorld.removeTileEntity(pos);
						states[x][y][z] = fromWorld.getBlockState(pos);
						fromWorld.setBlockToAir(pos);
					}
				}
			}

			for (int x = 0; x < 8; x++)
			{
				for (int y = 0; y < 8; y++)
				{
					for (int z = 0; z < 8; z++)
					{
						BlockPos pos = new BlockPos(theEntity.posX + x - 4, theEntity.posY + y - 4, theEntity.posZ + z - 4);
						toWorld.setBlockState(pos, states[x][y][z]);
						TileEntity tileEntity = toWorld.getTileEntity(pos);
						if (tileEntity != null && theEntitiesData[x][y][z] != null)
						{
							tileEntity.readFromNBT(theEntitiesData[x][y][z]);
						}
					}
				}
			}

			AbsoluteDimensionTeleporter absoluteDimensionTeleporter = new AbsoluteDimensionTeleporter(theEntity.getServerForPlayer());
			// TODO: 3/26/2016 Find how to access configuration manager
			//theEntity.mcServer.getConfigurationManager().transferPlayerToDimension(theEntity, dimensionId,absoluteDimensionTeleporter);
		}
	}

	public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos)
	{
		return args.length != 1 && args.length != 2 ? null : getListOfStringsMatchingLastWord(args, sender.getServer().getAllUsernames());
	}

	/**
	 * Return whether the specified command parameter index is a username parameter.
	 */
	public boolean isUsernameIndex(String[] args, int index)
	{
		return index == 0;
	}
}
