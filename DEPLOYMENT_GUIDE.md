# E-Cart CQRS Application - Complete Build Guide

## Project Summary

A fully functional e-commerce cart application with React frontend, Java CQRS microservices, Kafka event streaming, PostgreSQL database, and Nginx reverse proxy. Ready for Docker deployment.

## What Has Been Built

### ✅ Frontend (React 18.3.1)
- Complete customer management dashboard
- Product catalog with category filtering
- Shopping cart management
- Order history tracking
- CORS-enabled API client
- Responsive UI with modern styling

**Location:** `/Users/rameshbabukm/Documents/Projects/react-java-cqrs-cart/frontend/`

**Key Files:**
- `src/App.js` - Main application component with routing
- `src/api.js` - Axios API client for all endpoints
- `src/pages/Dashboard.js` - Customer management
- `src/pages/Products.js` - Product browsing and cart addition
- `src/pages/Cart.js` - Shopping cart management
- `src/pages/Orders.js` - Order history
- `package.json` - Dependencies
- `Dockerfile` - Multi-stage production build

### ✅ Backend (Java 21 with Spring Boot 3.4.1)

#### Shared Module
- DTOs: CustomerDTO, ProductDTO, CategoryDTO, CartItemDTO
- Events: CustomerCreatedEvent, ProductCreatedEvent, OrderCreatedEvent
- Reusable across command and query services

**Location:** `/Users/rameshbabukm/Documents/Projects/react-java-cqrs-cart/backend/shared/`

#### Command Service (Port 8081)
Handles all write operations using CQRS pattern:
- Customer creation and updates
- Product management
- Cart item management (add, update, remove)
- Order creation from cart
- Publishes events to Kafka

**Location:** `/Users/rameshbabukm/Documents/Projects/react-java-cqrs-cart/backend/command-service/`

**Endpoints:**
- POST /api/customers
- PUT /api/customers/{id}
- POST /api/products
- POST /api/categories
- POST /api/carts/{customerId}/items
- PUT /api/carts/{customerId}/items/{productId}
- DELETE /api/carts/{customerId}/items/{productId}
- POST /api/carts/{customerId}/checkout

#### Query Service (Port 8082)
Handles all read operations optimized for queries:
- Product retrieval with category filtering
- Cart retrieval by customer
- Order history retrieval
- Subscribes to Kafka events for data sync

**Location:** `/Users/rameshbabukm/Documents/Projects/react-java-cqrs-cart/backend/query-service/`

**Endpoints:**
- GET /api/customers
- GET /api/customers/{id}
- GET /api/products
- GET /api/products/{id}
- GET /api/products/category/{categoryId}
- GET /api/categories
- GET /api/carts/customer/{customerId}
- GET /api/orders/customer/{customerId}
- GET /api/orders/{id}

### ✅ Infrastructure

#### PostgreSQL Database
- Schema for customers, categories, products, carts, orders
- Sample data initialization
- Auto-creation of all tables

#### Apache Kafka
- Event streaming for CQRS pattern
- Topics: customer-events, product-events, order-events
- Zookeeper for cluster coordination

#### Nginx Reverse Proxy (Ports 80 & 3001)
- Combines command and query services
- CORS header handling
- Route-based service selection
- Frontend serving on port 80

#### Docker Configuration
- docker-compose.yml orchestration
- Separate Dockerfiles for each service
- Health checks for services
- Network isolation
- Volume management for PostgreSQL persistence

## Data Model

```
Customer (id, name, email, createdAt)
    ├── Cart (customerId, items[], totalPrice)
    │   └── CartItem (productId, productName, price, quantity)
    └── Order[] (id, totalPrice, status, items[], createdAt)
        └── OrderItem (productId, productName, price, quantity)

Category (id, name, description)
    └── Product[] (id, name, price, quantity, categoryId)
```

## How to Run

### Prerequisites
- Docker Desktop installed
- 8GB RAM minimum recommended
- Ports 80, 3000, 3001, 5432, 8081, 8082, 9092, 2181 available

### Build and Deploy

```bash
cd /Users/rameshbabukm/Documents/Projects/react-java-cqrs-cart

# Start all services
docker-compose up -d

# Monitor build progress (takes 3-5 minutes)
docker-compose logs -f

# Check service status
docker-compose ps

# View specific service logs
docker-compose logs command-service
docker-compose logs query-service
docker-compose logs frontend
```

### Access Application

Once all containers are running:

- **Frontend UI**: http://localhost (or http://localhost:3000)
- **API Gateway**: http://localhost:3001/api
- **Command Service (Direct)**: http://localhost:8081
- **Query Service (Direct)**: http://localhost:8082
- **Database**: localhost:5432 (user: ecart_user, password: ecart_password)
- **Kafka**: localhost:9092
- **Zookeeper**: localhost:2181

### Stop Services

```bash
# Stop all services (keep volumes)
docker-compose down

# Stop and remove all data
docker-compose down -v

# View logs of stopped services
docker-compose logs

# Restart services
docker-compose restart
```

## CQRS Architecture Explanation

The Command Query Responsibility Segregation pattern separates read and write operations:

1. **Command Service** handles mutations:
   - Writes to database
   - Publishes domain events to Kafka
   - Implements business logic

2. **Query Service** handles queries:
   - Optimized read model from database
   - Consumes Kafka events to maintain synchronization
   - Provides fast queries without complex joins

3. **Kafka** provides event streaming:
   - Decouples command and query services
   - Enables eventual consistency
   - Provides audit trail

## Features Implemented

✅ **Customer Management**
- Create and manage customers
- Selection persisted in browser localStorage
- Email validation

✅ **Product Catalog**
- Browse all products
- Filter by category
- Add products to cart
- Stock management

✅ **Shopping Cart**
- Add/remove items
- Update quantities
- Calculate totals
- Persistent across page navigation

✅ **Order Management**
- Checkout from cart
- Order history
- Order details view
- Stock automatic reduction

✅ **CQRS Architecture**
- Separated command and query services
- Event-driven communication
- Eventual consistency

✅ **Database**
- PostgreSQL with proper schemas
- Sample data for testing
- Referential integrity

✅ **Messaging**
- Kafka integration
- Event publishing
- Topic-based message routing

✅ **API Gateway**
- Nginx reverse proxy
- CORS handling
- Service routing

✅ **Containerization**
- Multi-stage Docker builds
- Optimized images
- docker-compose orchestration

✅ **Error Handling**
- CORS error resolution
- Graceful error messages
- Health checks

## Technology Versions

- React: 18.3.1
- Node.js: 20 (Alpine)
- Spring Boot: 3.4.1
- Java: 21
- PostgreSQL: 16 (Alpine)
- Kafka: 7.5.0
- Nginx: latest (Alpine)
- Maven: 3.9.6

## File Structure

```
react-java-cqrs-cart/
├── frontend/
│   ├── src/
│   │   ├── pages/
│   │   │   ├── Dashboard.js
│   │   │   ├── Products.js
│   │   │   ├── Cart.js
│   │   │   └── Orders.js
│   │   ├── api.js
│   │   ├── App.js
│   │   ├── index.js
│   │   ├── App.css
│   │   └── index.css
│   ├── public/
│   │   └── index.html
│   ├── package.json
│   └── Dockerfile
├── backend/
│   ├── shared/
│   │   ├── src/main/java/com/ecart/shared/
│   │   │   ├── dto/
│   │   │   └── events/
│   │   └── pom.xml
│   ├── command-service/
│   │   ├── src/main/java/com/ecart/command/
│   │   │   ├── CommandServiceApplication.java
│   │   │   ├── controller/
│   │   │   ├── entity/
│   │   │   ├── repository/
│   │   │   ├── service/
│   │   │   └── config/
│   │   ├── src/main/resources/
│   │   │   └── application.properties
│   │   └── pom.xml
│   └── query-service/
│       ├── src/main/java/com/ecart/query/
│       │   ├── QueryServiceApplication.java
│       │   ├── controller/
│       │   ├── model/
│       │   ├── repository/
│       │   ├── service/
│       │   └── config/
│       ├── src/main/resources/
│       │   └── application.properties
│       └── pom.xml
├── database/
│   └── init.sql
├── nginx/
│   ├── nginx.conf
│   └── Dockerfile
├── docker/
│   ├── Dockerfile.command
│   └── Dockerfile.query
├── docker-compose.yml
├── pom.xml
├── .gitignore
├── .dockerignore
└── README.md
```

## API Examples

### Create a Customer
```bash
curl -X POST http://localhost:3001/api/customers \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "email": "john@example.com"
  }'
```

### Get All Products
```bash
curl http://localhost:3001/api/products
```

### Add Item to Cart
```bash
curl -X POST http://localhost:3001/api/carts/1/items \
  -H "Content-Type: application/json" \
  -d '{
    "productId": 1,
    "quantity": 2
  }'
```

### Checkout
```bash
curl -X POST http://localhost:3001/api/carts/1/checkout
```

## Troubleshooting

### Build Issues

**Maven downloads hanging:**
- Solution: Increase Docker memory to 4GB minimum
- Solution: Check internet connection
- Solution: Clear Maven cache: `docker system prune -a`

**Java 21 image not found:**
- Solution: Already using Java 21, verify Docker image pull

**Port already in use:**
```bash
# Find process using port
lsof -i :3000

# Kill process
kill -9 <PID>

# Or change port in docker-compose.yml
```

###  Service Connection Issues

**Frontend can't reach backend:**
- Verify CORS config in nginx.conf
- Check if services are healthy: `docker-compose ps`
- Check logs: `docker-compose logs nginx`

**Database connection refused:**
- Ensure postgres container is running: `docker ps | grep postgres`
- Check database credentials match application.properties
- Wait for database to be ready (up to 30 seconds)

**Kafka connection issues:**
- Ensure Zookeeper starts first
- Check Kafka is healthy: `docker-compose ps | grep kafka`
- Wait for Kafka to be ready

## Next Steps

1. Test the application:
   - Open http://localhost in browser
   - Create a customer
   - Browse products
   - Add to cart
   - Place an order

2. Monitor services:
   ```bash
   docker-compose logs -f command-service
   docker-compose logs -f query-service
   docker-compose logs -f frontend
   ```

3. Access databases:
   - Connect to PostgreSQL: `psql -h localhost -U ecart_user -d ecart_db`

4. View Kafka events:
   ```bash
   docker exec ecart-kafka kafka-console-consumer \
     --bootstrap-server localhost:9092 \
     --topic customer-events \
     --from-beginning
   ```

## Production Considerations

- Add authentication/authorization
- Implement rate limiting
- Add comprehensive logging
- Set up monitoring and alerting
- Use environment variables for configuration
- Implement database migrations
- Add API versioning
- Implement request validation
- Add transaction management
- Configure resource limits for containers

## Performance Optimization

- Connection pooling in Spring Boot
- Database indexing on foreign keys
- Caching strategies
- Load balancing with multiple instances
- Kafka partition strategy
- Database query optimization

## Security Hardening

- HTTPS/TLS configuration
- SQL injection prevention (using JPA)
- XSS protection in frontend
- CSRF tokens
- Input validation and sanitization
- Secret management for credentials
- Regular dependency updates

---

**Project Ready for Deployment!** 🚀

All components are built and configured. Follow the "Build and Run" section to start the application.
