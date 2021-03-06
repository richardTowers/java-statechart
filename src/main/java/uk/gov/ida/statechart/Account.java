package uk.gov.ida.statechart;

import java.math.BigDecimal;

final class Account {
  private AccountState state = AccountState.Open.initial();

  void deposit(BigDecimal amount) { state = state.deposit(amount); }
  void withdraw(BigDecimal amount) { state = state.withdraw(amount); }
  void placeHold() { state = state.placeHold(); }
  void removeHold() { state = state.removeHold(); }
  void close() { state = state.close(); }
  void reopen() { state = state.reopen(); }

  BigDecimal availableToWithdraw() { return state.availableToWithdraw(); }

  String getState() { return state.getName(); }
  BigDecimal getBalance() { return state.getBalance(); }
}