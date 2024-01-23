import processing.core.PApplet;
import processing.core.PImage;
import java.util.ArrayList;
import java.util.Random;

public class Sketch extends PApplet {
  
  // Declare image variables
  PImage imgTitleScreen;
  PImage imgDeathScreen;
  PImage imgWinScreen;
  PImage imgBackground;
  PImage imgBackgroundHitBoxes;
  PImage imgPlayerW1;
  PImage imgPlayerW2;
  PImage imgBullet;
  PImage imgHealth;
  PImage imgZombie;
  

  // Whether or not it is title screen
  boolean blnShowTitleScreen;
  boolean blnShowDeathScreen;
  boolean blnShowEndScreen;
  boolean blnShowWinScreen;
  // Location of the player
  float fltPlayerX;
  float fltPlayerY;
  // Speed of the player
  float fltPlayerSpeedX;
  float fltPlayerSpeedY;
  // Rotation of the player
  float fltPlayerRotation;
  // Health of the player
  double dblHealth;
  // How many frames of immunity the player has after being hit by a zombie
  int intImmunityFrames;
  // Reload timer for the assault rifle
  int intReloadTimer;
  // Calculated inaccuracy angle of the assault rifle
  float fltInaccuracyAngle;

  // Bullet ArrayList
  ArrayList<Bullet> bullets = new ArrayList<Bullet>();



  // Zombie variables:
  // Wave number
  int intWave;
  // Zombie ArrayList
  ArrayList<Zombie> zombies = new ArrayList<Zombie>();
  // Spawn variables
  int intSpawnSide;
  float intSpawnX;
  float intSpawnY;

  // Random object
  Random random = new Random();



  public void settings() {
    size(768, 768);
  }



  public void setup() {

    // Load images
    imgTitleScreen = loadImage("Images/TitleScreen.png");
    imgDeathScreen = loadImage("Images/DeathScreen.png");
    imgWinScreen = loadImage("Images/WinScreen.png");
    imgBackground = loadImage("Images/Background.png");
    imgBackgroundHitBoxes = loadImage("Images/BackgroundHitBoxes.png");
    imgBullet = loadImage("Images/Bullet.png");
    imgPlayerW1 = loadImage("Images/PlayerWeapon1.png");
    imgPlayerW2 = loadImage("Images/PlayerWeapon2.png");
    imgHealth = loadImage("Images/Heart.png");
    imgZombie = loadImage("Images/Zombie.png");

    // Resize images
    imgBackground.resize(768, 768);
    imgBackgroundHitBoxes.resize(768, 768);
    imgPlayerW1.resize(imgPlayerW1.width / 12, imgPlayerW1.height / 12);
    imgPlayerW2.resize(imgPlayerW2.width / 12, imgPlayerW2.height / 12);
    imgBullet.resize(imgBullet.width / 30, imgBullet.height / 30);
    imgHealth.resize(imgHealth.width / 15, imgHealth.height / 15);
    imgZombie.resize(imgZombie.width / 14, imgZombie.height / 14);

    // Set the screen to the title screen
    blnShowTitleScreen = true; 
    blnShowDeathScreen = false;
    blnShowEndScreen = false;
    blnShowWinScreen = false;

    // Initialize Player variables
    fltPlayerX = width / 2;
    fltPlayerY = height / 2;
    dblHealth = 1000;
    intImmunityFrames = 0;

    // Misc. variables
    intWave = 0;
    zombies.clear();
    bullets.clear();
  }



  public void draw() {

    // Title Screen
    if (blnShowTitleScreen) {
      background(imgTitleScreen);
    }

    // Death Screen
    else if (blnShowDeathScreen) {

      background(imgDeathScreen);
      textFont(createFont("Constantia", 32));
      textSize(48);
      fill(136, 0, 27);
      // Prints the number of waves the player survived
      text("You survived " + intWave + " waves", width * 0.22f, height * 0.9f);
    }

    // Win Screen
    else if (blnShowWinScreen) {
      background(imgWinScreen);
    }

    // Game Screen
    else {

      // The player unlocks the assault rifle after surviving 5 waves
      if (intWave >= 5) {
        assaultRifle();
      }

      // The player wins after surviving 10 waves
      if (intWave == 11) {
        blnShowWinScreen = true;
      }

      // Calls all the methods
      Movement();
      DrawHealth();
      DrawBullets();
      SpawnZombies();
      ZombieMovement();

      // Creates a font for the text
      textFont(createFont("Constantia", 20));
      textSize(48);
      fill(136, 0, 27);
      // Displays the wave number
      text("Wave " + intWave, 10, height * 0.95f);
      // Displays the number of zombies left
      text(zombies.size() + " zombies left", 10, height * 0.85f);

    }
  }


  
  /*
   * Player movement and collision. Also draws the player.
   * @author: Joshua Yin
   * @param none
   * @return void
   */
  public void Movement() {

    // If the player is out of bounds, set the player's position to the edge of the screen
    // Left border
    if (fltPlayerX < 0) {
      fltPlayerX = 0;
    }
    // Right border
    else if (fltPlayerX > width - imgPlayerW1.width) {
      fltPlayerX = width - imgPlayerW1.width;
    }
    // Top border
    if (fltPlayerY + imgPlayerW1.height / 2 < 0) {
      fltPlayerY = -imgPlayerW1.height / 2;
    }
    // Bottom border
    else if (fltPlayerY > height - imgPlayerW1.height) {
      fltPlayerY = height - imgPlayerW1.height;
    }

    // Draws the hitbox map
    background(imgBackgroundHitBoxes);

    // Checks for collision with the hitbox map
    // If the player is colliding with a black pixel, move the player back

    // Top left corner
    if (get((int)fltPlayerX, (int)fltPlayerY + imgPlayerW1.height / 2) == color(0, 0, 0)) {
      if (fltPlayerSpeedX < 0) {
        fltPlayerX -= fltPlayerSpeedX;
      }
      if (fltPlayerSpeedY < 0) {
        fltPlayerY -= fltPlayerSpeedY;
      }
    }

    // Top middle
    else if (get((int)fltPlayerX + imgPlayerW1.width / 2, (int)fltPlayerY + imgPlayerW1.height / 2) == color(0, 0, 0)) {
      if (fltPlayerSpeedX > 0) {
        fltPlayerX -= fltPlayerSpeedX;
      }
      if (fltPlayerSpeedY < 0) {
        fltPlayerY -= fltPlayerSpeedY;
      }
    }

    // Top right corner
    else if (get((int)fltPlayerX + imgPlayerW1.width, (int)fltPlayerY + imgPlayerW1.height / 2) == color(0, 0, 0)) {
      if (fltPlayerSpeedX > 0) {
        fltPlayerX -= fltPlayerSpeedX;
      }
      if (fltPlayerSpeedY < 0) {
        fltPlayerY -= fltPlayerSpeedY;
      }
    }

    // Middle left
    else if (get((int)fltPlayerX, (int)fltPlayerY + imgPlayerW1.height / 4 * 3) == color(0, 0, 0)) {
      if (fltPlayerSpeedX < 0) {
        fltPlayerX -= fltPlayerSpeedX;
      }
      if (fltPlayerSpeedY > 0) {
        fltPlayerY -= fltPlayerSpeedY;
      }
    }

    // Bottom left corner
    else if (get((int)fltPlayerX, (int)fltPlayerY + imgPlayerW1.height) == color(0, 0, 0)) {
      if (fltPlayerSpeedX < 0) {
        fltPlayerX -= fltPlayerSpeedX;
      }
      if (fltPlayerSpeedY > 0) {
        fltPlayerY -= fltPlayerSpeedY;
      }
    }

    // Bottom middle
    else if (get((int)fltPlayerX + imgPlayerW1.width / 2, (int)fltPlayerY + imgPlayerW1.height) == color(0, 0, 0)) {
      if (fltPlayerSpeedX > 0) {
        fltPlayerX -= fltPlayerSpeedX;
      }
      if (fltPlayerSpeedY > 0) {
        fltPlayerY -= fltPlayerSpeedY;
      }
    }

    // Right middle
    else if (get((int)fltPlayerX + imgPlayerW1.width, (int)fltPlayerY + imgPlayerW1.height / 4 * 3) == color(0, 0, 0)) {
      if (fltPlayerSpeedX > 0) {
        fltPlayerX -= fltPlayerSpeedX;
      }
      if (fltPlayerSpeedY > 0) {
        fltPlayerY -= fltPlayerSpeedY;
      }
    }

    // Bottom right corner
    else if (get((int)fltPlayerX + imgPlayerW1.width, (int)fltPlayerY + imgPlayerW1.height) == color(0, 0, 0)) {
      if (fltPlayerSpeedX > 0) {
        fltPlayerX -= fltPlayerSpeedX;
      }
      if (fltPlayerSpeedY > 0) {
        fltPlayerY -= fltPlayerSpeedY;
      }
    }

    // Draws the Background image
    background(imgBackground);


    // Move the player
    fltPlayerX += fltPlayerSpeedX;
    fltPlayerY += fltPlayerSpeedY;

    // Rotate the player to face the mouse cursor
    fltPlayerRotation = atan2(mouseY - fltPlayerY - imgPlayerW1.height / 2, mouseX - fltPlayerX - imgPlayerW1.width / 2) + PI / 2;

    // Draw the player
    pushMatrix();
    translate(fltPlayerX + imgPlayerW1.width / 2, fltPlayerY + imgPlayerW1.height / 4 * 3);
    rotate(fltPlayerRotation);
    noTint();

    // Before wave 5, the player uses a normal rifle
    if (intWave < 5) {
      image(imgPlayerW1, -imgPlayerW1.width / 2, -imgPlayerW1.height / 2);
    }
    // After wave 5, the player unlocks an assault rifle
    else {
      image(imgPlayerW2, -imgPlayerW2.width / 2, -imgPlayerW2.height / 2);
    }

    popMatrix();
  }
  


  /*
   * Assault rifle method. Called after wave 5. Allows the player to shoot bullets by holding the mouse instead of clicking.
   * @author Joshua Yin
   * @param none
   * @return void
   */
  public void assaultRifle() {

    // Calculate the inaccuracy angle of the assault rifle
    // The player's gun will become more accurate as the waves go on
    fltInaccuracyAngle = PI / intWave;

    // If the mouse is pressed and the reload timer is 0, add a bullet to the ArrayList
    if (mousePressed && intReloadTimer <= 0) {

      // Creates new bullet object and adds it to the ArrayList
      bullets.add(new Bullet(fltPlayerX, fltPlayerY, fltPlayerRotation + (float)random.nextDouble(fltInaccuracyAngle) - fltInaccuracyAngle / 2, 0));

      // Reset the reload timer
      // The player reloads faster the more waves they survive
      intReloadTimer = 15 - intWave;
    }

    // If the reload timer is greater than 0, decrement it
    if (intReloadTimer > 0) {
      intReloadTimer--;
    }
  } 



  /*
   * Draws the bullets that you shoot. 
   * @author Joshua Yin
   * @param none
   * @return void
   */

  public void DrawBullets() {
    
    // Loops through the ArrayList
    for (int i = 0; i < bullets.size(); i++) {

      // Move the bullet in the direction it is facing
      bullets.get(i).x += 20 * cos(bullets.get(i).angle - PI / 2);
      bullets.get(i).y += 20 * sin(bullets.get(i).angle - PI / 2);

      // Draw the bullet
      pushMatrix();
      translate(bullets.get(i).x + imgPlayerW1.width / 2, bullets.get(i).y + imgPlayerW1.height * 0.75f);
      rotate(bullets.get(i).angle);
      noTint();
      image(imgBullet, -imgBullet.width / 2, -imgBullet.height / 2 - imgPlayerW1.height * 0.75f);
      popMatrix();

      // Increment the bullet's time
      bullets.get(i).time++;

      // If the bullet is off the screen or the time spent on screen is over 100, remove it from the ArrayList
      if (bullets.get(i).x < 0 || bullets.get(i).x > width || bullets.get(i).y < 0 || 
          bullets.get(i).y > height || bullets.get(i).time > 100) {
        bullets.remove(i);
      }
    }
  }



  /*
   * Draws the health bar of the player. Also checks if the player is dead. 
   * @author Joshua Yin
   * @param none
   * @return void
   */

  public void DrawHealth() { 
    
    // If the player's health is 0, show the death screen
    if (dblHealth <= 0) {
      blnShowDeathScreen = true;
    }

    // If the player's health is less than 1000, slowly regenerate it
    else if (dblHealth < 1000 && dblHealth > 0) {
      dblHealth += 0.2;
    }

    // Draw the health bar
    for (int i = 0; i < Math.floor(dblHealth / 100); i++) {
      image(imgHealth, 10 + i * imgHealth.width, 10);
    }
    
    // Draw an additional heart
    // The heart will be partially transparent based on how close the player is to the next 100 health
    tint(255, (int)(dblHealth % 100 * 255 / 100));
    image(imgHealth, 10 + imgHealth.width + ((int)Math.floor(dblHealth / 100) - 1) * imgHealth.width, 10);
  }



  /*
   * Spawns zombies. Called when there are no zombies left on the screen. 
   * @author Joshua Yin
   * @param none
   * @return void 
   */

  public void SpawnZombies() {

    // If there are no zombies left, spawn a new wave
    if (zombies.size() == 0) {

      intWave++;
      
      // The number of zombies spawned is equal to the wave number squared
      for (int i = 0; i < Math.pow(intWave, 2); i++) {

        // Randomly choose a side of the screen to spawn the zombie
        intSpawnSide = random.nextInt(4);

        // Randomly choose a location on the edge to spawn the zombie
        if (intSpawnSide == 0) {
          intSpawnX = random.nextInt(width);
          intSpawnY = 0;
        }
        else if (intSpawnSide == 1) {
          intSpawnX = width;
          intSpawnY = random.nextInt(height);
        }
        else if (intSpawnSide == 2) {
          intSpawnX = random.nextInt(width);
          intSpawnY = height;
        }
        else if (intSpawnSide == 3) {
          intSpawnX = 0;
          intSpawnY = random.nextInt(height);
        }

        // Add the zombie to the ArrayList
        zombies.add(new Zombie(intSpawnX, intSpawnY, 0, 3));
      }
    }
  }



  /*
   * Zombie AI pathfinding. Also checks for collision with the player and bullets. Also draws the zombies.
   * @author Joshua Yin
   * @param none
   * @return void
   */
  public void ZombieMovement() {

    // Try-catch block to prevent IndexOutOfBoundsException
    try {

      // Loops through the ArrayList
      for (int i = 0; i < zombies.size(); i++) {
        
        // Rotate the zombie to face the player
        zombies.get(i).rotation = atan2(fltPlayerY + imgPlayerW1.height / 2 - zombies.get(i).y - imgZombie.height / 2, 
                                        fltPlayerX + imgPlayerW1.width / 2 - zombies.get(i).x - imgZombie.width / 2) + PI / 2;

        // Move the zombie in the direction it is facing
        zombies.get(i).x += 1 * cos(zombies.get(i).rotation - PI / 2);
        zombies.get(i).y += 1 * sin(zombies.get(i).rotation - PI / 2);

        // Check collision with player
        // If the zombie is touching the player and the player is not immune, reduce the player's health by 100
        if (dist(zombies.get(i).x, zombies.get(i).y, fltPlayerX, fltPlayerY) < imgZombie.width / 2 && intImmunityFrames == 0) {
          dblHealth -= 100; // Reduce player's health by 100
          intImmunityFrames = 100; // Set immunity frames to 100
        }

        // Zombie hit cooldown
        if (intImmunityFrames > 0) {
          intImmunityFrames--;
        }

        // Check collision with bullets
        for (int j = 0; j < bullets.size(); j++) {

          // If the zombie is touching a bullet, reduce the zombie's health by 1 and remove the bullet
          if (dist(zombies.get(i).x, zombies.get(i).y, bullets.get(j).x, bullets.get(j).y) < imgZombie.width / 2 + imgBullet.width) {
            zombies.get(i).health -= 1;
            bullets.remove(j);
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

    // If the IndexOutOfBoundsException is caught, spawn a new wave
    catch (IndexOutOfBoundsException e) {
      SpawnZombies();
    }
  }


  /*
   * Key pressed method. Called when a key is pressed. Used for movement and debugging.
   * @author Joshua Yin
   * @param none
   * @return void
   */

  public void keyPressed() {
    
    // If any key is pressed and the title screen is showing, play the game
    if (blnShowTitleScreen) {
      blnShowTitleScreen = false;
    }

    // If the death screen is showing and the player presses 'r', restart the game
    if ((blnShowDeathScreen || blnShowWinScreen) && key == 'r') {
      setup();
    }

    // If the player is on the Win Screen and presses space, they continue the game on freeplay mode
    if (blnShowWinScreen && key == ' ') {
      blnShowWinScreen = false;
      zombies.clear();
      intWave = 12;
    }
    
    // Movement WASD
    if (key == 'w') {
      fltPlayerSpeedY = -2;
    }
    else if (key == 'a') {
      fltPlayerSpeedX = -2;
    }
    else if (key == 's') {
      fltPlayerSpeedY = 2;
    }
    else if (key == 'd') {
      fltPlayerSpeedX = 2;
    }
    
    // Debugging keys
    // Pressing backspace will reduce the player's health by 100
    if (keyCode == BACKSPACE) {
      dblHealth -= 100;
    }
    // Pressing enter will increase the player's health by 100
    if (keyCode == ENTER) {
      dblHealth += 100;
    }
    // Pressing shift will clear the screen of zombies
    if (keyCode == SHIFT) {
      zombies.clear();
    }
  }



  /*
   * Key released method. Called when a key is released. Used to stop the player from moving when the key is released.
   * @author Joshua Yin
   * @param none
   * @return void
   */
  public void keyReleased() {
    
    // Stop movement when the key is released
    if (key == 'w') {
      fltPlayerSpeedY = 0;
    }
    else if (key == 'a') {
      fltPlayerSpeedX = 0;
    }
    else if (key == 's') {
      fltPlayerSpeedY = 0;
    }
    else if (key == 'd') {
      fltPlayerSpeedX = 0;
    }
  }



  /*
   * Mouse pressed method. Called when the mouse is pressed. Used to shoot bullets.
   * @author Joshua Yin
   * @param none
   * @return void
   */
  
  public void mousePressed() {

    bullets.add(new Bullet(fltPlayerX, fltPlayerY, fltPlayerRotation, 0));

  }
}