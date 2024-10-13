package com.example.sqlite_baitap.Class;

public class SClass
{
    private String id;
    private String name;
    private String depart;
    private boolean isSelected;
    public SClass(String id, String name, String depart) {
        this.id = id;
        this.name = name;
        this.depart = depart;
        this.isSelected=false;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getDepart() {
        return depart;
    }

    public void setDepart(String depart) {
        this.depart = depart;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
