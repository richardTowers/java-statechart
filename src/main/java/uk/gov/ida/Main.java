package uk.gov.ida;

import uk.gov.ida.reflection.PlantUmlPrinter;
import uk.gov.ida.reflection.StateHierarchy;
import uk.gov.ida.reflection.StatechartAnalyzer;
import uk.gov.ida.statechart.AccountState;

import java.util.Set;

public class Main {
  public static void main(String[] args) {
    Set<StateHierarchy> stateHierarchy = StatechartAnalyzer.getStateHierarchy(
        AccountState.Open.class,
        AccountState.Held.class,
        AccountState.NotHeld.class,
        AccountState.Closed.class
    );
    System.out.println(PlantUmlPrinter.printPlantUml(stateHierarchy));
  }
}
