package tests;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Vector2f;

public class PlayerTest1 {
	
	private Vector2f pos;
	
	Image image;

	public PlayerTest1(Image playerImage) {
		this(new Vector2f(0.0f, 0.0f), playerImage);
	}
	
	public PlayerTest1(Vector2f initPos, Image playerImage) {
		pos.set(initPos);
		image = playerImage;
		//TODO: add other attributes
	}

	public Vector2f getPos() {
		return pos;
	}

	public void setPos(Vector2f pos) {
		this.pos.set(pos);
	}
	
	public float getX() {
		return pos.x;
	}
	
	public float getY() {
		return pos.y;
	}
	
	public void setX(float newx) {
		pos.x = newx;
	}
	
	public void setY(float newy) {
		pos.y = newy;
	}
	
	public void render(GameContainer gc, Graphics g) {
		//TODO: Render images and animations
	}
	
	public void update(GameContainer gc, Input input) {
		//TODO: handle smooth movement of the player
	}
	
}
