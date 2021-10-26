package com.putterfly.simpk.drawer;

import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.util.Pair;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class KSDrawer {
    public static void draw(Canvas canvas, List<String> labels,List<Pair<String,String>> relations){
        final int w = 90;
        final int h = 60;
        final int spanH = 40;
        final int spanV = 40;
        Map<String,int[]> nodes = new HashMap<>();
        GraphicsContext graphicsContext2D = canvas.getGraphicsContext2D();
        graphicsContext2D.clearRect(0,0,canvas.getWidth(),canvas.getHeight());
        graphicsContext2D.fillText("Kripke Structure Graph Transferred from the input program:",spanH,(double)spanV/2);
        int positionY = 0;
        int positionX = 0;
        Iterator<String> iter = labels.iterator();
        while(iter.hasNext()){
            positionX++;
            positionY += h+spanV;
            for(int j = 0;j<positionX;j++){
                String label = iter.next();
                if(!iter.hasNext()) break;
                nodes.put(label,new int[]{j*(w+spanH),positionY});
            }
        }

        for(Pair<String,String> r : relations){
            if(r.getValue()==null||r.getValue().isEmpty()) continue;
            if(r.getValue().equals(r.getKey())){
                int[] first = nodes.get(r.getValue());
                if (first==null) continue;
                graphicsContext2D.strokeLine((double)(first[0]+(double)w/2),first[1],(double)(first[0]+w+5),first[1]);
                graphicsContext2D.strokeLine((double)(first[0]+5+w),first[1],(double)(first[0]+5+w),(double)(first[1]+(double)h/2) );
                double[] getArr = calVertexes((double)(first[0]+5+w),(double)(first[1]+(double)h/2),(double)(first[0]+w),(double)(first[1]+(double)h/2));
                graphicsContext2D.strokeLine((double)(first[0]+5+w),(double)(first[1]+(double)h/2),(double)(first[0]+w),(double)(first[1]+(double)h/2));
                graphicsContext2D.strokeLine((double)(first[0]+w),(double)(first[1]+(double)h/2),getArr[0],getArr[1]);
                graphicsContext2D.strokeLine((double)(first[0]+w),(double)(first[1]+(double)h/2),getArr[2],getArr[3]);
                continue;
            }
            int[] start = nodes.get(r.getKey());
            int[] end = nodes.get(r.getValue());
            if(end==null||start ==null) continue;
            double[] scenter = new double[]{start[0]+(double)w/2,start[1]};
            double[] ecenter = new double[]{end[0]+(double)h/2,(double)(end[1]+10)};
            double[] arr = calVertexes(scenter[0],scenter[1],ecenter[0],ecenter[1]);
            graphicsContext2D.strokeLine(scenter[0],scenter[1],ecenter[0],ecenter[1]);
            graphicsContext2D.strokeLine(ecenter[0],ecenter[1],arr[0],arr[1]);
            graphicsContext2D.strokeLine(ecenter[0],ecenter[1],arr[2],arr[3]);
        }
        positionX = 0;
        positionY = 0;
        iter = labels.iterator();
        while(iter.hasNext()){
            positionX++;
            positionY += h+spanV;
            for(int j = 0;j<positionX;j++){
                String label = iter.next();
                if(!iter.hasNext()) break;
                graphicsContext2D.setFill(Color.GRAY);
                graphicsContext2D.fillOval(j*(w+spanH),positionY,w,h);
                graphicsContext2D.setTextAlign(TextAlignment.CENTER);
                graphicsContext2D.setTextBaseline(VPos.CENTER);
                graphicsContext2D.setFill(Color.WHITE);
                graphicsContext2D.fillText(label,Math.round(j*(w+spanH)+(double)w/2),Math.round(positionY+(double)h/2));
            }
        }

    }

    private static double[] calVertexes(double sx,double sy,double ex,double ey){
        double length = 6;
        double degree = 0.5;

        double angle = Math.atan2(ey-sy,ex-sx)+Math.PI;

        double x1 = ex + length * Math.cos(angle - degree);
        double y1 = ey + length * Math.sin(angle - degree);
        double x2 = ex + length * Math.cos(angle + degree);
        double y2 = ey + length * Math.sin(angle + degree);

        return new double[]{x1,y1,x2,y2};
    }

}
