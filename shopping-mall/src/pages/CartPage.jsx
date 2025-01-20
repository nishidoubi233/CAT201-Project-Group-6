import React from 'react';
import { useNavigate } from 'react-router-dom';
import Header from '../components/Header';
import { useCart } from '../context/CartContext';
import '../styles/CartPage.css';

const CartPage = () => {
  const { cartItems, removeFromCart, updateQuantity } = useCart();
  const navigate = useNavigate();
  
  const calculateTotal = () => {
    return cartItems.reduce((total, item) => total + (item.price * item.quantity), 0);
  };

  return (
    <div>
      <Header />
      <div className="cart-content">
        <div id="car" className="car">
          <div className="head_row hid">
            <div className="img left">Product</div>
            <div className="name left">Product Name</div>
            <div className="price left">Price</div>
            <div className="number left">Quantity</div>
            <div className="subtotal left">Subtotal</div>
            <div className="ctrl left">Action</div>
          </div>

          {cartItems.map(item => (
            <div key={item.item_id} className="row hid">
              <div className="img left">
                <img src={`/images/${item.image_id}`} width="80" height="80" alt={item.name} />
              </div>
              <div className="name left">
                <span>{item.name}</span>
              </div>
              <div className="price left">
                <span>RM{item.price}</span>
              </div>
              <div className="item_count_i">
                <div className="num_count">
                  <div className="count_d" onClick={() => updateQuantity(item.item_id, item.quantity - 1)}>-</div>
                  <div className="c_num">{item.quantity}</div>
                  <div className="count_i" onClick={() => updateQuantity(item.item_id, item.quantity + 1)}>+</div>
                </div>
              </div>
              <div className="subtotal left">
                <span>RM{(item.price * item.quantity).toFixed(2)}</span>
              </div>
              <div className="ctrl left">
                <a href="javascript:;" onClick={() => removeFromCart(item.item_id)}>×</a>
              </div>
            </div>
          ))}
        </div>

        <div id="sum_area">
          <div id="pay" onClick={() => navigate('/checkout')}>Checkout</div>
          <div id="pay_amout">
            Total: RM<span id="price_num">{calculateTotal().toFixed(2)}</span>
          </div>
        </div>
      </div>
    </div>
  );
};

export default CartPage; 