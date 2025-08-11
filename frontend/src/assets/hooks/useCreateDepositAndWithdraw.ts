import { useMutation, useQueryClient } from '@tanstack/react-query';
import { createAccount } from '../services/accuont.service';

export const useCreateDepositAndWithdraw = (onAccountCreated?: (accountNumber: string) => void) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: createAccount,
    onSuccess: (data) => {
      queryClient.invalidateQueries({ queryKey: ['accounts'] });
      if (onAccountCreated) {
        onAccountCreated(data.accountNumber); 
      }
    },
  });
};