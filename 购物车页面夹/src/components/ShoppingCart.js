import React, { useState, useEffect } from 'react';
import './ShoppingCart.css';

const productData = [
  {
    id: 1,
    imgUrl: "img/03-car-01.png",
    proName: "Mi Band 4 NFC Version",
    proPrice: 229,
    proComm: 1
  },
  {
    id: 2,
    imgUrl: "img/03-car-02.png",
    proName: "AirDots Wireless Bluetooth Earphones",
    proPrice: 99.9,
    proComm: 9.7
  },
  {
    id: 3,
    imgUrl: "img/03-car-03.png",
    proName: "Bluetooth Temperature & Humidity Monitor",
    proPrice: 65,
    proComm: 1.3
  },
  // ... 其他产品数据
];

function ShoppingCart() {
  const [cartItems, setCartItems] = useState([]);
  const [total, setTotal] = useState(0);

  useEffect(() => {
    calculateTotal();
  }, [cartItems]);

  const calculateTotal = () => {
    const newTotal = cartItems
      .filter(item => item.selected)
      .reduce((sum, item) => sum + item.proPrice * item.quantity, 0);
    setTotal(newTotal);
  };

  const addToCart = (product) => {
    const existingItem = cartItems.find(item => item.id === product.id);
    if (existingItem) {
      setCartItems(cartItems.map(item =>
        item.id === product.id
          ? { ...item, quantity: item.quantity + 1 }
          : item
      ));
    } else {
      setCartItems([...cartItems, { ...product, quantity: 1, selected: true }]);
    }
  };

  const removeFromCart = (id) => {
    if (window.confirm("Are you sure you want to delete?")) {
      setCartItems(cartItems.filter(item => item.id !== id));
    }
  };

  const updateQuantity = (id, delta) => {
    setCartItems(cartItems.map(item =>
      item.id === id
        ? { ...item, quantity: Math.max(1, item.quantity + delta) }
        : item
    ));
  };

  const toggleSelect = (id) => {
    setCartItems(cartItems.map(item =>
      item.id === id ? { ...item, selected: !item.selected } : item
    ));
  };

  const toggleSelectAll = () => {
    const allSelected = cartItems.every(item => item.selected);
    setCartItems(cartItems.map(item => ({ ...item, selected: !allSelected })));
  };

  return (
    <div className="shopping-cart">
      <header className="header">
        <div className="container">
          <div className="header-logo">
            <a href="/" className="logo">SHOP</a>
          </div>
          <div className="header-title">
            <h1>My Shopping Cart</h1>
          </div>
          <div className="topbar-info">
            <a href="/login" className="link">Login</a>
            <span className="sep">|</span>
            <a href="/register" className="link">Register</a>
          </div>
        </div>
      </header>

      <main className="container">
        <div className="car">
          <div className="head_row">
            <div className="check left">
              <input
                type="checkbox"
                checked={cartItems.length > 0 && cartItems.every(item => item.selected)}
                onChange={toggleSelectAll}
              />
              <label>Select All</label>
            </div>
            <div className="name left">Product Name</div>
            <div className="price left">Price</div>
            <div className="number left">Quantity</div>
            <div className="subtotal left">Subtotal</div>
            <div className="ctrl left">Action</div>
          </div>

          {cartItems.map(item => (
            <div key={item.id} className="row">
              <div className="check left">
                <input
                  type="checkbox"
                  checked={item.selected}
                  onChange={() => toggleSelect(item.id)}
                />
              </div>
              <div className="img left">
                <img src={item.imgUrl} alt={item.proName} width="80" height="80" />
              </div>
              <div className="name left">
                <span>{item.proName}</span>
              </div>
              <div className="price left">
                <span>${item.proPrice}</span>
              </div>
              <div className="item_count_i">
                <div className="num_count">
                  <button 
                    className="count_d"
                    onClick={() => updateQuantity(item.id, -1)}
                  >
                    -
                  </button>
                  <span className="c_num">{item.quantity}</span>
                  <button 
                    className="count_i"
                    onClick={() => updateQuantity(item.id, 1)}
                  >
                    +
                  </button>
                </div>
              </div>
              <div className="subtotal left">
                <span>${(item.proPrice * item.quantity).toFixed(2)}</span>
              </div>
              <div className="ctrl left">
                <button onClick={() => removeFromCart(item.id)}>×</button>
              </div>
            </div>
          ))}
        </div>

        <div id="sum_area">
          <button id="pay">Checkout</button>
          <div id="pay_amount">
            Total: $<span id="price_num">{total.toFixed(2)}</span>
          </div>
        </div>

        <div id="box">
          <h2 className="box_head">
            <span>Recommended Products</span>
          </h2>
          <ul>
            {productData.map(product => (
              <li key={product.id}>
                <div className="pro_img">
                  <img src={product.imgUrl} alt={product.proName} width="150" height="150" />
                </div>
                <h3 className="pro_name">
                  <a href="#">{product.proName}</a>
                </h3>
                <p className="pro_price">${product.proPrice}</p>
                <p className="pro_rank">{product.proComm}k Reviews</p>
                <button 
                  className="add_btn"
                  onClick={() => addToCart(product)}
                >
                  Add to Cart
                </button>
              </li>
            ))}
          </ul>
        </div>
      </main>
    </div>
  );
}

export default ShoppingCart;