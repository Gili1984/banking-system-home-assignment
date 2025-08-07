import { useQuery } from '@tanstack/react-query'
import {  getAccountsByCustomerId } from '../services/accuont.service'


// export const useAccounts = (customerId?: string) => {
//   return useQuery({
//     queryKey: customerId ? ['accounts', customerId] : ['accounts'],
//     queryFn: () =>
//       customerId ? getAccountsByCustomerId(customerId) : getAccounts(),
//     enabled: customerId !== undefined || customerId === undefined,
//   });
// };

export const useAccounts = (customerId: string) => {
  return useQuery({
    queryKey: ['accounts', customerId],
    queryFn: () => getAccountsByCustomerId(customerId),
    enabled: !!customerId, 
  });
};