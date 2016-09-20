package com.gustavofao.textprocessor;

import java.util.List;

/**
 * Created by faogustavo on 20/09/2016.
 */
public class Rule implements IRule{

    private Rule next;

    private static final String CORINGA = "*";

    private boolean matchAll = false;

    private String equalsCondition = null;
    private String startCondition = null;
    private String endCondition = null;

    public Rule(){}

    public Rule(List<String> words) {
        this(words.get(0));
        words.remove(0);
        if (!words.isEmpty()) {
            this.next = new Rule(words);
        }
    }

    private Rule(String condition) {
        if (condition == null || condition.isEmpty())
            throw new IllegalArgumentException("All conditions must match the specified rule.");

        if (condition.contains(CORINGA) && !condition.contains(CORINGA + CORINGA)) {
            if (CORINGA.equals(condition)) {
                this.matchAll = true;
            } else {
                if (condition.startsWith(CORINGA)) {
                    endCondition = condition.substring(1).toLowerCase();
                }

                if (condition.endsWith(CORINGA)) {
                    startCondition = condition.substring(0, condition.length() - 1).toLowerCase();
                }

                if (!condition.startsWith(CORINGA) && !condition.endsWith(CORINGA)) {
                    String[] conditions = condition.split(CORINGA);
                    if (conditions.length > 2)
                        throw new IllegalArgumentException(
                                String.format("You cant have more than one coringa on your statement. (%s)", condition)
                        );

                    startCondition = conditions[0];
                    endCondition = conditions[1];
                }
            }
        } else {
            this.equalsCondition = condition;
        }
    }

    @Override
    public boolean validate(String word) {
        if (word == null ||word.isEmpty())
            return false;

        boolean calculatedValue = matchAll;

        if (startCondition != null && endCondition != null)
            calculatedValue = checkStart(word) && checkEnd(word);
        else if (startCondition != null)
            calculatedValue = checkStart(word);
        else if (endCondition != null)
            calculatedValue = checkEnd(word);
        else if (equalsCondition != null)
            calculatedValue = equalsCondition.equals(word);

        if (!calculatedValue && next != null)
            return next.validate(word);
        else
            return calculatedValue;
    }

    private boolean checkStart(String word) {
        return word.startsWith(startCondition);
    }

    private boolean checkEnd(String word) {
        return word.endsWith(endCondition);
    }

}
