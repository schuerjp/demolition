package com.omniworks.demolition.world;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.MassData;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.DistanceJoint;
import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef;
import com.badlogic.gdx.physics.box2d.joints.PrismaticJoint;
import com.badlogic.gdx.physics.box2d.joints.PrismaticJointDef;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.physics.box2d.joints.RopeJoint;
import com.badlogic.gdx.physics.box2d.joints.RopeJointDef;
import com.badlogic.gdx.physics.box2d.joints.WeldJoint;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;
import com.badlogic.gdx.physics.box2d.joints.WheelJoint;
import com.badlogic.gdx.physics.box2d.joints.WheelJointDef;
import com.omniworks.demolition.utils.BodyData;
import com.omniworks.demolition.utils.FixtureData;

import static com.omniworks.demolition.utils.MathUtils.*;

public class Box2DFactory 
{

	public static Fixture createFixture(Map fixtureMap, World world, Body body, short catBits, short maskBits)
	{

		FixtureData fixUserData = new FixtureData();

		FixtureDef fix = new FixtureDef();
		
		fix.filter.categoryBits = catBits;
		fix.filter.maskBits = maskBits;
		
		if(fixtureMap.containsKey("density"))
		{
			fix.density = asFloat(fixtureMap.get("density"));
		}

		if(fixtureMap.containsKey("friction"))
		{
			fix.friction = asFloat(fixtureMap.get("friction"));
		}

		if(fixtureMap.containsKey("polygon"))
		{
			Object polyMap = fixtureMap.get("polygon");

			if(polyMap instanceof Map)
			{
				Object vertMap = ((Map)polyMap).get("vertices");

				if(vertMap instanceof Map)
				{
					float[] xVerts = toFloatArray((ArrayList)((Map)vertMap).get("x"));
					float[] yVerts = toFloatArray((ArrayList)((Map)vertMap).get("y"));

					Vector2[] vertArray = toVector2Array(xVerts, yVerts);

					PolygonShape polyShape = new PolygonShape();
					polyShape.set(vertArray);

					fix.shape = polyShape;

					fixUserData.setMinXY(min(xVerts), min(yVerts));
					fixUserData.setMaxXY(max(xVerts), max(yVerts));

				}
			}
		}

		if(fixtureMap.containsKey("circle"))
		{
			Object circleObj = fixtureMap.get("circle");


			if(circleObj instanceof Map)
			{

				CircleShape circShape = new CircleShape();

				if(((Map)circleObj).containsKey("center"))
				{

					Object centerObj = ((Map)circleObj).get("center");

					if(centerObj instanceof Integer)
					{
						float center = asFloat(centerObj);
						circShape.setPosition(new Vector2(center,center));
					}
					else if(centerObj instanceof Map)
					{
						float x = asFloat(((Map)centerObj).get("x"));
						float y = asFloat(((Map)centerObj).get("y"));

						circShape.setPosition(new Vector2(x,y));
					}

				}

				if(((Map)circleObj).containsKey("radius"))
				{

					float radius = asFloat(((Map)circleObj).get("radius"));
					circShape.setRadius(radius);

					fixUserData.setMinXY(-radius,-radius);
					fixUserData.setMaxXY(radius,radius);
				}

				fix.shape = circShape;
			}

		}

		if(fixtureMap.containsKey("chain"))
		{
			Object polyMap = fixtureMap.get("chain");

			if(polyMap instanceof Map)
			{
				Object vertMap = ((Map)polyMap).get("vertices");

				if(vertMap instanceof Map)
				{
					float[] xVerts = toFloatArray((ArrayList)((Map)vertMap).get("x"));
					float[] yVerts = toFloatArray((ArrayList)((Map)vertMap).get("y"));

					Vector2[] vertArray = toVector2Array(xVerts, yVerts);


					ChainShape chainShape = new ChainShape();
					chainShape.createChain(vertArray);

					fix.shape = chainShape;

					fixUserData.setMinXY(min(xVerts), min(yVerts));
					fixUserData.setMaxXY(max(xVerts), max(yVerts));

				}
			}
		}

		if(fixtureMap.containsKey("restitution"))
		{
			fix.restitution = asFloat(fixtureMap.get("restitution"));
		}


		Fixture fixture = body.createFixture(fix);

		if(fixtureMap.containsKey("name"))
		{
			fixUserData.setName((String) fixtureMap.get("name"));
		}

		fixture.setUserData(fixUserData);

		return fixture;
	}

	public static Fixture createFixture(Map fixtureMap, World world, Body body)
	{

		FixtureData fixUserData = new FixtureData();

		FixtureDef fix = new FixtureDef();
		
		if(fixtureMap.containsKey("density"))
		{
			fix.density = asFloat(fixtureMap.get("density"));
		}

		if(fixtureMap.containsKey("friction"))
		{
			fix.friction = asFloat(fixtureMap.get("friction"));
		}

		if(fixtureMap.containsKey("polygon"))
		{
			Object polyMap = fixtureMap.get("polygon");

			if(polyMap instanceof Map)
			{
				Object vertMap = ((Map)polyMap).get("vertices");

				if(vertMap instanceof Map)
				{
					float[] xVerts = toFloatArray((ArrayList)((Map)vertMap).get("x"));
					float[] yVerts = toFloatArray((ArrayList)((Map)vertMap).get("y"));

					Vector2[] vertArray = toVector2Array(xVerts, yVerts);

					PolygonShape polyShape = new PolygonShape();
					polyShape.set(vertArray);

					fix.shape = polyShape;

					fixUserData.setMinXY(min(xVerts), min(yVerts));
					fixUserData.setMaxXY(max(xVerts), max(yVerts));

				}
			}
		}

		if(fixtureMap.containsKey("circle"))
		{
			Object circleObj = fixtureMap.get("circle");


			if(circleObj instanceof Map)
			{

				CircleShape circShape = new CircleShape();

				if(((Map)circleObj).containsKey("center"))
				{

					Object centerObj = ((Map)circleObj).get("center");

					if(centerObj instanceof Integer)
					{
						float center = asFloat(centerObj);
						circShape.setPosition(new Vector2(center,center));
					}
					else if(centerObj instanceof Map)
					{
						float x = asFloat(((Map)centerObj).get("x"));
						float y = asFloat(((Map)centerObj).get("y"));

						circShape.setPosition(new Vector2(x,y));
					}

				}

				if(((Map)circleObj).containsKey("radius"))
				{

					float radius = asFloat(((Map)circleObj).get("radius"));
					circShape.setRadius(radius);

					fixUserData.setMinXY(-radius,-radius);
					fixUserData.setMaxXY(radius,radius);
				}

				fix.shape = circShape;
			}

		}

		if(fixtureMap.containsKey("chain"))
		{
			Object polyMap = fixtureMap.get("chain");

			if(polyMap instanceof Map)
			{
				Object vertMap = ((Map)polyMap).get("vertices");

				if(vertMap instanceof Map)
				{
					float[] xVerts = toFloatArray((ArrayList)((Map)vertMap).get("x"));
					float[] yVerts = toFloatArray((ArrayList)((Map)vertMap).get("y"));

					Vector2[] vertArray = toVector2Array(xVerts, yVerts);


					ChainShape chainShape = new ChainShape();
					chainShape.createChain(vertArray);

					fix.shape = chainShape;

					fixUserData.setMinXY(min(xVerts), min(yVerts));
					fixUserData.setMaxXY(max(xVerts), max(yVerts));

				}
			}
		}

		if(fixtureMap.containsKey("restitution"))
		{
			fix.restitution = asFloat(fixtureMap.get("restitution"));
		}


		Fixture fixture = body.createFixture(fix);

		if(fixtureMap.containsKey("name"))
		{
			fixUserData.setName((String) fixtureMap.get("name"));
		}


		fixture.setUserData(fixUserData);

		return fixture;
	}
	public static Body createBody(Map bodyMap, World world)
	{


		BodyData bodyData = new BodyData();

		BodyDef bodyDef = new BodyDef();

		if(bodyMap.containsKey("angle"))
		{
			bodyDef.angle = asFloat(bodyMap.get("angle"));
		}

		if(bodyMap.containsKey("angularVelocity"))
		{
			bodyDef.angularVelocity = asFloat(bodyMap.get("angularVelocity"));
		}

		if(bodyMap.containsKey("position"))
		{
			Object posObj = bodyMap.get("position");

			if(posObj instanceof Integer)
			{
				float pos = asFloat(posObj);
				bodyDef.position.set(new Vector2(pos,pos));
			}
			else if(posObj instanceof Map)
			{
				float xPos = asFloat(((Map)posObj).get("x"));
				float yPos = asFloat(((Map)posObj).get("y"));

				bodyDef.position.set(new Vector2(xPos,yPos));
			}

			bodyData.setInitialPos(bodyDef.position);
		}

		if(bodyMap.containsKey("linearVelocity"))
		{
			Object velObj = bodyMap.get("linearVelocity");

			if(velObj instanceof Integer)
			{
				float vel = asFloat(velObj);
				bodyDef.linearVelocity.set(new Vector2(vel,vel));
			}
			else if(velObj instanceof Map)
			{
				float xVel = asFloat(((Map)velObj).get("x"));
				float yVel = asFloat(((Map)velObj).get("y"));

				bodyDef.linearVelocity.set(new Vector2(xVel,yVel));
			}

		}


		if(bodyMap.containsKey("awake"))
		{
			bodyDef.awake = asBoolean(bodyMap.get("awake"));
		}

		if(bodyMap.containsKey("type"))
		{

			Object type = bodyMap.get("type");

			if(type instanceof Number)
			{
				int bodyType = ((Integer)type);
				if(bodyType == 0)
				{
					bodyDef.type = BodyType.StaticBody;
				}
				else if(bodyType == 1)
				{
					bodyDef.type = BodyType.KinematicBody;
				}
				else if(bodyType == 2)
				{
					bodyDef.type = BodyType.DynamicBody;
				}
			}
		}

		if(bodyMap.containsKey("customProperties"))
		{
			for (Object obj : WorldLayout.listForKey(bodyMap, "customProperties"))
			{
				// allow strings in JSON for comments
				if (!(obj instanceof Map)) continue;
				Map params = (Map)obj;

				if(params.containsKey("name"))
				{
					String bodyName = (String)params.get("name");

					if(bodyName.contains("bodyID") && params.containsKey("int"))
					{
						bodyData.setBodyID(asInt(params.get("int")));
					}
				}
			}
		}
		//get mass data of body
		MassData massData = new MassData();

		if(bodyMap.containsKey("massData-I"))
		{
			massData.I = asFloat(bodyMap.get("massData-I"));
		}

		if(bodyMap.containsKey("massData-mass"))
		{
			massData.mass = asFloat(bodyMap.get("massData-mass"));
		}

		//create body
		Body body = world.createBody(bodyDef);

		if(bodyMap.containsKey("massData-center"))
		{
			Object massObj = bodyMap.get("massData-center");

			if(massObj instanceof Integer)
			{
				float center = asFloat(massObj);
				massData.center.set(new Vector2(center,center));
			}
			else if(massObj instanceof Map)
			{
				float x = asFloat(((Map)massObj).get("x"));
				float y = asFloat(((Map)massObj).get("y"));

				massData.center.set(new Vector2(x,y));
			}

			body.setMassData(massData);

		}

		if(bodyMap.containsKey("name"))
		{
			bodyData.setName((String) bodyMap.get("name"));
		}

		body.setUserData(bodyData);

		return body;
	}

	public static Joint createJoint(Map jointMap, World world)
	{

		String jointType = "";

		Vector2 anchorA = new Vector2();
		Vector2 anchorB = new Vector2();
		Vector2 localAxisA = new Vector2();
		Vector2 localAxisB = new Vector2();
		boolean enableMotor = false;
		boolean enableLimit = false;
		boolean collideConnected = false;
		float frequency = 0;
		float dampingRatio = 0;
		float length = 0;
		float lowerLimit = 0;
		float maxMotorForce = 0;
		float motorSpeed = 0;
		String name = "";
		float refAngle = 0;
		float upperLimit = 0;
		float jointSpeed = 0;
		float maxMotorTorque = 0;
		float springDampingRatio = 0; 
		float springFrequency = 0;
		float maxLength = 0;
		int bodyAID = -1;
		int bodyBID = -1;

		if(jointMap.containsKey("anchorA"))
		{
			Object anchAObj = jointMap.get("anchorA");

			if(anchAObj instanceof Integer)
			{
				float center = asFloat(anchAObj);
				anchorA.set(center, center);
			}
			else if(anchAObj instanceof Map)
			{
				float x = asFloat(((Map)anchAObj).get("x"));
				float y = asFloat(((Map)anchAObj).get("y"));

				anchorA.set(new Vector2(x,y));
			}
		}

		if(jointMap.containsKey("anchorB"))
		{
			Object anchAObj = jointMap.get("anchorB");

			if(anchAObj instanceof Integer)
			{
				float center = asFloat(anchAObj);
				anchorB.set(center, center);
			}
			else if(anchAObj instanceof Map)
			{
				float x = asFloat(((Map)anchAObj).get("x"));
				float y = asFloat(((Map)anchAObj).get("y"));

				anchorB.set(new Vector2(x,y));
			}
		}



		if(jointMap.containsKey("dampingRatio"))
		{
			Object dampObj = jointMap.get("dampingRatio");

			if(dampObj instanceof Number)
			{
				dampingRatio = asFloat(dampObj);
			}
		}



		if(jointMap.containsKey("frequency"))
		{
			Object freqObj = jointMap.get("frequency");

			if(freqObj instanceof Number)
			{
				frequency = asFloat(freqObj);
			}
		}



		if(jointMap.containsKey("length"))
		{
			Object lenObj = jointMap.get("length");

			if(lenObj instanceof Number)
			{
				length = asFloat(lenObj);
			}
		}

		if(jointMap.containsKey("lowerLimit"))
		{
			Object obj = jointMap.get("lowerLimit");

			if(obj instanceof Number)
			{
				lowerLimit = asFloat(obj);
			}
		}

		if(jointMap.containsKey("maxMotorForce"))
		{
			Object obj = jointMap.get("maxMotorForce");

			if(obj instanceof Number)
			{
				maxMotorForce = asFloat(obj);
			}
		}

		if(jointMap.containsKey("motorSpeed"))
		{
			Object obj = jointMap.get("motorSpeed");

			if(obj instanceof Number)
			{
				motorSpeed = asFloat(obj);
			}
		}

		if(jointMap.containsKey("refAngle"))
		{
			Object obj = jointMap.get("refAngle");

			if(obj instanceof Number)
			{
				refAngle = asFloat(obj);
			}
		}

		if(jointMap.containsKey("upperLimit"))
		{
			Object obj = jointMap.get("upperLimit");

			if(obj instanceof Number)
			{
				upperLimit = asFloat(obj);
			}
		}

		if(jointMap.containsKey("jointSpeed"))
		{
			Object obj = jointMap.get("jointSpeed");

			if(obj instanceof Number)
			{
				jointSpeed = asFloat(obj);
			}
		}

		if(jointMap.containsKey("maxMotorTorque"))
		{
			Object obj = jointMap.get("maxMotorTorque");

			if(obj instanceof Number)
			{
				maxMotorTorque = asFloat(obj);
			}
		}

		if(jointMap.containsKey("springDampingRatio"))
		{
			Object obj = jointMap.get("springDampingRatio");

			if(obj instanceof Number)
			{
				springDampingRatio = asFloat(obj);
			}
		}

		if(jointMap.containsKey("springFrequency"))
		{
			Object obj = jointMap.get("springFrequency");

			if(obj instanceof Number)
			{
				springFrequency = asFloat(obj);
			}
		}

		if(jointMap.containsKey("maxLength"))
		{
			Object obj = jointMap.get("maxLength");

			if(obj instanceof Number)
			{
				maxLength = asFloat(obj);
			}
		}

		if(jointMap.containsKey("enableLimit"))
		{
			Object obj = jointMap.get("enableLimit");

			if(obj instanceof Boolean)
			{
				enableLimit = asBoolean(obj);
			}
		}



		if(jointMap.containsKey("enableMotor"))
		{
			Object obj = jointMap.get("enableMotor");

			if(obj instanceof Boolean)
			{
				enableMotor = asBoolean(obj);
			}
		}

		if(jointMap.containsKey("collideConnected"))
		{
			Object obj = jointMap.get("collideConnected");

			if(obj instanceof Boolean)
			{
				collideConnected = asBoolean(obj);
			}
		}

		if(jointMap.containsKey("localAxisA"))
		{
			Object obj = jointMap.get("localAxisA");

			if(obj instanceof Integer)
			{
				float center = asFloat(obj);
				localAxisA.set(center, center);
			}
			else if(obj instanceof Map)
			{
				float x = asFloat(((Map)obj).get("x"));
				float y = asFloat(((Map)obj).get("y"));

				localAxisA.set(new Vector2(x,y));
			}
		}

		if(jointMap.containsKey("localAxisB"))
		{
			Object obj = jointMap.get("localAxisB");

			if(obj instanceof Integer)
			{
				float center = asFloat(obj);
				localAxisB.set(center, center);
			}
			else if(obj instanceof Map)
			{
				float x = asFloat(((Map)obj).get("x"));
				float y = asFloat(((Map)obj).get("y"));

				localAxisB.set(new Vector2(x,y));
			}
		}

		for (Object obj : WorldLayout.listForKey(jointMap, "customProperties"))
		{
			// allow strings in JSON for comments
			if (!(obj instanceof Map)) continue;
			Map params = (Map)obj;

			if(params.containsKey("name"))
			{
				String bodyName = (String)params.get("name");

				if(bodyName.contains("bodyAID") && params.containsKey("int"))
				{
					bodyAID = asInt(params.get("int"));
				}
				else if(bodyName.contains("bodyBID") && params.containsKey("int"))
				{
					bodyBID = asInt(params.get("int"));
				}
			}
		}

		Body bodyA = null;
		Body bodyB = null;

		Iterator<Body> bodyIter = world.getBodies();
		boolean foundA = false;
		boolean foundB = false;

		while(bodyIter.hasNext())
		{
			Body tempBody = bodyIter.next();
			int bodyID = ((BodyData)tempBody.getUserData()).bodyID();
			if(bodyID == bodyAID)
			{
				bodyA = tempBody;
				foundA = true;
			}
			else if(bodyID == bodyBID)
			{
				bodyB = tempBody;
				foundB = true;
			}

			if(foundA & foundB)
			{
				break;
			}

		}

		if(jointMap.containsKey("type"))
		{
			jointType = (String) jointMap.get("type");
		}

		if(jointType.contains("distance"))
		{
			DistanceJointDef jointDef =  new DistanceJointDef();
			jointDef.initialize(bodyA, bodyB, anchorA.add(bodyA.getPosition()), anchorB.add(bodyB.getPosition()));
			jointDef.dampingRatio = dampingRatio;
			jointDef.frequencyHz = frequency;
			jointDef.length = length;
			jointDef.collideConnected = collideConnected;

			DistanceJoint dJoint = (DistanceJoint) world.createJoint(jointDef);
			return dJoint;

		}
		else if(jointType.contains("prismatic"))
		{
			PrismaticJointDef jointDef = new PrismaticJointDef();
			jointDef.initialize(bodyA, bodyB, bodyB.getWorldPoint(anchorB), localAxisA);
			jointDef.enableLimit = enableLimit;
			jointDef.enableMotor = enableMotor;
			jointDef.maxMotorForce = maxMotorForce;
			jointDef.motorSpeed = motorSpeed;
			jointDef.referenceAngle = refAngle;
			jointDef.lowerTranslation = lowerLimit;
			jointDef.upperTranslation = upperLimit;
			jointDef.collideConnected = collideConnected;

			PrismaticJoint pJoint = (PrismaticJoint) world.createJoint(jointDef);
			return pJoint;
		}
		else if(jointType.contains("revolute"))
		{
			RevoluteJointDef jointDef = new RevoluteJointDef();
			jointDef.initialize(bodyA, bodyB, bodyB.getWorldPoint(anchorB));
			jointDef.enableLimit = enableLimit;
			jointDef.enableMotor = enableMotor;
			jointDef.motorSpeed = motorSpeed;
			jointDef.referenceAngle = refAngle;
			jointDef.lowerAngle = lowerLimit;
			jointDef.upperAngle = upperLimit;
			jointDef.maxMotorTorque = maxMotorTorque;
			jointDef.collideConnected = collideConnected;

			RevoluteJoint rJoint = (RevoluteJoint) world.createJoint(jointDef);
			return rJoint;
		}
		else if(jointType.contains("wheel"))
		{
			WheelJointDef jointDef = new WheelJointDef();
			jointDef.initialize(bodyA, bodyB, bodyB.getWorldPoint(anchorB), localAxisA);
			jointDef.enableMotor = enableMotor;
			jointDef.maxMotorTorque = maxMotorTorque;
			jointDef.motorSpeed = motorSpeed;
			jointDef.dampingRatio = springDampingRatio;
			jointDef.frequencyHz = springFrequency;
			jointDef.collideConnected = collideConnected;

			WheelJoint wJoint = (WheelJoint) world.createJoint(jointDef);
			return wJoint;
		}
		else if(jointType.contains("rope"))
		{
			WeldJointDef weldDef = new WeldJointDef();
			weldDef.initialize(bodyA, bodyB, bodyB.getWorldPoint(anchorB));
			weldDef.collideConnected = collideConnected;
			WeldJoint wJoint = (WeldJoint) world.createJoint(weldDef);
			return wJoint;
		}
		else
		{
			return null;
		}

	}

}


