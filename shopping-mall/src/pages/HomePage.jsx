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
          <ul>
            <li>
              <i className="icon-phone"></i>
              Phones / Digital
              <div className="submenu">
                <Link to="/categories/phones"><p>Smartphones</p></Link>
                <Link to="/categories/laptops"><p>Laptops</p></Link>
                <Link to="/categories/tablets"><p>Tablets</p></Link>
                <Link to="/categories/cameras"><p>Cameras</p></Link>
              </div>
            </li>
            <li>
              Fashion / Sports
              <div className="submenu">
                <p>Men's Wear</p>
                <p>Women's Wear</p>
                <p>Sports Equipment</p>
                <p>Running Shoes</p>
                <p>Accessories</p>
              </div>
            </li>
            <li>
              Beauty / Personal Care
              <div className="submenu">
                <p>Skincare</p>
                <p>Makeup</p>
                <p>Hair Care</p>
                <p>Fragrances</p>
                <p>Personal Hygiene</p>
              </div>
            </li>
            <li>
              Home / Appliances
              <div className="submenu">
                <p>Kitchen Appliances</p>
                <p>Furniture</p>
                <p>Home Decor</p>
                <p>Lighting</p>
                <p>Storage</p>
              </div>
            </li>
            <li>
              Food / Pets
              <div className="submenu">
                <p>Groceries</p>
                <p>Pet Food</p>
                <p>Pet Supplies</p>
                <p>Snacks</p>
                <p>Beverages</p>
              </div>
            </li>
            <li>
              Books / Games
              <div className="submenu">
                <p>Fiction Books</p>
                <p>Video Games</p>
                <p>Board Games</p>
                <p>Educational</p>
                <p>Comics & Manga</p>
              </div>
            </li>
          </ul>
        </aside>

        <div className="content-area">
          <div className="main-promotion">
            <Link to="/promotions/special-sale">
              <img src="https://via.placeholder.com/1200x300" alt="Special Sale" />
              <div className="banner-content">
                <h2>Flash Sale</h2>
                <p>Super deals up to 70% off</p>
                <button className="view-more">Check it out &gt;</button>
              </div>
            </Link>
          </div>

          <div className="featured-grid">
            <Link to="#" className="featured-card yellow">
              <div className="card-content">
                <div className="card-text">
                  <h3>Fashion</h3>
                  <p>Trendy Styles</p>
                </div>
                <img src="https://via.placeholder.com/100" alt="Fashion" />
              </div>
            </Link>
            <Link to="#" className="featured-card blue">
              <div className="card-content">
                <div className="card-text">
                  <h3>Digital</h3>
                  <p>Hot Gadgets</p>
                </div>
                <img src="https://via.placeholder.com/100" alt="Digital" />
              </div>
            </Link>
            <Link to="#" className="featured-card green">
              <div className="card-content">
                <div className="card-text">
                  <h3>Anime</h3>
                  <p>Latest Collections</p>
                </div>
                <img src="https://via.placeholder.com/100" alt="Anime" />
              </div>
            </Link>
            <Link to="#" className="featured-card pink">
              <div className="card-content">
                <div className="card-text">
                  <h3>Coupons</h3>
                  <p>Save Big</p>
                </div>
                <img src="https://via.placeholder.com/100" alt="Coupons" />
              </div>
            </Link>
          </div>

          <div className="interest-tags">
            <a href="#" className="tag active">
              <i className="icon-star"></i>
              Recommended
            </a>
            <a href="#" className="tag">Second Hand</a>
            <a href="#" className="tag">BJD Dolls</a>
            <a href="#" className="tag">Fishing</a>
            <a href="#" className="tag">Guitar</a>
            <a href="#" className="tag">Table Tennis</a>
            <a href="#" className="tag">Photography</a>
          </div>

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