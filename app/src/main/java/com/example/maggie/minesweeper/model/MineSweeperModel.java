package com.example.maggie.minesweeper.model;

import java.util.Random;

/**
 * Created by Maggie on 2/25/16.
 */
public class MineSweeperModel {
    private static MineSweeperModel instance=null;

    public int totalMines=3;
    public int numColumns=5;
    public int numRows=5;
    public int mineNearCount=0;

    private MineSweeperModel(){
    }
    public static MineSweeperModel getInstance(){
        if(instance==null){
            instance=new MineSweeperModel();
        }
        return instance;
    }
    public static final short EMPTY=0;
    public static final short MINE=1;
    public static final short ZERO=2;
    public static final short ONE=3;
    public static final short TWO=4;
    public static final short THREE=5;
    public static final short FLAG=6;

    private short[][] model=new short[numColumns][numRows];

    public boolean hasMine(int x, int y) {
        if (model[x][y]==MINE) {
            return true;
        }else {
            return false;
        }
    }
    public void plantMine(int x, int y){
        model[x][y]=MINE;
    }
    public void mineNear(int x, int y) {
        for (int i = x - 1; i < x + 2; i++) {
            for (int j = y - 1; j < y + 2; j++) {
                if ((i > 4 || i < 0) || (j > 4 || j < 0)) {
                    continue;
                } else if (model[i][j] == MINE) {
                        mineNearCount++;
                }
            }
        }
        setNumberModel(x, y);
        if (hasMine(x, y)) {
                    System.out.println("YOU HIT A MINE THERE KIDDO");
                }
            mineNearCount=0;
    }

    private void setNumberModel(int x, int y) {
        switch (mineNearCount) {
            case 0:
                model[x][y]=ZERO;
                break;
            case 1:
                model[x][y]=ONE;
                break;
            case 2:
                model[x][y]=TWO;
                break;
            case 3:
                model[x][y]=THREE;
                break;
        }
    }

    public void setMines(){
        Random rand=new Random();
        int mineX, mineY;
        int mineCounter=0;


        for(int row=0;row<totalMines;row++){
            mineX=rand.nextInt(numRows);
            mineY = rand.nextInt(numColumns);
            if(mineCounter<3){
                    plantMine(mineX, mineY);
                    mineCounter++;
                }
            }
        }
    public boolean isFlag(int x, int y){
        if(model[x][y]==FLAG){
            return true;
        }return false;
    }

    public void resetModel(){
        for(int i=0;i<numRows;i++){
            for(int j=0;j<numColumns;j++){
                model[i][j]=EMPTY;
            }
        }
    }
    public short getFieldContent(int x, int y) {
        return model[x][y];
    }

    public short setFieldContent(int x, int y, short content) {
        return model[x][y] = content;
    }
}
