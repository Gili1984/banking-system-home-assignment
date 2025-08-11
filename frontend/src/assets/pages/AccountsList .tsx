import React, { useEffect, useState } from 'react';
import { useAccounts } from '../hooks/useAccounts';
import { AccountDashboard } from './AccountDashboard';
import 'bootstrap/dist/css/bootstrap.min.css';
import type { Account } from '../models/accuont';

interface AccountsListProps {
  customerId: string;
}

export const AccountsList: React.FC<AccountsListProps> = ({ customerId }) => {
  const { data, isLoading, isError, error } = useAccounts(customerId);
  const [accounts, setAccounts] = useState<Account[]>([]);

  useEffect(() => {
    if (data) {
      setAccounts(data);
      const accountNumbers = data.map((acc) => acc.accountNumber);
      localStorage.setItem('accountNumbers', JSON.stringify(accountNumbers));
    }
  }, [data]);

  if (!customerId) {
    return <div className="alert alert-warning text-center">אנא התחבר כדי לראות את החשבונות שלך</div>;
  }

  return (
    <div className="bg-light py-5" style={{ direction: 'rtl' }}>
      <div className="container">
        {isLoading && <div className="alert alert-info text-center">טוען נתונים...</div>}
        {isError && (
          <div className="alert alert-danger text-center">
            שגיאה: {(error as Error).message}
          </div>
        )}
        {accounts.length > 0 ? (
          <>
            <h5 className="mb-3 text-secondary">החשבונות שלך:</h5>
            <div className="row">
              {accounts.map((account) => (
                <div key={account.accountId} className="col-md-6 col-lg-4 mb-4">
                  <AccountDashboard account={account} />
                </div>
              ))}
            </div>
          </>
        ) : (
          <div className="alert alert-info text-center">לא נמצאו חשבונות</div>
        )}
      </div>
    </div>
  );
};
