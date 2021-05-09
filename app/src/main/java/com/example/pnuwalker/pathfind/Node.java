package com.example.pnuwalker.pathfind;
import java.util.HashMap;

public class Node{
    private Coordinate coords;
    private short parentIndex;

    private HashMap<Short, Double> neighbor;

    public Node(double lon, double lat,  HashMap<Short, Double> neighbor) {
        coords = new Coordinate(lon,lat);
        parentIndex = -1;
        this.neighbor = neighbor;
    }

    public String toString() {
        String s = String.format("%f,%f,",coords.getLon(),coords.getLat());
        for (short t : neighbor.keySet()) {
            s += Short.toString(t) + ":" + Double.toString(neighbor.get(t)) + ",";
        }
        s += "]";
        return s;
    }

    public Coordinate getCoord() { return coords; }
    public double getLon() { return coords.getLon(); }
    public double getLat() { return coords.getLat(); }
    public HashMap<Short, Double> getNeighbor() {
        return neighbor;
    }

    public void setParentIndex(short index) { parentIndex = index; }
    public short getParentIndex() {
        return parentIndex;
    }
}
