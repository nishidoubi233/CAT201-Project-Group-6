import React, { useEffect, useState } from 'react';
import { useParams, Link } from 'react-router-dom';
import Header from '../components/Header';
import Footer from '../components/Footer';
import itemData from '../data/item.json';
import '../styles/CategoryPage.css';

const CategoryPage = () => {
  const { category } = useParams();
  const [products, setProducts] = useState([]);
  const [categoryName, setCategoryName] = useState('');

  useEffect(() => {
    // Map category names to item_type
    const categoryMap = {
      'hard-drives': 'hard drive',
      'headphones': 'headphone',
      'keyboards': 'keyboard',
      'monitors': 'monitor',
      'mouse': 'mouse',
      'speakers': 'speaker'
    };

    const type = categoryMap[category];
    if (type) {
      const filteredProducts = itemData.filter(item => item.item_type === type);
      setProducts(filteredProducts);
      // Format display name
      const categoryDisplayNames = {
        'hard-drives': 'Hard Drives',
        'headphones': 'Headphones',
        'keyboards': 'Keyboards',
        'monitors': 'Monitors',
        'mouse': 'Mouse',
        'speakers': 'Speakers'
      };
      setCategoryName(categoryDisplayNames[category] || category);
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
    <div>
      <Header />
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
                <p className="price">RM{product.price}</p>
              </div>
            </Link>
          ))}
        </div>
      </div>
      <Footer />
    </div>
  );
};

export default CategoryPage; 