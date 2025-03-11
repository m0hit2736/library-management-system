# library-management-system

1) clone the repository
git clone https://github.com/your-username/library-management-system.git
cd library-management-system

2) build and run the project
    mvn clean install
   
3) Security Features
JWT Authentication: Secure API using JSON Web Token (JWT).
Role-Based Access Control (RBAC):
ADMIN: Can create, update, delete users & manage roles.
USER: Can view books and borrow them.
Password Hashing: Uses BCryptPasswordEncoder for secure password storage.
Token Blacklisting: Logout functionality invalidates tokens.

4) API Documentation
1️⃣ Authentication APIs
Method	Endpoint	Description	Role Required
POST	/api/auth/register	Register a new user	Public
POST	/api/auth/login	Login & get JWT token	Public
POST	/api/auth/logout	Invalidate token	User/Admin

2️⃣ User Management APIs (Admin)
Method	Endpoint	Description	Role Required
GET	/api/users	Get all users	Admin
GET	/api/users/{id}	Get user by ID	Admin
PUT	/api/users/{id}	Update user details	Admin
DELETE	/api/users/{id}	Delete user	Admin

3️⃣ Book APIs
Method	Endpoint	Description	Role Required
GET	/api/books	Get all books	User/Admin
POST	/api/books	Add new book	Admin
PUT	/api/books/{id}	Update book details	Admin
DELETE	/api/books/{id}	Remove book	Admin

5) test coverage report
   at location /target/jacoco-ut/index.html

6) Swagger UI
   http://localhost:9090/swagger-ui.html

