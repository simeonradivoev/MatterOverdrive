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
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraft.world.chunk.IChunkProvider;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.List;

/**
 * Created by Simeon on 11/17/2015.
 */
public abstract class MOImageGen<T extends MOImageGen.ImageGenWorker>
{
    public static final HashMap<Block,Integer> worldGenerationBlockColors = new HashMap<>();
    private final HashMap<Integer,BlockMapping> blockMap;
    protected ResourceLocation texture;
    private List<int[][]> layers;
    private int textureWidth;
    private int textureHeight;
    private int layerCount;
    protected int placeNotify;
    protected final int layerWidth;
    protected final int layerHeight;
    protected final Random localRandom;

    public MOImageGen(ResourceLocation texture,int layerWidth,int layerHeight)
    {
        localRandom = new Random();
        blockMap = new HashMap<>();
        this.layerWidth = layerWidth;
        this.layerHeight = layerHeight;
        setTexture(texture);
    }

    public void placeBlock(World world, int color, BlockPos pos, int layer, Random random, int placeNotify,T worker)
    {
        IBlockState block = getBlockFromColor(color,random);
        if (block != null)
        {
            world.setBlockState(pos, block, placeNotify);
            onBlockPlace(world,block,pos,random,color,worker);
        }
    }

    public abstract void onBlockPlace(World world,IBlockState block,BlockPos pos,Random random,int color,T worker);

    public IBlockState getBlockFromColor(int color, Random random)
    {
        BlockMapping blockMapping = blockMap.get(color & 0xffffff);
        if (blockMapping != null) {
            return blockMapping.getBlock(random).getStateFromMeta(getMetaFromColor(color,random));
        }
        return null;
    }

    public int getMetaFromColor(int color,Random random)
    {
        return 0;
    }

    public void generateFromImage(World world, Random random, BlockPos start, int layer, int placeNotify, T worker)
    {
        if (layers != null && layers.size() > 0)
        {
            for (BlockMapping blockMapping : blockMap.values())
            {
                blockMapping.reset(localRandom);
            }
            generateFromImage(world, random, new BlockPos(start.getX(),Math.min(start.getY(), world.getHeight() - layerCount),start.getZ()), layers, layer, placeNotify,worker);
        }
    }

    public void generateFromImage(World world, Random random, BlockPos start,List<int[][]> layers,int layer,int placeNotify,T worker)
    {
        for (int x = 0; x < layerWidth; x++) {
            for (int z = 0; z < layerHeight; z++) {

                placeBlock(world, layers.get(layer)[x][z], start.add(x,layer,z), layer,random,placeNotify,worker);
            }
        }
    }

    public static void generateFromImage(World world, BlockPos start,int layerWidth,int layerHeight,List<int[][]> layers,Map<Integer,IBlockState> blockMap)
    {
        for (int layer = 0; layer < layers.size(); layer++) {
            for (int x = 0; x < layerWidth; x++) {
                for (int z = 0; z < layerHeight; z++) {

                    int color = layers.get(layer)[x][z];
                    Color c = new Color(color,true);
                    IBlockState block = blockMap.get(color & 0xffffff);
                    if (block != null)
                    {
                        world.setBlockState(start.add(x,layer,z), block, 2);
                    }
                }
            }
        }
    }

    public boolean isOnSolidGround(World world,BlockPos pos,int leaway)
    {
        return isPointOnSolidGround(world,pos,leaway) && isPointOnSolidGround(world,pos.add(layerWidth,0,0),leaway) && isPointOnSolidGround(world,pos.add(layerWidth,0,layerHeight),leaway) && isPointOnSolidGround(world,pos.add(0,0,layerHeight),leaway);
    }

    public boolean isPointOnSolidGround(World world,BlockPos pos,int leaway)
    {
        for (int i = 0; i < leaway;i++)
        {
            if (isBlockSolid(world,pos.add(0,-i,0)))
            {
                return true;
            }
        }
        return false;
    }

    public boolean canFit(World world,BlockPos pos)
    {
        return !isBlockSolid(world,pos.add(0,layerCount,0)) && !isBlockSolid(world,pos.add(layerCount,layerCount,0)) && !isBlockSolid(world,pos.add(layerCount,layerCount,layerCount)) && !isBlockSolid(world,pos.add(0,layerCount,layerCount));
    }

    public boolean isBlockSolid(World world,BlockPos pos)
    {
        IBlockState block = world.getBlockState(pos);
        if (block.getBlock() == Blocks.log || block.getBlock() == Blocks.log2 && block.getBlock() == Blocks.leaves2 || block.getBlock() == Blocks.leaves)
        {
            return false;
        }
        return block.getBlock().isBlockSolid(world,pos, EnumFacing.UP);
    }

    private boolean inAirFloatRange(World world,BlockPos pos,int maxAirRange)
    {
        for (int i = 0; i < maxAirRange;i++)
        {
            if (isBlockSolid(world,pos.add(0,-i,0)) && !isBlockSolid(world,pos.add(0,-i+1,0)))
            {
                return true;
            }
        }
        return false;
    }

    protected boolean colorsMatch(int color0,int color1)
    {
        return (color0 & 0xffffff) == (color1 & 0xffffff);
    }

    public void manageTextureLoading()
    {
        if (layers == null || layers.size() == 0)
        {
            loadTexture(getTexture());
        }
    }

    private void loadTexture(ResourceLocation textureLocation) throws RuntimeException {
        try {

            String path = "/assets/"+textureLocation.getResourceDomain()+"/" + textureLocation.getResourcePath();
            InputStream imageStream = getClass().getResourceAsStream(path);
            BufferedImage image = ImageIO.read(imageStream);

            textureWidth = image.getWidth();
            textureHeight = image.getHeight();
            layerCount = (image.getWidth() / layerWidth) * (image.getHeight() / layerHeight);
            for (int i = 0; i < layerCount; i++) {
                layers.add(new int[layerWidth][layerHeight]);
            }
            convertTo2DWithoutUsingGetRGB(image,layerWidth,layerHeight,textureWidth,layers);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<int[][]> loadTexture(File textureLocation, int layerWidth,int layerHeight) {
        try {
            BufferedImage image = ImageIO.read(textureLocation);

            int textureWidth = image.getWidth();
            int layerCount = (image.getWidth() / layerWidth) * (image.getHeight() / layerHeight);
            List<int[][]> layers = new ArrayList<>();
            for (int i = 0; i < layerCount; i++) {
                layers.add(new int[layerWidth][layerHeight]);
            }
            convertTo2DWithoutUsingGetRGB(image,layerWidth,layerHeight,textureWidth,layers);
            return layers;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void convertTo2DWithoutUsingGetRGB(BufferedImage image,int layerWidth,int layerHeight,int textureWidth,List<int[][]> layers) {

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
                int layerIndex = Math.floorDiv(col,layerWidth) + ((textureWidth/layerWidth) * (Math.floorDiv(row,layerHeight)));
                layers.get(layerIndex)[col % layerWidth][row % layerHeight] = argb;
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
                int layerIndex = Math.floorDiv(col,layerWidth) + ((textureWidth/layerWidth) * (Math.floorDiv(row,layerHeight)));
                layers.get(layerIndex)[col % layerWidth][row % layerHeight] = argb;
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
        return color & 255;
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

    public int getLayerCount(){return layerCount;}

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
    }

    public static class BlockMapping
    {
        private final Block[] blocks;
        private boolean noise;
        private int lastSelected;

        public BlockMapping(boolean noise,Block... blocks)
        {
            this(blocks);
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

    public abstract void onGenerationWorkerCreated(T worker);
    public abstract T getNewWorkerInstance();

    public T createWorker(Random random, BlockPos pos, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider)
    {
        T worker = getNewWorkerInstance();
        worker.init(random,pos,world,chunkGenerator,chunkProvider);
        onGenerationWorkerCreated(worker);
        return worker;
    }

    public abstract static class ImageGenWorker
    {
        protected int currentLayer;
        protected Random random;
        BlockPos pos;
        World world;
        IChunkGenerator chunkGenerator;
        IChunkProvider chunkProvider;
        int placeNotify;

        public void init(Random random, BlockPos pos, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider)
        {
            this.pos = pos;
            this.random = random;
            this.world = world;
            this.chunkGenerator = chunkGenerator;
            this.chunkProvider = chunkProvider;
        }

        public abstract boolean generate();

        public void setPlaceNotify(int placeNotify)
        {
            this.placeNotify = placeNotify;
        }

        public World getWorld(){return world;}

        public BlockPos getPos(){return pos;}

        public IChunkGenerator getChunkGenerator(){return chunkGenerator;}

        public IChunkProvider getChunkProvider(){return chunkProvider;}
        public int getPlaceNotify(){return placeNotify;}
    }
}
