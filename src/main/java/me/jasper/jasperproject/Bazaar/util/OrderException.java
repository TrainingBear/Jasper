package me.jasper.jasperproject.Bazaar.util;

import lombok.Getter;
import me.jasper.jasperproject.Bazaar.Component.BazaarStock;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

@Getter
public class OrderException extends RuntimeException {
    private BazaarStock substitute;
    private Component log;
    public OrderException(String message) {
        super(message);
    }

    public OrderException(@NotNull Component component, BazaarStock stock){
        this.log = component;
        this.substitute = stock;
    }
    public OrderException(String message, BazaarStock stock){
        super(message);
        this.log = Component.text(message);
        this.substitute = stock;

    }
    public OrderException(BazaarStock stock){
        this.substitute = stock;
    }
}
