import { useQuery } from '@tanstack/react-query'
import { getuseCustomers } from '../services/customer.service'

export const useCustomer = () => {
  return useQuery({
    queryKey: ['customers'],
    queryFn: getuseCustomers,
  })
}
