import React, { useEffect, useState } from 'react';
import { useParams, Link } from 'react-router-dom';
import itemData from '../data/item.json';
import '../styles/CategoryPage.css';

const CategoryPage = () => {
  const { category } = useParams();
  const [products, setProducts] = useState([]);
  const [categoryName, setCategoryName] = useState('');

  useEffect(() => {
    // 将类别名称映射到item_type
    const categoryToType = {
      'hard-drives': 'hard drive',
      'headphones': 'headphone',
      'keyboards': 'keyboard',
      'monitors': 'monitor',
      'mice': 'mouse',
      'speakers': 'speaker'
    };

    const type = categoryToType[category];
    if (type) {
      const filteredProducts = itemData.filter(item => item.item_type === type);
      setProducts(filteredProducts);
      // 格式化显示名称
      const displayNames = {
        'hard-drives': 'Hard Drives',
        'headphones': 'Headphones',
        'keyboards': 'Keyboards',
        'monitors': 'Monitors',
        'mice': 'Mice',
        'speakers': 'Speakers'
      };
      setCategoryName(displayNames[category] || category);
    }
  }, [category]);

  if (products.length === 0) {
    return (
      <div className="category-page">
        <div className="category-header">
          <h1>No Products Found</h1>
          <p>Sorry, no products are available in this category.</p>
        </div>
      </div>
    );
  }

  return (
    <div className="category-page">
      <div className="category-header">
        <h1>{categoryName}</h1>
        <p>{products.length} products found</p>
      </div>

      <div className="products-grid">
        {products.map(product => (
          <Link to={`/product/${product.item_id}`} key={product.item_id} className="product-card">
            <div className="product-image">
              <img src={`/images/${product.image_id}`} alt={product.name} />
            </div>
            <div className="product-info">
              <h3>{product.name}</h3>
              <p className="description">{product.description.slice(0, 100)}...</p>
              <p className="price">${product.price}</p>
            </div>
          </Link>
        ))}
      </div>
    </div>
  );
};

export default CategoryPage; 