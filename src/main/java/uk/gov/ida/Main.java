package uk.gov.ida;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import uk.gov.ida.helpers.Triple;
import uk.gov.ida.reflection.StatechartAnalyzer;
import uk.gov.ida.statechart.Account;
import uk.gov.ida.statechart.AccountState;

public class Main {
  public static void main(String[] args) {
    Map<String, Map<String, Set<String>>> transitions = StatechartAnalyzer.getTransitions(AccountState.class);
    System.out.println("@startuml");
    System.out.println("skinparam monochrome true");
    System.out.println("skinparam Shadowing false");

    String initialState = new Account().getState();
    System.out.println("[*] --> " + initialState);

    String plantUml = transitions.entrySet().stream()
      .flatMap(Main::flattenTransitions)
      .map(x -> x.getLeft() + " --> " + x.getMiddle() + ": " + String.join(", ", x.getRight()))
      .collect(Collectors.joining("\n"));
    System.out.println(plantUml);

    System.out.println("@enduml");
  }

  private static Stream<Triple<String, String, Set<String>>> flattenTransitions(Entry<String, Map<String, Set<String>>> startState) {
    return startState.getValue().entrySet().stream()
        .map(transitions -> new Triple<>(startState.getKey(), transitions.getKey(), transitions.getValue()));
  }
}
