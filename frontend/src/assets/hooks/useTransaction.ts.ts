import { useMutation} from '@tanstack/react-query';
import { performTransaction, type TransactionDto, type WithdrawAndDepositDto } from '../services/transaction.service';

export const useTransaction = (
  type: 'deposit' | 'withdraw'|'transfer',
  onSuccess?: (msg: string) => void,
  onError?: (msg: string) => void
) => {
  return useMutation({
    mutationFn: (data: TransactionDto|WithdrawAndDepositDto) => performTransaction(data, type),
    onSuccess: (data) => {
      if (onSuccess) {
        let actionText = type === 'deposit' ? 'הפקדה' : 'משיכה';
        if(type==='transfer') actionText='ההעברה'
        onSuccess(`${actionText} בוצעה בהצלחה!:`);
      }
    },
onError: (error: any) => {
  let errorMsg = 'שגיאה בביצוע פעולה';

  if (error.message) {
    try {
      const parsed = JSON.parse(error.message);
      if (parsed.message) {
        errorMsg = parsed.message;
      } else {
        errorMsg = error.message; 
      }
    } catch {
      errorMsg = error.message;
    }
  }

  if (onError) onError(errorMsg);
},

  });
};
