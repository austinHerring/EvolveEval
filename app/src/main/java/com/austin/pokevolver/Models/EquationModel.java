package com.austin.pokevolver.Models;

import java.util.ArrayList;

/**
 * Created by austin on 8/19/16.
 */
public class EquationModel {
    public static int evolutions, exp;
    public static ArrayList<VariableModel> variables;

    //floor(x-1)/11
    //floor(x-2)/10

    public EquationModel() {
        variables = new ArrayList<>();
        exp = 0;
        evolutions = 0;
    }

    public static void addVariable(VariableModel variable) {
        variables.add(0, variable);
    }

    public static int getExp() {
        return exp;
    }
}
