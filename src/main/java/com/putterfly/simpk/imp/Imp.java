package com.putterfly.simpk.imp;

import javafx.util.Pair;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author YueChen
 * @version 1.0
 * @date 2021/10/20 1:09
 */

@Slf4j
public class Imp {
    public static int modValue = 3;


    public static List<List<Statement>> parseImp(String input) {
        List<String> processes = Parser.parseCoProcess(input);
        List<List<Statement>> smss = new LinkedList<>();
        for (String v : processes) {
            List<Statement> tmp = new LinkedList<>();
            Parser.parseStatements(v, tmp);
            smss.add(tmp);
        }
        return smss;
    }

    public static String makeLabel(List<List<Statement>> smss) {
        LabelMaker.labelStatements(smss);

        StringBuilder labeledImp = new StringBuilder();
        for (List<Statement> v : smss) {
            List<String> list = new LinkedList<>();
            LabelMaker.statementToList(v, list, "");
            String prefix = list.get(0).substring(0, 1);
            list.add(prefix + "E:");
            for (String ls : list) {
                labeledImp.append(ls).append("\n");
            }
        }

        return labeledImp.toString();
    }

    public static Pair<String, List<List<FirstOrderLogic>>> makeFirstOrderLogic(List<List<Statement>> smss) {
        List<List<FirstOrderLogic>> logicss = new LinkedList<>();
        StringBuilder logicString = new StringBuilder();
        boolean hasPc = smss.size() > 1;
        for (int i = 0; i < smss.size(); i++) {
            List<FirstOrderLogic> logics = FirstOrderLogic.toFormula(smss.get(i), new Statement());
            logicss.add(logics);
            for (FirstOrderLogic v : logics) {
                if (hasPc) {
                    String pc = String.format("pc%d", i);
                    String logicNew = v.toString();
                    logicNew = logicNew.replace("pc", pc);
                    logicString.append(String.format("pc=%s and %s", pc, logicNew)).append('\n');
                } else {
                    logicString.append(v.toString()).append('\n');
                }
            }
        }
        return new Pair<>(logicString.toString(), logicss);
    }


//    public static void main(String[] args) {
//        String s = "cobegin while true do wait(r==0); r = 1; endwhile; || while true do wait(r == 1); r = 0; endwhile; coend;";
//
//        List<List<Statement>> imp = parseImp(s);
//        System.out.println(makeLabel(imp));
//        System.out.println(makeFirstOrderLogic(imp).getKey());
//
//    }
}
