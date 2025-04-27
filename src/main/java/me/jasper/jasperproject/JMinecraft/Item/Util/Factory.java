package me.jasper.jasperproject.JMinecraft.Item.Util;

import me.jasper.jasperproject.JMinecraft.Item.JItem;

import java.io.ObjectStreamClass;
import java.io.Serializable;

public interface Factory extends Serializable {
    JItem create();

    default JItem finish(){
        JItem product = create();
        long serialVersionUID = ObjectStreamClass.lookup(this.getClass()).getSerialVersionUID();
        product.setVersion(serialVersionUID);
        product.update();
        return product;
    }
}
