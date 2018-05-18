package cn.jbit.entity;

import java.io.Serializable;

/**
 * 游戏类型类
 */
public class GamesType implements Serializable {
   private int tId;//游戏类型编号',
    private String tName;//游戏类型名称

    public int gettId() {
        return tId;
    }

    public void settId(int tId) {
        this.tId = tId;
    }

    public String gettName() {
        return tName;
    }

    public void settName(String tName) {
        this.tName = tName;
    }
}
