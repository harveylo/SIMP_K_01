package com.putterfly.simpk.ks;

import com.putterfly.simpk.imp.FirstOrderLogic;
import com.putterfly.simpk.imp.Variable;
import javafx.util.Pair;

import java.util.LinkedList;
import java.util.List;

public class KripkeTransfer {
    private static List<String> pcs = new LinkedList<>();
    private static List<Pair<String,String>> relations = new LinkedList<>();
    private static List<String> labels;
    private static List<FirstOrderLogic> lastLogics = new LinkedList<>();
    private static List<String> states = new LinkedList<>();
    private static List<KripkeStructure> nodes = new LinkedList<>();
    private static List<Variable> variables = new LinkedList<>();


//    public static String beginTransfer(List<List<FirstOrderLogic>> lgs){
//        for (List<FirstOrderLogic> lg : lgs) {
//            if(lgs.size()>1) pcs.add("U");
//            else pcs.add("");
//            lastLogics.add(new FirstOrderLogic());
//        }
//
//    }

    public static void createKLabels(List<List<FirstOrderLogic>> lgs,int deep){
        deep++;
        List<String> npcs = pcs;
        List<FirstOrderLogic> nlls = lastLogics;
//        for(int i = 0;i<pcs.size();i++){
//            KripkeStructure
//        }
    }
}
