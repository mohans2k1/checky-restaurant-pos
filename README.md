# Checky - Multi-Tenant Restaurant POS System

A modern, scalable Point of Sale (POS) system designed for multi-tenant restaurant management. Built with Spring Boot, JPA, and PostgreSQL.

## 🏗️ Architecture

### Multi-Tenant Strategy
- **Row-Level Security**: All tables include a `tenant_id` column for data isolation
- **Database-Based API Keys**: API keys stored in database with restaurant associations
- **Spring Security Integration**: Comprehensive security with custom authentication filter
- **Automatic Restaurant Context**: Restaurant ID automatically set on all entities

### Core Features
- ✅ Menu Management (Categories & Items)
- ✅ Order Processing
- ✅ Multi-tenant Data Isolation
- ✅ Database-Based API Key Authentication
- ✅ PostgreSQL Database
- ✅ RESTful APIs
- ✅ Custom Error Handling
- ✅ API Key Management
- ✅ Swagger API Documentation

## 🚀 Getting Started

### Prerequisites
- Java 17+
- PostgreSQL 12+
- Gradle 7+

### Database Setup
1. Create PostgreSQL database:
```sql
CREATE DATABASE checky_pos;
```

2. Update `application.properties` with your database credentials:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/checky_pos
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### Running the Application
```bash
./gradlew bootRun
```

The application will automatically initialize sample data including:
- 3 sample restaurants (Pizza Palace, Burger Joint, Sushi Bar)
- API keys for each restaurant

## 📚 API Documentation

### Swagger UI
Once the application is running, you can access the interactive API documentation:

- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/api-docs

### API Documentation Features
- ✅ **Interactive Documentation**: Test APIs directly from the browser
- ✅ **Authentication Support**: API key authentication configured
- ✅ **Request/Response Examples**: Detailed examples for all endpoints
- ✅ **Error Responses**: Documented error codes and messages
- ✅ **Parameter Validation**: Clear parameter descriptions and requirements
- ✅ **Grouped by Tags**: APIs organized by functionality (Menu Management, API Key Management, Public APIs)

### Using Swagger UI
1. Open http://localhost:8080/swagger-ui.html in your browser
2. Click on any endpoint to expand its details
3. Click "Try it out" to test the API
4. For protected endpoints, add your API key in the "Authorize" section
5. Fill in the required parameters and click "Execute"

### API Key Authentication in Swagger
1. Click the "Authorize" button at the top of the Swagger UI
2. Enter your API key in the format: `api_key_restaurant1`
3. Click "Authorize"
4. Now you can test protected endpoints

## 🔑 Security & Authentication

### Database-Based API Key Authentication
All protected API requests require an API key in the `X-API-Key` header:

```bash
curl -H "X-API-Key: api_key_restaurant1" \
     http://localhost:8080/api/menu/categories
```

### Available API Keys (Auto-Generated)
- `api_key_restaurant1` → Pizza Palace
- `api_key_restaurant2` → Burger Joint  
- `api_key_restaurant3` → Sushi Bar

### API Key Management
Restaurants can manage their API keys:

```bash
# Create a new API key
curl -X POST -H "X-API-Key: api_key_restaurant1" \
     -H "Content-Type: application/json" \
     -d '{"description":"Mobile App Key"}' \
     http://localhost:8080/api/keys

# List all API keys for the restaurant
curl -H "X-API-Key: api_key_restaurant1" \
     http://localhost:8080/api/keys

# Deactivate an API key
curl -X DELETE -H "X-API-Key: api_key_restaurant1" \
     http://localhost:8080/api/keys/api_key_to_deactivate
```

### Public Endpoints
Some endpoints don't require authentication:
```bash
# Health check
curl http://localhost:8080/api/public/health

# System info
curl http://localhost:8080/api/public/info
```

### Security Features
- ✅ **Database Storage**: API keys stored securely in database
- ✅ **Spring Security Integration**: Full security framework integration
- ✅ **Custom Authentication Filter**: API key validation against database
- ✅ **Role-Based Access Control**: RESTAURANT role for authenticated users
- ✅ **Custom Error Handling**: JSON error responses for authentication failures
- ✅ **Stateless Sessions**: No session management for scalability
- ✅ **CSRF Protection Disabled**: API-only application
- ✅ **API Key Expiration**: Support for expiring API keys
- ✅ **Usage Tracking**: Last used timestamp for API keys

## 📋 API Endpoints

### Public Endpoints (No Authentication Required)
- `GET /api/public/health` - Health check
- `GET /api/public/info` - System information

### Protected Endpoints (API Key Required)

#### API Key Management
- `GET /api/keys` - List all API keys for current restaurant
- `POST /api/keys` - Create a new API key
- `DELETE /api/keys/{apiKey}` - Deactivate an API key

#### Menu Management

##### Categories
- `GET /api/menu/categories` - Get all active categories
- `POST /api/menu/categories` - Create a new category

##### Menu Items
- `GET /api/menu/items` - Get all available menu items
- `GET /api/menu/items/category/{categoryId}` - Get items by category
- `POST /api/menu/items` - Create a new menu item
- `PUT /api/menu/items/{id}` - Update a menu item
- `DELETE /api/menu/items/{id}` - Delete a menu item

#### Order Management
- `GET /api/orders` - Get all orders for current restaurant
- `GET /api/orders/{id}` - Get order by ID
- `GET /api/orders/number/{orderNumber}` - Get order by order number
- `GET /api/orders/status/{status}` - Get orders by status
- `POST /api/orders` - Create a new order
- `PUT /api/orders/{id}/status` - Update order status
- `PUT /api/orders/{id}/payment` - Update payment status
- `DELETE /api/orders/{id}` - Cancel order
- `GET /api/orders/statuses` - Get available order statuses
- `GET /api/orders/payment-statuses` - Get available payment statuses

#### Table Management
- `GET /api/tables` - Get all tables for current restaurant
- `GET /api/tables/available` - Get available tables
- `GET /api/tables/reservable` - Get reservable tables
- `GET /api/tables/status/{status}` - Get tables by status
- `GET /api/tables/type/{type}` - Get tables by type
- `GET /api/tables/capacity/{minCapacity}` - Get tables by capacity
- `GET /api/tables/{id}` - Get table by ID
- `GET /api/tables/number/{tableNumber}` - Get table by number
- `POST /api/tables` - Create a new table
- `PUT /api/tables/{id}` - Update a table
- `PUT /api/tables/{id}/status` - Update table status
- `DELETE /api/tables/{id}` - Delete a table
- `GET /api/tables/statuses` - Get available table statuses
- `GET /api/tables/types` - Get available table types

#### Inventory Management
- `GET /api/inventory/items` - Get all inventory items
- `GET /api/inventory/items/category/{category}` - Get items by category
- `GET /api/inventory/items/low-stock` - Get low stock items
- `GET /api/inventory/items/out-of-stock` - Get out of stock items
- `GET /api/inventory/items/expiring/{daysAhead}` - Get expiring items
- `GET /api/inventory/items/search` - Search inventory items
- `GET /api/inventory/items/{id}` - Get item by ID
- `GET /api/inventory/items/code/{itemCode}` - Get item by code
- `POST /api/inventory/items` - Create new inventory item
- `PUT /api/inventory/items/{id}` - Update inventory item
- `DELETE /api/inventory/items/{id}` - Delete inventory item
- `GET /api/inventory/transactions` - Get all transactions
- `GET /api/inventory/transactions/item/{itemId}` - Get transactions by item
- `GET /api/inventory/transactions/type/{type}` - Get transactions by type
- `GET /api/inventory/transactions/date-range` - Get transactions by date range
- `GET /api/inventory/transactions/number/{transactionNumber}` - Get transaction by number
- `POST /api/inventory/stock-in` - Add stock to inventory
- `POST /api/inventory/stock-out` - Remove stock from inventory
- `POST /api/inventory/adjustment` - Adjust inventory stock
- `POST /api/inventory/transfer` - Transfer inventory between locations
- `GET /api/inventory/categories` - Get inventory categories
- `GET /api/inventory/transaction-types` - Get transaction types
- `GET /api/inventory/transactions/pending-approvals` - Get pending approvals

#### Recipe Management
- `GET /api/recipes` - Get all recipes
- `GET /api/recipes/{id}` - Get recipe by ID
- `GET /api/recipes/menu-item/{menuItemId}` - Get recipe by menu item
- `GET /api/recipes/cuisine/{cuisineType}` - Get recipes by cuisine type
- `GET /api/recipes/difficulty/{difficultyLevel}` - Get recipes by difficulty level
- `GET /api/recipes/vegetarian` - Get vegetarian recipes
- `GET /api/recipes/gluten-free` - Get gluten-free recipes
- `GET /api/recipes/search` - Search recipes
- `POST /api/recipes` - Create new recipe
- `PUT /api/recipes/{id}` - Update recipe
- `DELETE /api/recipes/{id}` - Delete recipe
- `GET /api/recipes/{recipeId}/ingredients` - Get recipe ingredients
- `POST /api/recipes/{recipeId}/ingredients` - Add ingredient to recipe
- `DELETE /api/recipes/{recipeId}/ingredients/{ingredientId}` - Remove ingredient from recipe
- `GET /api/recipes/{recipeId}/instructions` - Get recipe instructions
- `POST /api/recipes/{recipeId}/instructions` - Add instruction to recipe
- `DELETE /api/recipes/{recipeId}/instructions/{instructionId}` - Remove instruction from recipe
- `POST /api/recipes/track-inventory` - Track inventory for order item
- `GET /api/recipes/cuisine-types` - Get cuisine types
- `GET /api/recipes/difficulty-levels` - Get difficulty levels

## 🏪 Multi-Tenant Features

### Data Isolation
- Each restaurant's data is isolated by `tenant_id`
- API calls automatically filter data by current restaurant
- No cross-restaurant data access possible

### Security Context
- Restaurant identification via API key from database
- Automatic restaurant context setting through Spring Security
- Entity listeners for automatic restaurant assignment

## 🛡️ Security Architecture

### Authentication Flow
1. **API Key Extraction**: Custom filter extracts API key from `X-API-Key` header
2. **Database Validation**: Validates API key against database with expiration check
3. **Restaurant Lookup**: Retrieves restaurant ID from database
4. **Security Context**: Sets authentication token with restaurant information
5. **Authorization**: Spring Security enforces role-based access control

### Security Components
- **ApiKeyAuthenticationFilter**: Custom authentication filter
- **SecurityConfig**: Spring Security configuration
- **CustomAuthenticationEntryPoint**: Custom error handling
- **TenantSecurityService**: Security utility service
- **ApiKeyService**: Database-based API key management
- **ApiKeyRepository**: Database operations for API keys

## 🗄️ Database Schema

### Core Tables
- `restaurants` - Restaurant information
- `api_keys` - API key management with restaurant associations
- `categories` - Menu categories
- `menu_items` - Menu items
- `orders` - Customer orders
- `order_items` - Individual items in orders

### Multi-Tenant Columns
All tables include:
- `tenant_id` - Restaurant identifier
- `created_at` - Creation timestamp
- `updated_at` - Last update timestamp

### API Key Table
- `api_key` - Unique API key string
- `restaurant_id` - Associated restaurant
- `description` - Key description
- `is_active` - Active status
- `last_used_at` - Last usage timestamp
- `expires_at` - Optional expiration date

## 🔧 Configuration

### Security Configuration
```properties
# Spring Security is enabled by default
# API key authentication is configured in SecurityConfig
```

### Multi-Tenant Settings
```properties
app.multi-tenant.enabled=true
app.multi-tenant.strategy=row-level
```

### Database Configuration
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/checky_pos
spring.jpa.hibernate.ddl-auto=update
```

## 🚀 Production Considerations

### Security
- Store API keys securely (encrypted in database)
- Implement API key rotation policies
- Add rate limiting per restaurant
- Use HTTPS in production
- Implement audit logging
- Add API key expiration policies

### Database
- Use separate databases per restaurant for higher isolation
- Implement connection pooling
- Set up proper indexes on `tenant_id` and `api_key` columns
- Regular security audits
- Backup API key data

### Monitoring
- Restaurant-specific metrics
- API usage tracking per restaurant
- Performance monitoring per restaurant
- Security event logging
- API key usage analytics

## 📈 Future Enhancements

- [ ] Payment processing integration
- [ ] Inventory management
- [ ] Employee management
- [ ] Reporting and analytics
- [ ] Real-time order notifications
- [ ] Mobile app support
- [ ] Advanced reporting
- [ ] Integration with delivery platforms
- [ ] OAuth2/JWT authentication
- [ ] API rate limiting
- [ ] Audit logging
- [ ] API key scopes and permissions
- [ ] Webhook support

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests
5. Submit a pull request

## 📄 License

This project is licensed under the MIT License. 