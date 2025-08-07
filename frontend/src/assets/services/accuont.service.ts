import type { Account } from "../models/accuont"
import { accuontServerApi } from "./axios"

export const getAccounts = async (): Promise<Account[]> => {
  const response = await accuontServerApi.get('/accounts')
  return response.data
}

export const getAccountsByCustomerId = async (customerId: string): Promise<Account[]> => {
  const response = await accuontServerApi.get(`/accounts/customer/${customerId}`);
  console.log("response.data",response.data);
  
  return response.data;
};