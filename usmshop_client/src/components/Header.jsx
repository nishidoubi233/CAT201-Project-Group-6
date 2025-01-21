import React, { useState, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import '../styles/Header.css';

const Header = () => {
  const [userName, setUserName] = useState(null);
  const [searchTerm, setSearchTerm] = useState('');
  const navigate = useNavigate();

  useEffect(() => {
    // get user information from localStorage
    const storedUserName = localStorage.getItem('userName');
    if (storedUserName) {
      setUserName(storedUserName);
    }
  }, []);

  const handleLogout = () => {
    // clear user information from local storage
    localStorage.removeItem('userId');
    localStorage.removeItem('userName');
    localStorage.removeItem('userEmail');
    setUserName(null);
    navigate('/login');
  };

  const handleSearch = (e) => {
    e.preventDefault();
    if (searchTerm.trim()) {
      // use query parameters to navigate to the search results page
      navigate(`/search?q=${encodeURIComponent(searchTerm.trim())}`);
    }
  };

  return (
    <header>
      <div className="header-container">
        <Link to="/" className="logo">USMSHOP</Link>
        <div className="search-container">
          <form onSubmit={handleSearch} className="search-box">
            <input 
              type="text" 
              placeholder="search items..." 
              className="search-input"
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
            />
            <button type="submit" className="search-button">search</button>
          </form>
        </div>
        <div className="user-actions">
          {userName ? (
            <div className="user-menu">
              <Link to="/profile" className="user-name">{userName}</Link>
              <button onClick={handleLogout} className="logout-button">logout</button>
            </div>
          ) : (
            <Link to="/login" className="login">login</Link>
          )}
          <Link to="/cart" className="cart">cart</Link>
        </div>
      </div>
    </header>
  );
};

export default Header; 