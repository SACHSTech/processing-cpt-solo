import processing.core.PApplet;
import processing.core.PImage;
import java.util.ArrayList;
import java.util.LinkedList;

public class Sketch extends PApplet {
  
  PImage imgTitleImage;
  PImage imgBackground;
  PImage imgBackgroundHitBoxes;
  PImage imgPlayer;
  PImage imgBullet;

  boolean playGame;

  // Location of the player on the Map
  float playerXMap;
  float playerYMap;
  // Location of the player on the Screen
  float playerXScreen;
  float playerYScreen;
  float playerSpeedX;
  float playerSpeedY;
  float mapX;
  float mapY;
  boolean isColliding;
  float playerRotation;
  
  ArrayList<Bullet> bullets = new ArrayList<Bullet>();
  
  public void settings() {
    size(768, 768);
  }

  public void setup() {

    imgTitleImage = loadImage("TitleScreen.png");
    imgBackground = loadImage("Background.png");
    imgBackgroundHitBoxes = loadImage("BackgroundHitBoxes.png");
    imgBullet = loadImage("Bullet.png");

    imgPlayer = loadImage("PlayerWeapon1.png");

    imgBackground.resize(imgBackground.width / 4 * 3, imgBackground.height / 4 * 3);
    imgBackgroundHitBoxes.resize(imgBackgroundHitBoxes.width / 4 * 3, imgBackgroundHitBoxes.height / 4 * 3);
    imgPlayer.resize(imgPlayer.width / 4 * 3, imgPlayer.height / 4 * 3);
    imgPlayer.resize(imgPlayer.width / 5, imgPlayer.height / 5);
    imgBullet.resize(imgBullet.width / 25, imgBullet.height / 25);

    playGame = false;

    playerXMap = -(imgBackground.width / 2 - imgPlayer.width / 2);
    playerYMap = -(imgBackground.height / 2 - imgPlayer.height / 2);  

  }

  public void draw() {

    if (!playGame) {
      background(imgTitleImage);
    }
    else {
      collision();

      // Print playerX and playerY at the bottom right of the application
      textAlign(RIGHT, BOTTOM);
      textSize(16);
      fill(0);
      text("playerX: " + playerXMap + ", playerY: " + playerYMap, width, height);
      text("playerWidth: " + imgPlayer.width + ", playerHeight: " + imgPlayer.height, width, height - 16);
      text("MapSizeX: " + imgBackground.width + ", MapSizeY: " + imgBackground.height, width, height - 32);
      text("ScreenSizeX: " + width + ", ScreenSizeY: " + height, width, height - 48);
      
      // Draw the bullets
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

        // If the bullet is off the screen, remove it from the ArrayList
        if (bullets.get(i).x < 0 || bullets.get(i).x > width || bullets.get(i).y < 0 || bullets.get(i).y > height) {
          bullets.remove(i);
        }
      }
    }
  }

  /*
   * Collision detection and moving the map around the player.
   * @author: Joshua Yin
   * @param none
   * @return void
   */
  public void collision() {

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
    image(imgBackgroundHitBoxes, mapX, mapY);

    // Collision detection
    // If the player goes to the edge of the map, the player stops moving
    if (playerXMap > 0) {
      isColliding = true;
      playerXMap = 0;
    }
    else if (playerXMap < -(imgBackground.width)) {
      playerXMap = -(imgBackground.width);
    }
    else if (playerYMap > 0) {
      playerYMap = 0;
    }
    else if (playerYMap < -(imgBackground.height)) {
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

    // Draws the real map over the hitbox map
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

  public void keyPressed() {
    
    if (playGame == false) {

      playGame = true;
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