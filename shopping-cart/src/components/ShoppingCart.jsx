import React, { useState, useEffect } from 'react';
import './ShoppingCart.css';

const ShoppingCart = () => {
  const [cartItems, setCartItems] = useState([]);
  const [total, setTotal] = useState(0);

    // 商品数据
    const productData = [
      {
        imgUrl: "img/03-car-01.png",
        proName: "Band 4 NFC Version",
        proPrice: "229",
        proComm: "1"
      },
      {
        imgUrl: "img/03-car-02.png",
        proName: "AirDots Wireless Bluetooth Earphones",
        proPrice: "99.9",
        proComm: "9.7"
      },
      {
        imgUrl: "img/03-car-03.png",
        proName: "Bluetooth Temperature & Humidity Monitor",
        proPrice: "65",
        proComm: "1.3"
      },
      {
        imgUrl: "img/03-car-04.png",
        proName: "Smart Alarm Clock",
        proPrice: "149",
        proComm: "1.1"
      },
      {
        imgUrl: "img/03-car-05.png",
        proName: "Temperature & Humidity Monitor Pro",
        proPrice: "750",
        proComm: "0.3"
      },
      {
        imgUrl: "img/03-car-06.png",
        proName: "Band 3 NFC Version Pro",
        proPrice: "199",
        proComm: "3.3"
      },
      {
        imgUrl: "img/03-car-07.png",
        proName: "Band 3/4 Strap",
        proPrice: "19.9",
        proComm: "1.2"
      },
      {
        imgUrl: "img/03-car-08.png",
        proName: "Temperature & Humidity Sensor",
        proPrice: "45",
        proComm: "0.6"
      },
      {
        imgUrl: "img/03-car-09.png",
        proName: "Temperature & Humidity Monitor Pro (3-Pack)",
        proPrice: "207",
        proComm: "0.3"
      },
      {
        imgUrl: "img/03-car-10.png",
        proName: "Band 3",
        proPrice: "199",
        proComm: "7.2"
      }
    ];

  // 添加到购物车的函数
  const addToCart = (product) => {
    const existingItem = cartItems.find(item => item.imgUrl === product.imgUrl);
    
    if (existingItem) {
      setCartItems(cartItems.map(item =>
        item.imgUrl === product.imgUrl
          ? { 
              ...item, 
              quantity: item.quantity + 1,
              subtotal: parseFloat(item.proPrice) * (item.quantity + 1)
            }
          : item
      ));
    } else {
      const newItem = {
        ...product,
        quantity: 1,
        selected: false,
        subtotal: parseFloat(product.proPrice)
      };
      setCartItems([...cartItems, newItem]);
    }
  };

  // 更新商品数量
  const updateQuantity = (index, delta) => {
    setCartItems(cartItems.map((item, i) => {
      if (i === index) {
        const newQuantity = Math.max(1, item.quantity + delta);
        return {
          ...item,
          quantity: newQuantity,
          subtotal: parseFloat(item.proPrice) * newQuantity
        };
      }
      return item;
    }));
  };

  // 选择/取消选择商品
  const toggleSelect = (index) => {
    setCartItems(cartItems.map((item, i) => {
      if (i === index) {
        return { ...item, selected: !item.selected };
      }
      return item;
    }));
  };

  // 全选/取消全选
  const toggleSelectAll = () => {
    const allSelected = cartItems.every(item => item.selected);
    setCartItems(cartItems.map(item => ({
      ...item,
      selected: !allSelected
    })));
  };

  // 删除商品
  const removeFromCart = (index) => {
    if (window.confirm("确定要删除这个商品吗？")) {
      setCartItems(cartItems.filter((_, i) => i !== index));
    }
  };

  // 计算总价
  useEffect(() => {
    const newTotal = cartItems
      .filter(item => item.selected)
      .reduce((sum, item) => sum + item.subtotal, 0);
    setTotal(newTotal);
  }, [cartItems]);

  return (
    <div>
      {/* Header */}
      <div className="header">
        <div className="container">
          <div className="header-logo">
            <a className="logo ir" href="/" title="shop">SHOP</a>
          </div>
          <div className="header-title">
            <h2 style={{ fontSize: "30px" }}>My Shopping Cart</h2>
          </div>
          <div className="topbar-info">
            <a className="link" href="/login">Login</a>
            <span className="sep">|</span>
            <a className="link" href="/register">Register</a>
          </div>
        </div>
      </div>

      {/* Cart */}
      <div id="car" className="car">
        <div className="head_row hid">
          <div className="check left">
            <i 
              className={`i_check ${cartItems.length > 0 && cartItems.every(item => item.selected) ? 'i_acity2' : ''}`}
              onClick={toggleSelectAll}
            >
              √
            </i>
          </div>
          <div className="img left">Select All</div>
          <div className="name left">Product Name</div>
          <div className="price left">Price</div>
          <div className="number left">Quantity</div>
          <div className="subtotal left">Subtotal</div>
          <div className="ctrl left">Action</div>
        </div>

        {/* Cart Items */}
        {cartItems.map((item, index) => (
          <div key={index} className="row hid">
            <div className="check left">
              <i 
                className={`i_check ${item.selected ? 'i_acity' : ''}`}
                onClick={() => toggleSelect(index)}
              >
                √
              </i>
            </div>
            <div className="img left">
              <img src={item.imgUrl} alt={item.proName} width="80" height="80" />
            </div>
            <div className="name left">
              <span>{item.proName}</span>
            </div>
            <div className="price left">
              <span>RM {item.proPrice}</span>
            </div>
            <div className="item_count_i">
              <div className="num_count">
                <div className="count_d" onClick={() => updateQuantity(index, -1)}>-</div>
                <div className="c_num">{item.quantity}</div>
                <div className="count_i" onClick={() => updateQuantity(index, 1)}>+</div>
              </div>
            </div>
            <div className="subtotal left">
              <span>RM {item.subtotal.toFixed(2)}</span>
            </div>
            <div className="ctrl left">
              <a onClick={() => removeFromCart(index)}>×</a>
            </div>
          </div>
        ))}
      </div>

      {/* Total Area */}
      <div id="sum_area">
        <div id="pay">Checkout</div>
        <div id="pay_amout">
          Total: RM <span id="price_num">{total.toFixed(2)}</span>
        </div>
      </div>

      {/* Recommended Products */}
      <div id="box">
        <h2 className="box_head">
          <span>Recommended Products</span>
        </h2>
        <ul className="product-list">
          {productData.map((product, index) => (
            <li key={index}>
              <div className="pro_img">
                <img src={product.imgUrl} alt={product.proName} width="150" height="150" />
              </div>
              <h3 className="pro_name">
                <a href="#">{product.proName}</a>
              </h3>
              <p className="pro_price">RM {product.proPrice}</p>
              <p className="pro_rank">{product.proComm} Reviews</p>
              <div 
                className="add_btn"
                onClick={() => addToCart(product)}
              >
                Add to Cart
              </div>
            </li>
          ))}
        </ul>
      </div>
    </div>
  );
};

export default ShoppingCart;