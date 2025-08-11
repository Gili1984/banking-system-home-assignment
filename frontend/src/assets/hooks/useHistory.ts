import { useQuery } from "@tanstack/react-query";
import { getHistory } from "../services/transaction.service";

export const useHistory = (accountNumber: string) => {
  return useQuery({
    queryKey: ['history', accountNumber],
    queryFn: () => getHistory(accountNumber),
    enabled: !!accountNumber, 
  });
};