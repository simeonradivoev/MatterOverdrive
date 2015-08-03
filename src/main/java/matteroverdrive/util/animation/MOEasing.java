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

//penner's easing equations on processing
//Gotten from https://github.com/jesusgollonet/processing-penner-easing

/**
 * Created by Simeon on 7/29/2015.
 */
public class MOEasing
{
    // t - time
    // b - from value
    //c - to value
    //d - maxTime

    public static class Quad
    {
        public static float  easeIn(float t,float b , float c, float d) {
            return c*(t/=d)*t + b;
        }

        public static float  easeOut(float t,float b , float c, float d) {
            return -c *(t/=d)*(t-2) + b;
        }

        public static float  easeInOut(float t,float b , float c, float d) {
            if ((t/=d/2) < 1) return c/2*t*t + b;
            return -c/2 * ((--t)*(t-2) - 1) + b;
        }
    }

    public static class Bounce {

        public static float  easeIn(float t,float b , float c, float d) {
            return c - easeOut (d-t, 0, c, d) + b;
        }

        public static float  easeOut(float t,float b , float c, float d) {
            if ((t/=d) < (1/2.75f)) {
                return c*(7.5625f*t*t) + b;
            } else if (t < (2/2.75f)) {
                return c*(7.5625f*(t-=(1.5f/2.75f))*t + .75f) + b;
            } else if (t < (2.5/2.75)) {
                return c*(7.5625f*(t-=(2.25f/2.75f))*t + .9375f) + b;
            } else {
                return c*(7.5625f*(t-=(2.625f/2.75f))*t + .984375f) + b;
            }
        }

        public static float  easeInOut(float t,float b , float c, float d) {
            if (t < d/2) return easeIn (t*2, 0, c, d) * .5f + b;
            else return easeOut (t*2-d, 0, c, d) * .5f + c*.5f + b;
        }

    }

    public static class Sine {

        public static float  easeIn(float t,float b , float c, float d) {
            return -c * (float)Math.cos(t/d * (Math.PI/2)) + c + b;
        }

        public static float  easeOut(float t,float b , float c, float d) {
            return c * (float)Math.sin(t/d * (Math.PI/2)) + b;
        }

        public static float  easeInOut(float t,float b , float c, float d) {
            return -c/2 * ((float)Math.cos(Math.PI*t/d) - 1) + b;
        }

    }

    public static class Elastic {

        public static float  easeIn(float t,float b , float c, float d ) {
            if (t==0) return b;  if ((t/=d)==1) return b+c;
            float p=d*.3f;
            float a=c;
            float s=p/4;
            return -(a*(float)Math.pow(2,10*(t-=1)) * (float)Math.sin( (t*d-s)*(2*(float)Math.PI)/p )) + b;
        }

        public static float  easeIn(float t,float b , float c, float d, float a, float p) {
            float s;
            if (t==0) return b;  if ((t/=d)==1) return b+c;
            if (a < Math.abs(c)) { a=c;  s=p/4; }
            else { s = p/(2*(float)Math.PI) * (float)Math.asin (c/a);}
            return -(a*(float)Math.pow(2,10*(t-=1)) * (float)Math.sin( (t*d-s)*(2*Math.PI)/p )) + b;
        }

        public static float  easeOut(float t,float b , float c, float d) {
            if (t==0) return b;  if ((t/=d)==1) return b+c;
            float p=d*.3f;
            float a=c;
            float s=p/4;
            return (a*(float)Math.pow(2,-10*t) * (float)Math.sin( (t*d-s)*(2*(float)Math.PI)/p ) + c + b);
        }

        public static float  easeOut(float t,float b , float c, float d, float a, float p) {
            float s;
            if (t==0) return b;  if ((t/=d)==1) return b+c;
            if (a < Math.abs(c)) { a=c;  s=p/4; }
            else { s = p/(2*(float)Math.PI) * (float)Math.asin (c/a);}
            return (a*(float)Math.pow(2,-10*t) * (float)Math.sin( (t*d-s)*(2*(float)Math.PI)/p ) + c + b);
        }

        public static float  easeInOut(float t,float b , float c, float d) {
            if (t==0) return b;  if ((t/=d/2)==2) return b+c;
            float p=d*(.3f*1.5f);
            float a=c;
            float s=p/4;
            if (t < 1) return -.5f*(a*(float)Math.pow(2,10*(t-=1)) * (float)Math.sin( (t*d-s)*(2*(float)Math.PI)/p )) + b;
            return a*(float)Math.pow(2,-10*(t-=1)) * (float)Math.sin( (t*d-s)*(2*(float)Math.PI)/p )*.5f + c + b;
        }

        public static float  easeInOut(float t,float b , float c, float d, float a, float p) {
            float s;
            if (t==0) return b;  if ((t/=d/2)==2) return b+c;
            if (a < Math.abs(c)) { a=c; s=p/4; }
            else { s = p/(2*(float)Math.PI) * (float)Math.asin (c/a);}
            if (t < 1) return -.5f*(a*(float)Math.pow(2,10*(t-=1)) * (float)Math.sin( (t*d-s)*(2*(float)Math.PI)/p )) + b;
            return a*(float)Math.pow(2,-10*(t-=1)) * (float)Math.sin( (t*d-s)*(2*(float)Math.PI)/p )*.5f + c + b;
        }

    }

    public static class Cubic {

        public static float easeIn (float t,float b , float c, float d) {
            return c*(t/=d)*t*t + b;
        }

        public static float easeOut (float t,float b , float c, float d) {
            return c*((t=t/d-1)*t*t + 1) + b;
        }

        public static float easeInOut (float t,float b , float c, float d) {
            if ((t/=d/2) < 1) return c/2*t*t*t + b;
            return c/2*((t-=2)*t*t + 2) + b;
        }

    }

    public static class Quart {

        public static float  easeIn(float t,float b , float c, float d) {
            return c*(t/=d)*t*t*t + b;
        }

        public static float  easeOut(float t,float b , float c, float d) {
            return -c * ((t=t/d-1)*t*t*t - 1) + b;
        }

        public static float  easeInOut(float t,float b , float c, float d) {
            if ((t/=d/2) < 1) return c/2*t*t*t*t + b;
            return -c/2 * ((t-=2)*t*t*t - 2) + b;
        }

    }
}
