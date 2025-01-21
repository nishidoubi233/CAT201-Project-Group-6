# CAT201-Project

## Back-end project architecture using JAVA development:

```tree
usmshop_server/
├── src/
│   └── main/
│       ├── java/
│       │   └── usmshop_server/
│       │       ├── controller/    # control layer
│       │       │   ├── Auth_Servlet.java
│       │       │   ├── Cart_Servlet.java
│       │       │   └── User_Servlet.java
│       │       ├── service/       # business logic layer
│       │       │   ├── Auth_Service.java
│       │       │   ├── Cart_Service.java
│       │       │   └── User_Service.java
│       │       ├── dao/          # data access layer
│       │       │   ├── Cart_DAO.java
│       │       │   └── User_DAO.java
│       │       └── model/        # data model layer
│       │           ├── Cart.java
│       │           └── User.java
│       └── webapp/
│           └── WEB-INF/
│               └── web.xml       # Web Application Configuration File
└── pom.xml                      # Maven Configuration File
```

## Front-end project architecture for javasript built with react.js:

```tree
usmshop_client/
├── .gitignore 
├── eslint.config.js 
├── index.html # Main HTML entry point
├── package.json 
├── public/ 
│   └── vite.svg # logo
├── README.md 
└── src/ 
    ├── App.css # Main application styles
    ├── App.jsx # Root React component
    ├── assets/ # Asset files
    │   └── react.svg 
    ├── components/ 
    │   ├── Footer.jsx # Footer component
    │   └── Header.jsx # Header component  
    ├── context/ 
    │   └── CartContext.jsx # Shopping cart context
    ├── data/ 
    │   └── item.json # Product catalog data
    ├── index.css # Global styles
    ├── main.jsx # Application entry point
    ├── pages/ 
    │   ├── CartPage.jsx # Shopping cart page
    │   ├── CategoryPage.jsx # Product category page
    │   ├── CheckoutPage.jsx # Checkout page
    │   ├── HomePage.jsx # Home page
    │   ├── Login.jsx # Login page
    │   ├── ProductDetail.jsx # Product details page
    │   ├── Register.jsx # Registration page
    │   ├── SearchResults.jsx # Search results page
    │   └── UserProfile.jsx # User profile page
    └── styles/ # Component styles
```
