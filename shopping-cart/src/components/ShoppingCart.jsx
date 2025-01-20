import React, { useState, useEffect } from 'react';
import './ShoppingCart.css';

const ShoppingCart = () => {
  const [cartItems, setCartItems] = useState([]);
  const [total, setTotal] = useState(0);

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

  useEffect(() => {
    initializeProducts();
  }, []);

  const initializeProducts = () => {
    const oBox = document.getElementById("box");
    const oUl = oBox.getElementsByTagName("ul")[0];

    productData.forEach(data => {
      const oLi = document.createElement("li");
      oLi.innerHTML = `
        <div class="pro_img">
          <img src="${data.imgUrl}" width="150" height="150">
        </div>
        <h3 class="pro_name">
          <a href="#">${data.proName}</a>
        </h3>
        <p class="pro_price">${data.proPrice}RM</p>
        <p class="pro_rank">${data.proComm} Reviews</p>
        <div class="add_btn">Add to Cart</div>
      `;
      oUl.appendChild(oLi);
    });

    bindEvents();
  };

  const bindEvents = () => {
    const aBtn = document.querySelectorAll(".add_btn");
    aBtn.forEach((btn, index) => {
      btn.onclick = () => addToCart(productData[index]);
    });
  };

  const addToCart = (data) => {
    const oCar = document.getElementById("car");
    const oDiv = document.createElement("div");
    oDiv.className = "row hid";
    oDiv.innerHTML = `
      <div class="check left">
        <i class="i_check" onclick="checkItem(this)">√</i>
      </div>
      <div class="img left">
        <img src="${data.imgUrl}" width="80" height="80">
      </div>
      <div class="name left">
        <span>${data.proName}</span>
      </div>
      <div class="price left">
        <span>${data.proPrice}RM</span>
      </div>
      <div class="item_count_i">
        <div class="num_count">
          <div class="count_d">-</div>
          <div class="c_num">1</div>
          <div class="count_i">+</div>
        </div>
      </div>
      <div class="subtotal left">
        <span>${data.proPrice}RM</span>
      </div>
      <div class="ctrl left">
        <a href="javascript:;">×</a>
      </div>
    `;

    oCar.appendChild(oDiv);
    bindCartItemEvents(oDiv, data.proPrice);
    updateTotal();
  };

  const bindCartItemEvents = (oDiv, price) => {
    const minus = oDiv.querySelector(".count_d");
    const plus = oDiv.querySelector(".count_i");
    const num = oDiv.querySelector(".c_num");
    const subtotal = oDiv.querySelector(".subtotal span");
    const del = oDiv.querySelector(".ctrl a");

    minus.onclick = () => {
      let count = parseInt(num.textContent);
      if (count > 1) {
        count--;
        num.textContent = count;
        subtotal.textContent = `${(price * count).toFixed(2)}RM`;
        updateTotal();
      }
    };

    plus.onclick = () => {
      let count = parseInt(num.textContent);
      count++;
      num.textContent = count;
      subtotal.textContent = `${(price * count).toFixed(2)}RM`;
      updateTotal();
    };

    del.onclick = () => {
      if (window.confirm("Are you sure you want to delete?")) {
        oDiv.remove();
        updateTotal();
      }
    };
  };

  const updateTotal = () => {
    const checks = document.querySelectorAll(".i_check.i_acity");
    let total = 0;
    checks.forEach(check => {
      const row = check.closest(".row");
      const subtotal = row.querySelector(".subtotal span");
      total += parseFloat(subtotal.textContent);
    });
    setTotal(total);
  };

  const checkAll = () => {
    const checks = document.querySelectorAll(".i_check");
    const isChecked = !checks[0].classList.contains("i_acity2");
    
    checks.forEach(check => {
      if (isChecked) {
        check.classList.add("i_acity");
        checks[0].classList.add("i_acity2");
      } else {
        check.classList.remove("i_acity");
        checks[0].classList.remove("i_acity2");
      }
    });
    
    updateTotal();
  };

  return (
    <div>
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

      <div id="car" className="car">
        <div className="head_row hid">
          <div className="check left">
            <i onClick={checkAll}>√</i>
          </div>
          <div className="img left">Select All</div>
          <div className="name left">Product Name</div>
          <div className="price left">Price</div>
          <div className="number left">Quantity</div>
          <div className="subtotal left">Subtotal</div>
          <div className="ctrl left">Action</div>
        </div>
      </div>

      <div id="sum_area">
        <div id="pay">Checkout</div>
        <div id="pay_amout">
          Total: RM<span id="price_num">{total.toFixed(2)}</span>
        </div>
      </div>

      <div id="box">
        <h2 className="box_head">
          <span>Recommended Products</span>
        </h2>
        <ul></ul>
      </div>
    </div>
  );
};

export default ShoppingCart;