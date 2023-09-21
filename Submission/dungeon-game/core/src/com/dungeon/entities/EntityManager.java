package com.dungeon.entities;

import java.util.ArrayList;

/**
 * A class used for managing entities
 */
public class EntityManager {
    private static EntityManager entityManager;
    private static int nextId = -1;
    protected static Player player;

    private ArrayList<Entity> entities = new ArrayList<Entity>();
    private ArrayList<Entity> entitiesToAdd = new ArrayList<Entity>();
    private ArrayList<Entity> entitiesToRemove = new ArrayList<Entity>();

    private EntityManager() {}

    /**
     * Gets the entity manager instance.
     * 
     * @return the entity manager instance
     */
    public static EntityManager getInstance() {
        if (entityManager == null) {
            entityManager = new EntityManager();
        }
        return entityManager;
    }


    /**
     * Add entity to the array list.
     * 
     * @param e entity to add
     */
    public void addEntity(Entity e) {
        entitiesToAdd.add(e);
    }

    /**
     * Remove entity from array list.
     * 
     * @param e entity to remove
     */
    public void removeEntity(Entity e) {
        // entities.remove(e);
        entitiesToRemove.add(e);
    }

    /**
     * Gets the entity array list
     * 
     * @return
     */
    public ArrayList<Entity> getEntities() {
        return entities;
    }
    /**
     * Mutator method to set reference to the player within the entity manager class
     * @param p The player.
     */
    public void setPlayer(Player p) {
        player = p;
    }


    /**
     * Increments the ID and returns it.
     * 
     * @return new ID
     */
    public int getNewID() {
        nextId++;
        return nextId;
    }
    /**
     * Accessor method to return the player
     * @return A reference to the player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Updates every entity and
     * adds and removes entities to and from the list.
     */
    public void updateAll() {
        for (Entity e : entities) {
            e.update();
        }
        // Add all entities
        entities.addAll(entitiesToAdd);
        entitiesToAdd.clear();
        // Remove all entities
        entities.removeAll(entitiesToRemove);
        entitiesToRemove.clear();
    }

    /**
     * Adds and removes entities to and from the list.
     */
    public void updateEntitiesList() {
        // Add all entities
        entities.addAll(entitiesToAdd);
        entitiesToAdd.clear();
        // Remove all entities
        entities.removeAll(entitiesToRemove);
        entitiesToRemove.clear();
    }

    /**
     * Call this to kill all entities, excluding the player.
     */
    public static void killAllNotPlayer() {
        ArrayList<Entity> entities = new ArrayList<>(EntityManager.getInstance().getEntities());
        for (Entity e : entities) {
            if (!(e instanceof Player))
                EntityManager.getInstance().removeEntity(e);
        }
    }
}
