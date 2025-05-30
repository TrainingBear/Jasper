package me.jasper.jasperproject.JMinecraft.Entity.Traits;

import lombok.Getter;
import lombok.Setter;
import net.citizensnpcs.api.trait.Trait;

@Getter
@Setter
public class HPTrait extends Trait {
    private int lvl;
    public HPTrait(String name, int lvl) {
        super(name);
        this.lvl=lvl;
    }
    @Override
    public void run() {
    }
}
