import { useQuery } from '@tanstack/react-query'
import { getAccounts } from '../../pages/AccountDashboard'

export const useAccounts = () => {
  return useQuery({
    queryKey: ['accounts'],
    queryFn: getAccounts,
  })
}