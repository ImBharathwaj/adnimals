curl -X POST http://localhost:8080/ads \
  -H "Content-Type: application/json" \
  -d '{
    "id": "ad3",
    "title": "iOS Exclusive Promo",
    "content": "Only for iPhone users in the USA",
    "slotId": "home_top",
    "creativeUrl": "https://example.com/ios-deal.jpg",
    "targeting": {
      "geo": "us",
      "device": "ios"
    }
  }'

curl -X POST http://localhost:8080/ads \
  -H "Content-Type: application/json" \
  -d '{
    "id": "ad2",
    "title": "20% off on Android App",
    "content": "Exclusive deal for Android users in India!",
    "slotId": "home_top",
    "creativeUrl": "https://example.com/android-offer.jpg",
    "targeting": {
      "geo": "in",
      "device": "android"
    }
  }'

curl -X POST http://localhost:8080/ads \
  -H "Content-Type: application/json" \
  -d '{
    "id": "ad4",
    "title": "Special for You",
    "content": "Hello user123! We have something just for you.",
    "slotId": "home_top",
    "creativeUrl": "https://example.com/personal.jpg",
    "targeting": {
      "userId": "user123"
    }
  }'

curl "http://localhost:8080/ad?slot_id=home_top&geo=in&device=android"

curl "http://localhost:8080/admin/ads"

curl -X DELETE http://localhost:8080/ads/ad2

curl http://localhost:8080/admin/events

curl -X POST http://localhost:8080/ads \
  -H "Content-Type: application/json" \
  -d '{
    "id": "ad1",
    "title": "Shoes for You",
    "content": "Best running shoes!",
    "slotId": "homepage",
    "creativeUrl": "https://example.com/shoes.jpg",
    "targeting": {
      "geo": "IN",
      "device": "mobile"
    }
  }'

curl -X POST http://localhost:8080/ads \
  -H "Content-Type: application/json" \
  -d '{
    "id": "ad2",
    "title": "Tech Sale",
    "content": "Discount on gadgets",
    "slotId": "homepage",
    "creativeUrl": "https://example.com/gadgets.jpg",
    "targeting": {
      "geo": "IN"
    }
  }'

curl "http://localhost:8080/ad?slot_id=homepage&geo=IN&device=mobile"

curl "http://localhost:8080/ad?slot_id=homepage&geo=IN&device=desktop"

curl -X POST http://localhost:8080/ads \
  -H "Content-Type: application/json" \
  -d '{
    "id": "cap-test-ad",
    "title": "Laptop Deal",
    "content": "New MacBook from ₹99,999",
    "slotId": "tech-banner",
    "creativeUrl": "https://example.com/macbook.jpg",
    "targeting": {
      "geo": "IN"
    }
  }'

curl "http://localhost:8080/ad?slot_id=tech-banner&geo=IN&userId=U123"

curl "http://localhost:8080/ad?slot_id=tech-banner&geo=IN&userId=U123"