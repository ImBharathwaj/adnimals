## 📘 Adnimals API Documentation

### Version: `v0.1.0`

### Status: ✅ Live (local)

---

## 🛍 Table of Contents

* [GET /ad](#get-ad)
* [POST /event](#post-event)
* [Response Format](#response-format)
* [Example Requests](#example-requests)

---

## 🔹 GET `/ad`

### 📌 Description:

Fetch an ad based on `slot_id` and optional `geo`.

### 🟞 Query Parameters:

| Param     | Type     | Required | Description                         |
| --------- | -------- | -------- | ----------------------------------- |
| `slot_id` | `string` | ✅ yes    | Ad placement identifier             |
| `geo`     | `string` | ❌ no     | ISO country code (`in`, `us`, etc.) |

### ✅ Sample Request:

```
GET /ad?slot_id=home_top&geo=in
```

### ✅ Sample Response:

```json
{
  "id": "ad1",
  "title": "Buy Shoes",
  "content": "Best shoes in town!",
  "slotId": "home_top",
  "creativeUrl": "https://example.com/banner.jpg",
  "targeting": {
    "geo": "in"
  }
}
```

---

## 🔸 POST `/event`

### 📈 Description:

Track an ad interaction (click, impression).

### 📄 JSON Body:

| Field       | Type            | Required | Description                      |
| ----------- | --------------- | -------- | -------------------------------- |
| `adId`      | `string`        | ✅ yes    | The ad that was shown/clicked    |
| `eventType` | `string`        | ✅ yes    | One of `impression`, `click`     |
| `timestamp` | `number (unix)` | ✅ yes    | Unix timestamp in seconds        |
| `metadata`  | `object (map)`  | ❌ no     | Extra info: userId, device, etc. |

### ✅ Sample Request:

```json
{
  "adId": "ad1",
  "eventType": "click",
  "timestamp": 1720703500,
  "metadata": {
    "userId": "u123",
    "device": "android"
  }
}
```

### ✅ Response:

```json
{
  "status": "ok"
}
```

---

## 🔁 Example Workflow

1. App calls `GET /ad?slot_id=home_top&geo=in`
2. Server returns a matching ad
3. App displays the ad
4. App tracks:

    * `POST /event` with `"eventType": "impression"`
    * On click: `POST /event` with `"eventType": "click"`
