package com.putterfly.simpk.ks;

import com.putterfly.simpk.imp.Variable;
import lombok.Data;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@Data
public class KripkeStructure {
    String firstLabel;
    String lastLabel;
    List<Variable> firstVars;
    List<Variable> lastVars;
//    String operator;

    @Override
    public String toString(){
        if (
//                operator.equals("")&&
                firstLabel.isEmpty()&&lastLabel.isEmpty())
            return "";
        List<String> unidVars = new LinkedList<>();
        for(Variable s : lastVars){
            int index = Collections.binarySearch(firstVars,s);
            if (index>=0) unidVars.add(s.getName());
        }
        StringBuilder sb = new StringBuilder();
        sb.append("pc=(");
        sb.append(firstLabel.equals("")?"U":firstLabel);
        for(Variable v : firstVars){
            sb.append(',').append(v.toString(false));
        }
        for(String s:unidVars){
            sb.append(',').append(s).append("=u");
        }
        sb.append(") -> (pc'=").append(lastLabel.equals("")?"U":lastLabel);

        for(Variable v:lastVars){
            sb.append(',').append(v.toString(true));
        }
        sb.append(')');
        return sb.toString();
    }
}
