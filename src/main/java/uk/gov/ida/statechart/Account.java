package uk.gov.ida.statechart;

import java.math.BigDecimal;

import uk.gov.ida.statechart.annotations.State;

public final class Account {
  private AccountState state = new AccountState.Open();

  public void deposit(BigDecimal amount) { state = state.deposit(amount); }
  public void withdraw(BigDecimal amount) { state = state.withdraw(amount); }
  public void placeHold() { state = state.placeHold(); }
  public void removeHold() { state = state.removeHold(); }
  public void close() { state = state.close(); }
  public void reopen() { state = state.reopen(); }

  public BigDecimal availableToWithdraw() { return state.availableToWithdraw(); }

  public String getState() {
    // TODO this is not very nice:
    return state.getClass().getDeclaredAnnotation(State.class).name();
  }
  public BigDecimal getBalance() { return state.balance; }
}