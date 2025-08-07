import type { Account } from "../models/accuont"
import { accuontServerApi } from "./axios"

export const getAccounts = async (): Promise<Account[]> => {
  const response = await accuontServerApi.get('/accounts')
  return response.data
}