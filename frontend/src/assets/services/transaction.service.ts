import { transactionServerApi } from "./axios";

export interface WithdrawAndDepositDto {
  accountNumber: string;
  toAccountNumber?: string;
  amount: number;
  currency: string;
  description?: string;
}


export interface TransactionDto {
  fromAccountNumber: string;
  toAccountNumber?: string;
  amount: number;
  currency: string;
  description?: string;
}

export const getHistory = async (accountNumber:String): Promise<any> => {
  try {
    const response = await transactionServerApi.get(`account/${accountNumber}`);
    return response.data;
  } catch (error: any) {
    if (error.response && error.response.data) {
      const accountErrorRaw = error.response.data.accountServiceError;
      if (accountErrorRaw) {
        try {
          const parsedError = JSON.parse(accountErrorRaw);
          throw new Error(parsedError.message || accountErrorRaw);
        } catch {
          throw new Error(accountErrorRaw);
        }
      }
      throw new Error(error.response.data.message || JSON.stringify(error.response.data));
    }
    throw new Error(error.message || 'Unknown error');
  }
};


export const performTransaction = async (
  data: TransactionDto | WithdrawAndDepositDto,
  type: 'deposit' | 'withdraw'|'transfer'
): Promise<any> => {
  try {
    const response = await transactionServerApi.post(`/${type}`, data);
    return response.data;
  } catch (error: any) {
    if (error.response && error.response.data) {
      const accountErrorRaw = error.response.data.accountServiceError;
      if (accountErrorRaw) {
        try {
          const parsedError = JSON.parse(accountErrorRaw);
          throw new Error(parsedError.message || accountErrorRaw);
        } catch {
          throw new Error(accountErrorRaw);
        }
      }
      throw new Error(error.response.data.message || JSON.stringify(error.response.data));
    }
    throw new Error(error.message || 'Unknown error');
  }
};
