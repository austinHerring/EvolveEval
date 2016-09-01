package com.austin.evolveeval.Models;

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
    private static final int EXP_OLD = 500;
    private static final int EXP_NEW = 1000;

    public EquationModel() {
        variables = new ArrayList<>();
        exp = 0;
        evolutions = 0;
    }

    public static void evaluate() {
        int oldEvolutions = 0;
        int newEvolutions = 0;
        int adjustment = (transferOption) ? 2 : 1;

        for (VariableModel variable : variables) {
            int[] e = variable.calculateOldAndNewEvolutions(adjustment);
            oldEvolutions += e[0];
            newEvolutions += e[1];
        }

        evolutions = oldEvolutions + newEvolutions;
        exp = (oldEvolutions * EXP_OLD + newEvolutions * EXP_NEW) << 1;
    }

    public static void setTransferOption(boolean transferOption) {
        EquationModel.transferOption = transferOption;
    }

    public static void addVariable(VariableModel variable) {
        variables.add(0, variable);
    }
}
