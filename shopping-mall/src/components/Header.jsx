import React from 'react';
import { Link } from 'react-router-dom';
import '../styles/Header.css';

const Header = () => {
  return (
    <header>
      <div className="header-container">
        <Link to="/" className="logo">USMSHOP</Link>
        <div className="search-container">
          <div className="search-box">
            <input type="text" placeholder="Search" className="search-input" />
            <button type="submit" className="search-button">Search</button>
          </div>
        </div>
        <div className="user-actions">
          <Link to="/login" className="login">Login</Link>
          <Link to="/cart" className="cart">Cart</Link>
        </div>
      </div>
    </header>
  );
};

export default Header; 