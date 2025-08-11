import React from 'react'
import { useLocation, useParams } from 'react-router-dom';
import type { Account } from '../models/accuont';

const accountTypeMap: Record<string, string> = {
  SAVINGS: 'חיסכון',
  CHECKING: 'עו"ש',
};

const statusMap: Record<string, string> = {
  ACTIVE: 'פעיל',
  INACTIVE: 'לא פעיל',
  BLOCKED: 'חסום',
};

const currencyMap: Record<string, string> = {
  ILS: 'שקלים',
  USD: 'דולר',
  EUR: 'אירו',
};

export const AccountDetails: React.FC = () => {
  const location = useLocation();
  const state = location.state as { account?: Account };
  const account = state.account;


  if (!account) {
    return (
      <div className="alert alert-warning" style={{ maxWidth: '500px', margin: '20px auto' }}>
        פרטי החשבון לא נטענו. נא לרענן את הדף או לחזור לעמוד הקודם.
      </div>
    );
  }
  const createdAtDate = new Date(account.createdAt);
  const updatedAtDate = new Date(account.updatedAt);
  return (
    <div className="card" style={{ maxWidth: '500px', margin: '20px auto' }}>
      <div className="card-header bg-primary text-white">
        פרטי חשבון מספר: {account.accountNumber}
      </div>
      <ul className="list-group list-group-flush">
        <li className="list-group-item"><strong>מספר לקוח:</strong> {account.customerId}</li>
        <li className="list-group-item">
          <strong>יתרה:</strong> {account.balance.toLocaleString()} {currencyMap[account.currency] || account.currency}
        </li>        
        <li className="list-group-item">
          <strong>סוג חשבון: </strong>{accountTypeMap[account.accountType] || account.accountType}
        </li>
        <li className="list-group-item">
          <strong>סטטוס: </strong>{statusMap[account.status] || account.status}
        </li>
        <li className="list-group-item">
          <strong>תאריך יצירה:</strong> {createdAtDate.toLocaleDateString('he-IL')}
        </li>
        <li className="list-group-item">
          <strong>תאריך עדכון אחרון:</strong> {updatedAtDate.toLocaleDateString('he-IL')}
        </li>
      </ul>
    </div>
  );

}


