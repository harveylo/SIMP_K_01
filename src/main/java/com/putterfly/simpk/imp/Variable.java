package com.putterfly.simpk.imp;

import lombok.Data;
import java.util.Objects;

@Data
public class Variable {
    private final String name;
    private final VariableType type;
    private int value;

    public Variable(String name, VariableType type, int value) {
        this.name = name;
        this.type = type;
        this.value = value;
    }

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
}
