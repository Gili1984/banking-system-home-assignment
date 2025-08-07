import React from 'react'
import { useCustomer } from '../assets/hooks/useCustomer'

const AccountDashboard: React.FC = () => {
  const { data: customers, isLoading, isError, error } = useCustomer()

  if (isLoading) {
    return <p>טוען נתונים...</p>
  }

  if (isError) {
    return <p>אירעה שגיאה בעת שליפת הנתונים: {(error as Error).message}</p>
  }
    if (customers) {
   console.log("account",customers)
  }

  return (
    <div>
      <h2>רשימת חשבונות</h2>
      {customers?.length === 0 ? (
        <p>אין חשבונות להצגה.</p>
      ) : (
   <h1>הגיעו החשבונות</h1>
      )}
    </div>
  )
}

export default AccountDashboard
