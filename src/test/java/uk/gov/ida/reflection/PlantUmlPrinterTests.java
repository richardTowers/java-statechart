package uk.gov.ida.reflection;

import com.google.common.collect.ImmutableSet;
import org.junit.Test;

import java.util.Set;

import static java.util.Collections.emptySet;
import static org.junit.Assert.assertEquals;

public class PlantUmlPrinterTests {

  private Set<StateHierarchy> stateHierarchies = ImmutableSet.of(
      new StateHierarchy(
          "open",
          true,
          ImmutableSet.of(
              new StateHierarchy(
                  "held",
                  false,
                  emptySet(),
                  ImmutableSet.of(
                      new StateTransition("removeHold", "notHeld"),
                      new StateTransition("availableToWithdraw", "held")
                  )
              ),
              new StateHierarchy(
                  "notHeld",
                  true,
                  emptySet(),
                  ImmutableSet.of(
                      new StateTransition("placeHold", "held"),
                      new StateTransition("withdraw", "notHeld"),
                      new StateTransition("availableToWithdraw", "notHeld")
                  )
              )
          ),
          ImmutableSet.of(
              new StateTransition("deposit", "open"),
              new StateTransition("close", "closed")
          )
      ),
      new StateHierarchy(
          "closed",
          false,
          emptySet(),
          ImmutableSet.of(
              new StateTransition("reopen", "open")
          )
      )
  );

  @Test
  public void shouldPrintPlantUml() {
    String result = PlantUmlPrinter.printPlantUml(stateHierarchies);

    assertEquals("@startuml\n" +
        "skinparam monochrome true\n" +
        "skinparam Shadowing false\n" +
        "[*] --> open\n" +
        "state open {\n" +
        "  [*] --> held\n" +
        "  held --> notHeld: removeHold\n" +
        "  held --> held: availableToWithdraw\n" +
        "  notHeld --> held: placeHold\n" +
        "  notHeld --> notHeld: withdraw\n" +
        "  notHeld --> notHeld: availableToWithdraw\n" +
        "}\n" +
        "open --> open: deposit\n" +
        "open --> closed: close\n" +
        "closed --> open: reopen\n" +
        "@enduml\n", result);
  }
}