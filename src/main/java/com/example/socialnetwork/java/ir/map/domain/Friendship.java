package com.example.socialnetwork.java.ir.map.domain;

import java.time.LocalDate;
import java.util.Objects;

public class Friendship extends Entity<Tuple<Long,Long>> {
    LocalDate date;
    long user1;
    long user2;
    Status status;

    public Friendship(long user1, long user2) {
        this.date = LocalDate.now();
        this.user1 = user1;
        this.user2 = user2;
    }

    public Friendship(long user1, long user2, Status status) {
        this.date = LocalDate.now();
        this.user1 = user1;
        this.user2 = user2;
        this.status = status;
    }

    /**
     * @return the date when the friendship was created
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * @return status of the friendship
     */
    public Status getStatus() {
        return status;
    }

    /**
     * @return the first user
     */
    public long getUser1() {
        return user1;
    }

    /**
     * sets the status for the friend request
     * @param status
     */
    public void setStatus(Status status) {
        this.status = status;
    }

    /**
     * sets the first user
     *
     * @param user1 - the new first user
     */
    public void setUser1(long user1) {
        this.user1 = user1;
    }

    /**
     * @return the second user
     */
    public long getUser2() {
        return user2;
    }

    /**
     * sets the second user
     *
     * @param user2 - the new second user
     */
    public void setUser2(long user2) {
        this.user2 = user2;
    }

    @Override
    public String toString() {
        return "Friendship{" +
                "date=" + date +
                ", user1=" + user1 +
                ", user2=" + user2 +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Friendship that = (Friendship) o;
        return Objects.equals(date, that.date) && Objects.equals(user1, that.user1) && Objects.equals(user2, that.user2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), date, user1, user2);
    }
}


