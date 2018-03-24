package uk.gov.ida;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import uk.gov.ida.reflection.StatechartAnalyzer;
import uk.gov.ida.statechart.Account;
import uk.gov.ida.statechart.AccountState;

public class Main {
    public static void main(String[] args) {
        Map<String, Map<String, List<String>>> transitions = StatechartAnalyzer.getTransitions(AccountState.class);
        System.out.println("@startuml");
        System.out.println("skinparam monochrome true");
        System.out.println("skinparam Shadowing false");

        String initialState = new Account().getState();
        System.out.println("[*] --> " + initialState);
        for (Entry<String, Map<String, List<String>>> currentStateEntry : transitions.entrySet()) {
            String currentStateName = currentStateEntry.getKey();
            for (Entry<String, List<String>> nextStateEntry : currentStateEntry.getValue().entrySet()) {
                String nextStateName = nextStateEntry.getKey();
                System.out.println(currentStateName + " --> " + nextStateName + ": " + String.join(", ", nextStateEntry.getValue()));
            }
        }
        System.out.println("@enduml");
    }
}
