# Grocery Store Product Management API

A Spring Boot REST API for managing grocery store products with categories including Fruits, Vegetables, Dairy, and Beverages.

## Features

- **Product Management**: Create, read, update, and delete products
- **Category-based Filtering**: Filter products by category (Fruits, Vegetables, Dairy, Beverages)
- **Search Functionality**: Search products by name
- **Sample Data**: Pre-loaded sample data for testing
- **H2 Database**: In-memory database for development and testing

## Technology Stack

- **Java 17**
- **Spring Boot 3.2.0**
- **Spring Data JPA**
- **H2 Database** (in-memory)
- **Maven**

## Project Structure

src/main/java/com/grocerystore/
├── GroceryStoreApplication.java    # Main application class
├── entity/
│   ├── Product.java               # Product entity
│   ├── Category.java              # Category enum
│   └── CartItem.java              # Cart item entity
├── repository/
│   ├── ProductRepository.java     # Product JPA repository
│   └── CartRepository.java        # Cart JPA repository
├── service/
│   ├── ProductService.java        # Product business logic
│   └── CartService.java           # Cart business logic with validation
├── controller/
│   ├── ProductController.java     # Product REST endpoints
│   └── CartController.java        # Cart REST endpoints
├── dto/
│   ├── AddToCartRequest.java      # Add to cart request DTO
│   ├── UpdateCartRequest.java     # Update cart request DTO
│   └── CartResponse.java          # Cart response DTO with totals
└── config/
    └── DataLoader.java           # Sample data loader (products & carts)
```

## API Endpoints

### Product Endpoints

#### Get All Products
```http
GET /api/products
```
**Response**: List of all products ordered by name

### Get Products by Category
```http
GET /api/products/{category}
```
**Parameters**: 
- `category`: One of `fruits`, `vegetables`, `dairy`, `beverages`

**Example**:
```http
GET /api/products/fruits
```

### Create New Product (Admin)
```http
POST /api/products
Content-Type: application/json

{
  "name": "Fresh Tomatoes",
  "category": "VEGETABLES",
  "price": 3.99,
  "imageUrl": "https://example.com/tomatoes.jpg",
  "description": "Ripe red tomatoes, perfect for salads"
}
```

### Search Products by Name
```http
GET /api/products/search?name={searchTerm}
```
**Example**:
```http
GET /api/products/search?name=apple
```

### Update Product
```http
PUT /api/products/{id}
Content-Type: application/json

{
  "name": "Updated Product Name",
  "category": "FRUITS",
  "price": 4.99,
  "imageUrl": "https://example.com/updated-image.jpg",
  "description": "Updated description"
}
```

### Delete Product
```http
DELETE /api/products/{id}
```

### Shopping Cart Endpoints

#### Get User's Cart
```http
GET /api/cart?userId={userId}
```
**Response**: Complete cart with items, quantities, prices, and totals

#### Add Product to Cart
```http
POST /api/cart
Content-Type: application/json

{
  "productId": 1,
  "userId": "user123",
  "quantity": 2
}
```
**Note**: If the product already exists in the cart, quantities will be added together.

#### Update Cart Item Quantity
```http
PUT /api/cart/{itemId}?userId={userId}
Content-Type: application/json

{
  "quantity": 3
}
```

#### Remove Item from Cart
```http
DELETE /api/cart/{itemId}?userId={userId}
```

#### Clear Entire Cart
```http
DELETE /api/cart/clear?userId={userId}
```

#### Get Cart Item Count
```http
GET /api/cart/count?userId={userId}
```
**Response**: Total number of items in the user's cart

#### Check if Product is in Cart
```http
GET /api/cart/check?userId={userId}&productId={productId}
```
**Response**: `true` or `false`

## Product Categories

The API supports the following product categories:
- `FRUITS`
- `VEGETABLES` 
- `DAIRY`
- `BEVERAGES`

## Sample Data

The application automatically loads sample data on startup, including:

**Fruits**: Apples, Bananas, Oranges, Strawberries
**Vegetables**: Carrots, Spinach, Bell Peppers, Broccoli  
**Dairy**: Milk, Greek Yogurt, Cheddar Cheese, Butter
**Beverages**: Orange Juice, Sparkling Water, Green Tea, Coffee Beans

**Sample Cart Data**: Sample cart items for users "user123" and "user456" for testing cart functionality.

## Running the Application

### Prerequisites
- Java 17 or higher
- Maven 3.6 or higher

### Steps
1. Navigate to the project directory:
   ```bash
   cd grocery-store
   ```

2. Run the application:
   ```bash
   mvn spring-boot:run
   ```

3. The application will start on `http://localhost:8080`

4. Access the H2 database console (for development):
   - URL: `http://localhost:8080/h2-console`
   - JDBC URL: `jdbc:h2:mem:grocerystore`
   - Username: `sa`
   - Password: `password`

## Testing the API

### Using curl

**Get all products:**
```bash
curl http://localhost:8080/api/products
```

**Get fruits:**
```bash
curl http://localhost:8080/api/products/fruits
```

**Create a new product:**
```bash
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Fresh Mangoes",
    "category": "FRUITS",
    "price": 6.99,
    "imageUrl": "https://example.com/mangoes.jpg",
    "description": "Sweet tropical mangoes"
  }'
```

**Search products:**
```bash
curl "http://localhost:8080/api/products/search?name=apple"
```

**Get user's cart:**
```bash
curl "http://localhost:8080/api/cart?userId=user123"
```

**Add product to cart:**
```bash
curl -X POST http://localhost:8080/api/cart \
  -H "Content-Type: application/json" \
  -d '{
    "productId": 1,
    "userId": "user123",
    "quantity": 2
  }'
```

**Update cart item quantity:**
```bash
curl -X PUT "http://localhost:8080/api/cart/1?userId=user123" \
  -H "Content-Type: application/json" \
  -d '{"quantity": 5}'
```

**Remove item from cart:**
```bash
curl -X DELETE "http://localhost:8080/api/cart/1?userId=user123"
```

### Using a REST Client

You can also test the API using tools like:
- Postman
- Insomnia
- VS Code REST Client extension

## Configuration

Key configuration settings in `application.properties`:

- **Server Port**: `8080`
- **Database**: H2 in-memory database
- **JPA**: Auto-create database schema
- **Logging**: Debug level for application components

## Development Notes

- The H2 database is configured to recreate the schema on each restart (`create-drop`)
- Sample data is automatically loaded on startup
- CORS is enabled for all origins (restrict in production)
- Comprehensive logging is enabled for debugging
- Input validation is implemented using Bean Validation annotations

## Cart Features

- **Product Validation**: Validates products exist before adding to cart
- **Quantity Management**: Automatically combines quantities when adding existing products
- **User Isolation**: Cart items are isolated by user ID
- **Total Calculations**: Automatically calculates subtotals and cart totals
- **Rich Response Data**: Cart responses include complete product information
- **Security Checks**: User ownership validation for cart operations

## Future Enhancements

- Authentication and authorization for admin and cart endpoints
- User management system with proper user entities
- Inventory management and stock tracking
- Product images upload functionality
- Advanced search and filtering with pagination
- Order management and checkout process
- Payment integration
- Integration with external product databases
- Cart persistence across sessions
- Cart abandonment notifications 