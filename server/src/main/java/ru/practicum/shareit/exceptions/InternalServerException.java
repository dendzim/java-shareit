package ru.practicum.shareit.exceptions;

public class InternalServerException extends RuntimeException {
    public InternalServerException(String s) {
        super(s);
    }
}