import java.util.ArrayList;

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
                0,
                0,
                maxWidth,
                maxHeight,
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
        this.moveBoids();
    }

    private void alignBoids() {
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
                    targetVelocityX += boids.get(j).getVelocityX();
                    targetVelocityY += boids.get(j).getVelocityY();
                }
            }
            if (numNeighbours == 0) {
                continue;
            }
            targetVelocityX /= numNeighbours;
            targetVelocityY /= numNeighbours;
            boid.applyAcceleration(
                this.alignmentFactor * (targetVelocityX - boid.getVelocityX()),
                this.alignmentFactor * (targetVelocityY - boid.getVelocityY())
            );
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
                    aggregatePosX += other.getPosX();
                    aggregatePosY += other.getPosY();
                }
            }
            if (numNeighbours == 0) {
                continue;
            }
            aggregatePosX /= numNeighbours;
            aggregatePosY /= numNeighbours;
            double dx = aggregatePosX - boid.getPosX();
            double dy = aggregatePosY - boid.getPosY();
            boid.applyAcceleration(
                this.steeringFactor * dx,
                this.steeringFactor * dy
            );
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
                    aggregateDeltaX += boid.getPosX() - other.getPosX();
                    aggregateDeltaY += boid.getPosY() - other.getPosY();
                }
            }
            if (numNeighbours == 0) {
                continue;
            }
            boid.applyAcceleration(
                this.collisionAvoidanceFactor * aggregateDeltaX,
                this.collisionAvoidanceFactor * aggregateDeltaY
            );
        }
    }


    private void moveBoids() {
        for (int i = 0; i < boids.size(); i++) {
            boids.get(i).update();
        }
    }
}