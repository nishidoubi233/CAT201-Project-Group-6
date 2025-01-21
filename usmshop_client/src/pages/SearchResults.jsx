import React, { useEffect, useState } from 'react';
import { useSearchParams, Link } from 'react-router-dom';
import itemData from '../data/item.json';
import '../styles/SearchResults.css';
import Header from '../components/Header';
import Footer from '../components/Footer';

const SearchResults = () => {
  const [searchParams] = useSearchParams();
  const [results, setResults] = useState([]);
  
  useEffect(() => {
    const query = searchParams.get('q');
    if (query) {
      // Filter items by name containing the search query
      const filteredItems = itemData.filter(item => 
        item.name.toLowerCase().includes(query.toLowerCase())
      );
      setResults(filteredItems);
    }
  }, [searchParams]);

  return (
    <div className="page-container">
      <Header />
      <div className="search-results">
        <h2>Search Results: {searchParams.get('q')}</h2>
        {results.length === 0 ? (
          <p>No items found</p>
        ) : (
          <div className="results-grid">
            {results.map(item => (
              <Link to={`/product/${item.item_id}`} key={item.item_id} className="product-card">
                <img 
                  src={`/images/${item.image_id}`} 
                  alt={item.name}
                  onError={(e) => {
                    e.target.onerror = null;
                    e.target.src = '/images/placeholder.jpg';
                  }}
                />
                <h3>{item.name}</h3>
                <p className="price">${item.price.toFixed(2)}</p>
              </Link>
            ))}
          </div>
        )}
      </div>
      <Footer />
    </div>
  );
};

export default SearchResults; 