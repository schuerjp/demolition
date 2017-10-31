
package com.omniworks.demolition.utils;

import java.awt.List;
import java.util.ArrayList;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class MathUtils {

	public static float asFloat (Object obj, float defvalue) {
		if (obj instanceof Number) return ((Number)obj).floatValue();
		return defvalue;
	}
	
	public static boolean asBoolean (Object obj, boolean defValue)
	{
		if (obj instanceof Boolean) return ((Boolean)obj).booleanValue();
		return defValue;
	}
	
	public static boolean asBoolean(Object obj)
	{
		return asBoolean(obj, true);
	}

	public static float asFloat (Object obj) {
		return asFloat(obj, 0f);
	}
	
	public static int asInt(Object obj)
	{
		return asInt(obj, -1);
	}

	public static int asInt(Object obj, int defValue)
	{
		if(obj instanceof Number) return ((Integer)obj).intValue();
		return defValue;
	}
	
	public static float toRadians (float degrees) {
		return (float)(Math.PI / 180) * degrees;
	}
	
	public static float[] toFloatArray(double[] array)
	{
		if(array == null)
		{
			return null;
		}
		
		float[] farray = new float[array.length];
		
		for(int i = 0; i < array.length; i++)
		{
			farray[i] = (float)array[i];
			
		}
		
		return farray;
	}
	
	public static float[] toFloatArray(ArrayList list)
	{
		if(list == null)
		{
			return null;
		}
		
		float[] farray = new float[list.size()];
		
		for(int i = 0; i < list.size(); i++)
		{
			farray[i] = asFloat(list.get(i));
			
		}
		
		return farray;
	}
	
	public static Vector2[] toVector2Array(float[] x, float[] y)
	{
		if((x == null) || (y == null))
		{
			return null;
		}
		
		Vector2[] vecArray = new Vector2[x.length];
		
		for(int i = 0; i < x.length; i++)
		{
			vecArray[i] = new Vector2(x[i],y[i]);
		}
		
		return vecArray;
	}
	
	public static Vector2[] toVector2Array(double[] x, double[] y)
	{
		if((x == null) || (y == null))
		{
			return null;
		}
		
		Vector2[] vecArray = new Vector2[x.length];
		
		for(int i = 0; i < x.length; i++)
		{
			vecArray[i] = new Vector2((float)x[i],(float) y[i]);
		}
		
		return vecArray;
	}
	
	public static float min(float[] array)
	{
		float min = Float.MAX_VALUE;
		
		for(float fl : array)
		{
			if(fl < min)
			{
				min = fl;
			}
		}
		
		return min;
	}
	
	public static float max(float[] array)
	{
		float max = Float.MIN_VALUE;
		
		for(float fl : array)
		{
			if(fl > max)
			{
				max = fl;
			}
		}
		
		return max;
	}
	
	public static float toDegrees(float rad)
	{
		return (float)(rad*180/Math.PI);
	}
	
}
