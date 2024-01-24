import processing.core.PApplet;

/**
 * 
 * Those who remain zombie game. Defend yourself against wave after wave of infected. Survive to wave 10 to win!
 * @author Joshua Yin 
 *
 */
class Main {
  public static void main(String[] args) {
    
    String[] processingArgs = {"MySketch"};

	
	Sketch mySketch = new Sketch();
	
	PApplet.runSketch(processingArgs, mySketch);
  }
  
}
