import React from 'react';
import { Link } from 'react-router-dom';
import '../styles/HomePage.css';

const HomePage = () => {
  const products = [
    {
      id: 1,
      name: "精美金属保温杯",
      price: 49.9,
      image: "https://via.placeholder.com/600x600?text=Product+1"
    },
    {
      id: 2,
      name: "复古机械手表",
      price: 69.9,
      image: "https://via.placeholder.com/600x600?text=Product+2"
    },
    {
      id: 3,
      name: "便携式蓝牙音箱",
      price: 39.9,
      image: "https://via.placeholder.com/600x600?text=Product+3"
    },
    {
      id: 4,
      name: "多功能电子笔记本",
      price: 89.9,
      image: "https://via.placeholder.com/600x600?text=Product+4"
    }
  ];

  return (
    <div>
      <header>
        <div className="header-container">
          <div className="logo">Logo</div>
          <div className="search-bar">
            <input type="text" placeholder="Search" />
            <button type="submit">Search</button>
          </div>
          <div className="user-actions">
            <Link to="/login" className="login">Login</Link>
            <Link to="/cart" className="cart">Cart</Link>
          </div>
        </div>
      </header>

      <main>
        <aside className="category-menu">
          {/* 保持原有的类别菜单结构 */}
        </aside>

        <div className="content-area">
          <div className="product-grid">
            {products.map(product => (
              <Link to={`/product/${product.id}`} className="product-card" key={product.id}>
                <div className="product-image">
                  <img src={product.image} alt={product.name} />
                </div>
                <div className="product-info">
                  <h3>{product.name}</h3>
                  <p className="price">￥{product.price}</p>
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