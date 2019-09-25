package com.work.sqlServerProject.model;

import java.util.List;
import java.util.Set;

/**
 * Created by a.shcherbakov on 25.09.2019.
 */
public class ToJson {
    private int azimuthOfsector;
    private Set<String> colorset;
    private List<PointToMap> points;

    public int getAzimuthOfsector() {
        return azimuthOfsector;
    }

    public void setAzimuthOfsector(int azimuthOfsector) {
        this.azimuthOfsector = azimuthOfsector;
    }

    public Set<String> getColorset() {
        return colorset;
    }

    public void setColorset(Set<String> colorset) {
        this.colorset = colorset;
    }

    public List<PointToMap> getPoints() {
        return points;
    }

    public void setPoints(List<PointToMap> points) {
        this.points = points;
    }
}
