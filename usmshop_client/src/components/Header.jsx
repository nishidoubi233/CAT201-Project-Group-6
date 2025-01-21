import React, { useState, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import '../styles/Header.css';

const Header = () => {
  const [userName, setUserName] = useState(null);
  const navigate = useNavigate();

  useEffect(() => {
    // Get user information from localStorage
    const storedUserName = localStorage.getItem('userName');
    if (storedUserName) {
      setUserName(storedUserName);
    }
  }, []);

  const handleLogout = () => {
    // Clear user information from local storage
    localStorage.removeItem('userId');
    localStorage.removeItem('userName');
    localStorage.removeItem('userEmail');
    setUserName(null);
    navigate('/login');
  };

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
          {userName ? (
            <div className="user-menu">
              <Link to="/profile" className="user-name">{userName}</Link>
              <button onClick={handleLogout} className="logout-button">Logout</button>
            </div>
          ) : (
            <Link to="/login" className="login">Login</Link>
          )}
          <Link to="/cart" className="cart">Cart</Link>
        </div>
      </div>
    </header>
  );
};

export default Header; 