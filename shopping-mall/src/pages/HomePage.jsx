import React from 'react';
import { Link } from 'react-router-dom';
import '../styles/HomePage.css';

const HomePage = () => {
  const categories = [
    { id: 1, name: "Electronics", icon: "📱" },
    { id: 2, name: "Fashion", icon: "👔" },
    { id: 3, name: "Home & Living", icon: "🏠" },
    { id: 4, name: "Sports", icon: "⚽" },
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

  const discountItems = [
    { id: 1, name: "Winter Coat", price: 99, originalPrice: 199, image: "https://via.placeholder.com/200x200" },
    { id: 2, name: "Smart Watch", price: 29, originalPrice: 59, image: "https://via.placeholder.com/200x200" },
    { id: 3, name: "Wireless Earbuds", price: 88, originalPrice: 126, image: "https://via.placeholder.com/200x200" },
  ];

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
              <Link to={`/category/${category.id}`} key={category.id} className="category-item">
                <span className="category-icon">{category.icon}</span>
                {category.name}
              </Link>
            ))}
          </div>

          <div className="discount-section">
            <h2>Special Offers</h2>
            <div className="discount-items">
              {discountItems.map(item => (
                <Link to={`/product/${item.id}`} key={item.id} className="discount-item">
                  <img src={item.image} alt={item.name} />
                  <div className="item-info">
                    <h3>{item.name}</h3>
                    <div className="price-info">
                      <span className="current-price">${item.price}</span>
                      <span className="original-price">${item.originalPrice}</span>
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