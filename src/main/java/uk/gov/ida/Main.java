package uk.gov.ida;

import uk.gov.ida.statechart.Account;

public class Main {
    public static void main(String[] args) {
        Account account = new Account();
        System.out.println("Account is in initial state: " + account.getState());
        account.close();
        System.out.println("Account is in state: " + account.getState());
    }
}
