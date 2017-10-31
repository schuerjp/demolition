package com.omniworks.demolition.utils;

import java.util.ArrayList;

import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.Shape.Type;

public class ShapeUtils
{
	public static Polygon createPolygon(Body body)
	{
		ArrayList<Fixture> fixList = body.getFixtureList();
		
		if(!fixList.isEmpty())
		{
			Fixture fix = fixList.get(0);
			
			Shape shape = fix.getShape();
			Type type = shape.getType();
			if(type == Type.Polygon)
			{
				PolygonShape poly = (PolygonShape)shape;
				int vertCount = poly.getVertexCount();
				
				float[] vertsFloat = new float[vertCount*2];
				
				Vector2 vert = new Vector2();
				
				for(int i = 0; i < vertCount; i++)
				{
					poly.getVertex(i, vert);
					vertsFloat[2*i] = vert.x;
					vertsFloat[(2*i)+1] = vert.y;
				}
				
				Polygon polygon = new Polygon(vertsFloat);
				
				return polygon;
			}
			else return null;
		}
		else return null;
	}
	
	public static Polygon createPolygon(Fixture fix)
	{
		
		if(fix == null) return null;
		
		Shape shape = fix.getShape();
		Type type = shape.getType();
		
		if(type == Type.Circle)
		{
			float radius = shape.getRadius();
			float[] vertsFloat = new float[8];
			
			vertsFloat[0] = -radius; vertsFloat[1] = -radius;
			vertsFloat[2] = radius; vertsFloat[3] = -radius;
			vertsFloat[4] = radius; vertsFloat[5] = radius;
			vertsFloat[6] = -radius; vertsFloat[7] = radius;
			
			Polygon polygon = new Polygon(vertsFloat);

			return polygon;
		}
		
		
		PolygonShape poly = (PolygonShape)shape;
		int vertCount = poly.getVertexCount();
		
		float[] vertsFloat = new float[vertCount*2];
				
		Vector2 vert = new Vector2();
				
		for(int i = 0; i < vertCount; i++)
		{
			poly.getVertex(i, vert);
			vertsFloat[2*i] = vert.x;
			vertsFloat[(2*i)+1] = vert.y;
		}
				
		Polygon polygon = new Polygon(vertsFloat);

		return polygon;
	}
	
	public static Polygon createPolygon(Vector2 minXY, Vector2 maxXY)
	{
		
			float[] vertsFloat = new float[8];
			
			vertsFloat[0] = minXY.x; vertsFloat[1] = minXY.y;
			vertsFloat[2] = maxXY.x; vertsFloat[3] = minXY.y;
			vertsFloat[4] = maxXY.x; vertsFloat[5] = maxXY.y;
			vertsFloat[6] = minXY.x; vertsFloat[7] = maxXY.y;
			
			Polygon polygon = new Polygon(vertsFloat);

			return polygon;
	}
	
	public static Polygon rectangleToPolygon(Rectangle rect)
	{
		if(rect == null) return null;
		
		float[] vertsFloat = new float[8];
		
		vertsFloat[0] = rect.x; vertsFloat[1] = rect.y;
		vertsFloat[2] = rect.x+rect.width; vertsFloat[3] = rect.y;
		vertsFloat[4] = rect.x+rect.width; vertsFloat[5] = rect.y+rect.height;
		vertsFloat[6] = rect.x; vertsFloat[7] = rect.y+rect.height;
		
		Polygon polygon = new Polygon(vertsFloat);

		return polygon;
	}
}
