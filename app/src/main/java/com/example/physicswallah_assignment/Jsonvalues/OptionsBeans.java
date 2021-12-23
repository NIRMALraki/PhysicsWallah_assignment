package com.example.physicswallah_assignment.Jsonvalues;

public class OptionsBeans {
    String name,icon;int id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "OptionsBeans{" +
                "name='" + name + '\'' +
                ", icon='" + icon + '\'' +
                ", id=" + id +
                '}';
    }
}
