package com.cool.pulseit.utils;

public class Result<T> {

    private boolean isOk;
    private T value = null;
    private String message = null;

    public Result(boolean isOk, T value) {
        this.isOk = isOk;
        this.value = value;
    }

    public Result(boolean isOk, String message) {
        this.isOk = isOk;
        this.message = message;
    }

    public Result() {

    }

    public boolean isOk() {
        return isOk;
    }

    public void setOk(boolean ok) {
        isOk = ok;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
