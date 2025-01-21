import React from 'react';
import '../styles/Footer.css';

const Footer = () => {
  return (
    <footer className="footer">
      <div className="footer-content">
        <div className="footer-section">
          <h3>About Us</h3>
          <p>USM Shop is an e-commerce platform demo project built with React</p>
        </div>
        
        <div className="footer-section">
          <h3>Quick Links</h3>
          <ul>
            <li><a href="https://www.usm.my" target="_blank" rel="noopener noreferrer">USM Official Website</a></li>
            <li><a href="https://github.com/nishidoubi233/CAT201-Project-Group-6" target="_blank" rel="noopener noreferrer">GitHub Repository</a></li>
          </ul>
        </div>
        
        <div className="footer-section">
          <h3>Contact Information</h3>
          <p>Address: 11800 USM, Penang, Malaysia</p>
          <p>Email: contact@usmshop.com</p>
        </div>
      </div>
      
      <div className="footer-bottom">
        <p>&copy; {new Date().getFullYear()} USM Shop. All rights reserved.</p>
      </div>
    </footer>
  );
};

export default Footer; 