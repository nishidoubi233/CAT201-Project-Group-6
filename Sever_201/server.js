const express = require("express");
const mysql = require("mysql2");
const bodyParser = require("body-parser");
const cors = require("cors");
const bcrypt = require("bcrypt");

const app = express();
const PORT = process.env.PORT || 3019;

// Middleware configuration
app.use(bodyParser.json());
app.use(
  cors({
    origin: "http://localhost:8080", // Replace with your frontend address
    methods: ["GET", "POST"],
    allowedHeaders: ["Content-Type"],
  })
);

// Database connection configuration
const db = mysql.createConnection({
  host: "xxxxxxxx",// 这里是IP地址
  user: "ROOT",
  password: "xxxxxxxx", //这里是密码
  database: "app_users", 
  port: 3306
});

// Connect to the database
db.connect((err) => {
  if (err) {
    console.error("Failed to connect to the database:", err.message);
    process.exit(1);
  }
  console.log("Successfully connected to the database.");
});

// General database query function
const queryDatabase = (query, params = []) =>
  new Promise((resolve, reject) => {
    db.query(query, params, (err, results) => {
      if (err) {
        console.error("Database query failed:", err.message);
        reject(err);
      } else {
        resolve(results);
      }
    });
  });

// Utility function: Validate request body fields
const validateFields = (fields, requiredFields) => {
  for (const field of requiredFields) {
    if (!fields[field] || fields[field].toString().trim() === "") {
      return `Field "${field}" is required and cannot be empty.`;
    }
  }
  return null;
};

// Utility function: Log errors with additional context
const logError = (message, error) => {
  console.error(`${message}:`, error.message || error);
};

// Login route
app.post("/login", async (req, res) => {
  try {
    console.log("Login request received:", req.body);

    const { username, password } = req.body;

    // Validate input
    const validationError = validateFields(req.body, ["username", "password"]);
    if (validationError) {
      return res.status(400).json({ message: validationError });
    }

    const userQuery = "SELECT * FROM users WHERE username = ?";
    const users = await queryDatabase(userQuery, [username]);

    if (users.length === 0) {
      return res.status(404).json({ message: "User does not exist!" });
    }

    const user = users[0];
    const isPasswordValid = await bcrypt.compare(password, user.password);
    if (!isPasswordValid) {
      return res.status(400).json({ message: "Incorrect password!" });
    }

    res.status(200).json({
      message: "Login successful!",
      user: { id: user.id, username: user.username, email: user.email },
    });
  } catch (error) {
    logError("Error occurred during login", error);
    res.status(500).json({ message: "Internal server error." });
  }
});

// Register route
app.post("/register", async (req, res) => {
  try {
    console.log("Register request received:", req.body);

    const { username, password, email } = req.body;

    // Validate input
    const validationError = validateFields(req.body, ["username", "password", "email"]);
    if (validationError) {
      return res.status(400).json({ message: validationError });
    }

    const checkQuery = "SELECT * FROM users WHERE username = ?";
    const existingUsers = await queryDatabase(checkQuery, [username]);

    if (existingUsers.length > 0) {
      return res.status(409).json({ message: "Username already exists!" });
    }

    const hashedPassword = await bcrypt.hash(password, 10);
    const insertQuery = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
    const result = await queryDatabase(insertQuery, [username, hashedPassword, email]);

    res.status(201).json({
      message: "Registration successful!",
      user: { id: result.insertId, username, email },
    });
  } catch (error) {
    logError("Error occurred during registration", error);
    res.status(500).json({ message: "Internal server error." });
  }
});

// Save user health info route
app.post("/userHealthInfo", async (req, res) => {
  try {
    console.log("Health info save request received:", req.body);

    const { username, ...healthData } = req.body;

    // Validate input
    const validationError = validateFields(req.body, ["username"]);
    if (validationError) {
      return res.status(400).json({ message: validationError });
    }

    const userQuery = "SELECT id FROM users WHERE username = ?";
    const users = await queryDatabase(userQuery, [username]);

    if (users.length === 0) {
      return res.status(404).json({ message: "User not found." });
    }

    const userId = users[0].id;
    const fields = Object.entries(healthData).filter(([key, value]) => value !== null);

    if (fields.length === 0) {
      return res.status(400).json({ message: "No health data provided to save." });
    }

    const insertQuery = `
      INSERT INTO user_data (user_id, field_key, field_value)
      VALUES (?, ?, ?)
      ON DUPLICATE KEY UPDATE field_value = VALUES(field_value)
    `;

    for (const [fieldKey, fieldValue] of fields) {
      console.log(`Saving field: ${fieldKey} = ${fieldValue}`);
      await queryDatabase(insertQuery, [userId, fieldKey, fieldValue]);
    }

    res.status(200).json({ message: "Health information updated successfully." });
  } catch (error) {
    logError("Error occurred while updating health info", error);
    res.status(500).json({ message: "Internal server error." });
  }
});

// Get full user info and health data route
app.get("/userHealthInfo", async (req, res) => {
  try {
    const { username } = req.query;

    // Validate input
    const validationError = validateFields(req.query, ["username"]);
    if (validationError) {
      return res.status(400).json({ message: validationError });
    }

    const userQuery = "SELECT id, username, email FROM users WHERE username = ?";
    const users = await queryDatabase(userQuery, [username]);

    if (users.length === 0) {
      return res.status(404).json({ message: "User not found." });
    }

    const user = users[0];

    const healthQuery = "SELECT field_key, field_value FROM user_data WHERE user_id = ?";
    const healthResults = await queryDatabase(healthQuery, [user.id]);

    const healthInfo = {};
    healthResults.forEach((item) => {
      healthInfo[item.field_key] = item.field_value;
    });

    // Ensure healthData is always an object, even if empty
    res.status(200).json({
      id: user.id,
      username: user.username,
      email: user.email,
      healthData: healthInfo || {}, // Ensure we return an empty object if no data
    });
  } catch (error) {
    logError("Error occurred while fetching full user info", error);
    res.status(500).json({ message: "Internal server error." });
  }
});

// New route to get all health data
app.get("/allHealthInfo", async (req, res) => {
  try {
    // Query to fetch all users' health data
    const query = `
      SELECT u.username, ud.field_key, ud.field_value
      FROM users u
      JOIN user_data ud ON u.id = ud.user_id
    `;
    
    const healthData = await queryDatabase(query);

    if (healthData.length === 0) {
      return res.status(404).json({ message: "No health data found." });
    }

    // Organize data by username
    const result = [];
    healthData.forEach(item => {
      let user = result.find(u => u.username === item.username);
      if (!user) {
        user = { username: item.username };
        result.push(user);
      }
      user[item.field_key] = item.field_value;
    });

    res.status(200).json(result);
  } catch (error) {
    logError("Error fetching all health data:", error);
    res.status(500).json({ message: "Internal server error." });
  }
});

// Handle undefined routes
app.use((req, res) => {
  res.status(404).json({ message: "Path not found." });
});

// Start the server
app.listen(PORT, () => {
  console.log(`Server is running on port ${PORT}`);
});
