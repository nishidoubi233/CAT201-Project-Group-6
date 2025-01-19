import React, { useState, useEffect } from 'react';
import './App.css';

// 初始商品数据
const initialProducts = [
  {
    id: 1,
    name: '雷神电竞 17.1英寸 2025',
    price: 5599,
    quantity: 1,
    image: 'https://via.placeholder.com/150?text=Product+1',
  },
  {
    id: 2,
    name: '雷神 电竞 16英寸 2025',
    price: 7469,
    quantity: 1,
    image: 'https://via.placeholder.com/150?text=Product+2',
  },
  {
    id: 3,
    name: '雷神 ThunderRobot KGB308',
    price: 149,
    quantity: 1,
    image: 'https://via.placeholder.com/150?text=Product+3',
  },
  {
    id: 4,
    name: '雷神 ThunderRobot KGB309',
    price: 104,
    quantity: 1,
    image: 'https://via.placeholder.com/150?text=Product+4',
  },
];

const recommendedProducts = [
  {
    id: 5,
    name: '雷神 机械键盘',
    price: 399,
    image: 'https://via.placeholder.com/150?text=Product+5',
  },
  {
    id: 6,
    name: '雷神 高级耳机',
    price: 599,
    image: 'https://via.placeholder.com/150?text=Product+6',
  },
  {
    id: 7,
    name: '雷神 鼠标',
    price: 99,
    image: 'https://via.placeholder.com/150?text=Product+7',
  },
];

function App() {
  const [cartItems, setCartItems] = useState(initialProducts);
  const [totalPrice, setTotalPrice] = useState(0);

  // 更新购物车总价
  useEffect(() => {
    const total = cartItems.reduce((total, item) => total + item.price * item.quantity, 0);
    setTotalPrice(total);
  }, [cartItems]);

  // 增加商品数量
  const increaseQuantity = (id) => {
    setCartItems(cartItems.map(item =>
      item.id === id ? { ...item, quantity: item.quantity + 1 } : item
    ));
  };

  // 减少商品数量
  const decreaseQuantity = (id) => {
    setCartItems(cartItems.map(item =>
      item.id === id && item.quantity > 1
        ? { ...item, quantity: item.quantity - 1 }
        : item
    ));
  };

  // 删除商品
  const removeItem = (id) => {
    setCartItems(cartItems.filter(item => item.id !== id));
  };

  // 选择商品
  const toggleSelectItem = (id) => {
    setCartItems(cartItems.map(item =>
      item.id === id ? { ...item, selected: !item.selected } : item
    ));
  };

  return (
    <div className="App">
      {/* 黄色横条 */}
      <div className="header">
        <span>购物车</span>
        <input type="text" placeholder="搜索" />
        <button>搜索</button>
      </div>

      <h1>购物车</h1>

      {/* 购物车商品列表 */}
      <div className="cart">
        {cartItems.length === 0 ? (
          <p>购物车为空</p>
        ) : (
          cartItems.map(item => (
            <div key={item.id} className="cart-item">
              <input
                type="checkbox"
                checked={item.selected || false}
                onChange={() => toggleSelectItem(item.id)}
              />
              <img src={item.image} alt={item.name} />
              <div className="cart-item-info">
                <h3>{item.name}</h3>
                <p>￥{item.price}</p>
                <div className="quantity-controls">
                  <button onClick={() => decreaseQuantity(item.id)}>-</button>
                  <span>{item.quantity}</span>
                  <button onClick={() => increaseQuantity(item.id)}>+</button>
                </div>
                <button className="remove-btn" onClick={() => removeItem(item.id)}>删除</button>
              </div>
            </div>
          ))
        )}
      </div>

      {/* 总价和结算 */}
      <div className="cart-summary">
        <h3>总价: ￥{totalPrice}</h3>
        <button className="checkout-btn">去结算</button>
      </div>

      {/* 推荐商品部分 */}
      <div className="recommended-products">
        <h2>为你推荐</h2>
        <div className="recommended-list">
          {recommendedProducts.map(product => (
            <div key={product.id} className="recommended-item">
              <img src={product.image} alt={product.name} />
              <div className="recommended-item-info">
                <h3>{product.name}</h3>
                <p>￥{product.price}</p>
                <button className="add-to-cart-btn" onClick={() => addToCart(product)}>加入购物车</button>
              </div>
            </div>
          ))}
        </div>
      </div>
    </div>
  );

  // 添加推荐商品到购物车
  function addToCart(product) {
    const updatedItems = [...cartItems];
    const existingItem = updatedItems.find(item => item.id === product.id);

    if (existingItem) {
      existingItem.quantity += 1;
    } else {
      updatedItems.push({ ...product, quantity: 1 });
    }

    setCartItems(updatedItems);
  }
}

export default App;
