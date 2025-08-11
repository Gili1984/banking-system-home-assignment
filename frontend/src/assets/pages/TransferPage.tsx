import React, { useEffect, useState } from 'react';
import { useTransaction } from '../hooks/useTransaction.ts';

export const TransferPage: React.FC = () => {
  const [fromAccountNumber, setFromAccountNumber] = useState('');
  const [toAccountNumber, setToAccountNumber] = useState('');
  const [amount, setAmount] = useState('');
  const [currency, setCurrency] = useState('ILS');
  const [description, setDescription] = useState('');
  const [message, setMessage] = useState('');
  const [accountNumbers, setAccountNumbers] = useState<string[]>([]); 
  const [accountsEmptyError, setAccountsEmptyError] = useState(false);

  const currencyMap: Record<string, string> = {
    ILS: 'שקלים',
    USD: 'דולר',
    EUR: 'אירו',
  };

const transferMutation = useTransaction(
  'transfer',
  (msg) => setMessage(msg),
  (errMsg) => setMessage(errMsg)
);

  useEffect(() => {
    const savedAccountsJson = localStorage.getItem('accountNumbers');
    if (savedAccountsJson) {
      const savedAccounts = JSON.parse(savedAccountsJson) as string[];
      if (savedAccounts.length > 0) {
        setAccountNumbers(savedAccounts);
        setFromAccountNumber(savedAccounts[0]); 
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

    if (!fromAccountNumber || !amount || !toAccountNumber) {
      setMessage('נא למלא מספר חשבון וסכום');
      return;
    }

    const data = {
      fromAccountNumber,
      toAccountNumber,
      amount: parseFloat(amount),
      currency,
      description,
    };

    setMessage('...טוען');
    transferMutation.mutate(data);
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
          <h3 className="mb-4 text-center">טופס ההעברות</h3>
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
                value={fromAccountNumber}
                onChange={(e) => setFromAccountNumber(e.target.value)}
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
            <label htmlFor="toAccount" className="form-label">
              אל חשבון
            </label>
            <input
              type="text"
              id="toAccount"
              className="form-control"
              value={toAccountNumber}
              onChange={(e) => setToAccountNumber(e.target.value)}
              min="0.01"
              step="0.01"
              required
            />
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
