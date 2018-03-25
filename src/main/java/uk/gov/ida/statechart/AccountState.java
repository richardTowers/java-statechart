package uk.gov.ida.statechart;

import java.math.BigDecimal;

import uk.gov.ida.statechart.annotations.State;
import uk.gov.ida.statechart.annotations.Transition;

public interface AccountState {
  // Transitions

  default AccountState deposit(BigDecimal amount) { throw new RuntimeException("Invalid event"); }
  default AccountState withdraw(BigDecimal amount) { throw new RuntimeException("Invalid event"); }
  default Closed close() { throw new RuntimeException("Invalid event"); }
  default Held placeHold() { throw new RuntimeException("Invalid event"); }
  default NotHeld removeHold() { throw new RuntimeException("Invalid event"); }
  default Open reopen() { throw new RuntimeException("Invalid event"); }

  // Methods

  default BigDecimal availableToWithdraw() { throw new RuntimeException("Invalid method"); }
  BigDecimal getBalance();
  String getName();

  // States

  @State(name = Open.NAME)
  abstract class Open implements AccountState {
    static final String NAME = "open";
    static NotHeld initial() { return new NotHeld(BigDecimal.ZERO); }

    final BigDecimal balance;
    Open(BigDecimal balance) { this.balance = balance; }

    @Override @Transition public Open deposit(BigDecimal amount) { return clone(balance.add(amount)); }
    @Override @Transition public Closed close() { return new Closed(balance); }

    @Override public BigDecimal getBalance() { return balance; }

    abstract Open clone(BigDecimal balance);
  }

  @State(name = Held.NAME)
  final class Held extends Open {
    static final String NAME = "held";
    Held(BigDecimal balance) { super(balance);}

    @Override @Transition public NotHeld removeHold() { return new NotHeld(balance); }
    @Override @Transition public BigDecimal availableToWithdraw() { return BigDecimal.ZERO; }

    @Override public String getName() { return NAME; }
    public Held clone(BigDecimal balance) { return new Held(balance); }
  }

  @State(name = NotHeld.NAME)
  final class NotHeld extends Open {
    static final String NAME = "notHeld";
    NotHeld(BigDecimal balance) { super(balance);}

    @Override @Transition public Open withdraw(BigDecimal amount) { return new NotHeld(balance.subtract(amount)); }
    @Override @Transition public Held placeHold() { return new Held(balance); }
    @Override @Transition public BigDecimal availableToWithdraw() { return balance.compareTo(BigDecimal.ZERO) > 0 ? balance : BigDecimal.ZERO; }

    @Override public String getName() { return NAME; }

    public NotHeld clone(BigDecimal balance) { return new NotHeld(balance); }
  }

  @State(name = Closed.NAME)
  final class Closed implements AccountState {
    static final String NAME = "closed";
    private final BigDecimal balance;
    Closed(BigDecimal balance) { this.balance = balance; }

    @Override @Transition public Open reopen() { return new NotHeld(balance); }

    @Override public BigDecimal getBalance() { return balance; }
    @Override public String getName() { return NAME; }
  }
}
