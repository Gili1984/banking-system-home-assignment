import React, { useEffect, useState } from 'react';
import { useTransaction } from '../hooks/useTransaction.ts';

export const DepositAndWithdrawForm: React.FC = () => {
  const [accountNumber, setAccountNumber] = useState('');
  const [amount, setAmount] = useState('');
  const [currency, setCurrency] = useState('ILS');
  const [description, setDescription] = useState('');
  const [operationType, setOperationType] = useState<'DEPOSIT' | 'WITHDRAWAL'>('DEPOSIT');
  const [message, setMessage] = useState('');
  const [accountNumbers, setAccountNumbers] = useState<string[]>([]); 
  const [accountsEmptyError, setAccountsEmptyError] = useState(false);

  const currencyMap: Record<string, string> = {
    ILS: 'שקלים',
    USD: 'דולר',
    EUR: 'אירו',
  };

const depositMutation = useTransaction(
  'deposit',
  (msg) => setMessage(msg),
  (errMsg) => setMessage(errMsg)
);

const withdrawMutation = useTransaction(
  'withdraw',
  (msg) => setMessage(msg),
  (errMsg) => setMessage(errMsg)
);

  useEffect(() => {
    const savedAccountsJson = localStorage.getItem('accountNumbers');
    if (savedAccountsJson) {
      const savedAccounts = JSON.parse(savedAccountsJson) as string[];
      if (savedAccounts.length > 0) {
        setAccountNumbers(savedAccounts);
        setAccountNumber(savedAccounts[0]); 
        setAccountsEmptyError(false);
      } else {
        setAccountsEmptyError(true);
      }
    } else {
      setAccountsEmptyError(true);
    }
  }, []);
  
  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();

    if (!accountNumber || !amount) {
      setMessage('נא למלא מספר חשבון וסכום');
      return;
    }

    const data = {
      accountNumber,
      amount: parseFloat(amount),
      currency,
      description,
    };

    setMessage('...טוען');

    if (operationType === 'DEPOSIT') {
      depositMutation.mutate(data);
    } else {
      withdrawMutation.mutate(data);
    }

    setAccountNumber('');
    setAmount('');
    setCurrency('ILS');
    setDescription('');
    setOperationType('DEPOSIT');
  };

  const resetForm = () => setMessage('');

  return (
    <>
      {message ? (
        <div style={{ maxWidth: 500, margin: '50px auto', textAlign: 'center' }}>
          <div className="alert alert-info">{message}</div>
          <button className="btn btn-secondary mt-3" onClick={resetForm}>
            חזור לטופס
          </button>
        </div>
      ) : (
        <form
          onSubmit={handleSubmit}
          style={{ maxWidth: 500, margin: '20px auto' }}
          className="p-4 border rounded shadow-sm bg-light"
          dir="rtl"
        >
          <h3 className="mb-4 text-center">טופס הפקדה / משיכה</h3>

          <div className="mb-4">
            <label className="form-label d-block mb-2">סוג פעולה: </label>
            <div className="form-check form-check-inline">
              <input
                className="form-check-input"
                type="radio"
                id="deposit"
                name="operationType"
                value="DEPOSIT"
                checked={operationType === 'DEPOSIT'}
                onChange={() => setOperationType('DEPOSIT')}
              />
              <label className="form-check-label" htmlFor="deposit">
                הפקדה
              </label>
            </div>
            <div className="form-check form-check-inline">
              <input
                className="form-check-input"
                type="radio"
                id="withdrawal"
                name="operationType"
                value="WITHDRAWAL"
                checked={operationType === 'WITHDRAWAL'}
                onChange={() => setOperationType('WITHDRAWAL')}
              />
              <label className="form-check-label" htmlFor="withdrawal">
                משיכה
              </label>
            </div>
          </div>

          <div className="mb-3">
              <label htmlFor="amount" className="form-label">
              מספר חשבון
            </label>
            {accountsEmptyError ? (
              <div className="alert alert-danger" role="alert">
                לא נמצאו חשבונות ברשימה. אנא התחבר מחדש.
              </div>
            ) : (
              <select
                id="accountNumber"
                className="form-select"
                value={accountNumber}
                onChange={(e) => setAccountNumber(e.target.value)}
                required
              >
                {accountNumbers.map((accNum) => (
                  <option key={accNum} value={accNum}>
                    {accNum}
                  </option>
                ))}
              </select>
            )}
          </div>

          <div className="mb-3">
            <label htmlFor="amount" className="form-label">
              סכום
            </label>
            <input
              type="number"
              id="amount"
              className="form-control"
              value={amount}
              onChange={(e) => setAmount(e.target.value)}
              min="0.01"
              step="0.01"
              required
            />
          </div>

          <div className="mb-3">
            <label htmlFor="currency" className="form-label">
              מטבע
            </label>
            <select
              id="currency"
              className="form-select"
              value={currency}
              onChange={(e) => setCurrency(e.target.value)}
              required
            >
              <option value="ILS">{currencyMap['ILS']}</option>
              <option value="USD">{currencyMap['USD']}</option>
              <option value="EUR">{currencyMap['EUR']}</option>
            </select>
          </div>

          <div className="mb-3">
            <label htmlFor="description" className="form-label">
              תיאור (אופציונלי)
            </label>
            <textarea
              id="description"
              className="form-control"
              rows={3}
              value={description}
              onChange={(e) => setDescription(e.target.value)}
            />
          </div>
          <button type="submit" className="btn btn-primary w-100">
            שלח
          </button>
        </form>
      )}
    </>
  );
};
