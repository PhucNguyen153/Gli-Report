package com.gli.report.enumeration;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum RuleMassEnum {
    CHIEN(new ArrayList<>(Arrays.asList(DayOfWeek.MONDAY, DayOfWeek.THURSDAY, DayOfWeek.SUNDAY))),
    AU(new ArrayList<>(Arrays.asList(DayOfWeek.TUESDAY, DayOfWeek.THURSDAY, DayOfWeek.SUNDAY))),
    THIEU(new ArrayList<>(Arrays.asList(DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.SUNDAY))),
    NGHIA(new ArrayList<>(Arrays.asList(DayOfWeek.FRIDAY, DayOfWeek.THURSDAY, DayOfWeek.SUNDAY))),
    HIEP(new ArrayList<>(Arrays.asList(DayOfWeek.FRIDAY, DayOfWeek.THURSDAY, DayOfWeek.SUNDAY)));

    private final List<DayOfWeek> ruleDay;

    RuleMassEnum(List<DayOfWeek> ruleDay) {
        this.ruleDay = ruleDay;
    }

    public List<DayOfWeek> getValue() {
        return this.ruleDay;
    }
}
