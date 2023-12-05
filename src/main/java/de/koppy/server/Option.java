package de.koppy.server;

import de.koppy.lunaniasystem.LunaniaSystem;

import java.util.ArrayList;
import java.util.List;

public class Option {

    private String name;
    private Object defaultvalue;
    private List<String> description = new ArrayList<>();
    private boolean system = false;

    public Option(String name, Object defaultvalue, String description) {
        this.name = name;
        this.defaultvalue = defaultvalue;
        this.description.add(description);
    }

    public Option(String name, Object defaultvalue, String description, boolean system) {
        this.name = name;
        this.defaultvalue = defaultvalue;
        this.description.add(description);
        this.system = true;
    }

    public boolean isSystem() {
        return system;
    }

    public void setDefaultvalue(Object defaultvalue) {
        this.defaultvalue = defaultvalue;
    }

    public String getName() {
        return name;
    }

    public Object getDefaultvalue() {
        return defaultvalue;
    }

    public void setDescription(List<String> description) {
        this.description = description;
    }

    public List<String> getDescription() {
        return description;
    }
}