import React, { useEffect, useState } from 'react';
import { useParams, Link } from 'react-router-dom';
import Header from '../components/Header';
import itemData from '../data/item.json';
import '../styles/ProductDetail.css';
import { useCart } from '../context/CartContext';

const ProductDetail = () => {
  const { id } = useParams();
  const [product, setProduct] = useState(null);
  const [relatedProducts, setRelatedProducts] = useState([]);
  const { addToCart } = useCart();

  useEffect(() => {
    // 从JSON文件中查找对应ID的商品
    const currentProduct = itemData.find(item => item.item_id === parseInt(id));
    if (currentProduct) {
      setProduct(currentProduct);
      
      // 获取同类型的相关商品
      const related = itemData
        .filter(item => 
          item.item_type === currentProduct.item_type && 
          item.item_id !== currentProduct.item_id
        )
        .slice(0, 4); // 最多显示4个相关商品
      setRelatedProducts(related);
    }
  }, [id]);

  if (!product) {
    return <div>Loading...</div>;
  }

  const handleAddToCart = () => {
    addToCart(product);
    alert('Product added to cart successfully!');
  };

  return (
    <div>
      <Header />
      <div className="product-detail">
        <div className="product-main">
          <div className="product-gallery">
            <img src={`/images/${product.image_id}`} alt={product.name} />
          </div>
          
          <div className="product-info">
            <h1>{product.name}</h1>
            
            <div className="price-section">
              <div className="price-wrapper">
                <span className="currency">RM</span>
                <span className="price">{product.price}</span>
              </div>
            </div>
            
            <div className="product-meta">
              <div className="meta-item">
                <span className="meta-label">Product ID:</span>
                <span>{product.item_id}</span>
              </div>
              <div className="meta-item">
                <span className="meta-label">Category:</span>
                <span>{product.item_type}</span>
              </div>
            </div>

            <button className="add-to-cart" onClick={handleAddToCart}>Add to Cart</button>
          </div>
        </div>

        <div className="description-container">
          <h2>Product Description</h2>
          <div className="description-content">
            <p>{product.description}</p>
          </div>
        </div>

        <div className="related-products">
          <h2>Related Products</h2>
          <div className="related-grid">
            {relatedProducts.map(item => (
              <Link to={`/product/${item.item_id}`} key={item.item_id} className="related-item">
                <div className="related-image">
                  <img src={`/images/${item.image_id}`} alt={item.name} />
                </div>
                <div className="related-info">
                  <h3>{item.name}</h3>
                  <p className="price">
                    <span className="currency">RM</span>
                    {item.price}
                  </p>
                </div>
              </Link>
            ))}
          </div>
        </div>
      </div>
    </div>
  );
};

export default ProductDetail; 