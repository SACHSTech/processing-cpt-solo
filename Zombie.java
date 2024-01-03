public class Zombie {
    
    float xMap;
    float yMap;
    float xScreen;
    float yScreen;
    float speedX;
    float speedY;
    float rotation;
    int health;

    public Zombie (float xMap, float yMap, float xScreen, float yScreen, float speedX, float speedY, float rotation, int health) {
        this.xMap = xMap;
        this.yMap = yMap;
        this.xScreen = xScreen;
        this.yScreen = yScreen;
        this.speedX = speedX;
        this.speedY = speedY;
        this.rotation = rotation;
        this.health = health;
    }
}
