import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

@SuppressWarnings("serial")
public class SpaceInvaders extends JFrame {
	private int moveSpeed = 6;

	private boolean endgame = false;
	private collisionRectangle endgameCollision;

	private int endgameCollisionX;

	private final boolean continueGame = false;
	private final int count = 0;
	private final Image Missle;
	private final Image player;
	private final Image mysteryNPC;
	private final Clip noise;
	private final Invader[][] enemy;
	private final Mystery mystery;
	private int shotSpeed = 15;
	private boolean debug = false; //this is for testing purposes
	private int neededScore = 220;
	private boolean SpecialEnemy = false;
	private int tickCounter = 0;
	private int scoreValue = 0;
	private Timer timer;
	private int PlayerX;
	private int ptx = 0;
	private int pty = 0;
	private Clip enemyNoise;
	private boolean shot = false;
	private boolean left = false;
	private boolean right = false;

	private boolean invaderMoveDown = false;
	private boolean invaderMove = true;

	public SpaceInvaders() {
		super("Space Invaders");

		player = getImage("img_base.gif");
		Missle = getImage("player_Missle.gif");
		noise = getSound("aud_basefire.wav");
		mysteryNPC = getImage("img_mystery.gif");

		var menubar = new JMenuBar();
		setJMenuBar(menubar);
		var game = new JMenu("Game");
		var help = new JMenu("Help");
		menubar.add(game);
		menubar.add(help);
		var newGame = game.add("New Game");
		var pause = game.add("Pause");
		var resume = game.add("Resume");
		var quit = game.add("Quit");
		var about = help.add("About...");
		//build the enemy array
		enemy = new Invader[5][10];
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 10; j++) {
				if (i == 0) {
					enemy[i][j] = new InvaderTop(1 + (j * 50), 10 + (i * 50));
				} else if (i == 1 || i == 2) {
					enemy[i][j] = new InvaderMiddle(1 + (j * 50), 10 + (i * 50));
				} else {
					enemy[i][j] = new InvaderBottom(1 + (j * 50), 10 + (i * 50));
				}
			}

		}
		// an invicible rectangle that spawns on the bottom row of the enemy array
		endgameCollision = new collisionRectangle(enemy[4][0].getX(), enemy[4][0].getY());


		//for the special enemy, i will first spawn an unmoving mysteryNPC offscreen, when SpecialEnemy is true, i will move it to the screen and make it move
		mystery = new Mystery(1000, 0);


		//timer in here
		timer = new Timer(20, e -> {
			//print out collision rectangle for testing purposes




			//right arrowkey moves player right
			if (right) {
				PlayerX += 5;
			}
			//left arrowkey moves player left
			if (left) {
				PlayerX -= 5;
			}

			//make map circular
			if (PlayerX > 600) {
				PlayerX = 0;
			}
			if (PlayerX < 0) {
				PlayerX = 600;
			}
			if (shot) {
				pty -= shotSpeed;
			}

			if (debug) {
				shotSpeed = 20;
			}


			for (int i = 0; i < 5; i++) {
				for (int j = 0; j < 10; j++) {
					//for messing with collision,
//                    if(enemy[i][j].getX() < ptx && enemy[i][j].getX() + 40 > ptx && enemy[i][j].getY() < pty && enemy[i][j].getY() + 40 > pty){
//                        enemy[i][j].setX(1000);
//                        enemy[i][j].setY(1000);
//                        shot = false;
//                        pty = 0;
//                        ptx = 0;
//                    }
					//much better collision detection under here
					Rectangle enemyColision = enemy[i][j].getBounds();
					Rectangle missleColision = new Rectangle(ptx, pty, 5, 5);
					if (enemyColision.intersects(missleColision)) {
						enemy[i][j].setY(1000);
						enemy[i][j].killed();
						shot = false;
						pty = 0;
						ptx = 0;
						//get enemy type
						String type = enemy[i][j].getClass().getName();
						//if enemy is top
						if (type.equals("InvaderTop")) {
							//play sound
							enemyNoise = getSound("aud_hit.wav");
							noise.setFramePosition(0);
							enemyNoise.start();
							//add points
							scoreValue += 10;

						}
						//if enemy is middle
						else if (type.equals("InvaderMiddle")) {
							//play sound
							enemyNoise = getSound("aud_hit.wav");
							noise.setFramePosition(0);
							enemyNoise.start();
							scoreValue += 5;
						}
						//if enemy is bottom
						else if (type.equals("InvaderBottom")) {
							//play sound
							enemyNoise = getSound("aud_hit.wav");
							noise.setFramePosition(0);
							enemyNoise.start();
							scoreValue += 1;
						}

					}
				}
			}

			//every 10 ticks, move enemies down one pixel
			//every 10 ticks, move enemies left or right. Once all the way in one direction, enemies will down one pixel.
			if (timer.getDelay() == 20) {
				if (invaderMoveDown) {
					for (int i = 0; i < 5; i++) {
						for (int j = 0; j < 10; j++) {
							enemy[i][j].setY(enemy[i][j].getY() +  3);

						}
					}
					//check to see if endgameCollision is colliding with enemys
					Rectangle endgameCollisionBounds = endgameCollision.getBounds();
					for (int i = 0; i < 5; i++) {
						for (int j = 0; j < 10; j++) {
							Rectangle enemyBounds = enemy[i][j].getBounds();
							if (enemyBounds.intersects(endgameCollisionBounds)) {
								//if it is, end the game
								endgameCollision.setY(endgameCollision.getY() + 3);

							}
						}
					}
					//divide by 0 error fixer
					if (moveSpeed <= 2){
						moveSpeed = 1;
					}
					else{
						moveSpeed = moveSpeed - 2;					}
					invaderMoveDown = false;
					//movespeed grows x^
				}
				//print movespeeed
				//get the X coord of the top right enemy
				int rightMostX = enemy[0][9].getX();
				//get the X coord of the bottom right enemy
				int rightMostX2 = enemy[4][9].getX();
				//print both coords
				System.out.println("Top ShiftX: " + rightMostX);
				System.out.println("Control: " + rightMostX2);



				for (int i = 0; i < 5; i++) {
					
					for (int j = 0; j < 10; j++) {
						if (tickCounter % moveSpeed == 0) {
							if (invaderMove) {

								enemy[i][j].setX(enemy[i][j].getX() - 1);
								for (int t = 0; t < enemy.length; t++) {
									for(Invader[] r : enemy) {
										if (r[t].getX() <= 0) {
											invaderMove = false;
											invaderMoveDown = true;
											for (int k = 0; k < enemy.length; k++) {
												if (enemy[k][t].getX() != enemy[0][t].getX()) {
													enemy[k][t].setX(enemy[0][t].getX());
												}
											}
										}
									}
								}
							}
							else {
								enemy[i][j].setX(enemy[i][j].getX() + 1);
								for (int t = enemy.length-1; t >=0; t--) {
									for(int r = enemy[t].length-1; r >= 0; r--) {
										if (enemy[t][r].getX() == 500) {
											invaderMove = true;
											invaderMoveDown = true;
											for (int k = 0; k < enemy.length; k++) {
												if (enemy[k][t].getX() != enemy[0][t].getX()) {
													enemy[k][t].setX(enemy[0][t].getX());
												}
											}
										}
									}
								}
							}
//							enemy[i][j].setY(enemy[i][j].getY() + 1);
						}


					}
				}
			}
			//check to see iof

			//if the collision rectangle is at the bottom of the screen, end the game
			//check if collision rectangle intersects with player
			Rectangle playerColision = new Rectangle(PlayerX, 400, 40, 40);
			Rectangle endgameColision = endgameCollision.getBounds();
			//print the y of player and collision rectangle for testing purposes
			if (endgameColision.intersects(playerColision)) {
				//play sound
				enemyNoise = getSound("aud_hit.wav");
				noise.setFramePosition(0);
				enemyNoise.start();
				//end game
				endgame = true;
			}



			//every 500 ticks, 50% chance to spawn special enemy
			if (timer.getDelay() == 20) {
				if (tickCounter % 501 == 0) { // 501 to account for the starting tick 
					int random = (int) (Math.random() * 2);
					if (random == 1 && !SpecialEnemy) {
						//move mysteryNPC to screen
						mystery.setX(0);
						mystery.setY(0);
						SpecialEnemy = true;
					}
				}
			}

			if (SpecialEnemy) {
				//move and wrap screen
				mystery.setX(mystery.getX() + 1);
				if (mystery.getX() > 600) {
					mystery.setX(0);
				}

			}

			//if special enemy is hit, move offscreen and add points whithout using collision detection
			if (mystery.getX() < ptx && mystery.getX() + 40 > ptx && mystery.getY() < pty && mystery.getY() + 40 > pty) {
				mystery.setX(1000);
				mystery.setY(1000);
				shot = false;
				pty = 0;
				ptx = 0;
				//move enemys up 10 pixels
				for (int i = 0; i < 5; i++) {
					for (int j = 0; j < 10; j++) {
						enemy[i][j].setY(enemy[i][j].getY() - 10);
					}
				}
				//disable special enemy
				SpecialEnemy = false;

			}

			//to check to see if game is over, instead of having a double for loop checking if all enemys are dead, I will
			//check the score, if all enemys are killed the score will be 220, once reached 220, new enemys will spawn.
			// the issue with this is that when the next wave of enemys spawn, the score will already be at 220, so I
			// made another var called scoreCheck.  It starts off at 220 and if a new wave is spawned, it addes 220 to it.
			// this is what is comapred to the score to see if a new wave is spawned


			//if continueGame is false, respawn all enemys and reset score
			if (scoreValue == neededScore) {
				neededScore += 220;
				for (int i = 0; i < 5; i++) {
					for (int j = 0; j < 10; j++) {
						if (i == 0) {
							enemy[i][j] = new InvaderTop(1 + (j * 50), 10 + (i * 50));
						} else if (i == 1 || i == 2) {
							enemy[i][j] = new InvaderMiddle(1 + (j * 50), 10 + (i * 50));
						} else {
							enemy[i][j] = new InvaderBottom(1 + (j * 50), 10 + (i * 50));
						}
					}

				}
			}



			tickCounter++;
			repaint();


		});

		//onstartup, initalize second smaller window for score that is placed right next to main window
		var score = new JFrame("Score");
		//get dimensions of users screen
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		//get center of screen
		int centerX = (int) screenSize.getWidth() / 2;
		int centerY = (int) screenSize.getHeight() / 2;
		//set location of score window to be right next to main window
		score.setLocation(centerX + 600, centerY - 250);
		score.setSize(200, 200);
		JLabel scoreText = new JLabel("Score: " + scoreValue);
		//scorefont bigger
		scoreText.setFont(new Font("Serif", Font.PLAIN, 30));
		score.add(scoreText);
		//center text
		score.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));

//		score.setVisible(true);


		score.addWindowListener(new WindowAdapter() {
			//display score
			@Override
			public void windowOpened(WindowEvent e) {
				super.windowOpened(e);

			}
		});
		//update scoreText
		timer.addActionListener(e -> {
			scoreText.setText("Score: " + scoreValue);
		});


		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				timer.stop();
				//close score window
				score.dispose();
				super.windowClosing(e);

			}
		});

		add(new JPanel() {
			{
				setBackground(Color.BLACK);
				setFocusable(true);
				addKeyListener(new KeyAdapter() {
					@Override
					public void keyPressed(KeyEvent e) {
						if (e.getKeyCode() == KeyEvent.VK_LEFT) {
							left = true;
						}
						if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
							right = true;
						}
					}

					@Override
					public void keyReleased(KeyEvent e) {
						if (e.getKeyCode() == KeyEvent.VK_ENTER) {
							if (endgame == true){

							}
						}

						//if tilda is pressed, enable debug
						if (e.getKeyCode() == KeyEvent.VK_BACK_QUOTE) {
							debug = !debug;
						}


						if (e.getKeyCode() == KeyEvent.VK_LEFT) {
							left = false;
						}
						if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
							right = false;
						}
						if (e.getKeyCode() == KeyEvent.VK_SPACE) {

							//if shot is traveling, player cant shoot until it hits something
							if (pty > 0) {
							} else {
								pty = 400;
								noise.setFramePosition(0);
								noise.start();
								shot = true;
								ptx = PlayerX;
							}
						}
					}

				});
			}

			@Override
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(player, PlayerX, 400, this);
				//if shot is true then fire missile from player going up
				if (shot) {
					g.drawImage(Missle, ptx + 12, pty, this);
				}

				//if endgame is true, draw string gameover under player
				if (endgame == true) {
					g.setColor(Color.RED);
					g.setFont(new Font("Serif", Font.PLAIN, 15));
					g.drawString("GAME OVER", 200, 200);
					g.drawString("Score: " + scoreValue, 200, 250);
					g.drawString("Press Enter to restart", 200, 300);
					endgameScreen();

				}
				//draw score under player
				g.setColor(Color.WHITE);
				g.setFont(new Font("Serif", Font.PLAIN, 15));
				g.drawString("Score: " + scoreValue, 255, 470);






				Graphics2D g2 = (Graphics2D) g;
				//draw enemys
				for (int i = 0; i < 5; i++) {
					for (int j = 0; j < 10; j++) {
						enemy[i][j].draw(g2);
					}
				}
				endgameCollision.draw(g2);

				//draw mystery ship
				mystery.draw(g2);
			}
		});


		timer.start();
		setSize(600, 600);
		setResizable(false);
//        getContentPane().setBackground(Color.BLACK);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		//When Quit is clicked, exit the application
		quit.addActionListener(e -> System.exit(0));
		about.addActionListener(e -> JOptionPane.showMessageDialog(this, "Space Invaders v1.0", "About", JOptionPane.INFORMATION_MESSAGE));
		//pause
		pause.addActionListener(e -> timer.stop());
		//resume
		resume.addActionListener(e -> timer.start());
		//newgame
		newGame.addActionListener(e -> {
			timer.stop();
			timer.start();
			//reset player position
			PlayerX = 0;
			//reset score
			scoreValue = 0;
			//reset lives
			for (int i = 0; i < 5; i++) {
				for (int j = 0; j < 10; j++) {
					if (i == 0) {
						enemy[i][j] = new InvaderTop(1 + (j * 50), 10 + (i * 50));
					} else if (i == 1 || i == 2) {
						enemy[i][j] = new InvaderMiddle(1 + (j * 50), 10 + (i * 50));
					} else {
						enemy[i][j] = new InvaderBottom(1 + (j * 50), 10 + (i * 50));
					}
				}

			}

		});


	}

	public static void main(String[] args) {
		var f = new SpaceInvaders();
		f.setVisible(true);

	}

	private Clip getSound(String filename) {
		Clip clip = null;
		try {
			InputStream in = getClass().getResourceAsStream(filename);
			InputStream buf = new BufferedInputStream(in);
			AudioInputStream stream = AudioSystem.getAudioInputStream(buf);
			clip = AudioSystem.getClip();
			clip.open(stream);
		} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
			e.printStackTrace();
		}
		return clip;
	}


	private Image getImage(String filename) {
		URL url = getClass().getResource(filename);
		ImageIcon icon = new ImageIcon(url);
		return icon.getImage();
	}

	private void endgameScreen(){
		//despawn all enemys
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 10; j++) {
				enemy[i][j].setX(1000);
				enemy[i][j].setY(1000);
			}
		}
		//despawn mystery ship
		mystery.setX(1000);
		mystery.setY(1000);
		//despawn player
		PlayerX = 0;
		//despawn shot
		pty = 0;
		//despawn endgame screen
		endgameCollision.setX(1000);
		endgameCollision.setY(1000);
		//move player off screen
		PlayerX = 1000;
		timer.stop();
		endgame = true;

	}
}
