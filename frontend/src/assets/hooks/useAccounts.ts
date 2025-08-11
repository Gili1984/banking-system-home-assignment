import { useQuery } from '@tanstack/react-query'
import {  getAccountsByCustomerId } from '../services/accuont.service'


export const useAccounts = (customerId: string) => {
  return useQuery({
    queryKey: ['accounts', customerId],
    queryFn: () => getAccountsByCustomerId(customerId),
    enabled: !!customerId, 
  });
};