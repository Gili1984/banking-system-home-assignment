import React, { useState, useEffect } from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import { Link, Outlet } from 'react-router-dom';
import { useCustomerById } from '../hooks/useCustomer';

export const Home: React.FC = () => {
  const [showLoginModal, setShowLoginModal] = useState(false);
  const [tz, setTz] = useState('');
  const [customerId, setCustomerId] = useState<string | null>(null);

const { data, isLoading, isError, refetch } = useCustomerById(customerId ?? '', {
  enabled: !!customerId, 
});

  const [userName, setUserName] = useState<string | null>(null);

  useEffect(() => {
    if (data) {
      const customer = data; 
      setUserName(`שלום, ${customer.firstName} ${customer.lastName}`);
      setShowLoginModal(false);
      setTz('');
    }
  }, [data]);

  useEffect(() => {
  const savedCustomerId = localStorage.getItem('customerId');
  if (savedCustomerId) {
    setCustomerId(savedCustomerId);
  }
}, []);

 const handleLogin = () => {
  if (!tz) {
    alert('אנא הזן ת"ז');
    return;
  }
  setCustomerId(tz);
  localStorage.setItem('customerId', tz);
  refetch();
};


  return (
    <>
      <nav className="navbar navbar-expand-lg shadow-sm mb-4" style={{ backgroundColor: '#003366'}}>
        <div className="container-fluid px-4 d-flex justify-content-between align-items-center">
          <div className="navbar-brand d-flex align-items-center text-white fw-bold fs-4">
            <i className="bi bi-credit-card me-2"></i>💳 Giliz Bank
          </div>
          <ul className="navbar-nav flex-row align-items-center" style={{ position: 'relative' }}>
            <li className="nav-item mx-2">
              <Link className="dropdown-item" to="/" style={{color:'white'}}>
                      דף הבית
              </Link>
            </li>
                        <li className="nav-item mx-2">
              <Link className="dropdown-item" to="/history" style={{color:'white'}}>
                      היסטורית העברות 
              </Link>
            </li>
            <li className="nav-item mx-2" style={{ position: 'relative' }}>
              <div className="dropdown">
                <button
                  className="btn btn-secondary dropdown-toggle"
                  type="button"
                  data-bs-toggle="dropdown"
                  aria-expanded="false"
                >
                  פעולות נוספות
                </button>
                <ul
                  className="dropdown-menu"
                  style={{
                    position: 'absolute',
                    zIndex: 1050,
                    top: '100%',
                    left: 0,
                    marginTop: '0.25rem',
                  }}
                >                  <li>
                    <Link className="dropdown-item" to="/create-account">
                      יצירת חשבון
                    </Link>
                  </li>
                  <li>
                    <Link className="dropdown-item" to="/deposit-Withdraw">
                      הפקדות/משיכות
                    </Link>
                  </li>
                  <li>
                    <Link className="dropdown-item" to="/transfer">
                      העברות
                    </Link>
                  </li>
                </ul>
              </div>
            </li>
            <li className="nav-item d-flex align-items-center" style={{ marginLeft: 'auto', gap: '0.5rem' }}>
              {userName && (
                <span className="text-white">{userName}</span>
              )}
              <button
                onClick={() => setShowLoginModal(true)}
                className="btn btn-link p-0 m-0"
                aria-label="Login"
                style={{ color: 'white', fontSize: '32px', cursor: 'pointer' }}
              >
                <span className="material-symbols-outlined">account_circle</span>
              </button>
            </li>
          </ul>
        </div>
      </nav>

      <div className="container">
        <Outlet context={{ customerId }} />
      </div>
      {showLoginModal && (
        <div
          className="modal fade show"
          style={{ display: 'block', backgroundColor: 'rgba(0,0,0,0.5)' }}
          tabIndex={-1}
          role="dialog"
        >
          <div className="modal-dialog modal-dialog-centered" role="document">
            <div className="modal-content" dir="rtl">
              <div className="modal-header">
                <h5 className="modal-title">כניסה למערכת</h5>
              </div>
              <div className="modal-body">
                <label htmlFor="tzInput" className="form-label">
                  תעודת זהות
                </label>
                <input
                  id="tzInput"
                  type="text"
                  className="form-control"
                  value={tz}
                  onChange={(e) => setTz(e.target.value)}
                  placeholder="הזן תעודת זהות"
                />
                {isLoading && <p className="mt-2">טוען...</p>}
                {isError && <p className="text-danger mt-2">שגיאה בטעינת הנתונים</p>}
              </div>
              <div className="modal-footer">
                <button type="button" className="btn btn-secondary" onClick={() => setShowLoginModal(false)}>
                  ביטול
                </button>
                <button type="button" className="btn btn-primary" onClick={handleLogin}>
                  התחבר
                </button>
              </div>
            </div>
          </div>
        </div>
      )}
    </>
  );
};
