package com.suyog.Wallet;

public enum TransectionType {
    TRANSFER,    // PersonalUser to PersonalUser
    PAYMENT,     // PersonalUser to BusinessUser
    WITHDRAWAL,  // Withdraw from wallet
    DEPOSIT      // Deposit into wallet
}
