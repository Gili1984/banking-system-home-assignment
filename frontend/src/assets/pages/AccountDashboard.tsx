import React from 'react'
import type { Account } from '../models/accuont';

interface AccountCardProps {
  account: Account;
}

export const AccountDashboard: React.FC<AccountCardProps> = ({ account }) => {

  return (
    // <div className="card mb-3" style={{ maxWidth: '400px' }}>
    //   <div className="card-body">
    //     <h5 className="card-title">Account Number: {account.accountNumber}</h5>
    //     <p className="card-text">Balance: ${account.balance.toFixed(2)}</p>
    //   </div>
    // </div>
    <div className="card" style={{width: '18rem'}}>
  <div className="card-body">
    <h5 className="card-title">חשבון מספר:{account.accountNumber}</h5>
    <p className="card-text">סוג החשבון: {account.accountType}</p>
    <p className="card-text"> יתרה בחשבון: {account.balance}</p>
    <a href="#" className="btn btn-primary">Go somewhere</a>
  </div>
</div>
  );
  
}
