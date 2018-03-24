package uk.gov.ida.statechart;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class AccountTests {

  @Test
  public void shouldBeOpenInitially() {
    Account account = new Account();
    assertEquals("open", account.getState());
  }

  @Test
  public void shouldTransitionToClosed() {
    Account account = new Account();
    account.close();
    assertEquals("closed", account.getState());
  }
}