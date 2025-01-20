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
    { id: 5, name: "Mice", icon: "🖱️", path: "mice" },
    { id: 6, name: "Speakers", icon: "🔊", path: "speakers" }
  ];

  // 使用ProductDetail中的商品数据
  const recommendedProducts = {
    1: {
      name: "Metal Thermos",
      price: 49.9,
      description: "Premium stainless steel, 24-hour temperature retention",
      image: "https://via.placeholder.com/600x600?text=Product+1"
    },
    2: {
      name: "Vintage Watch",
      price: 69.9,
      description: "Classic mechanical movement, stainless steel band",
      image: "https://via.placeholder.com/600x600?text=Product+2"
    },
    3: {
      name: "Bluetooth Speaker",
      price: 39.9,
      description: "Wireless connectivity, premium sound quality",
      image: "https://via.placeholder.com/600x600?text=Product+3"
    },
    4: {
      name: "Digital Notebook",
      price: 89.9,
      description: "Smart handwriting recognition, cloud sync",
      image: "https://via.placeholder.com/600x600?text=Product+4"
    }
  };

  // 预先选定的折扣商品ID（这些ID是从item.json中随机选择的）
  const discountItemIds = [78451, 59234, 94627]; // 分别是一个耳机、键盘和音箱

  // 根据预选ID获取商品信息并添加折扣价
  const discountItems = discountItemIds
    .map(id => itemData.find(item => item.item_id === id))
    .filter(Boolean) // 移除可能的undefined结果
    .map(item => ({
      ...item,
      discountPrice: Math.round(item.price * 0.75 * 100) / 100, // 75%的原价，保留两位小数
      imageUrl: `/images/${item.image_id}`
    }));

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
                      src={item.imageUrl} 
                      alt={item.name}
                      onError={(e) => {
                        e.target.onerror = null; // 防止循环触发错误
                        e.target.src = '/images/placeholder.jpg'; // 设置默认图片
                      }}
                    />
                  </div>
                  <div className="item-info">
                    <h3>{item.name}</h3>
                    <div className="price-info">
                      <span className="current-price">${item.discountPrice}</span>
                      <span className="original-price">${item.price}</span>
                    </div>
                  </div>
                </Link>
              ))}
            </div>
          </div>
        </div>

        <div className="recommended-section">
          <h2>Recommended For You</h2>
          <div className="recommended-grid">
            {Object.entries(recommendedProducts).map(([id, product]) => (
              <Link to={`/product/${id}`} key={id} className="product-card">
                <div className="product-image">
                  <img src={product.image} alt={product.name} />
                </div>
                <div className="product-info">
                  <h3>{product.name}</h3>
                  <p className="description">{product.description}</p>
                  <p className="price">${product.price}</p>
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