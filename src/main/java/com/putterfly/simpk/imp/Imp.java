package com.putterfly.simpk.imp;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author YueChen
 * @version 1.0
 * @date 2021/10/20 1:09
 */

@Slf4j
public class Imp {
    public static int modValue = 3;



    public static void main(String[] args) {
        String line = "cobegin jsdia safjdioa \n safdjsaoo josidfja coend";
        String pattern = "cobegin([\\S\\s]+)coend";

        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(line);

//        if (m.find()) {
//            log.info(m.group(1));
//        } else {
//            log.info("abcccccccccccc");
//        }
//        log.info(Integer.valueOf(String.valueOf('1')).toString());

        String s = "x=0;\ny=2;\nif x < y then \n while x < 2 do \n x = x + 1; endwhile; else\n y = y + 1; \n endif;";
//        log.info(String.valueOf(s.indexOf("c")));
//        String processTmp = "abc || fdjasoifdj \n dasofdi";
//        ArrayList<String> processes = new ArrayList<>(Arrays.asList(processTmp.split("\\|\\|")));
//        for (int i = 0; i < processes.size(); i++) {
//            processes.set(i, processes.get(i).trim());
//        }
//        for (String ss : processes) {
//            System.out.println(ss);
//            System.out.println("end");
//        }
//        System.out.println(s.substring(1, 7));

//        Character c = 'e';
//        c = Character.valueOf((char) (c + 1));
//        System.out.println(c);

        ArrayList<String> processes = Parser.parseCoProcess(s);
        ArrayList<ArrayList<Statement>> smss = new ArrayList<>();
        for (String v : processes) {
            ArrayList<Statement> tmp = new ArrayList<>();
            Parser.parseStatements(v, tmp);
            smss.add(tmp);
        }

        LabelMaker.labelStatements(smss);

        System.out.println("Labeled function:");
        for (ArrayList<Statement> v : smss) {
            ArrayList<String> list = new ArrayList<>();
            LabelMaker.statementToList(v, list, "");
            String prefix = list.get(0).substring(0, 1);
            list.add(prefix + "E:");
            for (String ls : list) {
                System.out.println(ls);
            }
        }

        ArrayList<ArrayList<FirstOrderLogic>> logicss = new ArrayList<>();
        System.out.println("First order logic formula:");
        boolean hasPc = smss.size() > 1;
        for (int i = 0; i < smss.size(); i++) {
            ArrayList<FirstOrderLogic> logics = FirstOrderLogic.toFormula(smss.get(i), new Statement());
            logicss.add(logics);
            for (FirstOrderLogic v : logics) {
                if (hasPc) {
                    String pc = String.format("pc%d", i);
                    String logicNew = v.toString();
                    logicNew = logicNew.replace("pc", pc);
                    System.out.printf("pc=%s and %s%n", pc, logicNew);
                } else {
                    System.out.println(v.toString());
                }
            }
        }

    }
}
