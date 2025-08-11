import { useEffect, useState } from "react";
import { useHistory } from "../hooks/useHistory";
import type { Transaction } from "../models/Transaction";

const statusHebrewMap: Record<string, string> = {
  COMPLETED: "הושלם",
  PENDING: "ממתין",
  FAILED: "נכשל",
};

const typeHebrewMap: Record<string, string> = {
  DEPOSIT: "הפקדה",
  WITHDRAWAL: "משיכה",
  TRANSFER: "העברה",
};

export const TransactionHistory: React.FC = () => {
  const [accountNumbers, setAccountNumbers] = useState<string[]>([]);
  const [selectedAccount, setSelectedAccount] = useState<string>("");

  useEffect(() => {
    const accountsJson = localStorage.getItem("accountNumbers");
    if (accountsJson) {
      const accounts: string[] = JSON.parse(accountsJson);
      setAccountNumbers(accounts);
      if (accounts.length > 0) {
        setSelectedAccount(accounts[0]);
      }
    }
  }, []);

  const { data: history, error, isLoading } = useHistory(selectedAccount);

  const handleSelectChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
    setSelectedAccount(e.target.value);
  };

  if (accountNumbers.length === 0) {
    return (
      <div className="alert alert-info mt-3">
        אין חשבונות על שמך
      </div>
    );
  }

  return (
    <div className="container mt-4">
      <div className="mb-3">
        <label htmlFor="account-select" className="form-label">
          בחר חשבון
        </label>
        <select
          id="account-select"
          className="form-select"
          value={selectedAccount}
          onChange={handleSelectChange}
        >
          {accountNumbers.map((acc) => (
            <option key={acc} value={acc}>
              {acc}
            </option>
          ))}
        </select>
      </div>

      {isLoading && <div>טוען היסטוריה...</div>}

      {error && (
        <div className="alert alert-danger">
          שגיאה בטעינת ההיסטוריה: {(error as Error).message}
        </div>
      )}

      {!isLoading && !error && history && history.length === 0 && (
        <div className="alert alert-warning">
          אין היסטוריית העברות לחשבון זה.
        </div>
      )}

      {!isLoading && !error && history && history.length > 0 && (
        <table className="table table-striped table-bordered">
          <thead>
            <tr>
              <th>סכום</th>
              <th>מטבע</th>
              <th>סוג</th>
              <th>סטטוס</th>
              <th>תיאור</th>
              <th>מועד פעולה</th>
            </tr>
          </thead>
          <tbody>
            {history.map((tx: Transaction, index: number) => (
              <tr key={index}>
                <td>{tx.amount}</td>
                <td>{tx.currency}</td>
                <td>{typeHebrewMap[tx.type] || tx.type}</td>
                <td>{statusHebrewMap[tx.status] || tx.status}</td>
                <td>{tx.description || "-"}</td>
                <td>{new Date(tx.createdAt).toLocaleString("he-IL")}</td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
};
