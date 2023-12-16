package com.example.socialnetwork.java.ir.map.domain;

import java.io.Serializable;
import java.util.Objects;

public class Entity<ID> implements Serializable {
    protected ID id;

    public Entity() {}
    public Entity(ID id) {
        this.id = id;
    }

    // Getter
    /**
     * @return the entity's ID
     */
    public ID getId() {
        return id;
    }

    // Setter
    /**
     * @param id - the new ID of the entity
     */
    public void setId(ID id) {
        this.id = id;
    }

    /**
     * @param o - the object to be compared with
     * @return true if the objects are equal, false otherwise
     * Two entities are considered equal if their IDs are equal
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Entity<?> entity = (Entity<?>) o;
        return Objects.equals(id, entity.id);
    }

    /**
     * @return the hash code of the entity's ID
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    /**
     * @return a string containing the entity's ID
     */
    @Override
    public String toString() {
        return "Entity{" +
                "id=" + id +
                '}';
    }
}
