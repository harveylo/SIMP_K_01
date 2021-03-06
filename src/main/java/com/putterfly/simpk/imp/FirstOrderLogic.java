package com.putterfly.simpk.imp;


import javafx.util.Pair;
import lombok.Data;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author YueChen
 * @version 1.0
 * @date 2021/10/20 16:06
 */
@Data
public class FirstOrderLogic {
    private String preLabel;
    private String postLabel;
    private String condition;
    private String opr;
    private List<Variable> vars;

    public boolean isNull() {
        return (preLabel==null&&postLabel==null)||preLabel.isEmpty() && postLabel.isEmpty();
    }

    public FirstOrderLogic(){
        preLabel = "";
        postLabel = "";
        condition = "";
        opr = "";
        vars = new LinkedList<>();
    }
    public FirstOrderLogic(FirstOrderLogic f){
        preLabel = f.getPreLabel();
        postLabel = f.getPostLabel();
        condition = f.getCondition();
        opr = f.getOpr();
        vars = f.getVars();
    }
    public void changeAll(FirstOrderLogic f){
        preLabel = f.getPreLabel();
        postLabel = f.getPostLabel();
        condition = f.getCondition();
        opr = f.getOpr();
        vars = f.getVars();
    }

    public String findAssignVariable(String statement) {
        Pattern r = Pattern.compile("(\\w+)\\s*=\\s*");
        Matcher m = r.matcher(statement);

        if (m.find()) {
            return m.group(1);
        } else {
            return "";
        }
    }

    public String toString() {
        if (!condition.isEmpty()) {
            return String.format("pc=%s and pc'=%s and (%s) and SAME(V)", preLabel, postLabel, condition);
        } else {
            String tmp = String.format("pc=%s and pc'=%s and (%s)", preLabel, postLabel, opr);
            String var = findAssignVariable(opr);
            if (var.isEmpty()) {
                tmp += " and SAME(V)";
            } else {
                tmp += String.format(" and SAME(V\\{%s})", var);
            }
            return tmp;
        }
    }

    public String valueToString() {
        StringBuilder tmp = new StringBuilder();
        for (Variable v : vars) {
            if (tmp.length() > 0) {
                tmp.append(", ");
            }
            tmp.append(String.format("%s=%d", v.getName(), v.getValue()));
        }
        return tmp.toString();
    }

    public Pair<Character, Integer> assign() {
        if (opr.isEmpty() || !opr.contains("=")) {
            return new Pair<>(null, 0);
        }
        Pattern r = Pattern.compile("(\\w)\\s*=\\s*(\\w)\\s*([*+-])\\s*(\\w)");
        Matcher m = r.matcher(opr);
        if (m.find()) {
            Character ret = m.group(1).charAt(0);
            Character left = m.group(2).charAt(0);
            Character op = m.group(3).charAt(0);
            Character right = m.group(4).charAt(0); // the variable name are single lower letter, range of int is [0, 2]

            int nLeft = getVarValue(left);
            int nRight = getVarValue(right);
            int nRet = 0;
            if (op.equals('*')) {
                nRet = nLeft * nRight % Imp.modValue;
            } else if (op.equals('+')) {
                nRet = nLeft + nRight % Imp.modValue;
            } else if (op.equals('-')) {
                nRet = (nLeft - nRight + 3) % Imp.modValue;
            }

            return new Pair<>(ret, nRet);
        }

        r = Pattern.compile("(\\w+)\\s*=\\s*(\\d+)");
        m = r.matcher(opr);
        if(m.find()) {
            return new Pair<>(m.group(1).charAt(0), Integer.valueOf(m.group(2)));
        }
        return new Pair<>(null, 0);
    }

    public boolean hasAssign() {
        if (!condition.isEmpty() || opr.isEmpty()) {
            return false;
        }
        String var = findAssignVariable(opr);
        return !var.isEmpty();
    }

    public boolean isConditionOk() {
        if (condition.isEmpty()) {
            return true;
        }
        if (0 == condition.compareTo("true")) {
            return true;
        }
        if (0 == condition.compareTo("false")) {
            return false;
        }

        boolean hasNot = false;
        String conditionNew = condition;
        if (conditionNew.contains("not")) {
            hasNot = true;
            conditionNew = conditionNew.replace("not", "");
            conditionNew = conditionNew.trim();
        }

        Pattern r = Pattern.compile("(\\w)\\s*([><=andotr]+)\\s*(\\w)");
        Matcher m = r.matcher(conditionNew);
        if (!m.find()) throw new AssertionError();

        Character varLeft = m.group(1).charAt(0);
        String op = m.group(2);
        Character varRight = m.group(3).charAt(0);

        int left = getVarValue(varLeft);
        int right = getVarValue(varRight);

        boolean res = true;
        if (op.equals(">=")) {
            res = (left >= right);
        } else if (op.equals("==")) {
            res = (left == right);
        } else if (op.equals("<=")) {
            res = (left <= right);
        } else if (op.equals(">")) {
            res = (left > right);
        } else if (op.equals("<")) {
            res = (left < right);
        } else if (op.equals("and")) {
            res = (left > 0 && right > 0);
        } else if (op.equals("or")) {
            res = (left + right > 0);
        } else {
            assert false;
        }
        return hasNot != res; // has ? !res : res
    }

    Integer getVarValue(Character var) {
        if (Character.isDigit(var)) {
            return Integer.valueOf(String.valueOf(var));
        }
        for (Variable v : vars) {
            if (v.getName().equals(String.valueOf(var))) {
                return v.getValue();
            }
        }
        return 0;
    }

    static FirstOrderLogic toFormula(Statement pre, Statement post) {
        FirstOrderLogic logic = new FirstOrderLogic();
        logic.setPreLabel(pre.getLabel());
        logic.setPostLabel(post.getLabel());
        logic.setCondition(pre.getCondition());
        logic.setOpr(pre.getSeqBody());
        logic.setVars(pre.getVars());
        return logic;
    }

    public static List<FirstOrderLogic> toFormula(List<Statement> sms, Statement out) {
        if (sms.isEmpty()) {
            return new LinkedList<>();
        }
        if (out.getLabel().isEmpty()) {
            String prefix = sms.get(0).getLabel().substring(0, 1);
            out.setLabel(prefix + "E");
        }

        List<FirstOrderLogic> list = new LinkedList<>();
        for (int i = 0; i < sms.size(); i++) {
            Statement postSm = out;
            Statement sm = sms.get(i);
            if (i + 1 < sms.size()) {
                postSm = sms.get(i + 1);
            }
            if (sm.getType() == StatementType.If) {
                 if (!sm.getIfBody().isEmpty()) {
                     list.add(toFormula(sm, sm.getIfBody().get(0)));
                     list.addAll(toFormula(sm.getIfBody(), postSm));
                 }
                 if (!sm.getElseBody().isEmpty()) {
                     sm.reverseCondition();
                     list.add(toFormula(sm, sm.getElseBody().get(0)));
                     list.addAll(toFormula(sm.getElseBody(), postSm));
                 }
            } else if (sm.getType() == StatementType.While) {
                if (!sm.getWhileBody().isEmpty()) {
                    list.add(toFormula(sm, sm.getWhileBody().get(0)));
                    list.addAll(toFormula(sm.getWhileBody(), sm));
                }
                sm.reverseCondition();
                list.add(toFormula(sm, postSm));
            } else if (sm.getType() == StatementType.Wait) {
                sm.reverseCondition();
                list.add(toFormula(sm, sm));
                sm.reverseCondition();
                list.add(toFormula(sm, postSm));
            } else {
                list.add(toFormula(sm, postSm));
            }
        }
        return list;
    }
}
