package uk.gov.ida.statechart;

import java.math.BigDecimal;

public abstract class AccountState {
  public final String name;
  public final BigDecimal balance;

  AccountState(String name, BigDecimal balance) { this.name = name; this.balance = balance; }

  // Transitions

  AccountState deposit(BigDecimal amount) { throw new RuntimeException("Invalid event"); }
  AccountState withdraw(BigDecimal amount) { throw new RuntimeException("Invalid event"); }
  Closed close() { throw new RuntimeException("Invalid event"); }
  Held placeHold() { throw new RuntimeException("Invalid event"); }
  Open removeHold() { throw new RuntimeException("Invalid event"); }
  Open reopen() { throw new RuntimeException("Invalid event"); }

  // Methods

  BigDecimal availableToWithdraw() { throw new RuntimeException("Invalid method"); }

  // States

  public static final class Open extends AccountState {
    public static final String NAME = "open";

    Open() { this(BigDecimal.ZERO); }
    Open(BigDecimal balance) { super(NAME, balance); }

    @Override public Open deposit(BigDecimal amount) { return new Open(balance.add(amount)); }
    @Override public Open withdraw(BigDecimal amount) { return new Open(balance.subtract(amount)); }
    @Override public Held placeHold() { return new Held(balance); }
    @Override public Closed close() { return new Closed(balance); }
    @Override public BigDecimal availableToWithdraw() { return balance.compareTo(BigDecimal.ZERO) > 0 ? balance : BigDecimal.ZERO; }
  }

  public static final class Held extends AccountState {
    public static final String NAME = "held";
    Held(BigDecimal balance) { super(NAME, balance); }

    @Override public Open removeHold() { return new Open(balance); }
    @Override public Held deposit(BigDecimal amount) { return new Held(balance.add(amount)); }
    @Override public Closed close() { return new Closed(balance); }
    @Override public BigDecimal availableToWithdraw() { return BigDecimal.ZERO; }
  }

  public static final class Closed extends AccountState {
    public static final String NAME = "closed";
    Closed(BigDecimal balance) { super(NAME, balance); }

    @Override public Open reopen() { return new Open(balance); }
  }
}
