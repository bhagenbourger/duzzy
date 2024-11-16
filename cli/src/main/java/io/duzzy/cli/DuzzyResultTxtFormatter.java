package io.duzzy.cli;

import io.duzzy.core.DuzzyResult;

public class DuzzyResultTxtFormatter implements DuzzyResultFormatter {

    @Override
    public String format(DuzzyResult duzzyResult) {
        return "Duzzy generated " + duzzyResult.rows() +
                " rows in " + duzzyResult.duration()
                + " with seed " + duzzyResult.seed();
    }
}
