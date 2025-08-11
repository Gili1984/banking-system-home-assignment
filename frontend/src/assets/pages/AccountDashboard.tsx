import React from 'react'
import type { Account } from '../models/accuont';
import { Link } from 'react-router-dom';

interface AccountCardProps {
  account: Account;
}

export const AccountDashboard: React.FC<AccountCardProps> = ({ account }) => {

  return (
    <div className="card" style={{ width: '18rem' }}>
      <div className="card-body">
        <h5 className="card-title">חשבון מספר:{account.accountNumber}</h5>
        <p className="card-text">סוג החשבון: {account.accountType}</p>
        <p className="card-text"> יתרה בחשבון: {account.balance}</p>
        <Link
        style={{ backgroundColor: '#003366',color:'white' }}
          className="dropdown-item"
          to={`/account-details`}
          state={{ account }}
        >
          לפרטי החשבון:
        </Link>
      </div>
    </div>
  );

}
