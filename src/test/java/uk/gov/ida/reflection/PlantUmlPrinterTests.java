package uk.gov.ida.reflection;

import com.google.common.collect.ImmutableList;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class PlantUmlPrinterTests {

  private List<StateHierarchy> stateHierarchies = ImmutableList.of(
      new StateHierarchy(
          "open",
          ImmutableList.of(
              new StateHierarchy(
                  "held",
                  Collections.emptyList(),
                  ImmutableList.of(
                      new StateTransition("removeHold", "notHeld"),
                      new StateTransition("availableToWithdraw", "held")
                  )
              ),
              new StateHierarchy(
                  "notHeld",
                  Collections.emptyList(),
                  ImmutableList.of(
                      new StateTransition("placeHold", "held"),
                      new StateTransition("withdraw", "notHeld"),
                      new StateTransition("availableToWithdraw", "notHeld")
                  )
              )
          ),
          ImmutableList.of(
              new StateTransition("deposit", "open"),
              new StateTransition("close", "closed")
          )
      ),
      new StateHierarchy(
          "closed",
          Collections.emptyList(),
          ImmutableList.of(
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