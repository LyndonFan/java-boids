import java.util.ArrayList;
import java.util.List;

public class BoidManager {
    private int numberOfBoids;
    private double maxWidth;
    private double maxHeight;
    private double visibleRadius;
    private double steeringFactor;
    private double alignmentFactor;
    private double collisionRadius;
    private double collisionAvoidanceFactor;
    private ArrayList<Boid> boids;
    private double boidFixedVelocity = 5.0;

    public BoidManager(
        int numberOfBoids,
        double maxWidth,
        double maxHeight,
        double visibleRadius,
        double steeringFactor,
        double alignmentFactor,
        double collisionRadius,
        double collisionAvoidanceFactor
    ) {
        this.numberOfBoids = numberOfBoids;
        this.maxWidth = maxWidth;
        this.maxHeight = maxHeight;
        this.visibleRadius = visibleRadius;
        this.steeringFactor = steeringFactor;
        this.alignmentFactor = alignmentFactor;
        this.collisionRadius = collisionRadius;
        this.collisionAvoidanceFactor = collisionAvoidanceFactor;
        this.setBoids();
    }

    private void setBoids() {
        this.boids = new ArrayList<>();
        for (int i = 0; i < this.numberOfBoids; i++) {
            double posX = Math.random() * maxWidth;
            double posY = Math.random() * maxHeight;
            double angle = Math.random() * 2 * Math.PI;
            double velocityX = Math.cos(angle) * boidFixedVelocity;
            double velocityY = Math.sin(angle) * boidFixedVelocity;
            Boid boid = new Boid(
                posX,
                posY,
                velocityX,
                velocityY
            );
            this.boids.add(boid);
        }
    }

    public ArrayList<Boid> getBoids() {
        return this.boids;
    }

    public void update() {
        this.alignBoids();
        this.steerBoids();
        this.avoidCollisions();
        this.boundBoids();
        this.moveBoids();
    }

    private void alignBoids() {
        List<Double> oldVelocityX = new ArrayList<>();
        List<Double> oldVelocityY = new ArrayList<>();
        for (int i = 0; i < boids.size(); i++) {
            oldVelocityX.add(boids.get(i).velocityX);
            oldVelocityY.add(boids.get(i).velocityY);
        }
        for (int i = 0; i < boids.size(); i++) {
            double targetVelocityX = 0;
            double targetVelocityY = 0;
            Boid boid = boids.get(i);
            int numNeighbours = 0;
            for (int j = 0; j < boids.size(); j++) {
                if (i == j) {
                    continue;
                }
                if (boid.distanceTo(boids.get(j)) < visibleRadius) {
                    numNeighbours++;
                    // have to use oldVelocity as original velocity may be updated
                    targetVelocityX += oldVelocityX.get(j);
                    targetVelocityY += oldVelocityY.get(j);
                }
            }
            if (numNeighbours == 0) {
                continue;
            }
            targetVelocityX /= numNeighbours;
            targetVelocityY /= numNeighbours;
            // have to use oldVelocity as original velocity may be updated
            boid.velocityX = oldVelocityX.get(i) + this.alignmentFactor * (targetVelocityX - oldVelocityX.get(i));
            boid.velocityY = oldVelocityY.get(i) + this.alignmentFactor * (targetVelocityY - oldVelocityY.get(i));
        }
    }

    private void steerBoids() {
        for (int i = 0; i < boids.size(); i++) {
            Boid boid = boids.get(i);
            double aggregatePosX = 0;
            double aggregatePosY = 0;
            int numNeighbours = 0;
            for (int j = 0; j < boids.size(); j++) {
                Boid other = boids.get(j);
                if (boid.distanceTo(other) < visibleRadius) {
                    numNeighbours++;
                    aggregatePosX += other.posX;
                    aggregatePosY += other.posY;
                }
            }
            if (numNeighbours == 0) {
                continue;
            }
            aggregatePosX /= numNeighbours;
            aggregatePosY /= numNeighbours;
            boid.velocityX += this.steeringFactor * (aggregatePosX - boid.posX);
            boid.velocityY += this.steeringFactor * (aggregatePosY - boid.posY);
        }
    }


    private void avoidCollisions() {
        for (int i = 0; i < boids.size(); i++) {
            Boid boid = boids.get(i);
            double aggregateDeltaX = 0;
            double aggregateDeltaY = 0;
            int numNeighbours = 0;
            for (int j = 0; j < boids.size(); j++) {
                if (i == j) {
                    continue;
                }
                Boid other = boids.get(j);
                if (boid.distanceTo(other) < collisionRadius) {
                    numNeighbours++;
                    aggregateDeltaX += boid.posX - other.posX;
                    aggregateDeltaY += boid.posY - other.posY;
                }
            }
            if (numNeighbours == 0) {
                continue;
            }
            boid.velocityX += this.collisionAvoidanceFactor * aggregateDeltaX;
            boid.velocityY += this.collisionAvoidanceFactor * aggregateDeltaY;
        }
    }

    private void boundBoids() {
        for (int i = 0; i < boids.size(); i++) {
            Boid boid = boids.get(i);
            double velocityMagnitude = Math.sqrt(boid.velocityX * boid.velocityX + boid.velocityY * boid.velocityY);
            boid.velocityX *= boidFixedVelocity / velocityMagnitude;
            boid.velocityY *= boidFixedVelocity / velocityMagnitude;
            if (boid.posX + boid.velocityX < 0) {
                boid.velocityX *= -1;
            } else if (boid.posX + boid.velocityX > maxWidth) {
                boid.velocityX *= -1;
            }
            if (boid.posY + boid.velocityY < 0) {
                boid.velocityY *= -1;
            } else if (boid.posY + boid.velocityY > maxHeight) {
                boid.velocityY *= -1;
            }
        }
    }

    private void moveBoids() {
        for (int i = 0; i < boids.size(); i++) {
            Boid boid = boids.get(i);
            boid.posX += boid.velocityX;
            boid.posY += boid.velocityY;
        }
    }
}