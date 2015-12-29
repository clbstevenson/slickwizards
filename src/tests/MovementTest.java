package tests;

import org.newdawn.slick.AngelCodeFont;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.tiled.TiledMap;

import tests.PlayerTest.Direction;

public class MovementTest extends BasicGame {

	private static float version = 1.3f;
	private static String title = "Test Game - v" + version;
	private static final int TILE_SIZE = 32;
	
	private GameContainer gc;

	//TODO: also include the smallfont, and maybe more fonts too
	AngelCodeFont font1;
	Input input;
	int inputDelta;
	
	Image playerImage;
	Image map;
	Image[] images;
	SpriteSheet moves;

	// TODO: Map "frames": move from 1 section to another of a map
	// - a 2d array of Images for map backgrounds?
	// > actually, "map" objects better so can interact different
	// > for now: test with images

	Image[][] mapFrames;
	// TODO: Make the 2D array hold TilED maps
	TiledMap[][] tiledMapArr;
	Vector2f mapID;
	Vector2f pos;
	Vector2f mapPos;
	
	PlayerTest player;
	
	public MovementTest(String title) {
		super(title);
	}

	@Override
	public void init(GameContainer gc) throws SlickException {
		this.gc = gc;
		input = gc.getInput();
		inputDelta = 1000;
		
		font1 = new AngelCodeFont("res/font1.fnt", "res/font1_0.tga");
		
		images = new Image[5];

		moves = new SpriteSheet("testdata/testMovement2.png", 
				TILE_SIZE, TILE_SIZE);
		playerImage = moves.getSprite(0, 0);
		map = new Image("testdata/testMap1.png");

		mapID = new Vector2f(0, 1);
		mapFrames = new Image[3][3];
		for (int k = 0; k < mapFrames.length; k++) {
			mapFrames[k][0] = map;
			mapFrames[k][1] = map;
			mapFrames[k][2] = map;
		}

		// TODO: Make a loop to initialize the Tiled maps
		tiledMapArr = new TiledMap[1][3];
		tiledMapArr[0][0] = new TiledMap("testdata/map0-0.tmx");
		tiledMapArr[0][1] = new TiledMap("testdata/map0-1.tmx");
		tiledMapArr[0][2] = new TiledMap("testdata/map0-2.tmx");

		int x = (gc.getWidth() / 2) - (playerImage.getWidth() / 2);
		int y = (gc.getHeight() / 2) - (playerImage.getHeight() / 2);
		pos = new Vector2f(x, y);
		mapPos = new Vector2f(0, 0);
		
		player = new PlayerTest(pos, playerImage);
	}

	@Override
	public void render(GameContainer gc, Graphics g) throws SlickException {
		// map.draw(mapPos.x, mapPos.y);
		// draws the current map
		// mapFrames[(int) mapID.x][(int) mapID.y].draw(0, 0);
		tiledMapArr[(int) mapID.x][(int) mapID.y].render(0, 0);

		//playerImage.draw(pos.x, pos.y);

		g.drawString((int) mapID.x + ", " + (int) mapID.y, gc.getWidth() - 64,
				32);
		
		font1.drawString(32, 32, "player: (" + player.getX() + ", " +
				player.getY() + ")");

		//font1.drawString(32, 64, "part player: " + partPlayer(4, playerImage));
		font1.drawString(32,64, "player offset: " + player.getOffset());
		
		font1.drawString(32, 96, "tiledmap width: " + 
				tiledMapArr[(int) mapID.x][(int) mapID.y].getWidth()
				+ " * 32 = " + 
				tiledMapArr[(int) mapID.x][(int) mapID.y].getWidth() * 32);
		
		player.render(gc, g);
		
		// player.draw(pos.x, pos.y, 2.0f);
	}

	@Override
	public void update(GameContainer gc, int delta) throws SlickException {
		// TODO Auto-generated method stub

		input = gc.getInput();
		inputDelta -= delta;

		if (input.isKeyPressed(Input.KEY_ESCAPE)) {
			gc.exit();
		}

		if (input.isKeyPressed(Input.KEY_P)) {
			gc.setPaused(!gc.isPaused());
		}

		// TODO: Diagonals??
		// movementOne(input, delta);
		// movementTwo(input, delta);
		// 3: Moves the player around the map
		if(inputDelta < 0) {
			movePlayer(input, delta);
			inputDelta = 25;
		}
		// 4: Moves the map around the player
		// moveFour(input, delta);

		input.clearKeyPressedRecord();
	}

	/*
	private void movementOne(Input input, int delta) {
		if (input.isKeyDown(Input.KEY_DOWN)) {
			player = moves.getSprite(0, 1);
			pos.y += 5 * delta / 100;
		} else if (input.isKeyDown(Input.KEY_LEFT)) {
			player = moves.getSprite(1, 1);
			pos.x--;
		} else if (input.isKeyDown(Input.KEY_RIGHT)) {
			player = moves.getSprite(2, 1);
			pos.x++;
		} else if (input.isKeyDown(Input.KEY_UP)) {
			player = moves.getSprite(3, 1);
			pos.y--;
		} else {
			// no movement
			player = moves.getSprite(0, 0);
		}
	}
	*/
	
	/*
	private void movementTwo(Input input, int delta) {
		if (input.isKeyDown(Input.KEY_UP)) {
			player = moves.getSprite(3, 1);
		}
		if (input.isKeyDown(Input.KEY_RIGHT)) {
			player = moves.getSprite(2, 1);
		}
		if (input.isKeyDown(Input.KEY_LEFT)) {
			player = moves.getSprite(1, 1);
		}
		if (input.isKeyDown(Input.KEY_DOWN)) {
			player = moves.getSprite(0, 1);
		}
	}
	*/
	
	/**
	 * Moves the character around the map
	 * @param input
	 * @param delta
	 */
	private void movePlayer(Input input, int delta) {

		// TODO: Diagonals?
		// TODO: check for boundaries

		int spd = 10;

		if (input.isKeyDown(Input.KEY_LSHIFT)) {
			spd *= 3;
		}
		
		player.isMoving = false;
		//update the player's position and image/animation

		if (input.isKeyDown(Input.KEY_RIGHT)) {
			//moving = true;
			player.isMoving = true;
			player.update(Direction.RIGHT, gc, input, delta);
			//update the player's position and image/animation
			//player.update(gc, input, delta);
			//pos.x += spd * delta / 100;
			
			// check if the player collides with the right "wall"
			if(player.getPos().x + 3 * player.getOffset() > 
					tiledMapArr[(int)mapID.x][(int) mapID.y].getWidth() * TILE_SIZE) {
				//if there are more map screens to the right, change map
				if (mapID.x < tiledMapArr.length - 1) {
					mapID.x++;
					//pos.x = -(partPlayer(4, playerImage));
					player.setX(-(partPlayer(4, playerImage)));
				} else {
					//no more maps to the right, so collide & stay on screen
					//pos.x -= spd * delta / 100;
					player.move(Direction.LEFT, delta);
				}
			}
					
		}
		if (input.isKeyDown(Input.KEY_LEFT)) {
			player.isMoving = true;
			player.update(Direction.LEFT, gc, input, delta);
			//check if the player collided with the left "wall"
			if(player.getX() + player.getOffset() <= 0) {
				if(mapID.x > 0) {
					mapID.x--;
					player.setX(tiledMapArr[(int)mapID.x][(int)mapID.y]
							.getWidth() - 3 * player.getOffset());
				} else
					player.move(Direction.RIGHT, delta);
			}
			
			/*
			playerImage = moves.getSprite(1, 1);
			pos.x -= spd * delta / 100;
			if (pos.x + partPlayer(4, playerImage) <= 0) {
				if (mapID.x > 0) {
					mapID.x--;
					pos.x = tiledMapArr[(int) mapID.x][(int) mapID.y]
							.getWidth() - 3 * (partPlayer(4, playerImage));
				} else {
					pos.x += spd * delta / 100;
				}
			}
			*/
		}
		if (input.isKeyDown(Input.KEY_UP)) {
			player.isMoving = true;
			player.update(Direction.UP, gc, input, delta);
			//check if the player collided with the top "wall"
			if(player.getY() + player.getOffset() <= 0) {
				if (mapID.y > 0) {
					mapID.y--;
					player.setY(tiledMapArr[(int) mapID.x][(int) mapID.y]
					  .getHeight() * TILE_SIZE
					  - 3 * player.getOffset());
				} else {
					player.move(Direction.DOWN, delta);
				}
			}

			/*
			playerImage = moves.getSprite(3, 1);
			pos.y -= spd * delta / 100;
			if (pos.y + partPlayer(4, playerImage) <= 0) {
				if (mapID.y > 0) {
					mapID.y--;
					pos.y = tiledMapArr[(int) mapID.x][(int) mapID.y]
							.getHeight() * 32 
							- 3 * (partPlayer(4, playerImage));
				} else {
					pos.y += spd * delta / 100;
				}
			}
			*/
		}
		if (input.isKeyDown(Input.KEY_DOWN)) {
			player.isMoving = true;
			player.update(Direction.DOWN, gc, input, delta);
			//check if the player collided with the bottom "wall"
			if(player.getY() + 3 * player.getOffset() > 
					tiledMapArr[(int)mapID.x][(int)mapID.y].getHeight() * TILE_SIZE) {
				if (mapID.y < tiledMapArr[0].length - 1) {
					mapID.y++;
					pos.y = -(partPlayer(4, playerImage));
				} else {
					player.move(Direction.UP, delta);
				}
			}
			
			/*
			if (pos.y + 3 * partPlayer(4, playerImage) > tiledMapArr[(int) mapID.x][(int) mapID.y]
					.getHeight() * 32 ) {
				if (mapID.y < tiledMapArr[0].length - 1) {
					mapID.y++;
					pos.y = -(partPlayer(4, playerImage));
				} else {
					pos.y -= spd * delta / 100;
				}
			}
			*/
		}

		if (!player.isMoving)
			player.update(Direction.NONE, gc, input, delta);
			//playerImage = moves.getSprite(0, 0);

	}

	/**
	 * Moves the map around the character
	 * @param input
	 * @param delta
	 */
	private void moveFour(Input input, int delta) {

		boolean moving = false;
		int spd = 10;

		if (input.isKeyDown(Input.KEY_RIGHT)) {
			playerImage = moves.getSprite(2, 1);
			mapPos.x -= spd * delta / 100;
			moving = true;
		}
		if (input.isKeyDown(Input.KEY_LEFT)) {
			playerImage = moves.getSprite(1, 1);
			mapPos.x += spd * delta / 100;
			moving = true;
		}
		if (input.isKeyDown(Input.KEY_UP)) {
			playerImage = moves.getSprite(3, 1);
			mapPos.y += spd * delta / 100;
			moving = true;
		}
		if (input.isKeyDown(Input.KEY_DOWN)) {
			playerImage = moves.getSprite(0, 1);

			mapPos.y -= spd * delta / 100;
			moving = true;
		}

		if (!moving)
			playerImage = moves.getSprite(0, 0);
	}

	/*
	private int halfPlayer(Image player) {
		return (player.getWidth() / 2);
	}
	*/

	private int partPlayer(int parts, Image player) {
		return (player.getWidth() / parts);
	}

	public static void main(String[] args) {
		try {
			AppGameContainer agc = new AppGameContainer(new MovementTest(title));
			agc.setDisplayMode(640, 480, false);
			agc.setMinimumLogicUpdateInterval(20);
			agc.setMaximumLogicUpdateInterval(20);
			agc.setTargetFrameRate(60);
			agc.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
}
