import React from 'react'
import 'bootstrap/dist/css/bootstrap.min.css';
import { SearchAccuont } from './SearchAccount';

export const Home: React.FC = () => {

  return (
    <>
      <nav className="navbar navbar-expand-lg shadow-sm mb-4" style={{ backgroundColor: '#003366' }}>
        <div className="container-fluid px-4 d-flex justify-content-between align-items-center">
          <div className="navbar-brand d-flex align-items-center text-white fw-bold fs-4">
            <i className="bi bi-credit-card me-2"></i>💳 Giliz Bank
          </div>
          <ul className="navbar-nav flex-row">
            <li className="nav-item mx-2">
              <a className="nav-link text-white" href="#">Home</a>
            </li>
            <li className="nav-item mx-2">
              <a className="nav-link text-white" href="#">Link</a>
            </li>
            <li className="nav-item mx-2">
              <a className="nav-link text-white disabled" aria-disabled="true">Disabled</a>
            </li>
          </ul>
        </div>
      </nav>

      <div className="container">
        <SearchAccuont />
      </div>
    </>
  );
  
}


