package uk.gov.ida.reflection;

class StateTransition {
  final String name;
  final String targetState;

  StateTransition(String name, String targetState) {
    this.name = name;
    this.targetState = targetState;
  }
}