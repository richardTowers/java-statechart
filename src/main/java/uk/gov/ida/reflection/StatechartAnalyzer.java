package uk.gov.ida.reflection;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;

import java.lang.reflect.Method;
import java.util.*;

import uk.gov.ida.helpers.Pair;
import uk.gov.ida.statechart.annotations.State;
import uk.gov.ida.statechart.annotations.Transition;

/**
 * Gets the states and the transitions between them from a simple
 * container class laid out like AccountState.java.
 */
public class StatechartAnalyzer {

  /**
   * @param classes an array of state classes to analyze.
   *
   * @return a Map of state to next state to a set of all the named
   * transitions. This is useful for drawing diagrams.
   */
  public static Map<String, Map<String, Set<String>>> getTransitions(Class<?>... classes) {
    return Arrays.stream(classes)
      .filter(declaredClass -> declaredClass.isAnnotationPresent(State.class))
      .collect(toMap(
        StatechartAnalyzer::getStateName,
        StatechartAnalyzer::getNextStateTransitions
      ));
  }

  /**
   * Takes a class with a State annotation and works out all the transitions that state can perform.
   * 
   * @return a map of next state to set of transitions that reach that state.
   */
  private static Map<String, Set<String>> getNextStateTransitions(Class<?> currentStateClass) {
    return Arrays.stream(currentStateClass.getDeclaredMethods())
      .filter(method -> method.isAnnotationPresent(Transition.class))
      .map(transitionMethod -> new Pair<>(transitionMethod.getName(), getNewStateName(currentStateClass, transitionMethod)))
      .collect(groupingBy(Pair::getRight, mapping(Pair::getLeft, toSet())));
  }

  /**
   * Get the State returned by this transition method.
   * 
   * Transition methods can either return a State (a type annotated with
   * \@State) or another type. If they return a State then the state machine
   * will transition to that State. Otherwise the state machine will remain in
   * the current State.
   *
   * @return the name of the state from it's State annotation.
   */
  private static String getNewStateName(Class<?> currentStateClass, Method transitionMethod) {
    Class<?> returnType = transitionMethod.getReturnType();
    return returnType.isAnnotationPresent(State.class)
      ? getStateName(returnType)
      : getStateName(currentStateClass);
  }

  /**
   * @return the name of the State from it's State annotation.
   */
  private static <T> String getStateName(Class<T> stateClass) {
      return stateClass.getDeclaredAnnotation(State.class).name();
  }

}