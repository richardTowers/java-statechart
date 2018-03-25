package uk.gov.ida.reflection;

import java.util.List;

class PlantUmlPrinter {
  static String printPlantUml(List<StateHierarchy> stateHierarchy) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("@startuml\n")
        .append("skinparam monochrome true\n")
        .append("skinparam Shadowing false\n");

    printPlantUml(stateHierarchy, stringBuilder, "");
    stringBuilder.append("@enduml\n");
    return stringBuilder.toString();
  }

  private static void printPlantUml(List<StateHierarchy> stateHierarchies, StringBuilder stringBuilder, String indent) {
    if (stateHierarchies.size() == 0) { return; }

    boolean first = true;
    for (StateHierarchy currentState: stateHierarchies) {
      if (first) {
        first = false;
        stringBuilder.append(indent).append("[*] --> ").append(currentState.name).append("\n");
      }
      if (currentState.children.size() > 0) {
        stringBuilder.append(indent).append("state ").append(currentState.name).append(" {\n");
        printPlantUml(currentState.children, stringBuilder, indent + "  ");
        stringBuilder.append(indent).append("}\n");
      }
      currentState.transitions.forEach(x -> stringBuilder
          .append(indent).append(currentState.name).append(" --> ").append(x.targetState).append(": ").append(x.name).append("\n"));
    }
  }

}