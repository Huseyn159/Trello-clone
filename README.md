# Trello Clone – Backend

This repository contains the **backend implementation** of a Trello-like application.
The backend is **fully functional** and handles all core business logic, persistence, and ordering rules.

The system supports **boards, lists, cards, and drag & drop reordering**, with all changes **persisted in the database**.

---

## 🚀 Features

- User-based workspace structure
- Boards, Lists, and Cards
- Full CRUD operations
- **Drag & Drop support with database persistence**
- Card reordering within the same list
- Card movement between different lists
- List reordering inside boards
- Position recalculation using optimized SQL queries
- RESTful API design
- Validation and error handling

---

## 🧠 Drag & Drop Logic (Important)

- Card and List positions are stored as **1-based indexes** in the database
- When a card or list is moved:
  - Other affected entities are shifted using **bulk UPDATE queries**
  - Ordering consistency is preserved at all times
- Reordering works correctly:
  - Within the same list
  - Across different lists
  - For both upward and downward movements

This logic is fully implemented and tested on the backend level.

---

## 🛠️ Tech Stack

- Java 17
- Spring Boot
- Spring Data JPA (Hibernate)
- PostgreSQL
- REST API
- Validation (Jakarta Validation)
- Gradle

---

## 📦 Database

- Relational database (PostgreSQL)
- Position-based ordering for:
  - Boards
  - Lists
  - Cards
- Optimized JPQL / SQL update queries for reorder operations

---

## 📌 API Status

✔ Backend API is **complete and working**  
✔ Database logic is **fully implemented**  
✔ Drag & drop changes are **persisted correctly**  

⚠️ **Note:**  
Some backend features are not yet fully consumed by the frontend application.

---

## 🧪 Testing

- Manual API testing via Postman
- Reorder edge cases tested (same list / different list)

-----------
SCREENSHOTS
-----------



![Workspace Gif](https://github.com/user-attachments/assets/bce4a6a1-32a0-496b-a9ad-d1f6065c9b45)
![ Board Gif](https://github.com/user-attachments/assets/ceb59ba2-4ae8-49f4-bbe7-7dad4b8d910b)
![List Gif](https://github.com/user-attachments/assets/65589df6-fb12-42ce-9bfd-514fd6183f8b)




