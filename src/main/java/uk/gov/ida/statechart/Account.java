package uk.gov.ida.statechart;

import java.math.BigDecimal;

public class Account {
  private String state = "open";
  private BigDecimal balance = new BigDecimal("0");

  public void deposit(BigDecimal amount) {
    if (this.state == "open" || this.state == "held") {
      this.balance = this.balance.add(amount);
    }
    else {
      throw new RuntimeException("Invalid event");
    }
  }

  public void withdraw(BigDecimal amount) {
    if (this.state == "open") {
      this.balance = this.balance.subtract(amount);
    }
    else {
      throw new RuntimeException("Invalid event");
    }
  }

  public void placeHold() {
    if (this.state == "open") {
      this.state = "held";
    }
    else {
      throw new RuntimeException("Invalid event");
    }
  }

  public void removeHold() {
    if (this.state == "held") {
      this.state = "open";
    }
    else {
      throw new RuntimeException("Invalid event");
    }
  }

  public void close() {
    if (this.state == "open") {
      if (this.balance.compareTo(BigDecimal.ZERO) > 0) {
        // ...transfer balance into suspension account
      }
      this.state = "closed";
    }
    else {
      throw new RuntimeException("Invalid event");
    }
  }

  public void reopen() {
    if (this.state == "closed") {
      // ...restore balance if applicable
      this.state = "open";
    }
    else {
      throw new RuntimeException("Invalid event");
    }
  }

  public String getState() {
    return this.state;
  }

  public BigDecimal getBalance() {
    return this.balance;
  }
  
}