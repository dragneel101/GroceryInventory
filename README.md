# Grocery Inventory System

A grocery store inventory management web application rebuilt from a JavaFX 8 desktop app (originally created in NetBeans, Summer 2019) into a modern Spring Boot web application.

## Overview

The system supports two user roles:

- **Employee (Admin)** — full CRUD access to inventory, search, and a dashboard with stats
- **Customer** — browse inventory, search items, manage a shopping cart, and checkout

---

## Tech Stack

| Layer | Technology |
|---|---|
| Backend | Java 21, Spring Boot 3.3.5 |
| Web / UI | Thymeleaf, HTML/CSS |
| Security | Spring Security 6 (BCrypt, role-based access) |
| Database | PostgreSQL 16 (via Spring Data JPA) |
| Build | Maven |
| Container | Docker + Docker Compose |

---

## Project Structure

```
GroceryInventory/
├── build/                        # Original JavaFX compiled output (class files + FXML)
│   └── classes/project/          # loginController, adminController, cartController, etc.
└── web/                          # Spring Boot web application
    ├── src/main/java/com/grocery/
    │   ├── GroceryApplication.java
    │   ├── config/
    │   │   └── SecurityConfig.java       # Role-based access rules, BCrypt
    │   ├── controller/
    │   │   ├── HomeController.java       # / → redirects by role
    │   │   ├── AdminController.java      # /admin/** (EMPLOYEE only)
    │   │   └── CartController.java       # /cart/** (CUSTOMER only)
    │   ├── model/
    │   │   ├── Item.java                 # Inventory item entity
    │   │   ├── AppUser.java              # User entity (username, password, role)
    │   │   ├── CartItem.java             # Session-scoped cart entry
    │   │   ├── Transaction.java          # Completed purchase record
    │   │   └── TransactionItem.java      # Line item within a transaction
    │   ├── repository/
    │   │   ├── ItemRepository.java
    │   │   ├── AppUserRepository.java
    │   │   └── TransactionRepository.java
    │   └── service/
    │       ├── ItemService.java
    │       └── UserDetailsServiceImpl.java
    ├── src/main/resources/
    │   └── templates/
    │       ├── login.html
    │       ├── admin/
    │       │   ├── inventory.html        # Dashboard + item list
    │       │   ├── item-form.html        # Add / Edit item
    │       │   └── search.html           # Admin search
    │       └── cart/
    │           ├── browse.html           # Customer item browser
    │           ├── cart.html             # Cart view
    │           ├── search.html           # Customer search
    │           └── bill.html             # Post-checkout bill
    ├── Dockerfile
    └── docker-compose.yml
```

---

## Data Model

### Item
| Field | Type | Description |
|---|---|---|
| id | Long | Primary key |
| description | String | Item name |
| itemNumber | String | Store SKU |
| category | String | Product category |
| temperature | Double | Storage temperature requirement |
| location | String | Aisle / shelf location |
| quantity | Integer | Stock count |
| price | Double | Unit price |

---

## Routes

### Admin (requires `ROLE_EMPLOYEE`)
| Method | Path | Action |
|---|---|---|
| GET | `/admin/inventory` | Dashboard — item list, stats, low-stock alerts |
| GET | `/admin/item/new` | New item form |
| POST | `/admin/item/save` | Create item |
| GET | `/admin/item/{id}/edit` | Edit item form |
| POST | `/admin/item/{id}/update` | Update item |
| POST | `/admin/item/{id}/delete` | Delete item |
| GET | `/admin/search` | Search by name, category, or price |

### Customer (requires `ROLE_CUSTOMER`)
| Method | Path | Action |
|---|---|---|
| GET | `/cart/browse` | Browse all items |
| POST | `/cart/add/{itemId}` | Add item to cart |
| POST | `/cart/remove/{index}` | Remove item from cart |
| POST | `/cart/clear` | Empty cart |
| GET | `/cart/view` | View cart + running total |
| POST | `/cart/checkout` | Complete purchase, reduce stock, show bill |
| GET | `/cart/search` | Search by name or price |

---

## Running the App

### Option 1 — Docker Compose (recommended)

```bash
cd web
docker compose up --build
```

The app will be available at `http://localhost:8080`.

### Option 2 — Local (requires Java 21 + PostgreSQL)

Set the following environment variables (or configure `application.properties`):

```
DB_HOST=localhost
DB_PORT=5432
DB_NAME=grocery_db
DB_USER=grocery
DB_PASS=grocery123
```

Then build and run:

```bash
cd web
mvn package -DskipTests
java -jar target/grocery-inventory-1.0.0.jar
```

---

## Security

- Passwords are hashed with **BCrypt**.
- Route access is enforced by Spring Security:
  - `/admin/**` — `ROLE_EMPLOYEE` only
  - `/cart/**` — `ROLE_CUSTOMER` only
  - `/login`, static assets — public
- After login, users are redirected automatically based on their role.

---

## Original JavaFX App

The `build/` directory contains the compiled class files and FXML layouts from the original NetBeans desktop app. Source `.java` files are not in this repository. The original app used a flat file (`FileInput`) for persistence instead of a database.

Screen flow of the original app:
```
login.fxml → main.fxml (role selector)
    ├── admin.fxml      ← Employee inventory management
    │    └── adminFind.fxml
    └── cart.fxml       ← Customer cart
         └── find.fxml
```
