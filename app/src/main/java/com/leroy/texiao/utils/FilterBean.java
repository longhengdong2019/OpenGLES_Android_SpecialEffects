package com.leroy.texiao.utils;

public class FilterBean {

    private String name;
    private String color;

    public FilterBean(String name) {
        this.name = name;
    }

    public FilterBean(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

}
