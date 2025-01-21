import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import '../styles/UserProfile.css';

// User profile page showing user information and settings
const UserProfile = () => {
    const navigate = useNavigate();
    const [userData, setUserData] = useState({
        name: '',
        email: '',
        avatar: '/default-avatar.png',
        balance: 0
    });

    useEffect(() => {
        // Check if user is logged in
        const userId = localStorage.getItem('userId');
        if (!userId) {
            navigate('/login');
            return;
        }

        // Get basic information from localStorage
        const userName = localStorage.getItem('userName');
        const userEmail = localStorage.getItem('userEmail');
        
        // Get user details from backend
        const fetchUserData = async () => {
            try {
                const response = await fetch(`/api/user?id=${userId}`);
                const data = await response.json();
                
                if (data.status === 'ok') {
                    setUserData({
                        name: data.user.userName,
                        email: data.user.email,
                        avatar: data.user.avatar || '/default-avatar.png',
                        balance: data.user.balance || 0
                    });
                    
                    // Update localStorage with latest user info
                    localStorage.setItem('userName', data.user.userName);
                    localStorage.setItem('userEmail', data.user.email);
                }
            } catch (error) {
                console.error('Failed to fetch user data:', error);
            }
        };

        fetchUserData();
    }, [navigate]);

    const handleAvatarChange = (event) => {
        const file = event.target.files[0];
        if (file) {
            const reader = new FileReader();
            reader.onload = (e) => {
                setUserData(prev => ({
                    ...prev,
                    avatar: e.target.result
                }));
            };
            reader.readAsDataURL(file);
        }
    };

    return (
        <div className="page-container">
            <div className="header">
                <div className="back-button" onClick={() => navigate('/')}>
                    <span>&lt;</span>
                    <span>Back to Home</span>
                </div>
                <div className="title">My Account</div>
                <div className="placeholder"></div>
            </div>

            <div className="shop-info">
                <div className="avatar" onClick={() => document.getElementById('avatarUpload').click()}>
                    <img src={userData.avatar} alt="Profile" id="avatarImg" />
                    <input
                        type="file"
                        id="avatarUpload"
                        accept="image/*"
                        style={{ display: 'none' }}
                        onChange={handleAvatarChange}
                    />
                    <div className="avatar-overlay">
                        <span>Change</span>
                    </div>
                </div>
                <div className="shop-details">
                    <div className="shop-name">{userData.name}</div>
                    <div className="shop-phone">{userData.email}</div>
                </div>
            </div>

            <div className="stats-bar">
                <div className="stat-item">
                    <div className="stat-value">RM{userData.balance}</div>
                    <div className="stat-label">Balance</div>
                </div>
            </div>

            <div className="menu-list">
                <div className="menu-item" onClick={() => navigate('/cart')}>
                    <div className="menu-icon">🛍️</div>
                    <div className="menu-text">Shopping Cart</div>
                    <div className="arrow">›</div>
                </div>
                <div className="menu-item">
                    <div className="menu-icon">📍</div>
                    <div className="menu-text">Shipping Address</div>
                    <div className="arrow">›</div>
                </div>
                <div className="menu-item">
                    <div className="menu-icon">💳</div>
                    <div className="menu-text">Payment Methods</div>
                    <div className="arrow">›</div>
                </div>
                <div className="menu-item">
                    <div className="menu-icon">⭐</div>
                    <div className="menu-text">My Reviews</div>
                    <div className="arrow">›</div>
                </div>
            </div>
        </div>
    );
};

export default UserProfile; 