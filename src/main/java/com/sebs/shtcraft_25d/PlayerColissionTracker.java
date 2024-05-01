package com.sebs.shtcraft_25d;

public class PlayerColissionTracker extends WorldEntity {
    PlayerManager manager; // Reference to the PlayerManager
    UIManager ui; // Reference to the UIManager

    // Constructor
    public PlayerColissionTracker(PlayerManager manager_, UIManager ui_) {
        // Initialize with null image, position from PlayerManager, and dimensions from PlayerManager
        super(null, manager_.getPosition(), manager_.getDimensions().x);

        this.manager = manager_; // Assign PlayerManager reference
        this.ui = ui_; // Assign UIManager reference
    }

    // Update method
    @Override
    public void tick(double deltaTime) {
        // Update position and edge length based on PlayerManager's position and dimensions
        this.position = this.manager.getPosition().add((float) 0.0, (float) this.edgeLength * 1.75f / 4.0f);
        this.edgeLength = this.manager.getDimensions().x / 3;
    }

    // Collision handling method
    @Override
    protected void collissionWith(WorldEntity e) {
        // If collided with a Zombie entity
        if (e instanceof Zombie) {
            // Decrease UI by 3%
            this.ui.increaseByPercent(-0.03);
        }
    }

    // Method to define collision type
    @Override
    protected ColissionType getColissionType() {
        return ColissionType.Thick; // Thick collision type
    }
}
