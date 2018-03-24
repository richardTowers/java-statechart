package uk.gov.ida.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;

/**
 * Gets the states and the transitions between them from a simple
 * container class layed out like Account.java.
 * 
 * TODO: This would probably be a lot less hacky / more idiomatic if we used annotations instead of conventions.
 */
public class StatechartAnalyzer {
  public static <T> Map<String, Map<String, List<String>>> getTransitions(Class<T> containerClass) {
    Set<Class<?>> declaredClasses = new HashSet<>(Arrays.asList(containerClass.getDeclaredClasses()));

    return declaredClasses.stream()
      .collect(toMap(k -> getStateClassName(k), v -> getNextStateTransitions(v, declaredClasses)));
  }

  private static Map<String, List<String>> getNextStateTransitions(Class<?> currentStateClass, Set<Class<?>> declaredClasses) {
    return Arrays.stream(currentStateClass.getDeclaredMethods())
      .map(x -> new StringPair(x.getName(), getNewStateName(currentStateClass, x.getReturnType(), declaredClasses)))
      .filter(x -> x.right != null)
      .collect(groupingBy(x -> x.right, mapping(x -> x.left, toList())));
  }

  private static String getNewStateName(Class<?> currentStateClass, Class<?> returnType, Set<Class<?>> declaredClasses) {
    // If the class is abstract then we can't tell what the transition is, so skip it.
    if (Modifier.isAbstract(returnType.getModifiers())) {
      return null;
    }
    // If the return type was declared in the container class then it's a state we're transitioning to
    else if (declaredClasses.contains(returnType)) {
      return getStateClassName(returnType);
    }
    // If the return type is anything else then assume we're staying in the current state
    else {
      return getStateClassName(currentStateClass);
    }
  }

  private static <T> String getStateClassName(Class<T> stateClass) {
    try {
      Field field = stateClass.getField("NAME");
      field.setAccessible(true);
      return (String) field.get(null);
    } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
      throw new RuntimeException("Couldn't get NAME field for class " + stateClass.getCanonicalName(), e);
    }
  }
}