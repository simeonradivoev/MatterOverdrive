package matteroverdrive.handler.village;

import cpw.mods.fml.common.registry.VillagerRegistry;
import matteroverdrive.init.MatterOverdriveItems;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;

import java.util.Random;

/**
 * Created by Simeon on 5/30/2015.
 */
public class TradeHandlerMadScientist implements VillagerRegistry.IVillageTradeHandler
{
    @Override
    public void manipulateTradesForVillager(EntityVillager villager, MerchantRecipeList recipeList, Random random)
    {
        if (villager.getProfession() == 666)
        {
            addSellPart(recipeList, random, 0, 0.5f, 16 * 2, 8 * 2);
            addSellPart(recipeList, random, 1, 0.4f, 18 * 2, 10 * 2);
            addSellPart(recipeList, random, 2, 0.3f, 20 * 2, 12 * 2);
            addSellPart(recipeList, random, 3, 0.2f, 32 * 2, 8 * 2);

            addSellItemStack(recipeList,random,new ItemStack(MatterOverdriveItems.androidPill,1,1),1,16,16);
            addSellItemStack(recipeList, random, new ItemStack(MatterOverdriveItems.h_compensator), 0.5f, 32, 18);
            addBuyItemStack(recipeList, random, new ItemStack(MatterOverdriveItems.dilithium_ctystal), 1, 8, 8);
            addSellItemStack(recipeList, random, new ItemStack(MatterOverdriveItems.weapon_module_barrel, 1, 0), 1, 8, 4);
            addSellItemStack(recipeList, random, new ItemStack(MatterOverdriveItems.weapon_module_barrel, 1, 1), 1, 8, 4);
            addSellItemStack(recipeList, random, new ItemStack(MatterOverdriveItems.weapon_module_barrel, 1, 2), 1, 16, 16);
            addSellItemStack(recipeList, random, new ItemStack(MatterOverdriveItems.earl_gray_tea), 1, 3, 2);
        }
    }

    public void addBuyPart(MerchantRecipeList recipeList,Random random,int part,float chance,int price,int variation)
    {
         if (random.nextFloat() < chance)
         {
             MerchantRecipe recipe = new MerchantRecipe(new ItemStack(MatterOverdriveItems.androidParts,1,part),null,new ItemStack(Items.emerald,price + random.nextInt(variation)));
             recipeList.add(recipe);
         }
    }

    public void addSellPart(MerchantRecipeList recipeList,Random random,int part,float chance,int price,int variation)
    {
        if (random.nextFloat() < chance)
        {
            MerchantRecipe recipe = new MerchantRecipe(new ItemStack(Items.emerald,price + random.nextInt(variation)),null,new ItemStack(MatterOverdriveItems.androidParts,1,part));
            recipeList.add(recipe);
        }
    }

    public void addBuyItemStack(MerchantRecipeList recipeList,Random random,ItemStack stack,float chance,int price,int variation)
    {
        if (random.nextFloat() < chance)
        {
            MerchantRecipe recipe = new MerchantRecipe(stack,null,new ItemStack(Items.emerald,price + random.nextInt(variation)));
            recipeList.add(recipe);
        }
    }

    public void addSellItemStack(MerchantRecipeList recipeList,Random random,ItemStack stack,float chance,int price,int variation)
    {
        if (random.nextFloat() < chance)
        {
            MerchantRecipe recipe = new MerchantRecipe(new ItemStack(Items.emerald,price + random.nextInt(variation)),null,stack);
            recipeList.add(recipe);
        }
    }
}
