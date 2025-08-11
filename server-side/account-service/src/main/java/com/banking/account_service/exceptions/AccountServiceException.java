package com.banking.account_service.exceptions;

import lombok.Getter;

@Getter
public class AccountServiceException extends RuntimeException {
  private final Number errorCode;

  public AccountServiceException(Number errorCode, String message) {
    super(message);
    this.errorCode = errorCode;
  }

}
