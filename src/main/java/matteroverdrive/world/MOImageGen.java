/*
 * This file is part of Matter Overdrive
 * Copyright (c) 2015., Simeon Radivoev, All rights reserved.
 *
 * Matter Overdrive is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Matter Overdrive is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Matter Overdrive.  If not, see <http://www.gnu.org/licenses>.
 */

package matteroverdrive.world;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * Created by Simeon on 11/17/2015.
 */
public abstract class MOImageGen
{
    private HashMap<Integer,BlockMapping> blockMap;
    protected ResourceLocation texture;
    private List<int[][]> layers;
    private int textureWidth;
    private int textureHeight;
    private int layerCount;
    protected int placeNotify;
    protected final int layerSize;

    public MOImageGen(ResourceLocation texture,int layerSize)
    {
        blockMap = new HashMap<>();
        this.layerSize = layerSize;
        setTexture(texture);
    }

    public void placeBlock(World world,int color,int x,int y,int z,int layer,Random random)
    {
        Block block = getBlockFromColor(color,random);
        int meta = getMetaFromColor(color);
        if (block != null)
        {
            world.setBlock(x, y, z, block, meta, placeNotify);
            onBlockPlace(world,block,x,y,z,random,color);
        }
    }

    public abstract void onBlockPlace(World world,Block block,int x,int y,int z,Random random,int color);

    public Block getBlockFromColor(int color,Random random)
    {
        BlockMapping blockMapping = blockMap.get(color & 0xffffff);
        if (blockMapping != null) {
            return blockMapping.getBlock(random);
        }
        return null;
    }

    public int getMetaFromColor(int color)
    {
        return 0;
    }

    public void generateFromImage(World world,Random random,int startX,int startY,int startZ)
    {
        for (BlockMapping blockMapping : blockMap.values())
        {
            blockMapping.reset(random);
        }
        for (int layer = 0; layer < layerCount; layer++) {
            for (int x = 0; x < layerSize; x++) {
                for (int z = 0; z < layerSize; z++) {

                    placeBlock(world, layers.get(layer)[x][z], startX + x, startY + layer, startZ + z, layer,random);
                }
            }
        }
    }

    public boolean isOnSolidGround(World world,int x,int y,int z,int leaway)
    {
        return isPointOnSolidGround(world,x,y,z,leaway) && isPointOnSolidGround(world,x+layerSize,y,z,leaway) && isPointOnSolidGround(world,x+layerSize,y,z+layerSize,leaway) && isPointOnSolidGround(world,x,y,z+layerSize,leaway);
    }

    public boolean isPointOnSolidGround(World world,int x,int y,int z,int leaway)
    {
        for (int i = 0; i < leaway;i++)
        {
            if (isBlockSolid(world,x,y-i,z))
            {
                return true;
            }
        }
        return false;
    }

    public boolean canFit(World world,int x,int y,int z)
    {
        return !isBlockSolid(world,x,y+layerCount,z) && !isBlockSolid(world,x+layerSize,y+layerCount,z) && !isBlockSolid(world,x+layerSize,y+layerCount,z+layerSize) && !isBlockSolid(world,x,y+layerCount,z+layerSize);
    }

    public boolean isBlockSolid(World world,int x,int y,int z)
    {
        Block block = world.getBlock(x,y,z);
        if (block == Blocks.log || block == Blocks.log2 && block == Blocks.leaves2 || block == Blocks.leaves)
        {
            return false;
        }
        return block.isBlockSolid(world,x,y,z,ForgeDirection.UP.ordinal());
    }

    private boolean inAirFloatRange(World world,int x,int y,int z,int maxAirRange)
    {
        for (int i = 0; i < maxAirRange;i++)
        {
            if (isBlockSolid(world,x,y-i,z) && !isBlockSolid(world,x,y-i+1,z))
            {
                return true;
            }
        }
        return false;
    }

    private void loadTexture(ResourceLocation textureLocation) throws RuntimeException {
        try {

            String path = "/assets/"+textureLocation.getResourceDomain()+"/" + textureLocation.getResourcePath();
            InputStream imageStream = getClass().getResourceAsStream(path);
            BufferedImage image = ImageIO.read(imageStream);

            textureWidth = image.getWidth();
            textureHeight = image.getHeight();
            if (textureWidth % layerSize != 0) {
                throw new RuntimeException(String.format("Texture is not a multiple of the layer size: %s", layerSize));
            }
            layerCount = (image.getWidth() / layerSize) * (image.getHeight() / layerSize);
            for (int i = 0; i < layerCount; i++) {
                layers.add(new int[layerSize][layerSize]);
            }
            convertTo2DWithoutUsingGetRGB(image);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void convertTo2DWithoutUsingGetRGB(BufferedImage image) {

        final byte[] pixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        final boolean hasAlphaChannel = image.getAlphaRaster() != null;

        if (hasAlphaChannel) {
            final int pixelLength = 4;
            for (int pixel = 0, row = 0, col = 0; pixel < pixels.length; pixel += pixelLength) {
                int argb = 0;
                argb += (((int) pixels[pixel] & 0xff) << 24); // alpha
                argb += ((int) pixels[pixel + 1] & 0xff); // blue
                argb += (((int) pixels[pixel + 2] & 0xff) << 8); // green
                argb += (((int) pixels[pixel + 3] & 0xff) << 16); // red
                int layerIndex = Math.floorDiv(col,layerSize) + ((textureWidth/layerSize) * (Math.floorDiv(row,layerSize)));
                layers.get(layerIndex)[col % layerSize][row % layerSize] = argb;
                col++;
                if (col == textureWidth) {
                    col = 0;
                    row++;
                }
            }
        } else {
            final int pixelLength = 3;
            for (int pixel = 0, row = 0, col = 0; pixel < pixels.length; pixel += pixelLength) {
                int argb = 0;
                argb += -16777216; // 255 alpha
                argb += ((int) pixels[pixel] & 0xff); // blue
                argb += (((int) pixels[pixel + 1] & 0xff) << 8); // green
                argb += (((int) pixels[pixel + 2] & 0xff) << 16); // red
                int layerIndex = Math.floorDiv(col,layerSize) + ((textureWidth/layerSize) * (Math.floorDiv(row,layerSize)));
                layers.get(layerIndex)[col % layerSize][row % layerSize] = argb;
                col++;
                if (col == textureWidth) {
                    col = 0;
                    row++;
                }
            }
        }
    }

    private static void transpose(int[][] m)
    {
        for (int i = 0; i < m.length; i++) {
            for (int j = i; j < m[0].length; j++) {
                int x = m[i][j];
                m[i][j] = m[j][i];
                m[j][i] = x;
            }
        }
    }

    public static void swapRows(int[][] m) {
        for (int  i = 0, k = m.length - 1; i < k; ++i, --k) {
            int[] x = m[i];
            m[i] = m[k];
            m[k] = x;
        }
    }

    public static void rotateByNinetyToLeft(int[][] m) {
        transpose(m);
        swapRows(m);
    }

    public static void rotateByNinetyToRight(int[][] m) {
        swapRows(m);
        transpose(m);
    }

    public void rotateByNinetyToLeft()
    {
        layers.forEach(MOImageGen::rotateByNinetyToLeft);
    }

    public int getRedFromColor(int color)
    {
        return color >> 16 & 255;
    }

    public int getGreenFromColor(int color)
    {
        return color >> 8 & 255;
    }

    public int getBlueFromColor(int color)
    {
        return color >> 0 & 255;
    }

    public int getAlphaFromColor(int color)
    {
        return color >> 24 & 255;
    }

    public int getColorAt(int x,int y,int layer)
    {
        if (x < textureWidth && y < textureHeight)
        {
            return layers.get(layer)[textureHeight][textureWidth];
        }
        return 0;
    }

    public int getTextureWidth()
    {
        return textureWidth;
    }

    public int getTextureHeight()
    {
        return textureHeight;
    }

    public ResourceLocation getTexture()
    {
        return texture;
    }

    public void addMapping(int color,Block... blocks)
    {
        this.addMapping(color,new BlockMapping(blocks));
    }
    public void addMapping(int color,boolean noise,Block... blocks)
    {
        this.addMapping(color,new BlockMapping(noise,blocks));
    }
    public void addMapping(int color,BlockMapping blockMapping)
    {
        blockMap.put(color,blockMapping);
    }

    public BlockMapping getMapping(int color)
    {
        return blockMap.get(color);
    }

    public void setTexture(ResourceLocation textureLocation)
    {
        this.texture = textureLocation;
        if (layers == null)
            layers = new ArrayList<>();
        else
            layers.clear();
        loadTexture(textureLocation);
    }

    private static class BlockMapping
    {
        private Block[] blocks;
        private boolean noise;
        private int lastSelected;

        public BlockMapping(boolean noise,Block... blocks)
        {
            this.blocks = blocks;
            this.noise = noise;
        }

        public BlockMapping(Block... blocks)
        {
            this.blocks = blocks;
        }

        public void reset(Random random)
        {
            if (!noise)
            {
                lastSelected = random.nextInt(blocks.length);
            }
        }

        public Block getBlock(Random random)
        {
            if (noise)
            {
                return blocks[random.nextInt(blocks.length)];
            }else
            {
                return blocks[lastSelected];
            }
        }

        public Block[] getBlocks()
        {
            return blocks;
        }

        public boolean isNoise()
        {
            return noise;
        }
    }
}
