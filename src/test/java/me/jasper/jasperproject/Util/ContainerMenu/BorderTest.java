package me.jasper.jasperproject.Util.ContainerMenu;

import org.bukkit.Material;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BorderTest {
    @Test
    public void calc(){
        boolean[][] before = {
                {false, true, false},
                {true, true, false},
                {false, false, true},
        };

        boolean[] after = Border(before);
        for (boolean a : after) {
            System.out.print(a+", ");
        }
    }
    public boolean[] Border(boolean[][] layout){
        boolean[] newlayout = new boolean[layout.length*layout[0].length];
        int index = 0;
        for (int i = 0; i < layout.length; i++) {
            for (int j = 0; j < layout[0].length; j++) {
                System.out.println("writting "+index);
                newlayout[index] = layout[i][j];
                index++;
            }
        }
//        for (boolean[] booleans : layout) {
//            System.out.println();
//            for (boolean b : booleans) {
//                System.out.print(b +", ");
//            }
//        }
        return newlayout;
    }


}