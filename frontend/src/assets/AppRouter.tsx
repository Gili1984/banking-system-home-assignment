import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { CreateAccountForm } from './pages/createAccountForm';
import { Home } from './pages/home';

const AppRouter: React.FC = () => {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/create-account" element={<CreateAccountForm />} />
      </Routes>
    </Router>
  );
};

export default AppRouter;
