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

package matteroverdrive.util.animation;

import matteroverdrive.util.math.MOMathHelper;
import net.minecraft.util.MathHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Simeon on 11/20/2015.
 */
public class MOAnimationTimeline
{
    boolean autoLength;
    boolean isPlaying;
    List<Slice> slices = new ArrayList<>();
    private float maxTime;
    private boolean repeat;
    private float time;
    private float defaultValue;

    public MOAnimationTimeline(float maxTime, boolean repeat,boolean autoPlay,float defaultValue)
    {
        this.maxTime = maxTime;
        this.repeat = repeat;
        isPlaying = autoPlay;
        this.defaultValue = defaultValue;
    }

    public void addSlice(Slice slice)
    {
        slices.add(slice);
    }

    public void getActiveSlices(int time,List<Slice> list)
    {
        for (Slice slice : this.slices)
        {
            if (time <= slice.from + slice.length && time >= slice.from)
            {
                list.add(slice);
            }
        }
    }

    public List<Slice> getActiveSlices(int time)
    {
        List<Slice> slices = new ArrayList<>();
        getActiveSlices(time,slices);
        return slices;
    }

    public float getValueAt(float time)
    {
        float value = defaultValue;
        for (Slice slice : this.slices)
        {
            if (time <= slice.from + slice.length && time >= slice.from)
            {
                float localTime = slice.getLocalTime(time);
                value = slice.valueFrom + localTime*(slice.valueTo-slice.valueFrom);
            }
        }
        return value;
    }

    public void recalculateTime()
    {
        if (autoLength)
        {
            float maxTime = 0;
            for (Slice slice : slices)
            {
                if (slice.from + slice.length > time)
                {
                    maxTime = slice.from + slice.length;
                }
            }
            this.maxTime = maxTime;
        }
    }

    public float getCurrentValue()
    {
        return getValueAt(time);
    }

    public void play()
    {
        recalculateTime();
        isPlaying = true;
        if (time >= maxTime)
        {
            time = 0;
        }
    }

    public void replay()
    {
        time = 0;
        play();
    }

    public void stop()
    {
        recalculateTime();
        isPlaying = false;
        time = 0;
    }

    public void pause()
    {
        isPlaying = false;
    }

    public void tick()
    {
        tick(1);
    }

    public void tick(float tick)
    {
        if (isPlaying) {
            if (time < maxTime) {
                time = MathHelper.clamp_float(time+tick,0,maxTime);
            }
            else if (repeat)
            {
                time = 0;
            }else
            {
                pause();
            }
        }
    }

    public void sort()
    {
        float lastFinish = 0;
        for (Slice slice : slices)
        {
            slice.from = lastFinish;
            lastFinish += slice.length;
        }
    }

    public void setTime(float time)
    {
        this.time = time;
    }

    public boolean isPlaying()
    {
        return isPlaying;
    }
    public void setAutoLength(boolean autoLength){this.autoLength = true;}
    public boolean isAutoLength(){return this.autoLength;}
    public float getTime()
    {
        return time;
    }
    public float getMaxTime(){return maxTime;}
    public Slice getSlice(int index)
    {
        return slices.get(index);
    }

    public static class Slice
    {
        private float valueFrom,valueTo;
        private float from,length;

        public Easing getEasing() {
            return easing;
        }

        public void setEasing(Easing easing) {
            this.easing = easing;
        }

        public float getValueFrom() {
            return valueFrom;
        }

        public void setValueFrom(float valueFrom) {
            this.valueFrom = valueFrom;
        }

        public float getValueTo() {
            return valueTo;
        }

        public void setValueTo(float valueTo) {
            this.valueTo = valueTo;
        }

        public float getFrom() {
            return from;
        }

        public void setFrom(float from) {
            this.from = from;
        }

        public float getLength() {
            return length;
        }

        public void setLength(float length) {
            this.length = length;
        }

        private Easing easing;

        public Slice(float valueFrom,float valueTo,float from,float length,Easing easing)
        {
            this.valueFrom = valueFrom;
            this.valueTo = valueTo;
            this.from = from;
            this.length = length;
            this.easing = easing;
        }

        public float getLocalTime(float time)
        {
            if(easing != null) {
                return MathHelper.clamp_float(easing.calculate(time - from, 0, 1, length), 0, 1);
            }else
            {
                return MOMathHelper.Lerp(0,1,(time - from)/length);
            }
        }
    }

    public abstract static class Easing
    {
        public abstract float calculate(float t,float b , float c, float d);

        public static class QuadEaseIn extends Easing
        {
            @Override
            public float calculate(float t,float b , float c, float d) {
                return c*(t/=d)*t + b;
            }
        }

        public static class QuadEaseOut extends Easing
        {
            @Override
            public float calculate(float t,float b , float c, float d) {
                return -c *(t/=d)*(t-2) + b;
            }
        }

        public static class QuadEaseInOut extends Easing
        {
            @Override
            public float calculate(float t,float b , float c, float d) {
                if ((t/=d/2) < 1) return c/2*t*t + b;
                return -c/2 * ((--t)*(t-2) - 1) + b;
            }
        }
    }
}
