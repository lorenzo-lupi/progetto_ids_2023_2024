package it.cs.unicam.app_valorizzazione_territorio.repositories;

import it.cs.unicam.app_valorizzazione_territorio.abstractions.Identifiable;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This abstract class represents a generic repository of items, capable of storing and retrieving them.
 * This class follows the Repository design pattern and substitutes the persistence in the system.
 *
 * @param <I> the type of the items
 */
public abstract class Repository<I extends Identifiable> {
    private final Map<Long, I> items;
    private long nextID = 0L;

    public Repository() {
        this.items = new HashMap<>();
    }

    /**
     * Returns the items of the repository in the form of a stream.
     * @return the stream of items.
     */
    public Stream<I> getItemStream() {
        return items.values().stream();
    }

    /**
     * Returns a copy of the map of all the items of the repository.
     *
     * @return a copy of the map of all the items of the repository
     */
    public Map<Long, I> getAllItemsMap() {
        return items.values().stream().collect(Collectors.toMap(I::getID, Function.identity()));
    }

    /**
     * Adds an item to the repository.
     * If the repository already contains an item with the same ID, the new item will not be added.
     *
     * @param item the item to be added
     * @return the item already associated with the same ID, if any, null otherwise.
     */
    public I add(I item) {
        return this.items.putIfAbsent(item.getID(), item);
    }

    /**
     * Removes an item from the repository.
     * If the repository does not contain an item with the same ID that is equal to the given item,
     * nothing will be removed.
     *
     * @param item the item to be removed
     * @return true if the item has been removed, false otherwise
     */
    public boolean remove(I item) {
        return this.items.remove(item.getID(), item);
    }

    /**
     * Adds all the items of the specified collection to the repository.
     * @param items the items to be added
     */
    public void addAll(Collection<I> items) {
        for (I item : items) this.add(item);
    }

    /**
     * Removes all the items of the specified collection from the repository.
     * @param items the items to be removed
     */
    public void removeAll(Collection<I> items) {
        for (I item : items) this.remove(item);
    }

    /**
     * Checks if the repository contains the specified item.
     * @param item the item to be checked
     * @return true if the repository contains the specified item, false otherwise
     */
    public boolean contains(I item) {
        return this.items.containsValue(item);
    }

    /**
     * Returns the item corresponding to the specified ID, if any.
     *
     * @param ID the ID of the item to be returned
     * @return the item corresponding to the specified ID, if any
     */
    public I getItemByID(long ID) {
        return this.items.get(ID);
    }

    /**
     * Returns and updates the next available ID for the items of the repository.
     *
     * @return the next available ID for the items of the repository
     */
    public long getNextID() {
        return nextID++;
    }
}
