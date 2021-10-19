package com.putterfly.simpk.imp;

import lombok.Data;

import java.util.ArrayList;

/**
 * @author YueChen
 * @version 1.0
 * @date 2021/10/19 20:11
 */

@Data
public class Statement {
    private StatementType type;
    private String condition;
    private String label;
    private String seqBody;

    private ArrayList<Statement> ifBody;
    private ArrayList<Statement> elseBody;
    private ArrayList<Statement> whileBody;
    private ArrayList<Variable> vars;

    boolean isNull() {
        return condition.isEmpty() && seqBody.isEmpty();
    }

    void reverseCondition() {
        if (condition.contains("not")) {
            condition = condition.replace("not", "");
            condition = condition.trim();
        } else {
            condition = "not " + condition;
        }
    }
}
