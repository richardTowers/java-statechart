package uk.gov.ida;

import uk.gov.ida.statechart.Statechart;

public class Main {
    public static void main(String[] args) {
        Statechart statechart = new Statechart();
        System.out.println("Statechart is in initial state: " + statechart.isInInitialState());
    }
}
