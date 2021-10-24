package com.putterfly.simpk.imp;

import lombok.*;

import java.util.Objects;

@Data
public class Variable implements Comparable<Variable> {
    private final String name;
    private final VariableType type;
    private int value;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Variable variable = (Variable) o;
        return value == variable.value && name.equals(variable.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, value);
    }

    @Override
    public int compareTo(Variable o) {
        return name.compareTo(o.getName());
    }

    public String toString(boolean post){
        StringBuilder sb = new StringBuilder();
        if(post){
            sb.append(name).append("'=").append(value);
        }else sb.append(name).append('=').append(value);
        return sb.toString();
    }
}
