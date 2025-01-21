import React from 'react';
import { Link } from 'react-router-dom';
import '../styles/HomePage.css';
import itemData from '../data/item.json';
import Header from '../components/Header';
import Footer from '../components/Footer';

const HomePage = () => {
  const categories = [
    { id: 1, name: "Hard Drives", icon: "💽", path: "hard-drives" },
    { id: 2, name: "Headphones", icon: "🎧", path: "headphones" },
    { id: 3, name: "Keyboards", icon: "⌨️", path: "keyboards" },
    { id: 4, name: "Monitors", icon: "🖥️", path: "monitors" },
    { id: 5, name: "Mouse", icon: "🖱️", path: "mouse" },
    { id: 6, name: "Speakers", icon: "🔊", path: "speakers" }
  ];

  // Pre-selected discounted product IDs - using these three specific products
  const discountItemIds = [78451, 59234, 94627];  // 这三个ID对应：
  // 78451 - Wireless Ergonomic Earbuds
  // 59234 - RGB Mechanical Gaming Keyboard
  // 94627 - Classic Bookshelf Speaker

  
  // other item IDs
  const recommendItemIds = [
    48293, 74638, 58247, 91457, 83562,  // Hard Drives
    63928, 52713, 46219, 31567,         // Headphones
    74829, 83627, 92547, 47392, 58641,  // Keyboards
    76154, 83492, 94276, 54862, 67284, 39751,  // Monitors
    58239, 61345, 47192, 38562, 52984, 64721,  // Mouse
    78349, 49283, 68327, 76431, 85329,         // Speakers
  ]  // use other headphones, keyboards and speakers

  // Get random recommended product IDs
  const getRandomRecommendItemIds = () => {
    const randomIds = [];
    const availableIds = [...recommendItemIds]; // create a copy to avoid modifying the original array
    
    while (randomIds.length < 4 && availableIds.length > 0) {
      const randomIndex = Math.floor(Math.random() * availableIds.length);
      randomIds.push(availableIds[randomIndex]);
      availableIds.splice(randomIndex, 1); // remove the selected ID
    }
    return randomIds;
  };

  // Get discounted product information
  const discountItems = discountItemIds
    .map(id => itemData.find(item => item.item_id === id))
    .filter(Boolean)
    .map(item => ({
      ...item,
      discountPrice: Math.round(item.price * 0.75 * 100) / 100
    }));

  // Get recommended product information - using randomly generated IDs
  const recommendItems = getRandomRecommendItemIds()  // call function to get random ID
    .map(id => itemData.find(item => item.item_id === id))
    .filter(Boolean);

  return (
    <div style={{ display: 'flex', flexDirection: 'column', minHeight: '100vh' }}>
      <Header />
      <div className="home-page">
        <main className="main-content">
          <div className="top-section">
            <div className="categories-sidebar">
              {categories.map(category => (
                <Link 
                  to={`/category/${category.path}`} 
                  key={category.id} 
                  className="category-item"
                >
                  <span className="category-icon">{category.icon}</span>
                  <span className="category-name">{category.name}</span>
                </Link>
              ))}
            </div>

            <div className="discount-section">
              <h2>Special Offers</h2>
              <div className="discount-items">
                {discountItems.map(item => (
                  <Link 
                    to={`/product/${item.item_id}`} 
                    key={item.item_id} 
                    className="discount-item"
                  >
                    <div className="discount-image">
                      <img 
                        src={`/images/${item.image_id}`} 
                        alt={item.name}
                        onError={(e) => {
                          e.target.onerror = null;
                          e.target.src = '/images/placeholder.jpg';
                        }}
                      />
                    </div>
                    <div className="item-info">
                      <h3>{item.name}</h3>
                      <div className="price-info">
                        <span className="current-price">RM{item.discountPrice}</span>
                        <span className="original-price">RM{item.price}</span>
                      </div>
                    </div>
                  </Link>
                ))}
              </div>
            </div>
          </div>

          <div className="recommendation-section">
            <h2>Recommended For You</h2>
            <div className="recommendation-items">
              {recommendItems.map(item => (
                <Link 
                  to={`/product/${item.item_id}`} 
                  key={item.item_id} 
                  className="recommendation-item"
                >
                  <div className="recommendation-image">
                    <img 
                      src={`/images/${item.image_id}`} 
                      alt={item.name}
                      onError={(e) => {
                        e.target.onerror = null;
                        e.target.src = '/images/placeholder.jpg';
                      }}
                    />
                  </div>
                  <div className="item-info">
                    <h3>{item.name}</h3>
                    <div className="price-info">
                      <span className="current-price">RM{item.price}</span>
                    </div>
                  </div>
                </Link>
              ))}
            </div>
          </div>
        </main>
      </div>
      <Footer />
    </div>
  );
};

export default HomePage; 