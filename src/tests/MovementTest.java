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

public class MovementTest extends BasicGame {

	static String title = "Test Game - v1.0";

	//TODO: also include the smallfont, and maybe more fonts too
	AngelCodeFont font1;
	Input input;
	
	Image player;
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
	
	public MovementTest(String title) {
		super(title);
	}

	@Override
	public void init(GameContainer gc) throws SlickException {
		input = gc.getInput();
		
		font1 = new AngelCodeFont("res/font1.fnt", "res/font1_0.tga");
		
		images = new Image[5];

		moves = new SpriteSheet("res/tests_res/testMovement2.png", 32, 32);
		player = moves.getSprite(0, 0);
		map = new Image("res/tests_res/testMap1.png");

		mapID = new Vector2f(0, 1);
		mapFrames = new Image[3][3];
		for (int k = 0; k < mapFrames.length; k++) {
			mapFrames[k][0] = map;
			mapFrames[k][1] = map;
			mapFrames[k][2] = map;
		}

		// TODO: Make a loop to initialize the Tiled maps
		tiledMapArr = new TiledMap[1][3];
		tiledMapArr[0][0] = new TiledMap("res/tests_res/map0-0.tmx");
		tiledMapArr[0][1] = new TiledMap("res/tests_res/map0-1.tmx");
		tiledMapArr[0][2] = new TiledMap("res/tests_res/map0-2.tmx");

		int x = (gc.getWidth() / 2) - (player.getWidth() / 2);
		int y = (gc.getHeight() / 2) - (player.getHeight() / 2);
		pos = new Vector2f(x, y);
		mapPos = new Vector2f(0, 0);
	}

	@Override
	public void render(GameContainer gc, Graphics g) throws SlickException {
		// map.draw(mapPos.x, mapPos.y);
		// draws the current map
		// mapFrames[(int) mapID.x][(int) mapID.y].draw(0, 0);
		tiledMapArr[(int) mapID.x][(int) mapID.y].render(0, 0);

		player.draw(pos.x, pos.y);

		g.drawString((int) mapID.x + ", " + (int) mapID.y, gc.getWidth() - 64,
				32);
		
		font1.drawString(32, 32, "player: (" + pos.x + ", " +
				pos.y + ")");

		font1.drawString(32, 64, "part player: " + partPlayer(4, player));
		
		font1.drawString(32, 96, "tiledmap width: " + 
				tiledMapArr[(int) mapID.x][(int) mapID.y].getWidth()
				+ " * 32 = " + 
				tiledMapArr[(int) mapID.x][(int) mapID.y].getWidth() * 32);
		
		// player.draw(pos.x, pos.y, 2.0f);
	}

	@Override
	public void update(GameContainer gc, int delta) throws SlickException {
		// TODO Auto-generated method stub

		input = gc.getInput();

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
		movePlayer(input, delta);
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
		boolean moving = false;

		// TODO: Diagonals?
		// TODO: check for boundaries

		int spd = 10;

		if (input.isKeyDown(Input.KEY_LSHIFT)) {
			spd *= 3;
		}

		if (input.isKeyDown(Input.KEY_RIGHT)) {
			moving = true;
			// if not moving, set the sprite to be moving
			// I think this will improve efficiency slightly
			//if (!moving)
			// ^ did not work. 
			// TODO: make movement efficient, so the player sprite is not
			// 		re-set every loop
			player = moves.getSprite(2, 1);
			pos.x += spd * delta / 100;
			if (pos.x + 3 * partPlayer(4, player) > tiledMapArr[(int) mapID.x][(int) mapID.y]
					.getWidth() * 32 /* TODO: FIX */) {
				if (mapID.x < tiledMapArr.length - 1) {
					mapID.x++;
					pos.x = -(partPlayer(4, player));
				} else {
					pos.x -= spd * delta / 100;
				}
			}
			// if (pos.x + 3 * partPlayer(4, player) > mapFrames[(int)
			// mapID.x][(int) mapID.y]
			// .getWidth()) {
			// if (mapID.x < mapFrames[0].length - 1) {
			// mapID.x++;
			// pos.x = -(partPlayer(4, player));
			// } else {
			// pos.x -= spd * delta / 100;
			// }
			// }
		}
		if (input.isKeyDown(Input.KEY_LEFT)) {
			moving = true;
			player = moves.getSprite(1, 1);
			pos.x -= spd * delta / 100;
			if (pos.x + partPlayer(4, player) <= 0) {
				if (mapID.x > 0) {
					mapID.x--;
					pos.x = tiledMapArr[(int) mapID.x][(int) mapID.y]
							.getWidth() - 3 * (partPlayer(4, player));
				} else {
					pos.x += spd * delta / 100;
				}
			}
			// if (pos.x + partPlayer(4, player) <= 0) {
			// if (mapID.x > 0) {
			// mapID.x--;
			// pos.x = mapFrames[(int) mapID.x][(int) mapID.y]
			// .getWidth() - 3 * (partPlayer(4, player));
			// } else {
			// pos.x += spd * delta / 100;
			// }
			// }
		}
		if (input.isKeyDown(Input.KEY_UP)) {
			moving = true;
			player = moves.getSprite(3, 1);
			pos.y -= spd * delta / 100;
			if (pos.y + partPlayer(4, player) <= 0) {
				if (mapID.y > 0) {
					mapID.y--;
					pos.y = tiledMapArr[(int) mapID.x][(int) mapID.y]
							.getHeight() * 32 /* TODO: FIX */
							- 3 * (partPlayer(4, player));
				} else {
					pos.y += spd * delta / 100;
				}
			}

			// if (pos.y + partPlayer(4, player) <= 0) {
			// if (mapID.y > 0) {
			// mapID.y--;
			// pos.y = mapFrames[(int) mapID.x][(int) mapID.y]
			// .getHeight() - 3 * (partPlayer(4, player));
			// } else {
			// pos.y += spd * delta / 100;
			// }
			// }
		}
		if (input.isKeyDown(Input.KEY_DOWN)) {
			moving = true;
			player = moves.getSprite(0, 1);
			pos.y += spd * delta / 100;
			if (pos.y + 3 * partPlayer(4, player) > tiledMapArr[(int) mapID.x][(int) mapID.y]
					.getHeight() * 32 /* TODO: FIX */) {
				if (mapID.y < tiledMapArr[0].length - 1) {
					mapID.y++;
					pos.y = -(partPlayer(4, player));
				} else {
					pos.y -= spd * delta / 100;
				}
			}
			// if (pos.y + 3 * partPlayer(4, player) > mapFrames[(int)
			// mapID.x][(int) mapID.y]
			// .getHeight()) {
			// if (mapID.y < mapFrames[0].length - 1) {
			// mapID.y++;
			// pos.y = -(partPlayer(4, player));
			// } else {
			// pos.y -= spd * delta / 100;
			// }
			// }
		}

		if (!moving)
			player = moves.getSprite(0, 0);

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
			player = moves.getSprite(2, 1);
			mapPos.x -= spd * delta / 100;
			moving = true;
		}
		if (input.isKeyDown(Input.KEY_LEFT)) {
			player = moves.getSprite(1, 1);
			mapPos.x += spd * delta / 100;
			moving = true;
		}
		if (input.isKeyDown(Input.KEY_UP)) {
			player = moves.getSprite(3, 1);
			mapPos.y += spd * delta / 100;
			moving = true;
		}
		if (input.isKeyDown(Input.KEY_DOWN)) {
			player = moves.getSprite(0, 1);

			mapPos.y -= spd * delta / 100;
			moving = true;
		}

		if (!moving)
			player = moves.getSprite(0, 0);
	}

	private int halfPlayer(Image player) {
		return (player.getWidth() / 2);
	}

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
