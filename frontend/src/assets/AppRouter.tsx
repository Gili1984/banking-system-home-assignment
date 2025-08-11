import React from 'react';
import { BrowserRouter as Router, Routes, Route, useOutletContext } from 'react-router-dom';
import { Home } from './pages/Home';
import { CreateAccountForm } from './pages/CreateAccountForm';
import { AccountDetails } from './pages/AccountDetails';
import { DepositAndWithdrawForm } from './pages/DepositAndWithdrawForm';
import { AccountsList } from './pages/AccountsList ';
import { TransferPage } from './pages/TransferPage';
import { TransactionHistory } from './pages/TransactionHistory';


const AppRouter: React.FC = () => {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<Home />}>
          <Route
            index
            element={<AccountsListWrapper />}
          />
          <Route path="create-account" element={<CreateAccountForm />} />
          <Route path="account-details" element={<AccountDetails />} />
          <Route path="deposit-Withdraw" element={<DepositAndWithdrawForm />} />
          <Route path="transfer" element={<TransferPage />} />
          <Route path="history" element={<TransactionHistory/>}/>
        </Route>
      </Routes>
    </Router>
  );
};

const AccountsListWrapper: React.FC = () => {
  const { customerId } = useOutletContext<{ customerId: string | null }>();
  return <AccountsList customerId={customerId ?? ''} />;
};

export default AppRouter;
