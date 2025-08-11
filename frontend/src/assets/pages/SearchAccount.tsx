import React, { useEffect, useRef, useState } from 'react'
import { useAccounts } from '../hooks/useAccounts'
import { AccountDashboard } from './AccountDashboard';
import 'bootstrap/dist/css/bootstrap.min.css';
import type { Account } from '../models/accuont';

export const SearchAccuont: React.FC = () => {
  const inputRef = useRef<HTMLInputElement>(null);
  const [submittedId, setSubmittedId] = useState(''); 
  const [accounts, setAccounts] = useState<Account[]>([]);


  const { data, isLoading, isError, error } = useAccounts(submittedId);

  const handleSearch = () => {
    if (inputRef.current) {
      setSubmittedId(inputRef.current.value); 
    }
  };

   useEffect(() => {
    if (data) {
      setAccounts(data);
    }
  }, [data]);

  return (
    <div className="bg-light py-5" style={{ direction: 'rtl' }}>
      <div className="container">
        <div className="bg-white shadow rounded p-4 mb-4">
          <h4 className="fw-bold mb-3"  style={{ color: '#003366' }}>
            🔍 כנס אל החשבונות שלך
          </h4>
          <div className="row g-2 align-items-center">
            <div className="col-md-10">
              <input
                type="text"
                className="form-control"
                placeholder="הקלד מספר זהות"
                ref={inputRef}
              />
            </div>
            <div className="col-md-2 d-grid">
              <button className="btn text-white"  style={{ backgroundColor: '#003366'}} onClick={handleSearch}>
                חפש
              </button>
            </div>
          </div>
        </div>
        {isLoading && (
          <div className="alert alert-info text-center">טוען נתונים...</div>
        )}
        {isError && (
          <div className="alert alert-danger text-center">
            שגיאה: {(error as Error).message}
          </div>
        )}
        {accounts && accounts.length > 0 && (
          <div className="mt-4">
            <h5 className="mb-3 text-secondary">החשבונות שלך:</h5>
            <div className="row">
              {accounts.map((account) => (
                <div key={account.accountId} className="col-md-6 col-lg-4 mb-4">
                  <AccountDashboard account={account} />
                </div>
              ))}
            </div>
          </div>
        )}
      </div>
    </div>
  );
 
}


