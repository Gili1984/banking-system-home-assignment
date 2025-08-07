import type { Account } from "../assets/models/accuont"
import { accuontServerApi } from "../components/services/axios"

export const getAccounts = async (): Promise<Account[]> => {
  const response = await accuontServerApi.get('/accounts')
  return response.data
}