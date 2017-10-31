package com.omniworks.demolition;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class ContactItem
{
	private Body body;
	private Body contactedBody;
	private Vector2 contactPoint;
	private float impulse;
	private Vector2 impactVel;
	
	public ContactItem(Body body, Body contactedBody, Vector2 contactPoint, float impulse, Vector2 impactVel)
	{
		this.setBody(body);
		this.setContactedBody(contactedBody);
		this.setContactPoint(contactPoint);
		this.setImpulse(impulse);
		this.setImpactVel(impactVel);
	}

	public Body body() 
	{
		return body;
	}

	public void setBody(Body body) 
	{
		this.body = body;
	}

	public Vector2 contactPoint() 
	{
		return contactPoint;
	}

	public void setContactPoint(Vector2 contactPoint) 
	{
		this.contactPoint = contactPoint;
	}

	public float impulse() 
	{
		return impulse;
	}

	public void setImpulse(float impulse)
	{
		this.impulse = impulse;
	}

	public Vector2 impactVel()
	{
		return impactVel;
	}

	public void setImpactVel(Vector2 impactVel)
	{
		this.impactVel = impactVel;
	}

	public Body contactedBody() {
		return contactedBody;
	}

	public void setContactedBody(Body contactedBody) {
		this.contactedBody = contactedBody;
	}
	
	
}
