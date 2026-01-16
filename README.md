# E-Cart Application - CQRS Architecture

Complete e-commerce cart application built with modern technologies.

## Architecture Overview

```
                        ┌──────────────────┐
                        │   React Frontend │
                        │   (Port 3000)    │
                        └────────┬─────────┘
                                 │
                        ┌────────▼────────┐
                        │  Nginx Proxy    │
                        │  (Port 3001)    │
                        └────────┬────────┘
                                 │
                ┌────────────────┼────────────────┐
                │                │                │
        ┌───────▼──────┐   ┌──────▼──────┐  ┌───▼──────────┐
        │   Command    │   │    Query    │  │  Kafka Msg  │
        │   Service    │   │   Service   │  │   Broker    │
        │  (Port 8081) │   │  (Port 8082)│  │ (Port 9092) │
        └───────┬──────┘   └──────┬──────┘  └────┬────────┘
                │                │              │
                └────────────────┼──────────────┘
                                 │
                        ┌────────▼────────┐
                        │ PostgreSQL DB   │
                        │  (Port 5432)    │
                        └─────────────────┘
```

## Technology Stack

- **Frontend**: React 18.3.1, React Router, Axios
- **Backend**: Spring Boot 3.4.1, Java 25
- **Database**: PostgreSQL 16
- **Message Broker**: Apache Kafka 7.5.0
- **API Gateway**: Nginx
- **Containerization**: Docker & Docker Compose

## Project Structure

```
react-java-cqrs-cart/
├── frontend/                    # React application
│   ├── public/
│   ├── src/
│   │   ├── pages/              # Page components
│   │   ├── api.js              # API client
│   │   ├── App.js              # Main app
│   │   └── index.js
│   ├── Dockerfile
│   └── package.json
├── backend/
│   ├── shared/                 # Shared DTOs and events
│   │   ├── src/main/java/com/ecart/shared/
│   │   │   ├── dto/
│   │   │   └── events/
│   │   └── pom.xml
│   ├── command-service/        # Write operations
│   │   ├── src/main/java/com/ecart/command/
│   │   │   ├── controller/
│   │   │   ├── entity/
│   │   │   ├── repository/
│   │   │   ├── service/
│   │   │   └── config/
│   │   └── pom.xml
│   └── query-service/          # Read operations
│       ├── src/main/java/com/ecart/query/
│       │   ├── controller/
│       │   ├── model/
│       │   ├── repository/
│       │   ├── service/
│       │   └── config/
│       └── pom.xml
├── database/
│   └── init.sql               # Database initialization
├── nginx/
│   ├── nginx.conf             # Nginx configuration
│   └── Dockerfile
├── docker/
│   ├── Dockerfile.command
│   └── Dockerfile.query
└── docker-compose.yml         # Orchestration
```

## Quick Start

### Prerequisites

- Docker & Docker Compose installed
- Port 80, 3000, 3001, 5432, 8081, 8082, 9092, 2181 available

### Build and Run

```bash
# Navigate to project directory
cd /Users/rameshbabukm/Documents/Projects/react-java-cqrs-cart

# Start all services
docker-compose up -d

# Check if all services are running
docker-compose ps

# View logs (optional)
docker-compose logs -f
```

### Access Application

- **Frontend**: http://localhost:3000 or http://localhost
- **API Gateway**: http://localhost:3001/api
- **Command Service**: http://localhost:8081
- **Query Service**: http://localhost:8082
- **PostgreSQL**: localhost:5432
- **Kafka**: localhost:9092

## Data Models

### Customer
- `id`: Long
- `name`: String
- `email`: String (unique)
- `createdAt`: Long (timestamp)

### Category
- `id`: Long
- `name`: String (unique)
- `description`: String
- `createdAt`: Long

### Product
- `id`: Long
- `name`: String
- `description`: String
- `price`: Double
- `quantity`: Integer
- `categoryId`: Long (FK)
- `createdAt`: Long

### Cart
- `id`: Long
- `customerId`: Long (unique)
- `items`: CartItem[]
- `totalPrice`: Double (calculated)

### Order
- `id`: Long
- `customerId`: Long
- `items`: OrderItem[]
- `totalPrice`: Double
- `status`: String
- `createdAt`: Long

## API Endpoints

### Command Service (Write Operations)

**Customers**
- `POST /api/customers` - Create customer
- `PUT /api/customers/{id}` - Update customer

**Products**
- `POST /api/products` - Create product

**Categories**
- `POST /api/categories` - Create category

**Cart**
- `POST /api/carts/{customerId}/items` - Add item to cart
- `PUT /api/carts/{customerId}/items/{productId}` - Update cart item quantity
- `DELETE /api/carts/{customerId}/items/{productId}` - Remove item from cart

**Orders**
- `POST /api/carts/{customerId}/checkout` - Create order from cart

### Query Service (Read Operations)

**Customers**
- `GET /api/customers` - Get all customers
- `GET /api/customers/{id}` - Get customer by ID

**Products**
- `GET /api/products` - Get all products
- `GET /api/products/{id}` - Get product by ID
- `GET /api/products/category/{categoryId}` - Get products by category

**Categories**
- `GET /api/categories` - Get all categories

**Cart**
- `GET /api/carts/customer/{customerId}` - Get customer's cart

**Orders**
- `GET /api/orders/customer/{customerId}` - Get customer's orders
- `GET /api/orders/{id}` - Get order by ID

## CORS Configuration

Both services have CORS enabled for:
- `http://localhost:3000` (Frontend)
- `http://frontend:3000` (Docker network)

Nginx also handles CORS preflight requests on port 3001.

## Environment Configuration

### PostgreSQL
- Host: postgres
- Port: 5432
- Database: ecart_db
- User: ecart_user
- Password: ecart_password

### Kafka
- Host: kafka
- Port: 9092
- Topics: customer-events, product-events, order-events

### Services
- Command Service: port 8081
- Query Service: port 8082
- Frontend: port 3000
- Nginx: ports 80, 3001

## Features

✅ Customer Management
✅ Product Catalog with Categories
✅ Shopping Cart Management
✅ Order Processing
✅ Kafka Event Streaming
✅ CQRS Architecture
✅ PostgreSQL Persistence
✅ CORS Handling
✅ Nginx Reverse Proxy
✅ Docker Containerization
✅ Java 25 & Spring Boot 3.4.1

## Stopping Services

```bash
# Stop all services
docker-compose down

# Stop and remove volumes
docker-compose down -v
```

## Development Notes

- Command service handles all write operations and publishes events to Kafka
- Query service subscribes to Kafka events for data synchronization
- Frontend stores selected customer ID in localStorage
- All timestamps are in milliseconds (Unix epoch)
- Payment module not implemented (as per requirements)

## Troubleshooting

### CORS Errors
- Ensure Nginx is running on port 3001
- Check browser console for specific errors
- Verify localhost:3000 is allowed in CORS config

### Database Connection Issues
- Check if PostgreSQL container is healthy: `docker-compose ps`
- Verify credentials in environment variables
- Check database initialization: `docker-compose logs postgres`

### Kafka Issues
- Ensure Zookeeper is running first
- Check Kafka topics: `docker exec ecart-kafka kafka-topics --bootstrap-server kafka:9092 --list`
- View Kafka logs: `docker-compose logs kafka`

### Service Build Failures
- Ensure Maven is properly configured
- Check Java version compatibility (Java 25)
- Clear Maven cache: `docker system prune`
