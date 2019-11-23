package com.hexamind.coffeemoi;

public class Orders {
    private String username;
    private String size;
    private String type;
    private boolean expressoShot;

    public Orders(String username, String size, String type, boolean expressoShot) {
        this.username = username;
        this.size = size;
        this.type = type;
        this.expressoShot = expressoShot;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isExpressoShot() {
        return expressoShot;
    }

    public void setExpressoShot(boolean expressoShot) {
        this.expressoShot = expressoShot;
    }
}
