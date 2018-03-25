package uk.gov.ida.reflection;

import org.junit.Test;
import uk.gov.ida.statechart.AccountState;

import java.util.Set;

import static org.junit.Assert.assertNotNull;

public class StatechartAnalyzerTests {

  @Test
  public void shouldGetHierarchy() {
    Set<StateHierarchy> result = StatechartAnalyzer.getStateHierarchy(
        AccountState.Open.class,
        AccountState.Closed.class,
        AccountState.Held.class,
        AccountState.NotHeld.class
    );
    assertNotNull(result);
  }
}