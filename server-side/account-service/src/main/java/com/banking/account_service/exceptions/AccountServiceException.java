package com.banking.account_service.exceptions;

public class AccountServiceException extends RuntimeException {
  private final Number errorNumber;
  public AccountServiceException(Number errorNumber) {
    super();
    this.errorNumber = errorNumber;
  }
}
