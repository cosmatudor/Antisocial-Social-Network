package com.example.socialnetwork.java.ir.map.domain;

import java.time.LocalDate;
import java.util.Objects;

public class Message extends Entity<Long> {
    private String text;
    private long from;
    private long to;
    private LocalDate date;

    public Message(Long id, long from, long to, String text, LocalDate date) {
        super(id);
        this.text = text;
        this.from = from;
        this.to = to;
        this.date = date;
    }

    public Message(Long id, long from, long to, String text) {
        super(id);
        this.text = text;
        this.from = from;
        this.to = to;
        this.date = LocalDate.now();
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getFrom() {
        return from;
    }

    public void setFrom(long from) {
        this.from = from;
    }

    public long getTo() {
        return to;
    }

    public void setTo(long to) {
        this.to = to;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Message message = (Message) o;
        return from == message.from && to == message.to && Objects.equals(text, message.text) && Objects.equals(date, message.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), text, from, to, date);
    }
}
