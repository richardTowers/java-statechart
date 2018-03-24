package uk.gov.ida.statechart;

import java.math.BigDecimal;

public final class Account {

  private abstract class AccountState {
    public final String name;
    public final BigDecimal balance;

    AccountState(String name, BigDecimal balance) { this.name = name; this.balance = balance; }

    // Transitions
    AccountState deposit(BigDecimal amount) { throw new RuntimeException("Invalid event"); }
    AccountState withdraw(BigDecimal amount) { throw new RuntimeException("Invalid event"); }
    ClosedAccountState close() { throw new RuntimeException("Invalid event"); }
    HeldAccountState placeHold() { throw new RuntimeException("Invalid event"); }
    OpenAccountState removeHold() { throw new RuntimeException("Invalid event"); }
    OpenAccountState reopen() { throw new RuntimeException("Invalid event"); }

    // Methods
    BigDecimal availableToWithdraw() { throw new RuntimeException("Invalid method"); }
  }

  private final class OpenAccountState extends AccountState {
    public static final String NAME = "open";

    OpenAccountState() { this(BigDecimal.ZERO); }
    OpenAccountState(BigDecimal balance) { super(NAME, balance); }

    @Override public OpenAccountState deposit(BigDecimal amount) { return new OpenAccountState(this.balance.add(amount)); }
    @Override public OpenAccountState withdraw(BigDecimal amount) { return new OpenAccountState(this.balance.subtract(amount)); }
    @Override public HeldAccountState placeHold() { return new HeldAccountState(this.balance); }
    @Override public ClosedAccountState close() { return new ClosedAccountState(this.balance); }
    @Override public BigDecimal availableToWithdraw() { return balance.compareTo(BigDecimal.ZERO) > 0 ? balance : BigDecimal.ZERO; }
  }

  private final class HeldAccountState extends AccountState {
    public static final String NAME = "held";
    HeldAccountState(BigDecimal balance) { super(NAME, balance); }

    @Override public OpenAccountState removeHold() { return new OpenAccountState(this.balance); }
    @Override public HeldAccountState deposit(BigDecimal amount) { return new HeldAccountState(this.balance.add(amount)); }
    @Override public ClosedAccountState close() { return new ClosedAccountState(this.balance); }
    @Override public BigDecimal availableToWithdraw() { return BigDecimal.ZERO; }
  }

  private final class ClosedAccountState extends AccountState {
    public static final String NAME = "closed";
    ClosedAccountState(BigDecimal balance) { super(NAME, balance); }

    @Override public OpenAccountState reopen() { return new OpenAccountState(this.balance); }
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
  public BigDecimal availableToWithdraw() { return this.state.availableToWithdraw(); }
}