## Module 03, 04, 05 & 06 is added

### 12 Jul, 2025

### Added

  - Expanded delivery engine to account for frequency rules
  - Introduced frequency capping system
  - Deletion of ads via DELETE /ads/:id
  - Support for updating ads via PUT /ads/:id
  - Enabled re-targeting and correction of ad metadata
  - Improved ad model with optional creative fields and better field validation
  - Ad delivery endpoint (GET /ad)
  - Frequency capping
  - Logging for ad delivery events

## Module 02 is added

### 11 Jul, 2025

### Added
- CRUD endpoints for managing advertisements (`/ads`)
  - `POST /ads` to create ads
  - `GET /ads` to list ads
  - `PUT /ads/:id` to update ads
  - `DELETE /ads/:id` to delete ads
- Event tracking system (`/events`)
  - Supports `impression` and `click` events
- Stats endpoint (`/stats`)
  - Aggregates number of impressions and clicks per ad

### Tech
- Scala with Pekko HTTP
- Circe for JSON serialization
- In-memory storage (for initial version)

### Tested with
- Manual `curl` commands for API validation


## Module 01 is added

### 10 Jul, 2025

### Module 1: Advertisement & Event Core

#### Added
- Ad management endpoints (`/ads`)
  - Create, read, update, delete ads
- Event tracking system (`/events`)
  - Handles `impression` and `click` events via POST
- Statistics endpoint (`/stats`)
  - Returns grouped counts of impressions and clicks per ad

#### Tech Stack
- Scala + Pekko HTTP
- Circe (with custom collection encoder workaround)
- In-memory ad and event storage

#### Testing
- Validated using `curl` commands
- Confirmed JSON structure and response formats

## Project initiated

### 09 Jul, 2025

- Project initiated using `sbt`
