package matteroverdrive.data.quest.logic;

import cpw.mods.fml.common.eventhandler.Event;
import matteroverdrive.MatterOverdrive;
import matteroverdrive.api.quest.IQuestReward;
import matteroverdrive.api.quest.QuestStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Random;

/**
 * Created by Simeon on 12/30/2015.
 */
public class QuestLogicSpawnMobs extends AbstractQuestLogic
{
    private String customSpawnName;
    private Class<? extends Entity>[] mobClasses;
    private int minSpawnAmount;
    private int maxSpawnAmount;
    private int minSpawnRange;
    private int maxSpawnRange;

    public QuestLogicSpawnMobs(Class<? extends Entity>[] mobClasses,int minSpawnAmount,int maxSpawnAmount)
    {
        this.mobClasses = mobClasses;
        this.minSpawnAmount = minSpawnAmount;
        this.maxSpawnAmount = maxSpawnAmount;
    }

    public QuestLogicSpawnMobs(Class<? extends Entity> mobClass,int minSpawnAmount,int maxSpawnAmount)
    {
        this(new Class[]{mobClass},minSpawnAmount,maxSpawnAmount);
    }

    @Override
    public String modifyInfo(QuestStack questStack, String info)
    {
        info = info.replace("$spawnType",getSpawnName(questStack));
        info = info.replace("$spawnAmount",Integer.toString(getSpawnAmount(questStack)));
        return info;
    }

    @Override
    public boolean isObjectiveCompleted(QuestStack questStack, EntityPlayer entityPlayer, int objectiveIndex)
    {
        return true;
    }

    @Override
    public String modifyObjective(QuestStack questStack, EntityPlayer entityPlayer, String objective, int objectiveIndex)
    {
        objective.replace("$spawnType",getSpawnName(questStack));
        objective = objective.replace("$spawnAmount",Integer.toString(getSpawnAmount(questStack)));
        return objective;
    }

    @Override
    public void initQuestStack(Random random, QuestStack questStack)
    {
        initTag(questStack);
        getTag(questStack).setByte("SpawnType",(byte) random.nextInt(mobClasses.length));
        getTag(questStack).setShort("SpawnAmount",(short) random(random,minSpawnAmount,maxSpawnAmount));
    }

    @Override
    public boolean onEvent(QuestStack questStack, Event event, EntityPlayer entityPlayer)
    {
        return false;
    }

    @Override
    public void onTaken(QuestStack questStack, EntityPlayer entityPlayer)
    {
        int spawnAmount = getSpawnAmount(questStack);
        for (int i = 0;i < spawnAmount;i++)
        {
            Entity entity;
            try
            {
                entity = mobClasses[getSpawnType(questStack)].getConstructor(World.class).newInstance(entityPlayer.worldObj);
                positionSpawn(entity,entityPlayer);
                if (entity instanceof EntityLiving)
                {
                    ((EntityLiving) entity).onSpawnWithEgg(null);
                    if (customSpawnName != null)
                    {
                        ((EntityLiving) entity).setCustomNameTag(customSpawnName);
                    }
                }
                entityPlayer.worldObj.spawnEntityInWorld(entity);

            } catch (InstantiationException e)
            {
                MatterOverdrive.log.error("Count not instantiate entity of type %s",mobClasses[getSpawnType(questStack)]);
                break;
            } catch (IllegalAccessException e)
            {
                MatterOverdrive.log.error("Count not call private constructor for entity of type %s",mobClasses[getSpawnType(questStack)]);
                break;
            } catch (InvocationTargetException e)
            {
                MatterOverdrive.log.error("Count not call constructor for entity of type %s",mobClasses[getSpawnType(questStack)]);
                break;
            } catch (NoSuchMethodException e)
            {
                MatterOverdrive.log.error("Count not find appropriate constructor for entity of type %s",mobClasses[getSpawnType(questStack)]);
                break;
            }
        }
    }

    private void positionSpawn(Entity spawn,EntityPlayer entityPlayer)
    {
        spawn.setPosition(entityPlayer.posX,entityPlayer.posY,entityPlayer.posZ);
    }

    @Override
    public void onCompleted(QuestStack questStack, EntityPlayer entityPlayer)
    {

    }

    @Override
    public void modifyRewards(QuestStack questStack, EntityPlayer entityPlayer, List<IQuestReward> rewards)
    {

    }

    public String getSpawnName(QuestStack questStack)
    {
        return getEntityClassName(mobClasses[getSpawnType(questStack)],"Unknown Spawn");
    }

    public int getSpawnType(QuestStack questStack)
    {
        if (hasTag(questStack))
        {
            return MathHelper.clamp_int(getTag(questStack).getByte("SpawnType"),0,mobClasses.length);
        }
        return 0;
    }

    public int getSpawnAmount(QuestStack questStack)
    {
        if (hasTag(questStack))
        {
            return getTag(questStack).getShort("SpawnAmount");
        }
        return 0;
    }

    public void setCustomSpawnName(String customSpawnName)
    {
        this.customSpawnName = customSpawnName;
    }
}
