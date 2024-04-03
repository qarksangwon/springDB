package com.kh.os.cls;

public class ApplyVo {
    private String ID;
    private String NAME;
    private String TITLE;
    private String ROOM;

    public ApplyVo(String ID, String NAME, String TITLE, String ROOM) {
        this.ID = ID;
        this.NAME = NAME;
        this.TITLE = TITLE;
        this.ROOM = ROOM;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public String getTITLE() {
        return TITLE;
    }

    public void setTITLE(String TITLE) {
        this.TITLE = TITLE;
    }

    public String getROOM() {
        return ROOM;
    }

    public void setROOM(String ROOM) {
        this.ROOM = ROOM;
    }
}

