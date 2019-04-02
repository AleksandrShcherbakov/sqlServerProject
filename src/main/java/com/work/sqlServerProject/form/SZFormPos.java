package com.work.sqlServerProject.form;

/**
 * Created by a.shcherbakov on 14.03.2019.
 */
public class SZFormPos {
    private int posName;
    private String pathDir;

    public String getPathDir() {
        return pathDir;
    }

    public void setPathDir(String pathDir) {
        this.pathDir = pathDir;
    }

    public SZFormPos() {
    }

    public int getPosName() {
        return posName;
    }

    public void setPosName(int posName) {
        this.posName = posName;
    }
}
