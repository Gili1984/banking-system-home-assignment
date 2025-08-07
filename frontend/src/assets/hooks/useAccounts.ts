import { useQuery } from '@tanstack/react-query'
import { getAccounts } from '../services/accuont.service'

export const useAccounts = () => {
  return useQuery({
    queryKey: ['accounts'],
    queryFn: getAccounts,
  })
}

