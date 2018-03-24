package uk.gov.ida.statechart;

import java.math.BigDecimal;

public final class Account {

  private abstract class AccountState {
    public final String name;
    public final BigDecimal balance;

    AccountState(String name, BigDecimal balance) { this.name = name; this.balance = balance; }

    AccountState deposit(BigDecimal amount) { throw new RuntimeException("Invalid event"); }
    AccountState withdraw(BigDecimal amount) { throw new RuntimeException("Invalid event"); }
    ClosedAccountState close() { throw new RuntimeException("Invalid event"); }
    HeldAccountState placeHold() { throw new RuntimeException("Invalid event"); }
    OpenAccountState removeHold() { throw new RuntimeException("Invalid event"); }
    OpenAccountState reopen() { throw new RuntimeException("Invalid event"); }
  }

  private final class OpenAccountState extends AccountState {
    OpenAccountState() { this(BigDecimal.ZERO); }
    OpenAccountState(BigDecimal balance) { super("open", balance); }

    @Override
    public OpenAccountState deposit(BigDecimal amount) { return new OpenAccountState(this.balance.add(amount)); }
    @Override
    public OpenAccountState withdraw(BigDecimal amount) { return new OpenAccountState(this.balance.subtract(amount)); }
    @Override
    public HeldAccountState placeHold() { return new HeldAccountState(this.balance); }
    @Override
    public ClosedAccountState close() { return new ClosedAccountState(this.balance); }
  }

  private final class HeldAccountState extends AccountState {
    HeldAccountState(BigDecimal balance) { super("held", balance); }

    @Override
    public OpenAccountState removeHold() { return new OpenAccountState(this.balance); }
    @Override
    public OpenAccountState deposit(BigDecimal amount) { return new OpenAccountState(this.balance.add(amount)); }
    @Override
    public ClosedAccountState close() { return new ClosedAccountState(this.balance); }
  }

  private final class ClosedAccountState extends AccountState {
    ClosedAccountState(BigDecimal balance) { super("closed", balance); }
    @Override
    public OpenAccountState reopen() { return new OpenAccountState(this.balance); }
  }

  private AccountState state = new OpenAccountState();

  public void deposit(BigDecimal amount) { state = state.deposit(amount); }
  public void withdraw(BigDecimal amount) { state = state.withdraw(amount); }
  public void placeHold() { state = state.placeHold(); }
  public void removeHold() { state = state.removeHold(); }
  public void close() { state = state.close(); }
  public void reopen() { state = state.reopen(); }

  public String getState() { return this.state.name; }
  public BigDecimal getBalance() { return this.state.balance; }
}