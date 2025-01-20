import React, { useEffect, useState } from 'react';
import { useParams, Link } from 'react-router-dom';
import '../styles/ProductDetail.css';

const ProductDetail = () => {
  const { id } = useParams();
  const [product, setProduct] = useState(null);

  const products = {
    1: {
      name: "精美金属保温杯",
      price: 49.9,
      description: "高级不锈钢材质，24小时保温保冷，时尚外观设计，适合办公室和户外使用。",
      image: "https://via.placeholder.com/600x600?text=Product+1"
    },
    2: {
      name: "复古机械手表",
      price: 69.9,
      description: "经典机械机芯，精钢表带，防水设计，彰显品味的时尚配饰。",
      image: "https://via.placeholder.com/600x600?text=Product+2"
    },
    3: {
      name: "便携式蓝牙音箱",
      price: 39.9,
      description: "无线蓝牙连接，高清音质，续航持久，小巧便携，随时随地享受音乐。",
      image: "https://via.placeholder.com/600x600?text=Product+3"
    },
    4: {
      name: "多功能电子笔记本",
      price: 89.9,
      description: "智能手写识别，云端同步，长续航设计，为现代生活带来更多便利。",
      image: "https://via.placeholder.com/600x600?text=Product+4"
    }
  };

  useEffect(() => {
    if (id && products[id]) {
      setProduct(products[id]);
    }
  }, [id]);

  if (!product) return <div>Loading...</div>;

  return (
    <div>
      <div className="header">
        <div className="header-container">
          <Link to="/" className="logo">Back to Home</Link>
          <div className="user-actions">
            <Link to="/login" className="login-btn">Login</Link>
            <Link to="/cart" className="cart-btn">Cart</Link>
          </div>
        </div>
      </div>

      <div className="product-detail-container">
        <div className="product-detail-left">
          <img src={product.image} alt={product.name} />
        </div>
        <div className="product-detail-right">
          <h2>{product.name}</h2>
          <p>{product.description}</p>
          <div className="price">￥{product.price}</div>
          <div className="action-buttons">
            <button className="cart-btn">加入购物车</button>
            <button className="wishlist-btn">收藏</button>
            <button className="shop-btn">商铺</button>
          </div>
        </div>
      </div>

      <div className="recommendation-container">
        <h3>为你推荐</h3>
        <div className="recommendation-list">
          {Object.entries(products).map(([productId, item]) => (
            <div className="recommendation-item" key={productId}>
              <img src={item.image} alt={item.name} />
              <h4>{item.name}</h4>
              <p className="price">￥{item.price}</p>
              <Link 
                to={`/product/${productId}`} 
                className="view-detail"
              >
                查看详情
              </Link>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
};

export default ProductDetail; 