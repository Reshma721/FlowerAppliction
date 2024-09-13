Floral Feissy 🌸
Floral Feissy is a full-stack flower shop application built with Spring Boot on the backend and ReactJS on the frontend. It provides users with an easy-to-use interface to browse and purchase flowers, while administrators can manage the inventory and categories.

Table of Contents

Features
Technologies
Installation
Usage
API Endpoints
Future Enhancements
Contributing
License


Features
User Roles:

Users can browse flower categories and purchase items.
Admins can manage categories, update flower information, and track orders.
Categories Available:

Wedding Bouquet
Bouquet
Flower Pot


Authentication:
JWT-based authentication and role-based access control.
Secure Payments:

Integration with a payment gateway for seamless checkout.
Technologies
Backend:
Spring Boot
MySQL (Database)
Spring Security with JWT
Frontend:
ReactJS
Redux (State management)
Axios (HTTP client)
React-Slick (Carousel for flower images)
Installation
Prerequisites
Node.js (v16.x or higher)
Maven (for Spring Boot backend)
MySQL (for database)
Backend Setup
Clone the repository:

bash
Copy code
git clone https://github.com/your-username/floral-feissy.git
cd floral-feissy/backend
Set up the database in MySQL:

sql
Copy code
CREATE DATABASE floral_feissy;
Update the MySQL database credentials in application.properties.

Build and run the Spring Boot application:

bash
Copy code
mvn clean install
mvn spring-boot:run
Frontend Setup
Navigate to the frontend folder:

bash
Copy code
cd floral-feissy/frontend
Install dependencies:

bash
Copy code
npm install
Start the frontend application:

bash
Copy code
npm start
The frontend will be running on http://localhost:3000.

Usage
Open your browser and go to http://localhost:3000.
Browse through the flower categories and add items to your cart.
Proceed to checkout, authenticate, and complete the purchase.
API Endpoints
Here are some of the key API endpoints:

User Registration: POST /api/auth/register
User Login: POST /api/auth/login
Get All Categories: GET /api/categories
Add a Flower (Admin only): POST /api/flowers
Update Category (Admin only): PUT /api/categories/{id}
Place Order: POST /api/orders
For detailed API documentation, refer to the Swagger documentation hosted at http://localhost:8082/swagger-ui.

Future Enhancements
Search Functionality: Adding search capabilities for specific flowers and categories.
Mobile App: Developing a mobile-friendly version of the application.
Order Tracking: Allow users to track their orders in real time.
