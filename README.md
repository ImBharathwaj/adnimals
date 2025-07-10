## Create an Ad

curl -X POST http://localhost:8080/ads \
  -H "Content-Type: application/json" \
  -d '{
    "id": "ad1",
    "title": "Buy Shoes",
    "content": "Best shoes in town!"
  }'

## List All Ads

`curl http://localhost:8080/ads`

## Update an Ad

curl -X PUT http://localhost:8080/ads/ad1 \
  -H "Content-Type: application/json" \
  -d '{
    "id": "ad1",
    "title": "Buy More Shoes",
    "content": "Now 50% off!"
  }'

## Delete an Ad

`curl -X DELETE http://localhost:8080/ads/ad1`

## Send Ad Event (Impression or Click)

### Impression

curl -X POST http://localhost:8080/events \
  -H "Content-Type: application/json" \
  -d '{
    "adId": "ad1",
    "eventType": "impression"
  }'

### Click

curl -X POST http://localhost:8080/events \
  -H "Content-Type: application/json" \
  -d '{
    "adId": "ad1",
    "eventType": "click"
  }'

## List All Events

curl http://localhost:8080/events

## View Stats (Grouped per Ad)

curl http://localhost:8080/stats