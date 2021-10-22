package com.putterfly.simpk.imp;

import javafx.util.Pair;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;

/**
 * @author YueChen
 * @version 1.0
 * @date 2021/10/20 1:09
 */

@Slf4j
public class Imp {
    public static int modValue = 3;


    public static ArrayList<ArrayList<Statement>> parseImp(String input) {
        ArrayList<String> processes = Parser.parseCoProcess(input);
        ArrayList<ArrayList<Statement>> smss = new ArrayList<>();
        for (String v : processes) {
            ArrayList<Statement> tmp = new ArrayList<>();
            Parser.parseStatements(v, tmp);
            smss.add(tmp);
        }
        return smss;
    }

    public static String makeLabel(ArrayList<ArrayList<Statement>> smss) {
        LabelMaker.labelStatements(smss);

        StringBuilder labeledImp = new StringBuilder();
        for (ArrayList<Statement> v : smss) {
            ArrayList<String> list = new ArrayList<>();
            LabelMaker.statementToList(v, list, "");
            String prefix = list.get(0).substring(0, 1);
            list.add(prefix + "E:");
            for (String ls : list) {
                labeledImp.append(ls).append("\n");
            }
        }

        return labeledImp.toString();
    }

    public static Pair<String, ArrayList<ArrayList<FirstOrderLogic>>> makeFirstOrderLogic(ArrayList<ArrayList<Statement>> smss) {
        ArrayList<ArrayList<FirstOrderLogic>> logicss = new ArrayList<>();
        StringBuilder logicString = new StringBuilder();
        boolean hasPc = smss.size() > 1;
        for (int i = 0; i < smss.size(); i++) {
            ArrayList<FirstOrderLogic> logics = FirstOrderLogic.toFormula(smss.get(i), new Statement());
            logicss.add(logics);
            for (FirstOrderLogic v : logics) {
                if (hasPc) {
                    String pc = String.format("pc%d", i);
                    String logicNew = v.toString();
                    logicNew = logicNew.replace("pc", pc);
                    logicString.append(String.format("pc=%s and %s%n", pc, logicNew));
                } else {
                    logicString.append(v.toString());
                }
            }
        }
        return new Pair<>(logicString.toString(), logicss);
    }


    public static void main(String[] args) {
        String s = "cobegin while true do wait(r==0); r = 1; endwhile; || while true do wait(r == 1); r = 0; endwhile; coend;";

        ArrayList<ArrayList<Statement>> imp = parseImp(s);
        System.out.println(makeLabel(imp));
        System.out.println(makeFirstOrderLogic(imp).getKey());

    }
}
