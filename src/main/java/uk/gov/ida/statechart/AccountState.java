package uk.gov.ida.statechart;

import java.math.BigDecimal;

import uk.gov.ida.statechart.annotations.State;
import uk.gov.ida.statechart.annotations.Transition;

public abstract class AccountState<T extends AccountState<T>> {
  final BigDecimal balance;

  AccountState(BigDecimal balance) { this.balance = balance; }

  // Transitions

  T deposit(BigDecimal amount) { throw new RuntimeException("Invalid event"); }
  T withdraw(BigDecimal amount) { throw new RuntimeException("Invalid event"); }
  Closed close() { throw new RuntimeException("Invalid event"); }
  Held placeHold() { throw new RuntimeException("Invalid event"); }
  Open removeHold() { throw new RuntimeException("Invalid event"); }
  Open reopen() { throw new RuntimeException("Invalid event"); }

  // Methods

  BigDecimal availableToWithdraw() { throw new RuntimeException("Invalid method"); }

  // States

  @State(name = "open")
  public static final class Open extends AccountState<Open> {
    Open() { this(BigDecimal.ZERO); }
    Open(BigDecimal balance) { super(balance); }

    @Override @Transition public Open deposit(BigDecimal amount) { return new Open(balance.add(amount)); }
    @Override @Transition public Open withdraw(BigDecimal amount) { return new Open(balance.subtract(amount)); }
    @Override @Transition public Held placeHold() { return new Held(balance); }
    @Override @Transition public Closed close() { return new Closed(balance); }
    @Override @Transition public BigDecimal availableToWithdraw() { return balance.compareTo(BigDecimal.ZERO) > 0 ? balance : BigDecimal.ZERO; }
  }

  @State(name = "held")
  public static final class Held extends AccountState<Held> {
    Held(BigDecimal balance) { super(balance); }

    @Override @Transition public Open removeHold() { return new Open(balance); }
    @Override @Transition public Held deposit(BigDecimal amount) { return new Held(balance.add(amount)); }
    @Override @Transition public Closed close() { return new Closed(balance); }
    @Override @Transition public BigDecimal availableToWithdraw() { return BigDecimal.ZERO; }
  }

  @State(name = "closed")
  public static final class Closed extends AccountState<Closed> {
    Closed(BigDecimal balance) { super(balance); }

    @Override @Transition public Open reopen() { return new Open(balance); }
  }
}
