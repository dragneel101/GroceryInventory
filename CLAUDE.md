# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Important: Source Files Not Committed

The Java source (`.java`) files are **not in this repository** — only the compiled `.class` files and FXML UI layouts are present under `build/classes/`. The original project was built in NetBeans (Summer 2019). To make code changes, the source files must be added to the repo first.

## Project Overview

A JavaFX 8 desktop application for grocery store inventory management. Two user roles exist: **Employee** (admin) and **Customer**. Inventory data is persisted to a local file.

## Architecture

### Screen Flow
```
login.fxml (loginController)
    └── main.fxml (mainController)          ← role selector
         ├── admin.fxml (adminController)   ← Employee view
         │    └── adminFind.fxml (adminFind) ← Employee item search
         └── cart.fxml (cartController)     ← Customer view
              └── find.fxml (findController) ← Customer item search
```

### Key Classes (compiled only)
| Class | Purpose |
|-------|---------|
| `Project` | Application entry point |
| `GroceryType` | Data model — item fields: description, item number, temperature, location, quantity, price |
| `FileInput` | Reads/writes inventory data to file |
| `searches` | Search logic shared across find screens |
| `readCart` | Cart state reader |

### Controllers and Their Responsibilities
- **loginController** — authenticates Employee/Customer, routes to `main.fxml`
- **mainController** — landing screen; navigates to admin or cart view
- **adminController** — full CRUD for inventory items (Add, Edit, Save, Delete); left panel is item list, right panel is detail form
- **adminFind** — Employee search by name, category, or price
- **cartController** — customer browsing and checkout; shows item list, cart (description + rate columns), running total, and a bill overlay on "Finish & Pay"
- **findController** — Customer search by name or price; selected item appends to cart

## Building and Running

The original build was done via NetBeans. The `build/` directory contains the compiled output. To rebuild from source (once `.java` files are added):

```bash
# Compile (requires JavaFX 8 on classpath)
javac -cp /path/to/javafx.jar src/project/*.java -d build/classes

# Run
java -cp build/classes:/path/to/javafx.jar project.Project
```

The `.gitignore` excludes `*.class`, `*.jar`, and archive files — build artifacts should not be committed.
