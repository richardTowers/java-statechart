package uk.gov.ida.reflection;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

import uk.gov.ida.statechart.annotations.State;
import uk.gov.ida.statechart.annotations.Transition;

/**
 * Gets the states and the transitions between them from a simple
 * container class layed out like AccountState.java.
 */
public class StatechartAnalyzer {

  /**
   * Takes an outer class, searches for all inner classes marked with State annotations,
   * and then establishes the transitions permitted by those States.
   * 
   * Returns a Map of state to next state to a set of all the named
   * transitions. This is useful for drawing diagrams.
   */
  public static <T> Map<String, Map<String, Set<String>>> getTransitions(Class<T> containerClass) {
    return Arrays.stream(containerClass.getDeclaredClasses())
      .filter(declaredClass -> declaredClass.isAnnotationPresent(State.class))
      .collect(toMap(
        state -> getStateName(state),
        state -> getNextStateTransitions(state)
      ));
  }

  /**
   * Takes a class with a State annotation and works out all the transitions that state can perform.
   * 
   * Returns a map of next state to set of transitions that reach that state.
   */
  private static Map<String, Set<String>> getNextStateTransitions(Class<?> currentStateClass) {
    return Arrays.stream(currentStateClass.getDeclaredMethods())
      .filter(method -> method.isAnnotationPresent(Transition.class))
      .map(transitionMethod -> new StringPair(transitionMethod.getName(), getNewStateName(currentStateClass, transitionMethod)))
      .collect(groupingBy(pair -> pair.right, mapping(pair -> pair.left, toSet())));
  }

  /**
   * Get the State returned by this transition method.
   * 
   * Transition methods can either return a State (a type annotated with
   * @State) or another type. If they return a State then the state machine
   * will transition to that State. Otherwise the state machine will remain in
   * the current State.
   */
  private static String getNewStateName(Class<?> currentStateClass, Method transitionMethod) {
    Class<?> returnType = transitionMethod.getReturnType();
    return returnType.isAnnotationPresent(State.class)
      ? getStateName(returnType)
      : getStateName(currentStateClass);
  }

  /**
   * Get the name of the State from it's State annotation.
   */
  private static <T> String getStateName(Class<T> stateClass) {
      return stateClass.getDeclaredAnnotation(State.class).name();
  }
}