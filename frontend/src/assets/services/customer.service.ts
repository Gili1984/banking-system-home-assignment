import type { Customer } from "../models/customer"
import { accuontServerApi } from "./axios"

export const getuseCustomers = async (): Promise<Customer[]> => {
  const response = await accuontServerApi.get('/customers')
  return response.data
}