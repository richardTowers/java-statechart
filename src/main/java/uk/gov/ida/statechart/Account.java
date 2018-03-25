package uk.gov.ida.statechart;

import java.math.BigDecimal;

import uk.gov.ida.statechart.annotations.State;

public final class Account {
  private AccountState state = new AccountState.Open();

  void deposit(BigDecimal amount) { state = state.deposit(amount); }
  void withdraw(BigDecimal amount) { state = state.withdraw(amount); }
  void placeHold() { state = state.placeHold(); }
  void removeHold() { state = state.removeHold(); }
  void close() { state = state.close(); }
  void reopen() { state = state.reopen(); }

  BigDecimal availableToWithdraw() { return state.availableToWithdraw(); }

  public String getState() {
    // TODO this is not very nice:
    return state.getClass().getDeclaredAnnotation(State.class).name();
  }
  BigDecimal getBalance() { return state.balance; }
}