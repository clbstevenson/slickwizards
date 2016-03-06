package tests;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;

import tools.Direction;

/**
 * Turret.java
 * This file will be used to create a Turret object.
 * It is intended to fire a projectile repeatedly.
 * 
 * @author Caleb Stevenson
 *
 */
public class Turret {

	// TODO: have turret types
	// TODO: make this an animation
	Image turretImage;
	Shape turretShape;

	Vector2f pos;
	Direction dir;

	int fireDelta;
	int fireDeltaMax;

	ArrayList<Projectile> projectiles;
	ArrayList<Projectile> deadProjectiles;

	public Turret(Image turretImage) {
		this(new Vector2f(0.0f, 0.0f), Direction.RIGHT, turretImage);
	}

	public Turret(Vector2f initPos, Direction initDir, Image turretImage) {
		pos = initPos;
		dir = initDir;
		this.turretImage = turretImage;
		turretShape = new Rectangle(pos.x, pos.y, turretImage.getWidth(),
				turretImage.getHeight());
		fireDelta = fireDeltaMax = 2000;

		projectiles = new ArrayList<Projectile>();
	}

	public Vector2f getPos() {
		return pos;
	}

	public void setPos(Vector2f pos) {
		this.pos = pos;
	}

	public Direction getDir() {
		return dir;
	}

	public void setDir(Direction dir) {
		this.dir = dir;
	}

	public Shape getShape() {
		return turretShape;
	}
	
	
	//fire a new projectile
	private void fire() {
		//create a new projectile
		//TODO: use an image (based on turret type? or projectile type?)
		Projectile p = new Projectile(pos, dir);
		projectiles.add(p);
	}

	public void render(GameContainer gc, Graphics g) {
		// TODO render images, and render images for each projectile
		for(Projectile p: projectiles)
			p.render(gc, g);
		g.drawImage(turretImage, pos.x, pos.y);
	}

	public void update(GameContainer gc, Input input, int delta) {

		fireDelta -= delta;
		// once fireDelta is less than 0, then fire projectile
		if (fireDelta < 0) {
			// fire a projectile
			// ...
			fire();
			// reset fireDelta to the max value
			fireDelta = fireDeltaMax;
		}
		
		//attempt to update all of the currently fired projectiles
		for(Projectile p: projectiles) {
			//if the projectile is not done moving, update it
			if(p.dir != Direction.NONE)
				p.update(gc, delta);
			else
				//otherwise, it is now "dead"
				deadProjectiles.add(p);
		}
		//remove from the projectiles arraylist all of the "dead" projectiles
		projectiles.removeAll(deadProjectiles);
		//remove all of the dead projectiles from the list
		deadProjectiles.clear();
			
	}

	private class Projectile {
		Vector2f pos;
		Direction dir;
		float speed;
		
		int lifetime;

		// TODO: have projectile types
		// TODO: have an animation
		Image pImage;

		private Projectile(Vector2f initPos, Direction initDir) {
			pos = initPos;
			dir = initDir;
			pImage = null;
			speed = 1;
			lifetime = 1000;
		}

		private Projectile(Vector2f initPos, Direction initDir, Image initImage,
				float initSpeed, int initLifetime) {
			pos = initPos;
			dir = initDir;
			this.pImage = initImage;
			speed = initSpeed;
			lifetime = initLifetime;
		}

		private void render(GameContainer gc, Graphics g) {
			if (pImage == null) {
				Color c = g.getColor();
				g.setColor(Color.orange);
				g.fillOval(pos.x, pos.y, 4, 4);
				g.setColor(c);
			} else {
				g.drawImage(pImage, pos.x, pos.y);
			}

		}

		private void update(GameContainer gc, int delta) {
			lifetime -= delta;
			if(lifetime <= 0) 
				dir = Direction.NONE;
			
			switch (dir) {
			case RIGHT:
				pos.x += (speed + delta) / 10;
				break;
			case LEFT:
				pos.x -= (speed + delta) / 10;
				break;
			case UP:
				pos.y -= (speed + delta) / 10;
				break;
			case DOWN:
				pos.y += (speed + delta) / 10;
				break;
			case NONE:
				// don't move
				break;
			default:
				// don't move
				break;
			}
		}
	}

}
