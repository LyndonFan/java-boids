public class Boid {
    public double minX;
    public double minY;
    public double maxX;
    public double maxY;
    private double posX;
    private double posY;
    private double velocityX;
    private double velocityY;
    private double accelerationX = 0;
    private double accelerationY = 0;

    public Boid(
        double minX,
        double minY,
        double maxX,
        double maxY,
        double posX,
        double posY,
        double velocityX,
        double velocityY
    ) {
        this.minX = minX;
        this.minY = minY;
        this.maxX = maxX;
        this.maxY = maxY;
        this.posX = posX;
        this.posY = posY;
        this.velocityX = velocityX;
        this.velocityY = velocityY;
    }

    public double getPosX() {
        return posX;
    }

    public double getPosY() {
        return posY;
    }

    public double getVelocityX() {
        return velocityX;
    }

    public double getVelocityY() {
        return velocityY;
    }

    public double getVelocityMagnitude() {
        return Math.sqrt(velocityX * velocityX + velocityY * velocityY);
    }

    public void applyAcceleration(double accelerationX, double accelerationY) {
        this.accelerationX += accelerationX;
        this.accelerationY += accelerationY;
    }

    public double distanceTo(Boid other) {
        double dx = posX - other.posX;
        double dy = posY - other.posY;
        return Math.sqrt(dx * dx + dy * dy);
    }

    public void update() {
        velocityX += accelerationX;
        velocityY += accelerationY;
        if ((posX + velocityX < minX) || (posX + velocityX > maxX)) {
            velocityX *= -1;
        }
        if ((posY + velocityY < minY) || (posY + velocityY > maxY)) {
            velocityY *= -1;
        }
        posX += velocityX;
        posY += velocityY;
        accelerationX = 0;
        accelerationY = 0;
    }
}