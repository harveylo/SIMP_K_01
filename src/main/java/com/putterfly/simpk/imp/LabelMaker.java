package com.putterfly.simpk.imp;

import java.util.ArrayList;
import java.util.List;

/**
 * @author YueChen
 * @version 1.0
 * @date 2021/10/21 20:52
 */
public class LabelMaker {

    public static void labelStatements(List<List<Statement>> smss) {
        Character prefix = 'A';
        for (List<Statement> sms : smss) {
            int index = 0;
            labelStatements(prefix, index, sms);
            prefix = Character.valueOf((char) (prefix + 1));
        }
    }

    public static void statementToList(List<Statement> sms, List<String> list, String space) {
        if (sms.isEmpty()) {
            return;
        }
        String spaceNew = space + "    ";
        ArrayList<String> ls;
        for (Statement v : sms) {
            if (v.getType() == StatementType.Sequence) {
                list.add(String.format("%s: %s%s;", v.getLabel(), space, v.getSeqBody()));
            } else if (v.getType() == StatementType.Wait) {
                list.add(String.format("%s: %swait (%s);", v.getLabel(), space, v.getCondition()));
            } else if (v.getType() == StatementType.If) {
                list.add(v.getLabel() + ": " + space + "if " + v.getCondition() + " then");
                statementToList(v.getIfBody(), list, spaceNew);
                list.add(space + "    else");
                statementToList(v.getElseBody(), list, spaceNew);
                list.add(space + "    endif;");
            } else if (v.getType() == StatementType.While) {
                list.add(v.getLabel() + ": " + space + "while " + v.getCondition() + " do");
                statementToList(v.getWhileBody(), list, spaceNew);
                list.add(space + "    endwhile;");
            }
        }
    }

    public static int labelStatements(Character prefix, int index, List<Statement> sms) {
        if (sms.isEmpty()) {
            return index;
        }
        for (Statement v : sms) {
            v.setLabel(String.format("%s%d", prefix, index++));
            if (v.getType() == StatementType.If) {
                index = labelStatements(prefix, index, v.getIfBody());
                index = labelStatements(prefix, index, v.getElseBody());
            } else if (v.getType() == StatementType.While) {
                index = labelStatements(prefix, index, v.getWhileBody());
            }
        }
        return index;
    }

}
