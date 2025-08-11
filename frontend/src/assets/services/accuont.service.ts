import type { Account } from "../models/accuont"
import { accuontServerApi } from "./axios"

export const getAccounts = async (): Promise<Account[]> => {
  const response = await accuontServerApi.get('/accounts')
  return response.data
}

export const getAccountsByCustomerId = async (customerId: string): Promise<Account[]> => {
  const response = await accuontServerApi.get(`/accounts/customer/${customerId}`);  
  return response.data;
};

export const createAccount = async (accountData: Partial<Account>): Promise<Account> => {
  const { data } = await accuontServerApi.post('/accounts', accountData);
  return data;
};