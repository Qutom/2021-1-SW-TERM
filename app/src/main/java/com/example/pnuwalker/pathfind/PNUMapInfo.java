package com.example.pnuwalker.pathfind;

import android.content.Context;

import com.example.pnuwalker.R;

import java.util.ArrayList;
import java.util.HashMap;

//부산대 내부 지도를 표현하기 위한 클래스
public class PNUMapInfo {

    private HashMap<Short, Node> graph;
    private ArrayList<Coordinate> border;
    private Context c;

    public PNUMapInfo(Context c) {
        this.c = c;
        //부산대 샛길을 포함한 부산대 내부 지도 그래프를 설정
        short index = 0;
        graph = new HashMap<>(370);
        String[] pnuGraphStrings = c.getResources().getStringArray(R.array.pnu_graph);

        for (String s : pnuGraphStrings)
            addNodeToGraph(s);

        //부산대 내부인지 판별하는 기준이되는 polygon을 구성하는 border를 설정
        border = new ArrayList<>(23);
        String[] pnuBorderStrings = c.getResources().getStringArray(R.array.pnu_border);

        for (String s : pnuBorderStrings)
            addCoordinateToBorder(s);

    }

    private void addCoordinateToBorder(String data) {
        String[] temp = data.split(",");
        Coordinate coords = new Coordinate(Double.parseDouble(temp[0]),Double.parseDouble(temp[1]));
        border.add(coords);
    }

    private void addNodeToGraph(String data) { //data : "x,y,index,adjacentIndex들,weight들"
        String[] temp = data.split(",");
        double x = Double.parseDouble(temp[0]);
        double y = Double.parseDouble(temp[1]);

        ArrayList<Short> adjacentIndex = new ArrayList<Short>();
        ArrayList<Double> weight = new ArrayList<Double>();
        HashMap<Short, Double> hash = new HashMap<>();

        for ( String s : temp[3].split(":") ) {
            adjacentIndex.add(Short.parseShort(s));
        }

        for ( String s : temp[4].split(":") ) {
            weight.add(Double.parseDouble(s));
        }

        for (int i = 0; i < adjacentIndex.size(); i++)
            hash.put(adjacentIndex.get(i), weight.get(i));

        graph.put(Short.parseShort(temp[2]) ,new Node(x,y,hash));
    }

    ArrayList<Coordinate> getBorder() { return border; }
    HashMap<Short, Node> getGraph() { return graph; }
}
