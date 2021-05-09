package com.example.pnuwalker.pathfind;

import android.content.Context;
import android.os.AsyncTask;
import android.sax.EndElementListener;

import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapPolyLine;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;

public class FindPath {
    private TMapData tMapData;
    private TMapPolyLine polyline;
    private PNUMapInfo pnuMapInfo;
    HashMap<Short, Node> nodes;
    Context c;

    public FindPath(Context c) {
        this.c = c;
        tMapData = new TMapData();
        pnuMapInfo = new PNUMapInfo(c);
        nodes = pnuMapInfo.getGraph();
    }

    public String[] getPolyLineinStr() {
        String result[] = {"",""}; //lon , lat
        if (polyline != null) {
            ArrayList<TMapPoint> points = polyline.getLinePoint();
            for ( int i = 0; i < points.size(); i++ ) {
                TMapPoint p = points.get(i);
                result[0] += String.format("%f",p.getLongitude());
                result[1] += String.format("%f",p.getLatitude());

                if (i != points.size() - 1) {
                    result[0] += ",";
                    result[1] += ",";
                }

            }
        }
        return result;
    }

    public TMapPolyLine getPolyLine() { return polyline; }

    //===================================================================================
    //          TMapPath,PNUPath 통합버전 길찾기(findPath)
    //===================================================================================

    public void findPath(TMapPoint start, TMapPoint end, boolean needDelay) {
        boolean isStartPNU = isPNUPoint(start);
        boolean isEndPNU = isPNUPoint(end);

        System.out.println(isStartPNU);
        System.out.println(isEndPNU);

        if ( isStartPNU ^ isEndPNU ) { //pnu + tmap 경로를 사용

            TMapPoint inPNU = isStartPNU ? start : end;
            TMapPoint outPNU = isStartPNU ? end : start;
            System.out.println(inPNU);
            System.out.println(outPNU);
            //가장 가까운 출구를 찾음
            TMapPoint middle = findPNUExit(inPNU, outPNU);

            //두개의 PolyLine을 계산
            TMapPolyLine PNULine = findPNUPath(inPNU, middle);
            TMapPolyLine TMapLine = findTMapPath(outPNU, middle, needDelay);

            //Polyline을 합침
            ArrayList<TMapPoint> points = isStartPNU ? PNULine.getLinePoint() : TMapLine.getLinePoint();
            TMapPolyLine line = isStartPNU ? TMapLine : PNULine;

            for (TMapPoint p : points) {
                line.addLinePoint(p);
            }

            System.out.println(line.getDistance());
            polyline = line;
        } else {
            if ( isStartPNU )  //두점 모두 내부 = PNU 경로
                polyline = findPNUPath(start, end);
            else //두점 모두 외부 = Tmap 경로
                polyline = findTMapPath(start, end, needDelay);
        }

    }
    
    //해당 포인트가 부산대 내부에 있는지 판별, 부산대 내부에 있다면 true를 리턴
    private boolean isPNUPoint(TMapPoint point) {
        ArrayList<Coordinate> border = pnuMapInfo.getBorder();

        double pLon = point.getLongitude(); //y
        double pLat = point.getLatitude(); //x
        int crossCount = 0;

        int j = border.size() - 1;
        //Point in Polygon 알고리즘
        for( int i = 0; i < border.size(); i++ ) {
            double cLon1 = border.get(i).getLon();
            double cLat1 = border.get(i).getLat();
            double cLon2 = border.get(j).getLon();
            double cLat2 = border.get(j).getLat();

            if ( (pLon >= cLon1 && pLon < cLon2) || (pLon >= cLon2 && pLon < cLon1)
                    && (pLat >= cLat1 || pLat >= cLat2) ) {
                if ( cLat1 + (pLon - cLon1) / (cLon2 - cLon1) * (cLat2 - cLat1) < pLat) {
                    crossCount++;
                }
            }

            j = i;
        }

        if (crossCount % 2 == 0) {
            return false;
        } else {
            return true;
        }
    }

    //point에서 가장 가까운 부산대 입구를 찾아 point를 리턴
    private TMapPoint findPNUExit(TMapPoint inPNUPoint, TMapPoint outPNUPoint) {
        //내부 ~ 출구 까지의 유클리드 거리 + 출구에 대응되는 거리 + 출구에서 도착지까지의 유클리드 거리를 비교
        double bestDistance = Double.MAX_VALUE;
        short bestIndex = 99;

        for (short i = 99; i < 108; i++) {
            Coordinate exitC = nodes.get(i).getCoord();
            TMapPolyLine polyLine = new TMapPolyLine();
            polyLine.addLinePoint(new TMapPoint(exitC.getLat(), exitC.getLon()));
            polyLine.addLinePoint(outPNUPoint);

            double distance = findPNUPath(inPNUPoint, new TMapPoint(exitC.getLat(), exitC.getLon())).getDistance()
                    + polyLine.getDistance();

            if (distance < bestDistance) {
                bestDistance = distance;
                bestIndex = i;
            }
        }
        Coordinate best = nodes.get(bestIndex).getCoord();
        System.out.println(bestIndex);
        return new TMapPoint( best.getLat(), best.getLon() );
    }
    //===================================================================================
    //          TMapPath
    //===================================================================================

    public TMapPolyLine findTMapPath(TMapPoint start, TMapPoint end, boolean needDelay) {
        TMapPoint p[] = {start ,end};
        try {
            TMapPolyLine line = new FindPath.findTPath().execute(p).get();
            if ( needDelay )
                Thread.sleep(500);

            System.out.println(line.getDistance());
            return line;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    class findTPath extends AsyncTask<TMapPoint, Void,TMapPolyLine> {
        @Override
        protected TMapPolyLine doInBackground(TMapPoint... tMapPoints) {
            try {
                TMapPolyLine line = tMapData.findPathDataWithType(TMapData.TMapPathType.PEDESTRIAN_PATH, tMapPoints[0], tMapPoints[1]);
                return line;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    //===================================================================================
    //          PNUPath
    //===================================================================================
    private double euclid(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2)) * 10000;
    }

    public TMapPolyLine findPNUPath(TMapPoint start, TMapPoint end) {
        short startIndex = getPNUIndex(start);
        short endIndex = getPNUIndex(end);

        HashMap<Short, Double> f = new HashMap<>(370);
        HashMap<Short, Double> g = new HashMap<>(370);
        ArrayList<Short> closeList = new ArrayList<>(370);
        PriorityQueue<Short> openList = new PriorityQueue<>(370, new Comparator<Short>() {
            @Override
            public int compare(Short o1, Short o2) {
                if ( f.get(o1) < f.get(o2) ) {
                    return -1;
                } else if ( f.get(o1) == f.get(o2) ) {
                    return 0 ;
                } else {
                    return 1;
                }
            }
        });
        Node endNode = nodes.get(endIndex);

        //A* 알고리즘 수행
        openList.add(startIndex);
        g.put(startIndex, 0.0);
        f.put(startIndex, 0.0);

        while( !openList.isEmpty() ) {
            if ( closeList.contains(endIndex) ) {
                TMapPolyLine line = new TMapPolyLine();
                line.setLineWidth(2);
                line.setOutLineWidth(1);
                line.addLinePoint(end);

                short lineIndex = nodes.get(endIndex).getParentIndex();
                while (true) {
                    Node temp = nodes.get(lineIndex);
                    TMapPoint point = new TMapPoint(temp.getLat(), temp.getLon());
                    line.addLinePoint(point);
                    lineIndex = nodes.get(lineIndex).getParentIndex();
                    if ( lineIndex == startIndex )
                        break;
                }
                line.addLinePoint(start);
                System.out.println(line.getDistance());
                return line;
            }

            short currentIndex = openList.poll();
            Node currentNode = nodes.get(currentIndex);

            HashMap<Short, Double> neighbor = currentNode.getNeighbor();
            for ( short neighborIndex : neighbor.keySet() ) {
                Node neighborNode = nodes.get(neighborIndex);

                double gscore = g.get(currentIndex) + neighbor.get(neighborIndex); //현재 노드까지의 gscore + 두 node간 연결되어 있는 edge의 weight
                double hscore = euclid(neighborNode.getLon(), neighborNode.getLat(),
                        endNode.getLon(), endNode.getLat());

                if ( !closeList.contains(neighborIndex) ) {
                    if ( openList.contains(neighborIndex) ) { //포함되어 있는경우, 현재 계산된 gscore가 저장된 gscore보다 작다면 update
                        if ( gscore < g.get(neighborIndex) ) {
                            g.put(neighborIndex, gscore);
                            f.put(neighborIndex, gscore + hscore);
                            nodes.get(neighborIndex).setParentIndex(currentIndex);
                        }
                    } else { //없는 노드인 경우. openList에 추가
                        g.put(neighborIndex, gscore);
                        f.put(neighborIndex, gscore + hscore);
                        openList.add(neighborIndex);
                        nodes.get(neighborIndex).setParentIndex(currentIndex);
                    }
                }
            }
            closeList.add(currentIndex);
        }

        System.out.println("경로를 찾지못함");
        return null;
    }

    //TMapPoint를 받아 index로 변환
    private short getPNUIndex(TMapPoint point) {
        double pLon = point.getLongitude();
        double pLat = point.getLatitude();

        double bestDistance = Double.MAX_VALUE;
        short bestIndex = 0;

        for ( short s : nodes.keySet() ) {
            Coordinate c = nodes.get(s).getCoord();
            double lon = c.getLon();
            double lat = c.getLat();

            double distance = euclid(pLon, pLat, lon,lat);
            if ( distance < bestDistance ) {
                bestDistance = distance;
                bestIndex = s;
            }
        }

        return bestIndex;
    }
}
