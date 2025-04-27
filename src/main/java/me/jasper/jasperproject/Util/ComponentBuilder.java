package me.jasper.jasperproject.Util;

import lombok.Getter;
import net.kyori.adventure.text.Component;

public class ComponentBuilder {
    @Getter
    Component component;
    public ComponentBuilder append(Component component){
        component = component.append(component);
        return this;
    }

    public ComponentBuilder append(String component){
        return append(Util.deserialize(component));
    }

    public <T> ComponentBuilder append(T t){
        return append(String.valueOf(t));
    }
}
