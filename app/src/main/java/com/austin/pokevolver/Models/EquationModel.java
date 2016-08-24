package com.austin.pokevolver.Models;

import java.util.ArrayList;

/**
 * @author Austin Herring
 * @version 1.0
 *
 * A model that holds a list of variables. It then runs the formula on this list to determine the
 * final deliverables
 */
public class EquationModel {
    public static int evolutions, exp;
    public static ArrayList<VariableModel> variables;
    private static boolean transferOption;

    //floor(x-1)/11
    //floor(x-2)/10

    public EquationModel() {
        variables = new ArrayList<>();
        exp = 0;
        evolutions = 0;
    }

    public static void evaluate() {
        if (transferOption) {

        } else {

        }
    }

    public static void setTransferOption(boolean transferOption) {
        EquationModel.transferOption = transferOption;
    }

    public static void addVariable(VariableModel variable) {
        variables.add(0, variable);
    }
}
