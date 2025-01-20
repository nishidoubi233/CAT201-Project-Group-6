import React from 'react';
import { Link } from 'react-router-dom';
import '../styles/HomePage.css';
import itemData from '../data/item.json';

const HomePage = () => {
  const categories = [
    { id: 1, name: "Hard Drives", icon: "💽", path: "hard-drives" },
    { id: 2, name: "Headphones", icon: "🎧", path: "headphones" },
    { id: 3, name: "Keyboards", icon: "⌨️", path: "keyboards" },
    { id: 4, name: "Monitors", icon: "🖥️", path: "monitors" },
    { id: 5, name: "Mouse", icon: "🖱️", path: "mouse" },
    { id: 6, name: "Speakers", icon: "🔊", path: "speakers" }
  ];

  // 预先选定的折扣商品ID - 使用这三个特定商品
  const discountItemIds = [78451, 59234, 94627];  // 这三个ID对应：
  // 78451 - Wireless Ergonomic Earbuds
  // 59234 - RGB Mechanical Gaming Keyboard
  // 94627 - Classic Bookshelf Speaker

  // 预先选定的推荐商品ID - 使用其他商品
  const recommendItemIds = [63928, 52713, 47985, 85329];  // 使用其他耳机、键盘和音箱

  // 获取折扣商品信息
  const discountItems = discountItemIds
    .map(id => itemData.find(item => item.item_id === id))
    .filter(Boolean)
    .map(item => ({
      ...item,
      discountPrice: Math.round(item.price * 0.75 * 100) / 100
    }));

  // 获取推荐商品信息
  const recommendItems = recommendItemIds
    .map(id => itemData.find(item => item.item_id === id))
    .filter(Boolean);

  return (
    <div>
      <header>
        <div className="header-container">
          <div className="logo">USMSHOP</div>
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

      <main className="main-content">
        <div className="top-section">
          <div className="categories-sidebar">
            {categories.map(category => (
              <Link 
                to={`/category/${category.path}`} 
                key={category.id} 
                className="category-item"
              >
                <span className="category-icon">{category.icon}</span>
                <span className="category-name">{category.name}</span>
              </Link>
            ))}
          </div>

          <div className="discount-section">
            <h2>Special Offers</h2>
            <div className="discount-items">
              {discountItems.map(item => (
                <Link 
                  to={`/product/${item.item_id}`} 
                  key={item.item_id} 
                  className="discount-item"
                >
                  <div className="discount-image">
                    <img 
                      src={`/images/${item.image_id}`} 
                      alt={item.name}
                      onError={(e) => {
                        e.target.onerror = null;
                        e.target.src = '/images/placeholder.jpg';
                      }}
                    />
                  </div>
                  <div className="item-info">
                    <h3>{item.name}</h3>
                    <div className="price-info">
                      <span className="current-price">RM{item.discountPrice}</span>
                      <span className="original-price">RM{item.price}</span>
                    </div>
                  </div>
                </Link>
              ))}
            </div>
          </div>
        </div>

        <div className="recommendation-section">
          <h2>Recommended For You</h2>
          <div className="recommendation-items">
            {recommendItems.map(item => (
              <Link 
                to={`/product/${item.item_id}`} 
                key={item.item_id} 
                className="recommendation-item"
              >
                <div className="recommendation-image">
                  <img 
                    src={`/images/${item.image_id}`} 
                    alt={item.name}
                    onError={(e) => {
                      e.target.onerror = null;
                      e.target.src = '/images/placeholder.jpg';
                    }}
                  />
                </div>
                <div className="item-info">
                  <h3>{item.name}</h3>
                  <div className="price-info">
                    <span className="current-price">RM{item.price}</span>
                  </div>
                </div>
              </Link>
            ))}
          </div>
        </div>
      </main>
    </div>
  );
};

export default HomePage; 