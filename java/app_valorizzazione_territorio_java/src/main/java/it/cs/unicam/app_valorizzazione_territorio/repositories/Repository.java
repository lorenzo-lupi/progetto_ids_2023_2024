package it.cs.unicam.app_valorizzazione_territorio.repositories;

import java.util.*;

/**
 * This abstract class represents a generic repository of items, capable of storing and retrieving them.
 * This class follows the Repository design pattern and substitutes the persistence in the system.
 *
 * @param <I> the type of the items
 */
public class Repository<I> {
    Set<I> items;

    public Repository() {
        this.items = new HashSet<>();
    }

    /**
     * Returns an iterator over the items of the repository.
     * @return an iterator over the items of the repository
     */
    public Iterator<I> getIterator() {
        return this.items.iterator();
    }

    /**
     * Adds an item to the repository.
     * @param item the item to be added
     * @return true if the item has been added, false otherwise
     */
    public boolean add(I item) {
        return this.items.add(item);
    }

    /**
     * Removes an item from the repository.
     * @param item the item to be removed
     * @return true if the item has been removed, false otherwise
     */
    public boolean remove(I item) {
        return this.items.remove(item);
    }

    /**
     * Adds all the items of the specified collection to the repository.
     * @param items the items to be added
     * @return true if some items have been added, false otherwise
     */
    public boolean addAll(Collection<I> items) {
        return this.items.addAll(items);
    }

    /**
     * Removes all the items of the specified collection from the repository.
     * @param items the items to be removed
     * @return true if some items have been removed, false otherwise
     */
    public boolean removeAll(Collection<I> items) {
        return this.items.removeAll(items);
    }

    /**
     * Checks if the repository contains the specified item.
     * @param item the item to be checked
     * @return true if the repository contains the specified item, false otherwise
     */
    public boolean contains(I item) {
        return this.items.contains(item);
    }
}
