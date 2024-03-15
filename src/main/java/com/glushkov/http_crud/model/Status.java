package com.glushkov.http_crud.model;

public enum Status {
    ACTIVE(1), DELETED(2);

    private final int id;

    Status(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static Status getById(int id) {
        Status[] array = Status.values();
        for (Status status : array) {
            if (status.id == id) {
                return status;
            }
        }
        return null;
    }
}
