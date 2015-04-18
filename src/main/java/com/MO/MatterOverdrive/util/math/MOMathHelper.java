package com.MO.MatterOverdrive.util.math;

import com.MO.MatterOverdrive.util.Vector3;
import net.minecraft.util.MathHelper;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import java.util.Random;

public class MOMathHelper 
{
	public static final double PI2 = Math.PI * 2;
	public static double degreesToRadians = Math.PI / 180.0;

	public static Vector3 randomSpherePoint(double x0,double y0,double z0,Vector3 radius,Random rand)
    {
    	   double u = rand.nextDouble();
    	   double v = rand.nextDouble();
    	   double theta = 2 * Math.PI * u;
    	   double phi = Math.acos(2 * v - 1);
    	   double x = x0 + (radius.getX() * Math.sin(phi) * Math.cos(theta));
    	   double y = y0 + (radius.getY() * Math.sin(phi) * Math.sin(theta));
    	   double z = z0 + (radius.getZ() * Math.cos(phi));
    	   return new Vector3(x,y,z);
    }

    public static Vector3f randomCirclePoint(float radius,Random rand)
    {
        double u = rand.nextDouble();
        double theta = 2 * Math.PI * u;
        double x = (radius * Math.sin(theta));
        double z = (radius * Math.cos(theta));
        return new Vector3f((float)x,0,(float)z);
    }
	
	public static boolean getBoolean(int number,int pos)
	{

        return ((number >> pos) & 1) == 1;
	}
	
	public static int setBoolean(int number,int pos,boolean value)
	{
		if(value)
		{
			return number | (1 << pos);
		}else
		{
			return number & ~(1 << pos);
		}
	}

    public static Vector2f Intersects(Vector2f a1, Vector2f a2, Vector2f b1, Vector2f b2)
    {
        Vector2f b = Vector2f.sub(a2,a1,null);
        Vector2f d = Vector2f.sub(b2,b1,null);
        float bDotDPerp = b.x * d.y - b.y * d.x;

        // if b dot d == 0, it means the lines are parallel so have infinite intersection points
        if (bDotDPerp == 0)
            return null;

        Vector2f c = Vector2f.sub(b1,a1,null);
        float t = (c.x * d.y - c.y * d.x) / bDotDPerp;
        if (t < 0 || t > 1)
            return null;

        float u = (c.x * b.y - c.y * b.x) / bDotDPerp;
        if (u < 0 || u > 1)
            return null;

        b.scale(t);
        return Vector2f.add(a1,b,null);
    }

    public static float distance(Vector2f one,Vector2f two)
    {
        return MathHelper.sqrt_float((one.x - two.x) * (one.x - two.x) + (one.y - two.y) * (one.y - two.y));
    }

    public static float Lerp(float form,float to,float time)
    {
        float newTime = MathHelper.clamp_float(time,0,1);
        return (1 - newTime) * form + newTime * to;
    }
    
       // JAVA REFERENCE IMPLEMENTATION OF IMPROVED NOISE - COPYRIGHT 2002 KEN PERLIN.
       static public double noise(double x, double y, double z) {
          int X = (int)Math.floor(x) & 255,                  // FIND UNIT CUBE THAT
              Y = (int)Math.floor(y) & 255,                  // CONTAINS POINT.
              Z = (int)Math.floor(z) & 255;
          x -= Math.floor(x);                                // FIND RELATIVE X,Y,Z
          y -= Math.floor(y);                                // OF POINT IN CUBE.
          z -= Math.floor(z);
          double u = fade(x),                                // COMPUTE FADE CURVES
                 v = fade(y),                                // FOR EACH OF X,Y,Z.
                 w = fade(z);
          int A = p[X  ]+Y, AA = p[A]+Z, AB = p[A+1]+Z,      // HASH COORDINATES OF
              B = p[X+1]+Y, BA = p[B]+Z, BB = p[B+1]+Z;      // THE 8 CUBE CORNERS,

          return lerp(w, lerp(v, lerp(u, grad(p[AA  ], x  , y  , z   ),  // AND ADD
                                         grad(p[BA  ], x-1, y  , z   )), // BLENDED
                                 lerp(u, grad(p[AB  ], x  , y-1, z   ),  // RESULTS
                                         grad(p[BB  ], x-1, y-1, z   ))),// FROM  8
                         lerp(v, lerp(u, grad(p[AA+1], x  , y  , z-1 ),  // CORNERS
                                         grad(p[BA+1], x-1, y  , z-1 )), // OF CUBE
                                 lerp(u, grad(p[AB+1], x  , y-1, z-1 ),
                                         grad(p[BB+1], x-1, y-1, z-1 ))));
       }
       static double fade(double t) { return t * t * t * (t * (t * 6 - 15) + 10); }
       static double lerp(double t, double a, double b) { return a + t * (b - a); }
       static double grad(int hash, double x, double y, double z) {
          int h = hash & 15;                      // CONVERT LO 4 BITS OF HASH CODE
          double u = h<8 ? x : y,                 // INTO 12 GRADIENT DIRECTIONS.
                 v = h<4 ? y : h==12||h==14 ? x : z;
          return ((h&1) == 0 ? u : -u) + ((h&2) == 0 ? v : -v);
       }
       static final int p[] = new int[512], permutation[] = { 151,160,137,91,90,15,
       131,13,201,95,96,53,194,233,7,225,140,36,103,30,69,142,8,99,37,240,21,10,23,
       190, 6,148,247,120,234,75,0,26,197,62,94,252,219,203,117,35,11,32,57,177,33,
       88,237,149,56,87,174,20,125,136,171,168, 68,175,74,165,71,134,139,48,27,166,
       77,146,158,231,83,111,229,122,60,211,133,230,220,105,92,41,55,46,245,40,244,
       102,143,54, 65,25,63,161, 1,216,80,73,209,76,132,187,208, 89,18,169,200,196,
       135,130,116,188,159,86,164,100,109,198,173,186, 3,64,52,217,226,250,124,123,
       5,202,38,147,118,126,255,82,85,212,207,206,59,227,47,16,58,17,182,189,28,42,
       223,183,170,213,119,248,152, 2,44,154,163, 70,221,153,101,155,167, 43,172,9,
       129,22,39,253, 19,98,108,110,79,113,224,232,178,185, 112,104,218,246,97,228,
       251,34,242,193,238,210,144,12,191,179,162,241, 81,51,145,235,249,14,239,107,
       49,192,214, 31,181,199,106,157,184, 84,204,176,115,121,50,45,127, 4,150,254,
       138,236,205,93,222,114,67,29,24,72,243,141,128,195,78,66,215,61,156,180
       };
       static { for (int i=0; i < 256 ; i++) p[256+i] = p[i] = permutation[i]; }
}
