import React, { useState } from 'react';
import './Checkout.css';

const Checkout = ({ cartItems, total, onBack, onComplete }) => {
  const [paymentMethod, setPaymentMethod] = useState('credit');
  const [formData, setFormData] = useState({
    name: '',
    email: '',
    phone: '',
    address: '',
    cardNumber: '',
    expiryDate: '',
    cvv: ''
  });

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    // 这里可以添加表单验证逻辑
    onComplete(formData);
  };

  return (
    <div className="checkout-container">
      <div className="checkout-header">
        <button className="back-button" onClick={onBack}>← Back to Cart</button>
        <h1>Checkout</h1>
      </div>

      <div className="checkout-content">
        <div className="order-summary">
          <h2>Order Summary</h2>
          <div className="order-items">
            {cartItems.map((item, index) => (
              <div key={index} className="order-item">
                <img src={item.imgUrl} alt={item.proName} />
                <div className="item-details">
                  <h3>{item.proName}</h3>
                  <p>Quantity: {item.quantity}</p>
                  <p>Price: RM {item.proPrice}</p>
                </div>
                <div className="item-total">
                  RM {(item.quantity * parseFloat(item.proPrice)).toFixed(2)}
                </div>
              </div>
            ))}
          </div>
          <div className="order-total">
            <div className="subtotal">
              <span>Subtotal:</span>
              <span>RM {total.toFixed(2)}</span>
            </div>
            <div className="shipping">
              <span>Shipping:</span>
              <span>RM 10.00</span>
            </div>
            <div className="total">
              <span>Total:</span>
              <span>RM {(total + 10).toFixed(2)}</span>
            </div>
          </div>
        </div>

        <form className="checkout-form" onSubmit={handleSubmit}>
          <div className="form-section">
            <h2>Shipping Information</h2>
            <div className="form-group">
              <label>Full Name</label>
              <input
                type="text"
                name="name"
                value={formData.name}
                onChange={handleInputChange}
                required
              />
            </div>
            <div className="form-group">
              <label>Email</label>
              <input
                type="email"
                name="email"
                value={formData.email}
                onChange={handleInputChange}
                required
              />
            </div>
            <div className="form-group">
              <label>Phone Number</label>
              <input
                type="tel"
                name="phone"
                value={formData.phone}
                onChange={handleInputChange}
                required
              />
            </div>
            <div className="form-group">
              <label>Shipping Address</label>
              <textarea
                name="address"
                value={formData.address}
                onChange={handleInputChange}
                required
              />
            </div>
          </div>

          <div className="form-section">
            <h2>Payment Method</h2>
            <div className="payment-methods">
              <label className={`payment-method ${paymentMethod === 'credit' ? 'selected' : ''}`}>
                <input
                  type="radio"
                  name="paymentMethod"
                  value="credit"
                  checked={paymentMethod === 'credit'}
                  onChange={(e) => setPaymentMethod(e.target.value)}
                />
                <span className="method-icon">💳</span>
                <span>Credit Card</span>
              </label>
              <label className={`payment-method ${paymentMethod === 'paypal' ? 'selected' : ''}`}>
                <input
                  type="radio"
                  name="paymentMethod"
                  value="paypal"
                  checked={paymentMethod === 'paypal'}
                  onChange={(e) => setPaymentMethod(e.target.value)}
                />
                <span className="method-icon">🅿️</span>
                <span>PayPal</span>
              </label>
            </div>

            {paymentMethod === 'credit' && (
              <div className="credit-card-details">
                <div className="form-group">
                  <label>Card Number</label>
                  <input
                    type="text"
                    name="cardNumber"
                    value={formData.cardNumber}
                    onChange={handleInputChange}
                    placeholder="1234 5678 9012 3456"
                    required
                  />
                </div>
                <div className="form-row">
                  <div className="form-group">
                    <label>Expiry Date</label>
                    <input
                      type="text"
                      name="expiryDate"
                      value={formData.expiryDate}
                      onChange={handleInputChange}
                      placeholder="MM/YY"
                      required
                    />
                  </div>
                  <div className="form-group">
                    <label>CVV</label>
                    <input
                      type="text"
                      name="cvv"
                      value={formData.cvv}
                      onChange={handleInputChange}
                      placeholder="123"
                      required
                    />
                  </div>
                </div>
              </div>
            )}
          </div>

          <button type="submit" className="place-order-btn">
            Place Order - RM {(total + 10).toFixed(2)}
          </button>
        </form>
      </div>
    </div>
  );
};

export default Checkout;