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
