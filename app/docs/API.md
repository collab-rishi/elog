## ELogisticsTendering REST API

Comprehensive, developer-friendly documentation for all endpoints in the ELogisticsTendering service. Each endpoint includes purpose, parameters, request/response schemas, and copy-pasteable examples.

### Conventions
- **Base URL**: Define `BASE_URL` as your server root (e.g., `http://localhost:9090`). All paths below are absolute.
- **Auth**: No auth annotations are present in shown controllers; if enforced, it’s external (gateway/filters).
- **Content-Type**: `application/json` unless noted.
- **Dates/Times**: ISO-8601. Dates as `YYYY-MM-DD`.

### Common HTTP status codes
- **200 OK**: Successful request
- **201 Created**: Resource created
- **204 No Content**: No matching data
- **400 Bad Request**: Validation/input error
- **401 Unauthorized**: Incorrect credentials
- **404 Not Found**: Resource not found
- **409 Conflict**: Already exists/conflict
- **500 Internal Server Error**: Unexpected server error

### Table of Contents
- [Authentication and Account](#authentication-and-account)
- [Password Reset](#password-reset)
- [Users](#users)
- [Tenders (3PL)](#tenders-3pl)
- [LSP Responses](#lsp-responses)
- [3PL Actions](#3pl-actions-on-responses-and-assignments)
- [LSP Assignments](#lsp-assignments-vehicle-details)
- [Data Models](#data-models-response-schemas)

## Authentication and Account

### Health check
- **Method**: GET
- **URL**: `/auth/ping`
- **Purpose**: Quick service availability probe.
- **Headers**: none required
- **Example request**:
```bash
curl -s -X GET "$BASE_URL/auth/ping"
```
- **Example responses**:
  - 200 OK
```text
ELogisticsTendering is up and running.
```

### Signup (initiate)
- **Method**: POST
- **URL**: `/auth/signup`
- **Purpose**: Register a new company user and send OTP for verification.
- **Request body**:
```json
{
  "email": "user@example.com",
  "companyName": "Acme Logistics",
  "mobileNumber": "9876543210",
  "location": "Pune, MH",
  "password": "StrongP@ssword123",
  "role": "LSP"
}
```
- **Validation highlights**:
  - `email`: valid email, <= 255 chars
  - `companyName`: 2-255 chars, letters/numbers/spaces/[-&.,()]
  - `mobileNumber`: Indian 10-digit starting 6-9
  - `password`: >= 8 chars with upper/lower/digit/special
  - `role`: `LSP` or `THREE_PL`
- **Example request**:
```bash
curl -s -X POST "$BASE_URL/auth/signup" \
  -H 'Content-Type: application/json' \
  -d '{
    "email":"user@example.com",
    "companyName":"Acme Logistics",
    "mobileNumber":"9876543210",
    "location":"Pune, MH",
    "password":"StrongP@ssword123",
    "role":"LSP"
  }'
```
- **Example responses**:
  - 201 Created
```json
"OTP sent successfully to company: Acme Logistics"
```
  - 409 Conflict / 400 Bad Request / 500 Internal Server Error: error message string

### Verify signup OTP
- **Method**: POST
- **URL**: `/auth/verify-signup-otp`
- **Purpose**: Confirm OTP sent during signup and create the account.
- **Request body**:
```json
{ "mobileNumber": "9876543210", "otp": "1234" }
```
- **Example request**:
```bash
curl -s -X POST "$BASE_URL/auth/verify-signup-otp" \
  -H 'Content-Type: application/json' \
  -d '{"mobileNumber":"9876543210","otp":"1234"}'
```
- **Example responses**:
  - 200 OK
```json
"Account created successfully for company: Acme Logistics"
```
  - 400 Bad Request / 500 Internal Server Error: error message string

### Login with email/password
- **Method**: POST
- **URL**: `/auth/login`
- **Purpose**: Email/password login.
- **Request body**:
```json
{ "email": "user@example.com", "password": "VeryStrongP@ssw0rd" }
```
- **Example request**:
```bash
curl -s -X POST "$BASE_URL/auth/login" \
  -H 'Content-Type: application/json' \
  -d '{"email":"user@example.com","password":"VeryStrongP@ssw0rd"}'
```
- **Example responses**:
  - 200 OK: success message string
  - 401 Unauthorized: "Incorrect password."
  - 404 Not Found: "No account found with this email."
  - 500 Internal Server Error: error message string

### Login via mobile (request OTP)
- **Method**: POST
- **URL**: `/auth/login-with-mobile`
- **Purpose**: Initiate OTP-based login to a registered mobile number.
- **Request body**:
```json
{ "mobileNumber": "9876543210" }
```
- **Example request**:
```bash
curl -s -X POST "$BASE_URL/auth/login-with-mobile" \
  -H 'Content-Type: application/json' \
  -d '{"mobileNumber":"9876543210"}'
```
- **Example responses**:
  - 200 OK: message string
  - 404 Not Found: message string
  - 500 Internal Server Error: message string

### Verify login OTP
- **Method**: POST
- **URL**: `/auth/verify-otp`
- **Purpose**: Verify OTP received for mobile login.
- **Request body**:
```json
{ "mobileNumber": "9876543210", "otp": "1234" }
```
- **Example request**:
```bash
curl -s -X POST "$BASE_URL/auth/verify-otp" \
  -H 'Content-Type: application/json' \
  -d '{"mobileNumber":"9876543210","otp":"1234"}'
```
- **Example responses**:
  - 200 OK: success message string
  - 400 Bad Request: "OTP verification failed."
  - 500 Internal Server Error: message string

## Password Reset

### Request password reset (send OTP)
- **Method**: POST
- **URL**: `/auth/request-password-reset`
- **Purpose**: Send a reset OTP to the specified email.
- **Request body**:
```json
{ "email": "user@example.com" }
```
- **Example request**:
```bash
curl -s -X POST "$BASE_URL/auth/request-password-reset" \
  -H 'Content-Type: application/json' \
  -d '{"email":"user@example.com"}'
```
- **Example responses**:
  - 200 OK: message string
  - 400 Bad Request / 500 Internal Server Error: message string

### Verify reset OTP
- **Method**: POST
- **URL**: `/auth/verify-reset-otp`
- **Purpose**: Verify reset OTP prior to password change.
- **Request body**:
```json
{ "email": "user@example.com", "otp": "1234" }
```
- **Example request**:
```bash
curl -s -X POST "$BASE_URL/auth/verify-reset-otp" \
  -H 'Content-Type: application/json' \
  -d '{"email":"user@example.com","otp":"1234"}'
```
- **Example responses**:
  - 200 OK: message string
  - 400 Bad Request / 500 Internal Server Error: message string

### Reset password
- **Method**: POST
- **URL**: `/auth/reset-password`
- **Purpose**: Set a new password after verifying OTP.
- **Request body**:
```json
{
  "email": "user@example.com",
  "newPassword": "NewVeryStrongP@ssw0rd",
  "confirmPassword": "NewVeryStrongP@ssw0rd"
}
```
- **Example request**:
```bash
curl -s -X POST "$BASE_URL/auth/reset-password" \
  -H 'Content-Type: application/json' \
  -d '{"email":"user@example.com","newPassword":"NewVeryStrongP@ssw0rd","confirmPassword":"NewVeryStrongP@ssw0rd"}'
```
- **Example responses**:
  - 200 OK: message string
  - 400 Bad Request / 500 Internal Server Error: message string

## Users

### List all LSP users
- **Method**: GET
- **URL**: `/users/lsp`
- **Purpose**: Retrieve all users with role `LSP`.
- **Example request**:
```bash
curl -s -X GET "$BASE_URL/users/lsp"
```
- **Example responses**:
  - 200 OK: array of `Signup` (see [Data Models](#data-models-response-schemas))

### List all 3PL users
- **Method**: GET
- **URL**: `/users/3pl`
- **Purpose**: Retrieve all users with role `THREE_PL`.
- **Example request**:
```bash
curl -s -X GET "$BASE_URL/users/3pl"
```
- **Example responses**:
  - 200 OK: array of `Signup`

### List all users
- **Method**: GET
- **URL**: `/users/all`
- **Purpose**: Retrieve all user records.
- **Example request**:
```bash
curl -s -X GET "$BASE_URL/users/all"
```
- **Example responses**:
  - 200 OK: array of `Signup`

### Get user profile
- **Method**: POST
- **URL**: `/users/profile`
- **Purpose**: Look up a user's profile by email and mobile number.
- **Request body**:
```json
{ "email": "user@example.com", "mobileNumber": "9876543210" }
```
- **Example request**:
```bash
curl -s -X POST "$BASE_URL/users/profile" \
  -H 'Content-Type: application/json' \
  -d '{"email":"user@example.com","mobileNumber":"9876543210"}'
```
- **Example responses**:
  - 200 OK: `SignupDTO`
  - 404 Not Found: "User not found."
  - 400/500: message string

## Tenders (3PL)

### Create a new tender
- **Method**: POST
- **URL**: `/3PL/tenders/create`
- **Purpose**: 3PL creates a new tender for LSPs to respond to.
- **Query params**:
  - `companyName` (string, required): 3PL creating the tender
- **Request body** (`TenderDTO`):
```json
{
  "sourceLocation": "Pune",
  "destinationLocation": "Delhi",
  "pickupDate": "2025-01-15",
  "dropDate": "2025-01-16",
  "weight": 1200.5,
  "specialInstructions": "Handle with care",
  "tenderPrice": 50000
}
```
- **Example request**:
```bash
curl -s -X POST "$BASE_URL/3PL/tenders/create?companyName=Acme%203PL" \
  -H 'Content-Type: application/json' \
  -d '{
    "sourceLocation":"Pune",
    "destinationLocation":"Delhi",
    "pickupDate":"2025-01-15",
    "dropDate":"2025-01-16",
    "weight":1200.5,
    "specialInstructions":"Handle with care",
    "tenderPrice":50000
  }'
```
- **Example responses**:
  - 201 Created: `Tender` JSON (see [Data Models](#data-models-response-schemas))
  - 400 Bad Request: `null` body per controller

### Search tenders by company and status
- **Method**: POST
- **URL**: `/3PL/tenders/search`
- **Purpose**: List tenders by creating company and status.
- **Request body**:
```json
{ "companyName": "Acme 3PL", "status": "ACTIVE" }
```
- **Example request**:
```bash
curl -s -X POST "$BASE_URL/3PL/tenders/search" \
  -H 'Content-Type: application/json' \
  -d '{"companyName":"Acme 3PL","status":"ACTIVE"}'
```
- **Example responses**:
  - 200 OK: array of `Tender` or info string
  - 400 Bad Request: "Invalid status. Allowed values: ACTIVE, PENDING, COMPLETED."
  - 404 Not Found: message string

## LSP Responses

### Get responses by company
- **Method**: GET
- **URL**: `/api/lsp/responses`
- **Purpose**: List responses filtered by the 3PL company that created the tender.
- **Query params**:
  - `companyName` (string, required)
- **Example request**:
```bash
curl -s -X GET "$BASE_URL/api/lsp/responses?companyName=Acme%203PL"
```
- **Example responses**:
  - 200 OK: array of `LspResponse`
  - 204 No Content

### Reply to a tender (LSP)
- **Method**: PUT
- **URL**: `/api/lsp/responses/reply/{tenderNo}`
- **Purpose**: Submit an LSP bid/response.
- **Path params**:
  - `tenderNo` (string, required)
- **Query params**:
  - `companyName` (string, required): LSP company
- **Request body** (`TenderReplyRequest`):
```json
{ "estimatedArrivalDate": "2025-01-17", "bidPrice": 47000, "lspMessage": "We can deliver on time" }
```
- **Example request**:
```bash
curl -s -X PUT "$BASE_URL/api/lsp/responses/reply/TND-2025-0001?companyName=BestLSP" \
  -H 'Content-Type: application/json' \
  -d '{"estimatedArrivalDate":"2025-01-17","bidPrice":47000,"lspMessage":"We can deliver on time"}'
```
- **Example responses**:
  - 200 OK: "Tender reply submitted successfully."
  - 404 Not Found: empty body per controller

### Filter responses by company and status
- **Method**: POST
- **URL**: `/api/lsp/responses/filter`
- **Purpose**: Search responses by company and status.
- **Request body**:
```json
{ "companyName": "Acme 3PL", "status": "PENDING" }
```
- **Example request**:
```bash
curl -s -X POST "$BASE_URL/api/lsp/responses/filter" \
  -H 'Content-Type: application/json' \
  -d '{"companyName":"Acme 3PL","status":"PENDING"}'
```
- **Example responses**:
  - 200 OK: array of `LspResponse` or message string when none
  - 400 Bad Request: "Invalid status. Allowed values: PENDING, INPROCESS, COMPLETED."

### Filter responses by tender and company (view DTO)
- **Method**: POST
- **URL**: `/api/lsp/responses/filter-by-tender`
- **Purpose**: For a given company and tender number, return simplified LSP bid views.
- **Request body** (`LspResponseFilterRequest`):
```json
{ "companyName": "Acme 3PL", "tender_no": "TND-2025-0001" }
```
- **Example request**:
```bash
curl -s -X POST "$BASE_URL/api/lsp/responses/filter-by-tender" \
  -H 'Content-Type: application/json' \
  -d '{"companyName":"Acme 3PL","tender_no":"TND-2025-0001"}'
```
- **Example responses**:
  - 200 OK: array of `LspResponseViewDTO`

## 3PL Actions on Responses and Assignments

### Confirm an LSP response for a tender
- **Method**: POST
- **URL**: `/3pl/confirm-lsp`
- **Purpose**: 3PL confirms the selected LSP and rejects all others.
- **Request body** (`LspConfirmationRequest`):
```json
{ "tenderNo": "TND-2025-0001", "lspCompanyName": "BestLSP" }
```
- **Example request**:
```bash
curl -s -X POST "$BASE_URL/3pl/confirm-lsp" \
  -H 'Content-Type: application/json' \
  -d '{"tenderNo":"TND-2025-0001","lspCompanyName":"BestLSP"}'
```
- **Example responses**:
  - 200 OK: confirmation message string
  - 400/500: message string

### Get all responses for a tender
- **Method**: GET
- **URL**: `/3pl/tender/{tenderNo}/responses`
- **Purpose**: List all LSP responses submitted for a tender.
- **Example request**:
```bash
curl -s -X GET "$BASE_URL/3pl/tender/TND-2025-0001/responses"
```
- **Example responses**:
  - 200 OK: array of `LspResponse`

### Get confirmed response for a tender
- **Method**: GET
- **URL**: `/3pl/tender/{tenderNo}/confirmed-response`
- **Purpose**: Retrieve the confirmed LSP response for a tender.
- **Example request**:
```bash
curl -s -X GET "$BASE_URL/3pl/tender/TND-2025-0001/confirmed-response"
```
- **Example responses**:
  - 200 OK: `LspResponse`
  - 404 Not Found

### Get assignment details for a tender
- **Method**: GET
- **URL**: `/3pl/tender/{tenderNo}/assignment`
- **Purpose**: View assignment details after confirmation and vehicle submission.
- **Example request**:
```bash
curl -s -X GET "$BASE_URL/3pl/tender/TND-2025-0001/assignment"
```
- **Example responses**:
  - 200 OK: `LspAssignment`
  - 404 Not Found

### Get all assignments for a 3PL company
- **Method**: GET
- **URL**: `/3pl/assignments`
- **Purpose**: List all assignments under a 3PL company.
- **Query params**: `companyName` (required)
- **Example request**:
```bash
curl -s -X GET "$BASE_URL/3pl/assignments?companyName=Acme%203PL"
```
- **Example responses**:
  - 200 OK: array of `LspAssignment`

## LSP Assignments (Vehicle Details)

### Submit vehicle details by LSP response ID
- **Method**: POST
- **URL**: `/lsp/assignment/{lspResponseId}/vehicle-details`
- **Purpose**: LSP submits the vehicle details for a confirmed response.
- **Request body** (`VehicleDetailsRequest`):
```json
{ "vehicleNumber": "MH12AB1234", "driverName": "John Doe", "driverContact": "9876543210" }
```
- **Example request**:
```bash
curl -s -X POST "$BASE_URL/lsp/assignment/10/vehicle-details" \
  -H 'Content-Type: application/json' \
  -d '{"vehicleNumber":"MH12AB1234","driverName":"John Doe","driverContact":"9876543210"}'
```
- **Example responses**:
  - 201 Created: `LspAssignment`
  - 500 Internal Server Error

### Submit vehicle details by tender number
- **Method**: POST
- **URL**: `/lsp/assignment/{tenderNo}/vehicle-details`
- **Purpose**: Alternate submission using tender number.
- **Request body**: same as above
- **Example request**:
```bash
curl -s -X POST "$BASE_URL/lsp/assignment/TND-2025-0001/vehicle-details" \
  -H 'Content-Type: application/json' \
  -d '{"vehicleNumber":"MH12AB1234","driverName":"John Doe","driverContact":"9876543210"}'
```
- **Example responses**:
  - 201 Created: `LspAssignment`
  - 500 Internal Server Error

### Get assignment by tender number
- **Method**: GET
- **URL**: `/lsp/assignment/tender/{tenderNo}`
- **Purpose**: Retrieve assignment details using tender number.
- **Example request**:
```bash
curl -s -X GET "$BASE_URL/lsp/assignment/tender/TND-2025-0001"
```
- **Example responses**:
  - 200 OK: `LspAssignment`
  - 404 Not Found

### Get assignment by LSP response ID
- **Method**: GET
- **URL**: `/lsp/assignment/{lspResponseId}`
- **Purpose**: Retrieve assignment details using the response ID.
- **Example request**:
```bash
curl -s -X GET "$BASE_URL/lsp/assignment/10"
```
- **Example responses**:
  - 200 OK: `LspAssignment`
  - 404 Not Found

### Get all assignments for an LSP company
- **Method**: GET
- **URL**: `/lsp/assignment/company/{companyName}`
- **Purpose**: List all assignments that were assigned to a given LSP.
- **Example request**:
```bash
curl -s -X GET "$BASE_URL/lsp/assignment/company/BestLSP"
```
- **Example responses**:
  - 200 OK: array of `LspAssignment`

## Data Models (Response Schemas)

### Tender
```json
{
  "id": 1,
  "tenderNo": "TND-2025-0001",
  "sourceLocation": "Pune",
  "destinationLocation": "Delhi",
  "pickupDate": "2025-01-15",
  "dropDate": "2025-01-16",
  "weight": 1200.5,
  "specialInstructions": "Handle with care",
  "status": "ACTIVE",
  "createdBy": "Acme 3PL",
  "createdAt": "2025-01-10T14:30:00",
  "tenderPrice": 50000
}
```

### LspResponse
```json
{
  "id": 10,
  "tenderNo": "TND-2025-0001",
  "lspCompanyName": "BestLSP",
  "createdByCompanyName": "Acme 3PL",
  "sourceLocation": "Pune",
  "destinationLocation": "Delhi",
  "pickupDate": "2025-01-15",
  "dropDate": "2025-01-16",
  "weight": 1200.5,
  "specialInstructions": "Handle with care",
  "tenderPrice": 50000,
  "estimatedArrivalDate": "2025-01-17",
  "bidPrice": 47000,
  "lspMessage": "We can deliver on time",
  "status": "ACTIVE",
  "selectionStatus": "PENDING",
  "createdAt": "2025-01-10T15:00:00"
}
```

### LspResponseViewDTO
```json
{
  "lspCompanyName": "BestLSP",
  "bidPrice": 47000,
  "estimatedArrivalDate": "2025-01-17",
  "lspMessage": "We can deliver on time"
}
```

### LspAssignment
```json
{
  "id": 5,
  "lspResponseId": 10,
  "tenderNo": "TND-2025-0001",
  "assignedBy3pl": "Acme 3PL",
  "lspCompanyName": "BestLSP",
  "vehicleNumber": "MH12AB1234",
  "driverName": "John Doe",
  "driverContact": "9876543210",
  "assignedAt": "2025-01-12T09:00:00"
}
```

### Signup (user record)
```json
{
  "id": 100,
  "email": "user@example.com",
  "companyName": "Acme 3PL",
  "mobileNumber": "9876543210",
  "password": "<hashed>",
  "role": "LSP",
  "location": "Pune",
  "fcmToken": "..."
}
```

## Why these endpoints exist
- **Signup & OTP flows**: Verify and onboard companies/users securely.
- **Email and Mobile logins**: Support both traditional and OTP-based flows for field teams.
- **Tender lifecycle**: 3PLs publish loads; statuses enable tracking from active to completion.
- **LSP responses**: LSPs bid with price/ETA/messages; filters power dashboards and follow-ups.
- **3PL confirmation**: Single-award behavior locks the chosen LSP and rejects others.
- **Vehicle submission**: Capture operational details (vehicle/driver) for execution and compliance.
- **Assignments visibility**: Both sides can query their active/assigned work.



