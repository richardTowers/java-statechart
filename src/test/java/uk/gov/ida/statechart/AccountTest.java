package uk.gov.ida.statechart;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.junit.Test;

public class AccountTest {

  @Test
  public void shouldBeOpenInitially() {
    Account account = new Account();
    assertEquals("notHeld", account.getState());
  }

  @Test
  public void shouldAcceptDepositsWhenOpen() {
    Account account = new Account();
    assertEquals("notHeld", account.getState());

    BigDecimal amount = new BigDecimal("12.99");
	  account.deposit(amount);
    assertEquals(amount, account.getBalance());
  }

  @Test
  public void shouldAcceptWithdrawalsWhenOpen() {
    Account account = new Account();
    assertEquals("notHeld", account.getState());

    BigDecimal amount = new BigDecimal("12.99");
	  account.withdraw(amount);
    assertEquals(amount, account.getBalance().negate());
  }

  @Test
  public void shouldTransitionFromOpenToClosed() {
    Account account = new Account();
    assertEquals("notHeld", account.getState());
    account.close();
    assertEquals("closed", account.getState());
  }

  @Test
  public void shouldTransitionFromOpenToHeld() {
    Account account = new Account();
    account.placeHold();
    assertEquals("held", account.getState());
  }

  @Test
  public void shouldAcceptDepositsWhenHeld() {
    Account account = new Account();
    account.placeHold();
    assertEquals("held", account.getState());

    BigDecimal amount = new BigDecimal("12.99");
	  account.deposit(amount);
    assertEquals(amount, account.getBalance());
  }

  @Test(expected = RuntimeException.class)
  public void shouldNotAcceptWithdrawalsWhenHeld() {
    Account account = new Account();
    account.placeHold();
    assertEquals("held", account.getState());

    BigDecimal amount = new BigDecimal("12.99");
	  account.withdraw(amount);
  }

  @Test
  public void shouldTransitionFromHeldToOpen() {
    Account account = new Account();
    account.placeHold();
    assertEquals("held", account.getState());
    account.removeHold();
    assertEquals("notHeld", account.getState());
  }

  @Test
  public void shouldGetTheBalanceAvailableToWithdraw() {
    Account account = new Account();

    BigDecimal amount = new BigDecimal("12.99");
	  account.deposit(amount);
    assertEquals(amount, account.availableToWithdraw());

    account.placeHold();
    assertEquals(BigDecimal.ZERO, account.availableToWithdraw());

    account.removeHold();
    account.withdraw(new BigDecimal("1000"));
    assertEquals(BigDecimal.ZERO, account.availableToWithdraw());
  }

}