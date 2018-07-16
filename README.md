This application is to serve the UI of Live Orders Board for Silver Bars Marketplace.
The application has following api's:
1. To register an order:
    Endpoint -> http://localhost:8080/steel-bars-marketplace/api/v1/orders
    Http Method -> POST
    Request -> {
               	"userId":"user4",
               	"quantity":"2.0",
               	"price":"306",
               	"type":"SELL"
               }
               
2. To cancel an order:
    Endpoint -> http://localhost:8080/steel-bars-marketplace/api/v1/orders
    Http Method -> DELETE
    Request (OrderId) -> 1

3. To get summary information of live orders:
    Endpoint -> http://localhost:8080/steel-bars-marketplace/api/v1/orders/summary
    Http Method -> GET
    
For installing and testing the application run following command from the project root:

mvn package && java -jar target/live-order-board-1.0.jar


    