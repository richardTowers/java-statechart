package uk.gov.ida.statechart;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

public class StatechartTest {
    @Test
    public void shouldBeInInitialStateInitially() {
      Statechart statechart = new Statechart();
      assertTrue(statechart.isInInitialState());
    }
}
