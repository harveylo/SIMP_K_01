package com.putterfly.simpk.imp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author YueChen
 * @version 1.0
 * @date 2021/10/21 1:11
 */
public class Parser {
    public static List<String> parseCoProcess(String text) {
        List<String> processes;
        Pattern r = Pattern.compile("cobegin([\\s\\S]+)coend");
        Matcher m = r.matcher(text);
        if (m.find()) {
            String processTmp = m.group(1);
            processTmp = processTmp.trim();
            processes = new ArrayList<>(Arrays.asList(processTmp.split("\\|\\|")));
        } else {
            processes = new ArrayList<>(Collections.singleton(text));
        }
        for (int i = 0; i < processes.size(); i++) {
            processes.set(i, processes.get(i).trim());

        }
        return processes;
    }

    public static void parseStatements(String input, List<Statement> statements) {
        int s = 0;
        int e = 0;
        while (s < input.length()) {
            int posIf = input.indexOf("if", s);
            int posWhile = input.indexOf("while", s);
            int pos;
            if (posIf == -1 || posWhile == -1) {
                pos = Math.max(posIf, posWhile);
            } else {
                pos = Math.min(posIf, posWhile);
            }
            if (-1 == pos) {
                e = input.length();
                String inputSplit = input.substring(s, e);
                statements.addAll(parseSequence(inputSplit));
                s = e;
            } else {
                if (pos > s) {
                    statements.addAll(parseSequence(input.substring(s, pos)));
                }
                s = pos;
                if (input.charAt(pos) == 'i') {
                    e = input.indexOf("endif");
                    e += 6;
                    statements.add(parseIf(input.substring(s, e)));
                } else {
                    e = input.indexOf("endwhile");
                    e += 9;
                    statements.add(parseWhile(input.substring(s, e)));
                }
                s = e;
            }
        }
    }

    static Statement parseWait(String input) {
        Statement sm = new Statement();
        String condition;
        Pattern r = Pattern.compile("wait\\((.+)\\)");
        Matcher m = r.matcher(input);
        if (!m.find()) throw new AssertionError();
        condition = m.group(1).trim();

        sm.setType(StatementType.Wait);
        sm.setCondition(condition);
        return sm;
    }

    static ArrayList<Statement> parseSequence(String input) {
        ArrayList<Statement> sms = new ArrayList<>();
        input = input.trim();
        String[] l = input.split(";");
        for (String v : l) {
            Statement sm = new Statement();
            v = v.trim();
            if (v.contains("wait")) {
                sm = parseWait(input);
            } else {
                sm.setType(StatementType.Sequence);
                sm.setSeqBody(v);
            }
            sms.add(sm);
        }
        return sms;
    }

    static Statement parseIf(String input) {
        Statement sm = new Statement();
        String inputNew = input.replace("\n", " ");
        String condition;
        String ifBody;
        String elseBody;

        if (input.contains("else")) {
            Pattern r = Pattern.compile("if(.+)then(.+)else(.+)endif;");
            Matcher m = r.matcher(inputNew);
            if (!m.find()) throw new AssertionError();
            condition = m.group(1).trim();
            ifBody = m.group(2).trim();
            elseBody = m.group(3).trim();
        } else {
            Pattern r = Pattern.compile("if(.+)then(.+)endif;");
            Matcher m = r.matcher(inputNew);
            condition = m.group(1).trim();
            ifBody = m.group(2).trim();
            elseBody = "";
        }

        sm.setType(StatementType.If);
        sm.setCondition(condition);
        parseStatements(ifBody, sm.getIfBody());
        parseStatements(elseBody, sm.getElseBody());
        return sm;
    }

    static Statement parseWhile(String input) {
        Statement sm = new Statement();
        String inputNew = input.replace("\n", " ");
        String condition;
        String body;
        Pattern r = Pattern.compile("while(.+)do(.+)endwhile;");
        Matcher m = r.matcher(inputNew);
        if (!m.find()) throw new AssertionError();
        condition = m.group(1).trim();
        body = m.group(2).trim();

        sm.setType(StatementType.While);
        sm.setCondition(condition);
        parseStatements(body, sm.getWhileBody());
        return sm;
    }

}
