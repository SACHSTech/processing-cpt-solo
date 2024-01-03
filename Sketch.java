import processing.core.PApplet;
import processing.core.PImage;
import java.util.ArrayList;
import java.util.Random;

public class Sketch extends PApplet {
  
  // Declare image variables
  PImage imgTitleScreen;
  PImage imgDeathScreen;
  PImage imgBackground;
  PImage imgBackgroundHitBoxes;
  PImage imgPlayer;
  PImage imgBullet;
  PImage imgHealth;
  PImage imgZombie;

  // Whether or not it is title screen
  boolean showTitleScreen;
  boolean showDeathScreen;
  boolean showEndScreen;
  // Location of the player on the Map
  float playerXMap;
  float playerYMap;
  // Location of the player on the Screen
  float playerXScreen;
  float playerYScreen;
  // Speed of the player
  float playerSpeedX;
  float playerSpeedY;
  // Rotation of the player
  float playerRotation;
  // Health of the player
  double health;
  // Location of the map on the screen
  float mapX;
  float mapY;
  ArrayList<Bullet> bullets = new ArrayList<Bullet>();

  // Zombie variables
  int wave;
  ArrayList<Zombie> zombies = new ArrayList<Zombie>();
  int spawnSide;
  float spawnX;
  float spawnY;
  int zombieHealth;
  float zombieAngle;
  float zombieScreenX;
  float zombieScreenY;

  Random zombieRandom = new Random();

  public void settings() {
    size(768, 768);
  }

  public void setup() {

    // Load images
    imgTitleScreen = loadImage("TitleScreen.png");
    imgDeathScreen = loadImage("DeathScreen.png");
    imgBackground = loadImage("Background.png");
    imgBackgroundHitBoxes = loadImage("BackgroundHitBoxes.png");
    imgBullet = loadImage("Bullet.png");
    imgPlayer = loadImage("PlayerWeapon1.png");
    imgHealth = loadImage("Heart.png");
    imgZombie = loadImage("Zombie.png");

    // Resize images
    imgBackground.resize(imgBackground.width / 4 * 3, imgBackground.height / 4 * 3);
    imgBackgroundHitBoxes.resize(imgBackgroundHitBoxes.width / 4 * 3, imgBackgroundHitBoxes.height / 4 * 3);
    imgPlayer.resize(imgPlayer.width / 4 * 3, imgPlayer.height / 4 * 3);
    imgPlayer.resize(imgPlayer.width / 5, imgPlayer.height / 5);
    imgBullet.resize(imgBullet.width / 25, imgBullet.height / 25);
    imgHealth.resize(imgHealth.width / 15, imgHealth.height / 15);
    imgZombie.resize(imgZombie.width / 8, imgZombie.height / 8);

    // Initialize Screen variables
    showTitleScreen = true; 
    showDeathScreen = false;
    showEndScreen = false;

    // Initialize Player variables
    playerXMap = -(imgBackground.width / 2 - imgPlayer.width / 2);
    playerYMap = -(imgBackground.height / 2 - imgPlayer.height / 2);
    health = 1000;
    wave = 0;
  }

  public void draw() {

    if (showTitleScreen) {
      background(imgTitleScreen);
    }
    else if (showDeathScreen) {
      background(imgDeathScreen);
      textFont(createFont("Constantia", 32));
      textSize(48);
      fill(136, 0, 27);
      text("You survived " + wave + " waves", width * 0.22f, height * 0.9f);
    }
    else {
      movement();
      drawBullets();
      drawHealth();

      if (zombies.size() == 0) {
        wave++;
        spawnZombies();
      }
      zombieAI();

      // Print number of zombies
      textFont(createFont("Arial", 16));
      fill(0);
      textAlign(LEFT, BOTTOM);
      text("Zombies: " + zombies.size(), 10, height - 10);
      text("Wave: " + wave, 10, height - 30);
      text("ZombieX: " + zombies.get(0).xMap, 10, height - 50);
      text("ZombieY: " + zombies.get(0).yMap, 10, height - 70);
      text("PlayerX: " + playerXMap, 10, height - 90);
      text("PlayerY: " + playerYMap, 10, height - 110);
      
    }
  }

  /*
   * Player movement and collision. Whether the player moves around the map, or the map moves around the player.
   * @author: Joshua Yin
   * @param none
   * @return void
   */
  public void movement() {

    // If the Player gets close to the edge of the map, the player moves instead of the map
    if (playerXMap > -width / 2) {
      mapX = 0;
      mapY = playerYMap + height / 2;
      playerXScreen = -playerXMap - imgPlayer.width / 2;
      playerYScreen = height / 2 - imgPlayer.height / 2;
    }
    else if (playerXMap < -(imgBackground.width - width / 2)) {
      mapX = -imgBackground.width + width;
      mapY = playerYMap + height / 2;
      playerXScreen = width - imgPlayer.width / 2 - (imgBackground.width + playerXMap);
      playerYScreen = height / 2 - imgPlayer.height / 2;
    }
    else if (playerYMap > -height / 2) {
      mapX = playerXMap + width / 2;
      mapY = 0;
      playerXScreen = width / 2 - imgPlayer.width / 2;
      playerYScreen = -playerYMap - imgPlayer.height / 2;
    }
    else if (playerYMap < -(imgBackground.height - height / 2)) {
      mapX = playerXMap + width / 2;
      mapY = -imgBackground.height + height;
      playerXScreen = width / 2 - imgPlayer.width / 2;
      playerYScreen = height - imgPlayer.height / 2 - (imgBackground.height + playerYMap);
    }

    // Otherwise, the player stays in the center of the screen and the map moves around the player
    else {
      mapX = playerXMap + width / 2;
      mapY = playerYMap + height / 2;
      playerXScreen = width / 2 - imgPlayer.width / 2;
      playerYScreen = height / 2 - imgPlayer.height / 2;
    }

    // Draws the hitbox map
    noTint();
    image(imgBackgroundHitBoxes, mapX, mapY);

    // Collision detection:

    // If the player goes to the edge of the map, the player stops moving
    if (playerXMap > 0) {
      playerXMap = 0;
    }
    else if (playerXMap < -(imgBackground.width)) {
      playerXMap = -(imgBackground.width);
    }
    else if (playerYMap > 0) {
      playerYMap = 0;
    }
    else if (playerYMap - imgPlayer.height / 2 < -(imgBackground.height)) {
      playerYMap = -(imgBackground.height);
    }

    // If the player collides with a black pixel, they stop moving

    else if (get((int)playerXScreen, (int)playerYScreen + imgPlayer.height / 2) == color(0, 0, 0)) {
      if (playerSpeedX > 0) {
        playerXMap -= playerSpeedX;
      }
      if (playerSpeedY > 0) {
        playerYMap -= playerSpeedY;
      }
    }
    else if (get((int)playerXScreen + imgPlayer.width, (int)playerYScreen + imgPlayer.height / 2) == color(0, 0, 0)) {
      if (playerSpeedX < 0) {
        playerXMap -= playerSpeedX;
      }
      if (playerSpeedY > 0) {
        playerYMap -= playerSpeedY;
      }
    }
    else if (get((int)playerXScreen, (int)playerYScreen + imgPlayer.height) == color(0, 0, 0)) {
      if (playerSpeedX > 0) {
        playerXMap -= playerSpeedX;
      }
      if (playerSpeedY < 0) {
        playerYMap -= playerSpeedY;
      }
    }
    else if (get((int)playerXScreen + imgPlayer.width, (int)playerYScreen + imgPlayer.height) == color(0, 0, 0)) {
      if (playerSpeedX < 0) {
        playerXMap -= playerSpeedX;
      }
      if (playerSpeedY < 0) {
        playerYMap -= playerSpeedY;
      }
    }
    
    // Moves the player
    playerXMap += playerSpeedX;
    playerYMap += playerSpeedY;

    // Draws the real map on top of the hitbox map
    image(imgBackground, mapX, mapY);

    // Rotate the player towards the mouse pointer
    playerRotation = atan2(mouseY - (playerYScreen + imgPlayer.height / 4 * 3), mouseX - (playerXScreen + imgPlayer.width / 2)) + PI / 2;

    // Draws the player with rotation
    pushMatrix();
    translate(playerXScreen + imgPlayer.width / 2, playerYScreen + imgPlayer.height * 0.75f);
    rotate(playerRotation);
    image(imgPlayer, -imgPlayer.width / 2, -imgPlayer.height);
    popMatrix();
  }

  public void drawBullets() {
  
    for (int i = 0; i < bullets.size(); i++) {

      // Move the bullet
      bullets.get(i).x += 40 * cos(bullets.get(i).angle - PI / 2);
      bullets.get(i).y += 40 * sin(bullets.get(i).angle - PI / 2);

      // Draw the bullet
      pushMatrix();
      translate(bullets.get(i).x + imgPlayer.width / 2, bullets.get(i).y + imgPlayer.height * 0.75f);
      rotate(bullets.get(i).angle);
      image(imgBullet, -imgBullet.width / 2, -imgBullet.height / 2 - imgPlayer.height * 0.75f);
      popMatrix();

      bullets.get(i).time++;

      // If the bullet is off the screen, remove it from the ArrayList
      if (bullets.get(i).x < 0 || bullets.get(i).x > width || bullets.get(i).y < 0 || bullets.get(i).y > height || bullets.get(i).time > 100) {
        bullets.remove(i);
      }
    }
  }

  public void drawHealth() { 
    
    if (health <= 0) {
      showDeathScreen = true;
    }
    else if (health < 1000 && health > 0) {
      health += 0.2;
    }

    for (int i = 0; i < Math.floor(health / 100); i++) {
      image(imgHealth, 10 + i * imgHealth.width, 10);
    }
    
    // Draw an additional heart
    tint(255, (int)(health % 100 * 255 / 100));
    image(imgHealth, 10 + imgHealth.width + ((int)Math.floor(health / 100) - 1) * imgHealth.width, 10);
  }

  public void spawnZombies() {

    for (int i = 0; i < Math.pow(wave, 2); i++) {
      spawnSide = zombieRandom.nextInt(4);
      if (spawnSide == 0) {
        spawnX = -zombieRandom.nextInt(imgBackground.width);
        spawnY = 0;
      }
      else if (spawnSide == 1) {
        spawnX = -imgBackground.width;
        spawnY = -zombieRandom.nextInt(imgBackground.height);
      }
      else if (spawnSide == 2) {
        spawnX = -zombieRandom.nextInt(imgBackground.width);
        spawnY = -imgBackground.height;
      }
      else if (spawnSide == 3) {
        spawnX = 0;
        spawnY = -zombieRandom.nextInt(imgBackground.height);  
      }
      zombies.add(new Zombie(spawnX, spawnY, 0, 0, 0, 0, 0, zombieHealth));
      }
  }
  

  public void zombieAI() {
    
    for (int i = 0; i < zombies.size(); i++) {

      // Makes the zombie rotate toward the player
      zombies.get(i).rotation = atan2(playerYMap - zombies.get(i).yMap, playerXMap - zombies.get(i).xMap);

      // Makes the zombie move toward the player
      zombies.get(i).xMap += 2 * cos(zombies.get(i).rotation);
      zombies.get(i).yMap += 2 * sin(zombies.get(i).rotation);
      
      // Determines where to draw the zombie on the screen
      zombies.get(i).xScreen = zombies.get(i).xMap - playerXMap + width / 2 - imgZombie.width / 2;
      zombies.get(i).yScreen = zombies.get(i).yMap - playerYMap + height / 2 - imgZombie.height / 2;

      // Draws the zombie
      pushMatrix();
      translate(zombies.get(i).xScreen + imgZombie.width / 2, zombies.get(i).yScreen + imgZombie.height / 2);
      rotate(zombies.get(i).rotation - HALF_PI);
      noTint();
      image(imgZombie, -imgZombie.width / 2, -imgZombie.height / 2);
      popMatrix();
    }
  } 

  public void keyPressed() {
    
    if (showTitleScreen == true) {
      showTitleScreen = false;
    }
    if (showDeathScreen == true && key == 'r') {
      setup();
    }
    
    if (key == 'w') {
      playerSpeedY = 5;
    }
    else if (key == 'a') {
      playerSpeedX = 5;
    }
    else if (key == 's') {
      playerSpeedY = -5;
    }
    else if (key == 'd') {
      playerSpeedX = -5;
    }

    if (keyCode == BACKSPACE) {
      health -= 100;
    }
  }

  public void keyReleased() {
    
    if (key == 'w') {
      playerSpeedY = 0;
    }
    else if (key == 'a') {
      playerSpeedX = 0;
    }
    else if (key == 's') {
      playerSpeedY = 0;
    }
    else if (key == 'd') {
      playerSpeedX = 0;
    }
  }

  public void mousePressed() {

    bullets.add(new Bullet(playerXScreen, playerYScreen, playerRotation, 0));

  }
}