# AVA - Acquire Variety Assets 🏡

## 📌 Overview
AVA is a real estate property management platform that allows sellers to list properties and buyers to express interest in properties. The system includes role-based authentication, property management, and user interactions.

## 🚀 Features
- 🏠 Property listing (Land, Shop, Flat, House)
- 🛡️ Role-based authentication (Buyers, Sellers, Both) ADMIN(In future)
- 🔍 Search & filter properties (Currentlty not implemented)
- ⭐ Interested property list for users
- 🗑️ Property deletion with automatic cleanup
- 📊 Insights and analytics

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
Update application.properties (or application.yml) with your MySQL credentials:

spring.datasource.url=jdbc:mysql://localhost:3306/avadb(your database schema name)
spring.datasource.username=root
spring.datasource.password=yourpassword

🛡️ Authentication Endpoints
Login: /api/auth/login             :- login user with credentials(email, password)
Register: /api/auth/register       :- register user
JWT Token: Used for securing API endpoints

Other API endpoints:

User:
GET :- /user/profile               :- Retrieve User profile
PUT :- /user/update                :- Update User
GET :- /profile/get-Interested     :- Retrieve List of Interested property list from user

Property:
POST :- /property/add                                                  :- Add new property (Only seller or seller and buyer authorized)
GET :- /property/{propertyId}                                          :- Retrieve Property by Id
POST :- /property//add-to-interested-list/{propertyId}                 :- Adds property to user's interested list by property Id
DELETE :- /property/delete-from-interested-list/{propertyId}           :- Deletes property from interested list
DELETE :- /property/delete/{property}                                  :- Delete property by Id

--> You can use Postman (API testing tool) to test APIs 

📌 You can see more about endpoints on controllers directory in source code

🤝 Contribution Guidelines
Fork the repo 🍴
Create a feature branch (git checkout -b feature-name)
Commit your changes (git commit -m "Added new feature")
Push to your branch (git push origin feature-name)
Create a Pull Request (PR)

You can showcase your skill by optimizing authentication by,
          - Email verification
          - Phone number verification
          - Create method for Edit Credentials like email, password, phone number
          - Register with Google account
          - Optimizing Controller's or service's method
          - Optimizing Relationships between Entities

NOTE:- Make sure you prints a Logs at every action you take, it will save your vast amount of time when error or bug occurs.
      - You can see example of logs in every Service's methods like add property, get property.
