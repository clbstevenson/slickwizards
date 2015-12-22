package tests;

import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Vector2f;

public class PlayerTest1 {
	
	protected enum Direction {
		RIGHT, LEFT, UP, DOWN
	}
	
	private Vector2f pos;
	
	Image currImage;
	SpriteSheet playerSheet;
	Animation playerAnim;
	boolean isMoving;
	
	int speed;

	public PlayerTest1(Image playerImage) throws SlickException{
		this(new Vector2f(0.0f, 0.0f), playerImage);
	}
	
	public PlayerTest1(Vector2f initPos, Image playerImage) throws SlickException{
		//pos.set(initPos);
		pos = initPos;
		currImage = playerImage;
		speed = 10;
		isMoving = false;
		
		playerSheet = new SpriteSheet("res/tests_res/testMovement2.png", 
		  32, 32);
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
	
	public int getOffset() {
		return (currImage.getWidth() / 4);
	}
	
	public void render(GameContainer gc, Graphics g) {
		//TODO: Render images and animations
		currImage.draw(pos.x, pos.y);
	}
	
	public void update(GameContainer gc, Input input, int delta) {
		//TODO: handle smooth movement of the player
		if (input.isKeyDown(Input.KEY_RIGHT)) {
			isMoving = true;
			// if not moving, set the sprite to be moving
			// I think this will improve efficiency slightly
			//if (!moving)
			// ^ did not work. 
			// TODO: make movement efficient, so the player sprite is not
			// 		re-set every loop
			currImage = playerSheet.getSprite(2, 1);
			move(Direction.RIGHT, delta);
		}
		if(!isMoving)
			currImage = playerSheet.getSprite(0, 0);
	}
	
	protected void move(Direction dir, int delta) {
		switch(dir) {
		case RIGHT:
			//move right
			System.out.println("move right");
			//pos.x += speed * delta / 100;
			pos.x += speed * (delta / 100.0f);
			break;
		case LEFT:
			//move left
			System.out.println("move left");
			pos.x -= speed * (delta / 100.0f);
			break;
		case UP:
			//move up
			System.out.println("move up");
			pos.y -= speed * (delta / 100.0f);
			break;
		case DOWN:
			//move down
			System.out.println("move down");
			pos.y += speed * (delta / 100.0f);
			break;
		}
	}
	
}
