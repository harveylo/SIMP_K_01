package com.putterfly.simpk.ks;

import com.putterfly.simpk.imp.FirstOrderLogic;
import com.putterfly.simpk.imp.Variable;
import com.putterfly.simpk.imp.VariableType;
import javafx.util.Pair;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class KripkeTransfer {


    public static String beginTransfer(List<List<FirstOrderLogic>> lgss){
        List<String> pcs = new LinkedList<>();
        List<Pair<String,String>> relations = new LinkedList<>();
        List<String> labels = new LinkedList<>();
        List<FirstOrderLogic> lastLogics = new LinkedList<>();
        List<String> states = new LinkedList<>();
        List<KripkeStructure> nodes = new LinkedList<>();
        List<Variable> variables = new LinkedList<>();
        for (List<FirstOrderLogic> lg : lgss) {
            if(lgss.size()>1) pcs.add("U");
            else pcs.add("");
            lastLogics.add(new FirstOrderLogic());
        }
        createKLabels(lgss,pcs,relations,labels,lastLogics,variables,states,nodes
//                ,0
        );
        StringBuilder sb = new StringBuilder();
        sb.append("\nStates are:\n");
        int i = 0;
        for(String l : labels){
            sb.append('S').append(i++).append(":(").append(l).append(')').append('\n');
        }
        sb.append('\n').append("Relations are:\n");
        i=0;
        for(KripkeStructure ks : nodes){
            if(!ks.toString().isEmpty()){
                sb.append('R').append(i++).append(":=").append(ks.toString()).append('\n');
            }
        }
        return sb.toString();

    }

    public static void createKLabels(List<List<FirstOrderLogic>> lgss,
                                     List<String> pcs,
                                     List<Pair<String,String>> relations,
                                     List<String> labels,
                                     List<FirstOrderLogic> lastLogics,
                                     List<Variable> variables,
                                     List<String> states,
                                     List<KripkeStructure> nodes
//                                     ,int deep
    ){
//        deep++;
        List<String> tmp = new LinkedList<>(pcs);
        List<FirstOrderLogic> lastLogicsTmp =  new LinkedList<>(lastLogics);
        for(int i = 0;i<pcs.size();i++){
            KripkeStructure kripkeStructure = new KripkeStructure();
            StringBuilder oldLabel = new StringBuilder();
            int count = 0;
            for (String npc : tmp) {
                if(count++>0) oldLabel.append(' ');
                oldLabel.append(npc);
            }
            oldLabel = new StringBuilder(oldLabel.toString().trim());
            kripkeStructure.setFirstLabel(oldLabel.toString());
            FirstOrderLogic lastLogicsTmpI = lastLogicsTmp.get(i);
            FirstOrderLogic lastLg = new FirstOrderLogic(lastLogicsTmpI);
            lastLogicsTmpI.setVars(new LinkedList<>(variables));
            String lastArgs = lastLogicsTmpI.valueToString();
            kripkeStructure.setFirstVars(lastLogicsTmpI.getVars());
            if (oldLabel.length()!=0&&lastArgs.length()!=0){
                oldLabel.append(',').append(lastArgs);
            }
            lastLogicsTmpI.changeAll(next(lgss.get(i),lastLogicsTmpI));
            tmp.add(i,lastLogicsTmpI.getPostLabel());
            tmp.remove(i+1);
            List<Variable> nVars = lastLogicsTmpI.getVars();
            StringBuilder newLabel = new StringBuilder();
            count = 0;
            for(String s : tmp){
                if(count++>0) newLabel.append(' ');
                newLabel.append(s);
            }
            kripkeStructure.setLastLabel(newLabel.toString());
            kripkeStructure.setLastVars(lastLogicsTmpI.getVars());
            kripkeStructure.setOperator(lastLogicsTmpI.getOpr());
            nodes.add(kripkeStructure);

            String oneState = lastLogicsTmpI.valueToString();
            if (!oneState.isEmpty()&&!states.contains(oneState)){
                states.add(oneState);
            }
            if(newLabel.length()!=0&&!oneState.isEmpty()){
                newLabel.append(',').append(oneState);
            }
            Pair<String,String> r = new Pair<>(oldLabel.toString(),newLabel.toString());
            if(relations.contains(r)){
                tmp.add(i,pcs.get(i));
                tmp.remove(i+1);
                lastLogicsTmp.add(i,lastLg);
                lastLogicsTmp.remove(i+1);
//                lastLogicsTmpI = lastLg;
                continue;
            }
            if (oldLabel.length()!=0&&newLabel.length()!=0){
                relations.add(new Pair<String,String>(oldLabel.toString(),newLabel.toString()));
            }
            if (!labels.contains(oldLabel.toString())&&oldLabel.length()!=0){
                labels.add(oldLabel.toString());
            }
            if(!labels.contains(newLabel.toString())&&newLabel.length()!=0){
                labels.add(newLabel.toString());
            }
            createKLabels(lgss,tmp,relations,labels,lastLogicsTmp,nVars,states,nodes
//                    ,deep+1
            );
            tmp.add(i,pcs.get(i));
            tmp.remove(i+1);
            lastLogicsTmp.add(i,lastLg);
            lastLogicsTmp.remove(i+1);
        }
    }
    private static void changeValues(List<Variable> vars,Character va,int value){
        String var=""+va;
        boolean flag = false;
        for(Variable v : vars){
            if (v.getName().equals(var)){
                v.setValue(value);
                flag=true;
                break;
            }
        }
        if(!flag)
            vars.add(new Variable(var,VariableType.Int,value));
    }
    private static void nextVars(List<Variable> src,FirstOrderLogic dst){
        dst.getVars().clear();
        dst.setVars(src);
        Pair<Character,Integer> pair = dst.assign();
        if(pair.getKey()!=null){
            changeValues(dst.getVars(),pair.getKey(),pair.getValue());
        }

    }

    private static FirstOrderLogic next(List<FirstOrderLogic> lg,FirstOrderLogic current){
        FirstOrderLogic nlg = new FirstOrderLogic();
        if(current.isNull()){
            nlg.setPostLabel(lg.get(0).getPreLabel());
            nextVars(current.getVars(),nlg);
            return nlg;
        }
        for(FirstOrderLogic f : lg){
            if(f.getPreLabel().equals(current.getPostLabel())){
                nextVars(current.getVars(),f);
                if (f.isConditionOk())
                    return f;
            }
        }
        return nlg;
    }
}
