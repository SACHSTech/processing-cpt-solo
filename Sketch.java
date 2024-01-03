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
  // Location of the player
  float playerX;
  float playerY;
  // Speed of the player
  float playerSpeedX;
  float playerSpeedY;
  // Rotation of the player
  float playerRotation;
  // Health of the player
  double health;
  int immunityFrames;

  ArrayList<Bullet> bullets = new ArrayList<Bullet>();

  // Zombie variables
  int wave;
  int waveTimer;
  ArrayList<Zombie> zombies = new ArrayList<Zombie>();
  int spawnSide;
  float spawnX;
  float spawnY;

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
    imgBackground.resize(768, 768);
    imgBackgroundHitBoxes.resize(768, 768);
    imgPlayer.resize(imgPlayer.width / 12, imgPlayer.height / 12);
    imgBullet.resize(imgBullet.width / 30, imgBullet.height / 30);
    imgHealth.resize(imgHealth.width / 15, imgHealth.height / 15);
    imgZombie.resize(imgZombie.width / 14, imgZombie.height / 14);

    // Initialize Screen variables
    showTitleScreen = true; 
    showDeathScreen = false;
    showEndScreen = false;

    // Initialize Player variables
    playerX = width / 2;
    playerY = height / 2;
    health = 1000;
    immunityFrames = 0;

    // Initialize Zombie variables
    wave = 0;
    zombies.clear();
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
      drawHealth();
      drawBullets();
      spawnZombies();
      zombieMovement();
      
      // Print number of zombies
      textFont(createFont("Arial", 16));
      fill(0);
      textAlign(LEFT, BOTTOM);
      text("Zombies: " + zombies.size(), 10, height - 10);
      text("Wave: " + wave, 10, height - 30);
      text("ZombieX: " + zombies.get(0).x, 10, height - 50);
      text("ZombieY: " + zombies.get(0).y, 10, height - 70);
      text("PlayerX: " + playerX, 10, height - 90);
      text("PlayerY: " + playerY, 10, height - 110);
            
    }
  }

  /*
   * Player movement and collision. Whether the player moves around the map, or the map moves around the player.
   * @author: Joshua Yin
   * @param none
   * @return void
   */
  public void movement() {

    // Player collision with border
    if (playerX < 0) {
      playerX = 0;
    }
    else if (playerX > width - imgPlayer.width) {
      playerX = width - imgPlayer.width;
    }
    if (playerY + imgPlayer.height / 2 < 0) {
      playerY = -imgPlayer.height / 2;
    }
    else if (playerY > height - imgPlayer.height) {
      playerY = height - imgPlayer.height;
    }


    background(imgBackgroundHitBoxes);
    
    // Player Collision with black pixels

    if (get((int)playerX, (int)playerY + imgPlayer.height / 2) == color(0, 0, 0)) {
      if (playerSpeedX < 0) {
        playerX -= playerSpeedX;
      }
      if (playerSpeedY < 0) {
        playerY -= playerSpeedY;
      }
    }
    else if (get((int)playerX + imgPlayer.width / 2, (int)playerY + imgPlayer.height / 2) == color(0, 0, 0)) {
      if (playerSpeedX > 0) {
        playerX -= playerSpeedX;
      }
      if (playerSpeedY < 0) {
        playerY -= playerSpeedY;
      }
    }
    else if (get((int)playerX + imgPlayer.width, (int)playerY + imgPlayer.height / 2) == color(0, 0, 0)) {
      if (playerSpeedX > 0) {
        playerX -= playerSpeedX;
      }
      if (playerSpeedY < 0) {
        playerY -= playerSpeedY;
      }
    }
    else if (get((int)playerX, (int)playerY + imgPlayer.height / 4 * 3) == color(0, 0, 0)) {
      if (playerSpeedX < 0) {
        playerX -= playerSpeedX;
      }
      if (playerSpeedY > 0) {
        playerY -= playerSpeedY;
      }
    }
    else if (get((int)playerX, (int)playerY + imgPlayer.height) == color(0, 0, 0)) {
      if (playerSpeedX < 0) {
        playerX -= playerSpeedX;
      }
      if (playerSpeedY > 0) {
        playerY -= playerSpeedY;
      }
    }
    else if (get((int)playerX + imgPlayer.width / 2, (int)playerY + imgPlayer.height) == color(0, 0, 0)) {
      if (playerSpeedX > 0) {
        playerX -= playerSpeedX;
      }
      if (playerSpeedY > 0) {
        playerY -= playerSpeedY;
      }
    }
    else if (get((int)playerX + imgPlayer.width, (int)playerY + imgPlayer.height / 4 * 3) == color(0, 0, 0)) {
      if (playerSpeedX > 0) {
        playerX -= playerSpeedX;
      }
      if (playerSpeedY > 0) {
        playerY -= playerSpeedY;
      }
    }
    else if (get((int)playerX + imgPlayer.width, (int)playerY + imgPlayer.height) == color(0, 0, 0)) {
      if (playerSpeedX > 0) {
        playerX -= playerSpeedX;
      }
      if (playerSpeedY > 0) {
        playerY -= playerSpeedY;
      }
    }

    // Draws the Background
    background(imgBackground);

    // Move the player
    playerX += playerSpeedX;
    playerY += playerSpeedY;

    // Rotate the player
    playerRotation = atan2(mouseY - playerY - imgPlayer.height / 2, mouseX - playerX - imgPlayer.width / 2) + PI / 2;

    // Draw the player
    pushMatrix();
    translate(playerX + imgPlayer.width / 2, playerY + imgPlayer.height / 4 * 3);
    rotate(playerRotation);
    noTint();
    image(imgPlayer, -imgPlayer.width / 2, -imgPlayer.height / 2);
    popMatrix();
  }

  public void drawBullets() {
  
    for (int i = 0; i < bullets.size(); i++) {

      // Move the bullet
      bullets.get(i).x += 20 * cos(bullets.get(i).angle - PI / 2);
      bullets.get(i).y += 20 * sin(bullets.get(i).angle - PI / 2);

      // Draw the bullet
      pushMatrix();
      translate(bullets.get(i).x + imgPlayer.width / 2, bullets.get(i).y + imgPlayer.height * 0.75f);
      rotate(bullets.get(i).angle);
      noTint();
      image(imgBullet, -imgBullet.width / 2, -imgBullet.height / 2 - imgPlayer.height * 0.75f);
      popMatrix();

      bullets.get(i).time++;

      // If the bullet is off the screen, remove it from the ArrayList
      if (bullets.get(i).x < 0 || bullets.get(i).x > width || bullets.get(i).y < 0 || 
          bullets.get(i).y > height || bullets.get(i).time > 100) {
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

    if (zombies.size() == 0) {

      wave++;
      waveTimer = 0;
      
      for (int i = 0; i < Math.ceil(Math.pow(wave, 2) / 2); i++) {

        spawnSide = zombieRandom.nextInt(4);

        if (spawnSide == 0) {
          spawnX = zombieRandom.nextInt(width);
          spawnY = 0;
        }
        else if (spawnSide == 1) {
          spawnX = width;
          spawnY = zombieRandom.nextInt(height);
        }
        else if (spawnSide == 2) {
          spawnX = zombieRandom.nextInt(width);
          spawnY = height;
        }
        else if (spawnSide == 3) {
          spawnX = 0;
          spawnY = zombieRandom.nextInt(height);
        }

        zombies.add(new Zombie(spawnX, spawnY, 0, 3));
      }
    }
  }

  public void zombieMovement() {

    try {
      for (int i = 0; i < zombies.size(); i++) {

        // Rotate the zombie
        zombies.get(i).rotation = atan2(playerY + imgPlayer.height / 2 - zombies.get(i).y - imgZombie.height / 2, 
                                        playerX + imgPlayer.width / 2 - zombies.get(i).x - imgZombie.width / 2) + PI / 2;

        // Move the zombie
        zombies.get(i).x += 1 * cos(zombies.get(i).rotation - PI / 2);
        zombies.get(i).y += 1 * sin(zombies.get(i).rotation - PI / 2);

        // Check collision with player
        if (dist(zombies.get(i).x, zombies.get(i).y, playerX, playerY) < imgZombie.width / 2 + imgPlayer.width / 2 && immunityFrames == 0) {
          health -= 100; // Reduce player's health by 100
          immunityFrames = 100; // Set immunity frames to 100
        }
        if (immunityFrames > 0) {
          immunityFrames--;
        }

        // Check collision with bullets
        for (int j = 0; j < bullets.size(); j++) {
          if (dist(zombies.get(i).x, zombies.get(i).y, bullets.get(j).x, bullets.get(j).y) < imgZombie.width / 2 + imgBullet.width / 2) {
            zombies.get(i).health -= 1; // Reduce zombie's health by 100
            bullets.remove(j); // Remove bullet
          }
        }
        
        // If the zombie's health is 0, remove it from the ArrayList
        if (zombies.get(i).health <= 0) {
          zombies.remove(i);
        }

        // Draw the zombie
        pushMatrix();
        translate(zombies.get(i).x + imgZombie.width / 2, zombies.get(i).y + imgZombie.height / 2);
        rotate(zombies.get(i).rotation + PI);
        noTint();
        image(imgZombie, -imgZombie.width / 2, -imgZombie.height / 2);
        popMatrix();

      }
    }
    catch (IndexOutOfBoundsException e) {
      spawnZombies();
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
      playerSpeedY = -2;
    }
    else if (key == 'a') {
      playerSpeedX = -2;
    }
    else if (key == 's') {
      playerSpeedY = 2;
    }
    else if (key == 'd') {
      playerSpeedX = 2;
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

    bullets.add(new Bullet(playerX, playerY, playerRotation, 0));

  }
}