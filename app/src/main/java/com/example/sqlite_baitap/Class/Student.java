package com.example.sqlite_baitap.Class;

public class Student {
    private String id;
    private String name;
    private String birthday;
    private boolean isSelected;
    public Student(String id, String name, String birthday) {
        this.id = id;
        this.name = name;
        this.birthday = birthday;
        this.isSelected=false;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }
}
