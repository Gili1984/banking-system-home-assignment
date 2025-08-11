import { useQuery } from '@tanstack/react-query'
import { getCustomerById } from '../services/customer.service'

export const useCustomerById = (customerId: string, p0: { enabled: boolean; }) => {
  return useQuery({
    queryKey: ['customers', customerId],
    queryFn: () => getCustomerById(customerId),
    enabled: !!customerId, 
  });
};