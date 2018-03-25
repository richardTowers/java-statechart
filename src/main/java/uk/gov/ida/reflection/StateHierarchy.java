package uk.gov.ida.reflection;

import java.util.List;

class StateHierarchy {
  final String name;
  final List<StateHierarchy> children;
  final List<StateTransition> transitions;

  StateHierarchy(String name, List<StateHierarchy> children, List<StateTransition> transitions) {
    this.name = name;
    this.children = children;
    this.transitions = transitions;
  }
}