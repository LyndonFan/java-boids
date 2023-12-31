public class Boid {
    public double posX;
    public double posY;
    public double velocityX;
    public double velocityY;

    public Boid(
        double posX,
        double posY,
        double velocityX,
        double velocityY
    ) {
        this.posX = posX;
        this.posY = posY;
        this.velocityX = velocityX;
        this.velocityY = velocityY;
    }

    public double distanceTo(Boid other) {
        double dx = posX - other.posX;
        double dy = posY - other.posY;
        return Math.sqrt(dx * dx + dy * dy);
    }
}