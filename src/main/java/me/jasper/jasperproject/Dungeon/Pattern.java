package me.jasper.jasperproject.Dungeon;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Random;

public class Pattern {
    private static Random random = new Random();
    public static class Room{
        /*
         *           This is for the Path
         * */
        public static boolean is3x1(char[][] size, int i, int j,char type){
            /*
             *  [][][]
             * */
            if(type!=0){
                return (j + 2 < size[i].length) && (size[i][j]==type) &&
                        ((size[i][j+1]==0)||(size[i][j+1]==type))&&
                        ((size[i][j+2]==0||(size[i][j+2]==type)));
            }
            return ((j + 2 < size[i].length) && (size[i][j] ==0) && (size[i][j+1]==0)&&(size[i][j+2]==0));
        }
        public static boolean is1x3(char[][] size, int i, int j,char type){
            /*
             * []
             * []
             * []
             * */
            if(type!=0){
                return  ((i + 2 < size.length) && (size[i][j]==type) &&
                        ((size[i+1][j]==0)||(size[i+1][j]==type))&&
                        ((size[i+2][j]==0)||(size[i+2][j]==type)));
            }
            return  ((i + 2 < size.length) && (size[i][j] ==0) && (size[i+1][j]==0)&&(size[i+2][j]==0));
        }

        public static boolean is2x1(char[][] size, int i, int j,char type){
            /*
             * [][]
             * */
            if(type!=0){
                return ((j + 1 < size[i].length) && (size[i][j]==type) &&
                        ((size[i][j+1]==0)||(size[i][j+1]==type)));
            }
            return ((j + 1 < size[i].length) && (size[i][j] ==0) && (size[i][j+1]==0));

        }
        public static boolean is1x2(char[][] size, int i, int j,char type) {
            /*
             * []
             * []
             * */
            if(type!=0){
                return ((i + 1 < size.length) && (size[i][j]==type) &&
                        ((size[i + 1][j] == 0)||(size[i + 1][j] == type)));
            }
            return ((i + 1 < size.length) && (size[i][j] == 0) && (size[i + 1][j] == 0));
        }

        public static boolean isLShaped90D(char[][] size, int i, int j,char type){
            /*
             * [][]
             * []
             * */
            if(type!=0){
                return ((j+1 < size[i].length) && (i+1<size.length) && (size[i][j]==type) &&
                        ((size[i+1][j]==0)||(size[i+1][j]==type))&&
                        ((size[i][j+1]==0)||(size[i][j+1]==type))) ;
            }
            return ((j+1 < size[i].length) && (i+1<size.length) && (size[i][j]==0) && (size[i+1][j]==0)&&(size[i][j+1]==0)) ;
        }
        public static boolean isLShape180D(char[][] size, int i, int j,char type){
            /*
             * [][]
             *   []
             * */
            if(type!=0){
                return ((i+1<size.length)&&(j+1 < size[i].length)&&(size[i][j]==type)&&
                        ((size[i][j+1]==0)||(size[i][j+1]==type))&&
                        ((size[i+1][j+1]==0)||(size[i+1][j+1]==type)));
            }
            return ((i+1<size.length)&&(j+1 < size[i].length)&&(size[i][j]==0)&&(size[i][j+1]==0)&&(size[i+1][j+1])==0) ;
        }
        public static boolean isLShaped0D(char[][] size, int i, int j,char type) {
            /*
             * []
             * [][]
             * */
            if(type!=0){
                return ((i + 1 < size.length) && (j + 1 < size[i].length) && (size[i][j]==type) &&
                        ((size[i + 1][j] == 0)||(size[i + 1][j] == type)) &&
                        ((size[i + 1][j + 1] == 0)||(size[i + 1][j + 1] == type)));
            }
            return ((i + 1 < size.length) && (j + 1 < size[i].length) && (size[i][j] == 0) && (size[i + 1][j] == 0) && (size[i + 1][j + 1] == 0));
        }
        public static boolean isLShaped_90D(char[][] size, int i, int j,char type){
            /*
             *   []
             * [][]
             * */
            if(type!=0){
                return    ((i+1< size.length)&&(j-1>=0)&&(size[i][j]==type)&&
                        ((size[i+1][j]==0)||(size[i+1][j]==type))&&
                        ((size[i+1][j-1]==0)||(size[i+1][j-1]==type)));
            }
            return    ((i+1< size.length)&&(j-1>=0)&&(size[i][j]==0)&&(size[i+1][j]==0)&&(size[i+1][j-1]==0));
        }

        public static boolean is2x2(char[][] size, int i, int j,char type){
            if(type!=0){
                return ((i+1< size.length)&&(j+2<size[i].length)&&(size[i][j]==type)&&
                        ((size[i][j+1]==0||(size[i][j+1]==type)))&&
                        ((size[i+1][j]==0)||(size[i+1][j]==type))&&
                        ((size[i+1][j+1]==0)||(size[i+1][j+1]==type)));
            }
            return ((i+1< size.length)&&(j+2<size[i].length)&&(size[i][j]==0)&&(size[i][j+1]==0)&&(size[i+1][j]==0)&&(size[i+1][j+1]==0));
        }
        public static boolean is1x1(char[][] size, int i, int j,char type){
            if(type!=0){
                return ((size[i][j]==type));
            }
            return ((size[i][j]==0));
        }
        public static boolean is4x1(char[][] size, int i, int j,char type){
            /*
             * [][][][]
             * */
            if(type!=0){
                return ((j + 3 < size[i].length) && (size[i][j]==type) &&
                        ((size[i][j+1]==0)||(size[i][j+1]==type))&&
                        ((size[i][j+2]==0)||(size[i][j+2]==type))&&
                        ((size[i][j+3]==0)||(size[i][j+3]==type)));
            }
            return ((j + 3 < size[i].length) && (size[i][j] ==0) && (size[i][j+1]==0)&&(size[i][j+2]==0)&&(size[i][j+3]==0));
        }
        public static boolean is1x4(char[][] size, int i, int j,char type){
            /*
             * []
             * []
             * []
             * []
             * */
            if(type!=0){
                return  ((i + 3 < size.length) && (size[i][j]==type) &&
                        ((size[i+1][j]==0)||(size[i+1][j]==type))&&
                        ((size[i+2][j]==0)||(size[i+2][j]==type))&&
                        ((size[i+3][j]==0)||(size[i+3][j]==type)));
            }
            return  ((i + 3 < size.length) && (size[i][j] ==0) && (size[i+1][j]==0)&&(size[i+2][j]==0)&&(size[i+3][j]==0));
        }
    }

    public static boolean isStraightI(char[][] roomused, int pathid, int i, int j){
        if(pathid==4){
            return (j+1 < roomused[i].length) && (j-1 >= 0) &&
                    ((roomused[i][j] == 4) && (
                            (roomused[i][j+1]==4)
                                    ||(roomused[i][j+1] == 1)
                                    ||(roomused[i][j+1] == 2)) &&
                            ((roomused[i][j-1] == 4)
                                    ||(roomused[i][j-1] == 1)
                                    ||(roomused[i][j-1] == 2)));
        }
        return (j+1 < roomused[i].length) && (j-1 >= 0) &&
                ((roomused[i][j] == 5) && (
                        (roomused[i][j+1]==5)
                                ||(roomused[i][j+1] == 2)
                                ||(roomused[i][j+1] == 3)) &&
                        ((roomused[i][j-1] == 5)
                                ||(roomused[i][j-1] == 2)
                                ||(roomused[i][j-1] == 3)));

    }
    public static boolean isStraight_(char[][] roomused, int pathid, int i, int j){
        if(pathid==4){
            return  (i+1 < roomused.length) && (i-1 >= 0) &&
                    ((roomused[i][j] == 4) && (
                            (roomused[i+1][j]==4)
                                    ||(roomused[i+1][j] == 1)
                                    ||(roomused[i+1][j] == 2)) &&
                            ((roomused[i-1][j] == 4)
                                    ||(roomused[i-1][j] == 1)
                                    ||(roomused[i-1][j] == 2)));
        }
        return  (i+1 < roomused.length) && (i-1 >= 0) &&
                        ((roomused[i][j] == 5) && (
                                (roomused[i+1][j]==5)
                                        ||(roomused[i+1][j] == 2)
                                        ||(roomused[i+1][j] == 3)) &&
                                ((roomused[i-1][j] == 5)
                                        ||(roomused[i-1][j] == 2)
                                        ||(roomused[i-1][j] == 3)));

    }
    public static boolean is0L(char[][] roomused, int pathid, int i, int j){
        if(pathid==4){
            return  ((i-1 >= 0) && (j+1 < roomused[i].length)) &&
                    ((roomused[i][j] == 4) && (
                            (roomused[i-1][j]==4)
                                    ||(roomused[i-1][j] == 1)
                                    ||(roomused[i-1][j] == 2)) &&
                            ((roomused[i][j+1] == 4)
                                    ||(roomused[i][j+1] == 1)
                                    ||(roomused[i][j+1] == 2)));
        }
        return  ((i-1 >= 0) && (j+1 < roomused[i].length)) &&
                ((roomused[i][j] == 5) && (
                        (roomused[i-1][j]==5)
                                ||(roomused[i-1][j] == 3)
                                ||(roomused[i-1][j] == 2)) &&
                        ((roomused[i][j+1] == 5)
                                ||(roomused[i][j+1] == 3)
                                ||(roomused[i][j+1] == 2)));
    }
    public static boolean is_90L(char[][] roomused, int pathid, int i, int j){
        if(pathid==4){
            return  ((i-1 >= 0) && (j-1 >= 0)) &&
                    ((roomused[i][j] == 4) && (
                            (roomused[i-1][j]==4)
                                    ||(roomused[i-1][j] == 1)
                                    ||(roomused[i-1][j] == 2)) &&
                            ((roomused[i][j-1] == 4)
                                    ||(roomused[i][j-1] == 1)
                                    ||(roomused[i][j-1] == 2)));
        }
        return  ((i-1 >= 0) && (j-1 >= 0)) &&
                ((roomused[i][j] == 5) && (
                        (roomused[i-1][j]==5)
                                ||(roomused[i-1][j] == 3)
                                ||(roomused[i-1][j] == 2)) &&
                        ((roomused[i][j-1] == 5)
                                ||(roomused[i][j-1] == 3)
                                ||(roomused[i][j-1] == 2)));
    }
    public static boolean is90L(char[][] roomused, int pathid, int i, int j){
        if(pathid==4){
            return  ((i+1 < roomused.length) && (j+1 < roomused[i].length)) &&
                    ((roomused[i][j] == 4) && (
                            (roomused[i+1][j]==4)
                                    ||(roomused[i+1][j] == 1)
                                    ||(roomused[i+1][j] == 2)) &&
                            ((roomused[i][j+1] == 4)
                                    ||(roomused[i][j+1] == 1)
                                    ||(roomused[i][j+1] == 2)));
        }
        return  ((i+1 < roomused.length) && (j+1 < roomused[i].length)) &&
                ((roomused[i][j] == 5) && (
                        (roomused[i+1][j]==5)
                                ||(roomused[i+1][j] == 3)
                                ||(roomused[i+1][j] == 2)) &&
                        ((roomused[i][j+1] == 5)
                                ||(roomused[i][j+1] == 3)
                                ||(roomused[i][j+1] == 2)));
    }
    public static boolean is180L(char[][] roomused, int pathid, int i, int j){
        if(pathid==4){
            return  ((i+1 < roomused.length) && (j-1 >= 0)) &&
                    ((roomused[i][j] == 4) && (
                            (roomused[i+1][j]==4)
                                    ||(roomused[i+1][j] == 1)
                                    ||(roomused[i+1][j] == 2)) &&
                            ((roomused[i][j-1] == 4)
                                    ||(roomused[i][j-1] == 1)
                                    ||(roomused[i][j-1] == 2)));
        }
        return  ((i+1 < roomused.length) && (j-1 >= 0)) &&
                ((roomused[i][j] == 5) && (
                        (roomused[i+1][j]==5)
                                ||(roomused[i+1][j] == 3)
                                ||(roomused[i+1][j] == 2)) &&
                        ((roomused[i][j-1] == 5)
                                ||(roomused[i][j-1] == 3)
                                ||(roomused[i][j-1] == 2)));
    }

    public static boolean isTop(char[][] roomused, char pathid1,char pathid2, int i, int j){
        if(pathid2!=0){
            return ((i+1 < roomused.length))
                    && (roomused[i+1][j]==pathid1 || roomused[i+1][j]==pathid2);
        }
        return ((i+1 < roomused.length))
                && roomused[i+1][j]==pathid1;
    }
    public static boolean isBottom(char[][] roomused, char pathid1,char pathid2, int i, int j){
        if(pathid2!=0){
            return ((i-1 >= 0))
                    && (roomused[i-1][j]==pathid1 || roomused[i-1][j]==pathid2);
        }
        return ((i-1 >= 0))
                && roomused[i-1][j]==pathid1;
    }
    public static boolean isRight(char[][] roomused, char pathid1,char pathid2, int i, int j){
        if(pathid2!=0){
            return ((j-1 >= 0))
                    && (roomused[i][j-1]==pathid1||roomused[i][j-1]==pathid2);
        }
        return ((j-1 >= 0))
                && roomused[i][j-1]==pathid1;
    }
    public static boolean isLeft(char[][] roomused, char pathid1,char pathid2, int i, int j){
        if(pathid2!=0){
            return ((j+1 < roomused[i].length))
                    && (roomused[i][j+1]==pathid1||roomused[i][j+1]==pathid2);
        }
        return ((j+1 < roomused[i].length))
                && roomused[i][j+1]==pathid1;
    }

    public static void generate1Door(char[][] roomused, char pathid, int x, int y,String roomname){
        if (Pattern.isTop(roomused, pathid,'\u0000',x,y)){
            RS.loadAndPasteSchematic(roomname,
                    RS.getPastepoint(x, y),
                    180);
        }
        if (Pattern.isBottom(roomused, pathid,'\u0000',x,y)){
            RS.loadAndPasteSchematic(roomname,
                    RS.getPastepoint(x, y),
                    0);
        }
        if (Pattern.isRight(roomused, pathid,'\u0000',x,y)){
            RS.loadAndPasteSchematic(roomname,
                    RS.getPastepoint(x, y),
                    -90);
        }
        if (Pattern.isLeft(roomused, pathid,'\u0000',x,y)){
            RS.loadAndPasteSchematic(roomname,
                    RS.getPastepoint(x, y),
                    90);
        }
    }
    public static void generate1Door(char[][] roomused, char pathid1,char pathid2, int x, int y,String roomname){
        if (Pattern.isTop(roomused, pathid1,pathid2,x,y)){
            RS.loadAndPasteSchematic(roomname,
                    RS.getPastepoint(x, y),
                    180);
        }
        if (Pattern.isBottom(roomused, pathid1,pathid2,x,y)){
            RS.loadAndPasteSchematic(roomname,
                    RS.getPastepoint(x, y),
                    0);
        }
        if (Pattern.isRight(roomused, pathid1,pathid2,x,y)){
            RS.loadAndPasteSchematic(roomname,
                    RS.getPastepoint(x, y),
                    -90);
        }
        if (Pattern.isLeft(roomused, pathid1,pathid2,x,y)){
            RS.loadAndPasteSchematic(roomname,
                    RS.getPastepoint(x, y),
                    90);
        }
    }

    public static class Load{
    private static int limit22,limitL,limit1,limit2,limit3,limit4 = 0;
        public static char[][] room(HashMap<Character,Integer> roomLimit,char[][] roomused, Player player){
            int priority;
            int attempt = 0;
            boolean suit;
            for (int i = 0; i < roomused.length ; i++) {
                for (int j = 0; j < roomused[i].length ; j++) {
                    if(roomused[i][j]=='\u0000'){
                        do {
                            priority = random.nextInt(12);
                            suit = switch (priority) {
                                case 0: {
                                    if (limit1 < roomLimit.get('1') && Room.is1x1(roomused, i, j, '\u0000')) {
                                        roomused = fill1x1(roomused, i, j, '\u0000');
                                        limit1++;
                                        yield true;
                                    }
                                    yield false;
                                }
                                case 1 : {
                                    if (limit2 < roomLimit.get('2') && Room.is1x2(roomused, i, j, '\u0000')) {
                                        roomused = fill1x2(roomused, i, j, '\u0000');
                                        limit2++;
                                        yield true;
                                    }
                                    yield false;
                                }
                                case 2 : {
                                    if (limit3 < roomLimit.get('3') && Room.is1x3(roomused, i, j, '\u0000')) {
                                        roomused = fill1x3(roomused, i, j, '\u0000');
                                        limit3++;
                                        yield true;
                                    }
                                    yield false;
                                }
                                case 3 : {
                                    if (limit4 < roomLimit.get('4') && Room.is1x4(roomused, i, j, '\u0000')) {
                                        roomused = fill1x4(roomused, i, j, '\u0000');
                                        limit4++;
                                        yield true;
                                    }
                                    yield false;
                                }
                                case 4 : {
                                    if (limit4 < roomLimit.get('4') && Room.is4x1(roomused, i, j, '\u0000')) {
                                        roomused = fill4x1(roomused, i, j, '\u0000');
                                        limit4++;
                                        yield true;
                                    }
                                    yield false;
                                }
                                case 5 : {
                                    if (limit3 < roomLimit.get('3') && Room.is3x1(roomused, i, j, '\u0000')) {
                                        roomused = fill3x1(roomused, i, j, '\u0000');
                                        limit3++;
                                        yield true;
                                    }
                                    yield false;
                                }
                                case 6 : {
                                    if (limit2 < roomLimit.get('2') && Room.is2x1(roomused, i, j, '\u0000')) {
                                        roomused = fill2x1(roomused, i, j, '\u0000');
                                        limit2++;
                                        yield true;
                                    }
                                    yield false;
                                }
                                case 7 : {
                                    if(limit22 < roomLimit.get('#') && Room.is2x2(roomused, i, j, '\u0000')) {
                                        roomused = fill2x2(roomused, i, j, '\u0000');
                                        limit22++;
                                        yield true;
                                    }
                                    yield false;
                                }
                                case 8 : if(limitL < roomLimit.get('L') &&   Room.isLShaped0D(roomused, i, j,'\u0000')) {
                                    roomused = fillLLShaped0D(roomused, i, j, '\u0000');
                                    limitL++;
                                    yield true;
                                }
                                yield false;
                                case 9 : if(limitL < roomLimit.get('L') && Room.isLShaped_90D(roomused, i, j,'\u0000')) {
                                    roomused = fillLShaped_90D(roomused, i, j, '\u0000');
                                    limitL++;
                                    yield true;
                                }
                                yield false;
                                case 10: if(limitL < roomLimit.get('L') &&  Room.isLShape180D(roomused, i, j,'\u0000')) {
                                    roomused = fillLShape180D(roomused, i, j, '\u0000');
                                    limitL++;
                                    yield true;
                                };
                                yield false;
                                case 11: if (limitL < roomLimit.get('L') &&  Room.isLShaped90D(roomused, i, j,'\u0000')) {
                                    roomused = fillLShaped90D(roomused, i, j, '\u0000');
                                    limitL++;
                                    yield true;
                                }
                                yield false;
                                default : throw new IllegalStateException("Unexpected priority: " + priority);
                            };
                            attempt++;
                            if(attempt>100){
                                player.sendMessage(ChatColor.RED +"Too manny attempt! no room can be fit!");
                                break;
                            }
                        } while (!suit);
                        if(attempt<100){
                            player.sendMessage(ChatColor.GREEN+"Generating room "+priority+" took "+attempt+" attempt");
                        }
                        suit=false;
                    }
                }
            }
            return roomused;
        }
        public static char[][] path(HashMap<Character,Integer> roomLimit, char[][] roomused, char type, Player player){
            int priority;
            int attempt = 0;
            boolean suit;
            for (int i = 0; i < roomused.length ; i++) {
                for (int j = 0; j < roomused[i].length ; j++) {
                    if(roomused[i][j]==type){
                        do {
                            priority = random.nextInt(12);
                            suit = switch (priority) {
                                case 0: {
                                    if (limit1 < roomLimit.get('1') && Room.is1x1(roomused, i, j, type)) {
                                        roomused = fill1x1(roomused, i, j, type);
                                        limit1++;
                                        yield true;
                                    }
                                    yield false;
                                }
                                case 1 : {
                                    if (limit2 < roomLimit.get('2') && Room.is1x2(roomused, i, j, type)) {
                                        roomused = fill1x2(roomused, i, j, type);
                                        limit2++;
                                        yield true;
                                    }
                                    yield false;
                                }
                                case 2 : {
                                    if (limit3 < roomLimit.get('3') && Room.is1x3(roomused, i, j, type)) {
                                        roomused = fill1x3(roomused, i, j, type);
                                        limit3++;
                                        yield true;
                                    }
                                    yield false;
                                }
                                case 3 : {
                                    if (limit4 < roomLimit.get('4') && Room.is1x4(roomused, i, j, type)) {
                                        roomused = fill1x4(roomused, i, j, type);
                                        limit4++;
                                        yield true;
                                    }
                                    yield false;
                                }
                                case 4 : {
                                    if (limit4 < roomLimit.get('4') && Room.is4x1(roomused, i, j, type)) {
                                        roomused = fill4x1(roomused, i, j, type);
                                        limit4++;
                                        yield true;
                                    }
                                    yield false;
                                }
                                case 5 : {
                                    if (limit3 < roomLimit.get('3') && Room.is3x1(roomused, i, j, type)) {
                                        roomused = fill3x1(roomused, i, j, type);
                                        limit3++;
                                        yield true;
                                    }
                                    yield false;
                                }
                                case 6 : {
                                    if (limit2 < roomLimit.get('2') && Room.is2x1(roomused, i, j, type)) {
                                        roomused = fill2x1(roomused, i, j, type);
                                        limit2++;
                                        yield true;
                                    }
                                    yield false;
                                }
                                case 7 : {
                                    if(limit22 < roomLimit.get('#') && Room.is2x2(roomused, i, j, type)) {
                                        roomused = fill2x2(roomused, i, j, type);
                                limit22++;
                                yield true;
                                    }
                                    yield false;
                                }
                                case 8 : if (limitL < roomLimit.get('L') &&   Room.isLShaped0D(roomused, i, j,type)) {
                                    roomused = fillLLShaped0D(roomused, i, j, type);
                                    limitL++;
                                    yield true;
                                }
                                yield false;
                                case 9 : if(limitL < roomLimit.get('L') && Room.isLShaped_90D(roomused, i, j,type)) {
                                    roomused = fillLShaped_90D(roomused, i, j, type);
                                    limitL++;
                                    yield true;
                                }
                                yield false;
                                case 10: if (limitL < roomLimit.get('L') &&  Room.isLShape180D(roomused, i, j,type)) {
                                    roomused = fillLShape180D(roomused, i, j, type);
                                    limitL++;
                                    yield true;
                                }
                                yield false;
                                case 11: if (limitL < roomLimit.get('L') &&  Room.isLShaped90D(roomused, i, j,type)) {
                                    roomused = fillLShaped90D(roomused, i, j, type);
                                    limitL++;
                                    yield true;
                                }
                                yield false;
                                default : throw new IllegalStateException("Unexpected priority: " + priority);
                            };
                            attempt++;
                            if(attempt>40){
                                player.sendMessage(ChatColor.RED +"Too manny attempt! need a breaking point!");}
                        } while (!suit);
                        if(attempt<41){
                            player.sendMessage(ChatColor.GREEN+"Generating room "+priority+" took "+attempt+" attempt");
                        }
                        suit=false;
                    }
                }
            }
            return roomused;
        }
    }


        public static char[][] fill3x1(char[][] size, int i, int j,char type){
                    if(Room.is3x1(size,i,j,type)){
                        Location room1 = new Location(Bukkit.getWorld("test"),i*32,70,j*32);
                        Location room2 = new Location(Bukkit.getWorld("test"),i*32,70,(j+1)*32);
                        Location room3 = new Location(Bukkit.getWorld("test"),i*32,70,(j+2)*32);
                        Block block1 = room1.getBlock();
                        Block block2 = room2.getBlock();
                        Block block3 = room3.getBlock();
                        block1.setType(RT.a3X1.getType());
                        block2.setType(RT.a3X1.getType());
                        block3.setType(RT.a3X1.getType());
                        size[i][j] = RS.a3x1Shape.getID();
                        size[i][j+1]=RS.a3x1Shape.getID();
                        size[i][j+2]=RS.a3x1Shape.getID();
                        RS.loadAndPasteSchematic("1x3",
                                RS.a3x1Shape.getPastepoint(i,j),
                                RS.a3x1Shape.getAngle());
                    }
                    return size;
        }
        public static char[][] fill1x3(char[][] size, int i, int j,char type) {
            if (Room.is1x3(size,i,j,type)) {
                Location room1 = new Location(Bukkit.getWorld("test"), i*32, 70, j*32);
                Location room2 = new Location(Bukkit.getWorld("test"), (i + 1)*32, 70, j*32);
                Location room3 = new Location(Bukkit.getWorld("test"), (i + 2)*32, 70, j*32);
                Block block1 = room1.getBlock();
                Block block2 = room2.getBlock();
                Block block3 = room3.getBlock();
                block1.setType(RT.a3X1.getType());
                block2.setType(RT.a3X1.getType());
                block3.setType(RT.a3X1.getType());
                size[i][j] = RS.a1x3Shape.getID();
                size[i + 1][j] = RS.a1x3Shape.getID();
                size[i + 2][j] = RS.a1x3Shape.getID();
                RS.loadAndPasteSchematic("1x3",
                        RS.a1x3Shape.getPastepoint(i,j),
                        RS.a1x3Shape.getAngle());
            }
            return size;
        }
        public static char[][] fill2x1(char[][] size, int i, int j,char type){
        //==
            if (Room.is2x1(size,i,j,type)){
                Location room1 = new Location(Bukkit.getWorld("test"),i*32,70,j*32);
                Location room2 = new Location(Bukkit.getWorld("test"),i*32,70,(j+1)*32);
                Block block1 = room1.getBlock();
                Block block2 = room2.getBlock();
                block1.setType(RT.a2X1.getType());
                block2.setType(RT.a2X1.getType());
                size[i][j] = RS.a2x1Shape.getID();
                size[i][j+1]=RS.a2x1Shape.getID();
                RS.loadAndPasteSchematic("1x2",
                        RS.a2x1Shape.getPastepoint(i,j),
                        RS.a2x1Shape.getAngle());
            }
            return size;
        }
        public static char[][] fill1x2(char[][] size, int i, int j,char type) {
//                =
//                =
            if (Room.is1x2(size,i,j,type)){
                Location room1 = new Location(Bukkit.getWorld("test"),i*32,70,j*32);
                Location room2 = new Location(Bukkit.getWorld("test"),(i+1)*32,70,j*32);
                Block block1 = room1.getBlock();
                Block block2 = room2.getBlock();
                block1.setType(RT.a2X1.getType());
                block2.setType(RT.a2X1.getType());
                size[i][j] = RS.a1x2Shape.getID();
                size[i+1][j]=RS.a1x2Shape.getID();
                RS.loadAndPasteSchematic("1x2",
                        RS.a1x2Shape.getPastepoint(i,j),
                        RS.a1x2Shape.getAngle());
            }
            return size;
        }

        public static char[][] fillLShaped90D(char[][] size, int i, int j,char type){
            //==
            //=
            if (Room.isLShaped90D(size,i,j,type)){
                Location room1 = new Location(Bukkit.getWorld("test"), i*32, 70, j*32);
                Location room3 = new Location(Bukkit.getWorld("test"), i*32, 70, (j+1)*32);
                Location room2 = new Location(Bukkit.getWorld("test"), (i + 1)*32, 70, j*32);
                Block block1 = room1.getBlock();
                Block block2 = room2.getBlock();
                Block block3 = room3.getBlock();
                block1.setType(RT.aL.getType());
                block2.setType(RT.aL.getType());
                block3.setType(RT.aL.getType());
                size[i][j]=RS.LShape90D.getID();
                size[i][j+1]=RS.LShape90D.getID();
                size[i+1][j]=RS.LShape90D.getID();
                RS.loadAndPasteSchematic("Lshape",
                        RS.getPastepoint(i,j, (int) 2),
                        RS.LShape90D.getAngle());
            }
            return size;
        }
        public static char[][] fillLShape180D(char[][] size, int i, int j,char type){
//                ==
//                 =
            if (Room.isLShape180D(size,i,j,type)){
                Location room1 = new Location(Bukkit.getWorld("test"), i*32, 70, j*32);
                Location room2 = new Location(Bukkit.getWorld("test"), i*32, 70, (j+1)*32);
                Location room3 = new Location(Bukkit.getWorld("test"), (i+1)*32, 70, (j+1)*32);
                Block block1 = room1.getBlock();
                Block block2 = room2.getBlock();
                Block block3 = room3.getBlock();
                block1.setType(RT.aL.getType());
                block2.setType(RT.aL.getType());
                block3.setType(RT.aL.getType());
                size[i][j]=RS.LShape180D.getID();
                size[i][j+1]=RS.LShape180D.getID();
                size[i+1][j+1]=RS.LShape180D.getID();
                RS.loadAndPasteSchematic("Lshape",
                        RS.getPastepoint(i,j, (int) 2),
                        RS.LShape180D.getAngle());
            }
        return size;
        }
        public static char[][] fillLLShaped0D(char[][] size, int i, int j,char type) {
//                =
//                ==
            if (Room.isLShaped0D(size,i,j,type)){
                Location room1 = new Location(Bukkit.getWorld("test"), i*32, 70, j*32);
                Location room2 = new Location(Bukkit.getWorld("test"), (i+1)*32, 70, j*32);
                Location room3 = new Location(Bukkit.getWorld("test"), (i+1)*32, 70, (j+1)*32);
                Block block1 = room1.getBlock();
                Block block2 = room2.getBlock();
                Block block3 = room3.getBlock();
                block1.setType(RT.aL.getType());
                block2.setType(RT.aL.getType());
                block3.setType(RT.aL.getType());
                size[i][j]=RS.LShape0D.getID();
                size[i+1][j]=RS.LShape0D.getID();
                size[i+1][j+1]=RS.LShape0D.getID();

                RS.loadAndPasteSchematic("Lshape",
                        RS.LShape0D.getPastepoint(i,j, (int) 2),
                        RS.LShape0D.getAngle());
            }
        return size;
        }
        public static char[][] fillLShaped_90D(char[][] size, int i, int j,char type){
//                 =
//                ==
            if    (Room.isLShaped_90D(size,i,j,type)){
                Location room1 = new Location(Bukkit.getWorld("test"), i*32, 70, j*32);
                Location room2 = new Location(Bukkit.getWorld("test"), (i+1)*32, 70, j*32);
                Location room3 = new Location(Bukkit.getWorld("test"), (i+1)*32, 70, (j-1)*32);
                Block block1 = room1.getBlock();
                Block block2 = room2.getBlock();
                Block block3 = room3.getBlock();
                block1.setType(RT.aL.getType());
                block2.setType(RT.aL.getType());
                block3.setType(RT.aL.getType());
                size[i][j]=RS.LShape_90D.getID();
                size[i+1][j]=RS.LShape_90D.getID();
                size[i+1][j-1]=RS.LShape_90D.getID();
                RS.loadAndPasteSchematic("Lshape",
                        RS.getPastepoint(i,j, (int) 1),
                        RS.LShape_90D.getAngle());
            }
        return size;
        }

        public static char[][] fill2x2(char[][] size, int i, int j,char type){
            if (Room.is2x2(size,i,j,type)){
                Location room1 = new Location(Bukkit.getWorld("test"),i*32,70,j*32);
                Location room2 = new Location(Bukkit.getWorld("test"),i*32,70,(j+1)*32);
                Location room3 = new Location(Bukkit.getWorld("test"),(i+1)*32,70,j*32);
                Location room4 = new Location(Bukkit.getWorld("test"),(i+1)*32,70,(j+1)*32);
                Block block1 = room1.getBlock();
                Block block2 = room2.getBlock();
                Block block3 = room3.getBlock();
                Block block4 = room4.getBlock();
                block1.setType(RT.a2X2.getType());
                block2.setType(RT.a2X2.getType());
                block3.setType(RT.a2X2.getType());
                block4.setType(RT.a2X2.getType());
                size[i][j] =    RS.a2x2Shape.getID();
                size[i][j+1] =  RS.a2x2Shape.getID();
                size[i+1][j] =  RS.a2x2Shape.getID();
                size[i+1][j+1] =RS.a2x2Shape.getID();
                RS.loadAndPasteSchematic("2x2",
                        RS.a2x2Shape.getPastepoint(i,j, (int) 0),
                        RS.a2x1Shape.getAngle());
            }
            return size;
        }
        public static char[][] fill1x1(char[][] size, int i, int j,char type){
            if (Room.is1x1(size,i,j,type)){
                Location room = new Location(Bukkit.getWorld("test"),i*32,70,j*32);
                Block block = room.getBlock();
                block.setType(RT.a1X1.getType());
                size[i][j] = RS.a1x1Shape.getID();
                RS.loadAndPasteSchematic("1x1",
                        RS.a1x1Shape.getPastepoint(i,j),
                        RS.a1x1Shape.getAngle());
            }
            return size;
        }
        public static char[][] fill4x1(char[][] size, int i, int j,char type){
            //====
            if (Room.is4x1(size,i,j,type)){
                Location room1 = new Location(Bukkit.getWorld("test"),i*32,70,j*32);
                Location room2 = new Location(Bukkit.getWorld("test"),i*32,70,(j+1)*32);
                Location room3 = new Location(Bukkit.getWorld("test"),i*32,70,(j+2)*32);
                Location room4 = new Location(Bukkit.getWorld("test"),i*32,70,(j+3)*32);
                Block block1 = room1.getBlock();
                Block block2 = room2.getBlock();
                Block block3 = room3.getBlock();
                Block block4 = room4.getBlock();
                block1.setType(RT.a4X1.getType());
                block2.setType(RT.a4X1.getType());
                block3.setType(RT.a4X1.getType());
                block4.setType(RT.a4X1.getType());
                size[i][j] =   RS.a4x1Shape.getID();
                size[i][j+1] = RS.a4x1Shape.getID();
                size[i][j+2] = RS.a4x1Shape.getID();
                size[i][j+3] = RS.a4x1Shape.getID();
                RS.loadAndPasteSchematic("1x4",
                        RS.a4x1Shape.getPastepoint(i,j),
                        RS.a4x1Shape.getAngle());
            }
            return size;
        }
        public static char[][] fill1x4(char[][] size, int i, int j,char type){
//                =
//                =
//                =
//                =
            if  (Room.is1x4(size,i,j,type)){
                Location room1 = new Location(Bukkit.getWorld("test"),i*32,70,j*32);
                Location room2 = new Location(Bukkit.getWorld("test"),(i+1)*32,70,j*32);
                Location room3 = new Location(Bukkit.getWorld("test"),(i+2)*32,70,j*32);
                Location room4 = new Location(Bukkit.getWorld("test"),(i+3)*32,70,j*32);
                Block block1 = room1.getBlock();
                Block block2 = room2.getBlock();
                Block block3 = room3.getBlock();
                Block block4 = room4.getBlock();
                block1.setType(RT.a4X1.getType());
                block2.setType(RT.a4X1.getType());
                block3.setType(RT.a4X1.getType());
                block4.setType(RT.a4X1.getType());
                size[i][j] =   RS.a1x4Shape.getID();
                size[i+1][j] = RS.a1x4Shape.getID();
                size[i+2][j] = RS.a1x4Shape.getID();
                size[i+3][j] = RS.a1x4Shape.getID();
                RS.loadAndPasteSchematic("1x4",
                        RS.a1x4Shape.getPastepoint(i,j),
                        RS.a1x4Shape.getAngle());
            }
        return size;
        }
    }


