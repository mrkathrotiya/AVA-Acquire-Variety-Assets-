# AVA - Acquire Variety Assets 🏡

## 📌 Overview
AVA is a real estate property management platform that allows sellers to list properties and buyers to express interest in properties. The system includes role-based authentication, property management, and user interactions.

## 🚀 Current Features
- 🏠 Property listing (Land, Shop, Flat, House) 
- 🛡️ Role-based authentication (Buyers, Sellers, Both) ADMIN(In future)
- 🔍 Search & filter properties (Currentlty not implemented)
- 🔼 Asset/Property updation
- ⭐ Interested property list for users
- 🗑️ Property deletion with automatic cleanup
- 📊 Insights and analytics
- And More features will be added!

## 🛠️ Technologies Used
- **Backend:** Java, Spring Boot, Hibernate, JWT Authentication
- **Database:** MySQL
- **Frontend:** (will be builded after backend server)
- **Version Control:** Git, GitHub

## ⚡ Setup & Installation

### 1️⃣ Clone the Repository
Run this, on your IDE's terminal or cmd in you machine, However I'm using Intellij Idea(CE)
git clone https://github.com/mrkathrotiya/AVA-Acquire-Variety-Assets-.git

2️⃣ Configure Database
Update application.properties (or application.yml) with your MySQL credentials: <br />

spring.datasource.url=jdbc:mysql://localhost:3306/avadb(your database schema name) <br />
spring.datasource.username=root <br />
spring.datasource.password=yourpassword <br />

🛡️ Authentication Endpoints
Login: /api/auth/login             :- login user with credentials(email, password) <br />
Register: /api/auth/register       :- register user <br />
JWT Token: Used for securing API endpoints <br />

Other API endpoints:

User: 
GET :- /user/profile               :- Retrieve User profile <br />
PUT :- /user/update                :- Update User <br />
GET :- /profile/get-Interested     :- Retrieve List of Interested property list from user <br />

Property:
POST :- /property/add                                                  :- Add new property (Only seller or seller and buyer authorized) <br />
GET :- /property/{propertyId}                                          :- Retrieve Property by Id <br />
POST :- /property//add-to-interested-list/{propertyId}                 :- Adds property to user's interested list by property Id <br />
DELETE :- /property/delete-from-interested-list/{propertyId}           :- Deletes property from interested list <br />
DELETE :- /property/delete/{property}                                  :- Delete property by Id <br />

--> You can use Postman (API testing tool) to test APIs <br />

📌 You can see more about endpoints on controllers directory in source code <br />

🤝 Contribution Guidelines <br />
Fork the repo 🍴 <br />
Create a feature branch (git checkout -b feature-name) <br />
Commit your changes (git commit -m "Added new feature") <br />
Push to your branch (git push origin feature-name) <br />
Create a Pull Request (PR) <br />

You can showcase your skill by optimizing authentication by, <br />
          - Email verification <br />
          - Phone number verification <br />
          - Create method for Edit Credentials like email, password, phone number <br />
          - Register with Google account <br />
          - Optimizing Controller's or service's method <br />
          - Optimizing Relationships between Entities <br />

NOTE:- Make sure you prints a Logs at every action you take, it will save your vast amount of time when error or bug occurs.
      - You can see example of logs in every Service's methods like add property, get property.
