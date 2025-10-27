# ELogisticsTendering API Documentation

## Authentication & Account Management

### Health Check
- **Method**: GET
- **URL**: `/auth/ping`
- **Function**: Service health check
- **Response**: 200 OK - "ELogisticsTendering is up and running."

### User Registration
- **Method**: POST
- **URL**: `/auth/signup`
- **Function**: Register new company user and send OTP
- **Request Body**: SignupDTO (email, companyName, mobileNumber, location, password, role)
- **Response**: 201 Created - OTP sent message

### Verify Registration OTP
- **Method**: POST
- **URL**: `/auth/verify-signup-otp`
- **Function**: Verify OTP and create account
- **Request Body**: SignupVerificationDTO (mobileNumber, otp)
- **Response**: 200 OK - Account created message

### Email Login
- **Method**: POST
- **URL**: `/auth/login`
- **Function**: Login with email and password
- **Request Body**: EmailLoginDTO (email, password)
- **Response**: 200 OK - Success message

### Mobile Login (Request OTP)
- **Method**: POST
- **URL**: `/auth/login-with-mobile`
- **Function**: Request OTP for mobile login
- **Request Body**: MobileLoginDTO (mobileNumber)
- **Response**: 200 OK - OTP sent message

### Verify Mobile Login OTP
- **Method**: POST
- **URL**: `/auth/verify-otp`
- **Function**: Verify OTP for mobile login
- **Request Body**: MobileOtpVerifyDTO (mobileNumber, otp)
- **Response**: 200 OK - Login success message

## Password Reset

### Request Password Reset
- **Method**: POST
- **URL**: `/auth/request-password-reset`
- **Function**: Send reset OTP to email
- **Request Body**: VerifyResetOtpDTO (email)
- **Response**: 200 OK - Reset OTP sent message

### Verify Reset OTP
- **Method**: POST
- **URL**: `/auth/verify-reset-otp`
- **Function**: Verify reset OTP
- **Request Body**: VerifyResetOtpDTO (email, otp)
- **Response**: 200 OK - OTP verified message

### Reset Password
- **Method**: POST
- **URL**: `/auth/reset-password`
- **Function**: Set new password after OTP verification
- **Request Body**: PasswordResetDTO (email, newPassword, confirmPassword)
- **Response**: 200 OK - Password reset message

## User Management

### Get All LSP Users
- **Method**: GET
- **URL**: `/users/lsp`
- **Function**: Retrieve all users with LSP role
- **Response**: 200 OK - List of Signup objects

### Get All 3PL Users
- **Method**: GET
- **URL**: `/users/3pl`
- **Function**: Retrieve all users with THREE_PL role
- **Response**: 200 OK - List of Signup objects

### Get All Users
- **Method**: GET
- **URL**: `/users/all`
- **Function**: Retrieve all user records
- **Response**: 200 OK - List of Signup objects

### Get User Profile
- **Method**: POST
- **URL**: `/users/profile`
- **Function**: Get user profile by email and mobile
- **Request Body**: ProfileRequestDTO (email, mobileNumber)
- **Response**: 200 OK - SignupDTO object

## Tender Management (3PL)

### Create Tender
- **Method**: POST
- **URL**: `/3PL/tenders/create`
- **Function**: Create new tender for LSPs to respond
- **Query Param**: companyName (required)
- **Request Body**: TenderDTO (sourceLocation, destinationLocation, pickupDate, dropDate, weight, specialInstructions, tenderPrice)
- **Response**: 201 Created - Tender object

### Search Tenders
- **Method**: POST
- **URL**: `/3PL/tenders/search`
- **Function**: Search tenders by company and status
- **Request Body**: TenderSearchRequest (companyName, status)
- **Response**: 200 OK - List of Tender objects

## LSP Response Management

### Get Responses by Company
- **Method**: GET
- **URL**: `/api/lsp/responses`
- **Function**: Get LSP responses filtered by 3PL company
- **Query Param**: companyName (required)
- **Response**: 200 OK - List of LspResponse objects

### Reply to Tender
- **Method**: PUT
- **URL**: `/api/lsp/responses/reply/{tenderNo}`
- **Function**: Submit LSP bid/response to tender
- **Path Param**: tenderNo (required)
- **Query Param**: companyName (required)
- **Request Body**: TenderReplyRequest (estimatedArrivalDate, bidPrice, lspMessage)
- **Response**: 200 OK - Success message

### Filter Responses by Status
- **Method**: POST
- **URL**: `/api/lsp/responses/filter`
- **Function**: Filter responses by company and status
- **Request Body**: ResponseSearchRequest (companyName, status)
- **Response**: 200 OK - List of LspResponse objects

### Filter Responses by Tender
- **Method**: POST
- **URL**: `/api/lsp/responses/filter-by-tender`
- **Function**: Get simplified LSP bid views for tender
- **Request Body**: LspResponseFilterRequest (companyName, tender_no)
- **Response**: 200 OK - List of LspResponseViewDTO objects

## 3PL Actions

### Confirm LSP Response
- **Method**: POST
- **URL**: `/3pl/confirm-lsp`
- **Function**: Confirm selected LSP and reject others
- **Request Body**: LspConfirmationRequest (tenderNo, lspCompanyName)
- **Response**: 200 OK - Confirmation message

### Get All Responses for Tender
- **Method**: GET
- **URL**: `/3pl/tender/{tenderNo}/responses`
- **Function**: Get all LSP responses for a tender
- **Path Param**: tenderNo (required)
- **Response**: 200 OK - List of LspResponse objects

### Get Confirmed Response
- **Method**: GET
- **URL**: `/3pl/tender/{tenderNo}/confirmed-response`
- **Function**: Get confirmed LSP response for tender
- **Path Param**: tenderNo (required)
- **Response**: 200 OK - LspResponse object

### Get Assignment Details
- **Method**: GET
- **URL**: `/3pl/tender/{tenderNo}/assignment`
- **Function**: Get assignment details for tender
- **Path Param**: tenderNo (required)
- **Response**: 200 OK - LspAssignment object

### Get All 3PL Assignments
- **Method**: GET
- **URL**: `/3pl/assignments`
- **Function**: Get all assignments for 3PL company
- **Query Param**: companyName (required)
- **Response**: 200 OK - List of LspAssignment objects

## LSP Assignment Management

### Submit Vehicle Details (by Response ID)
- **Method**: POST
- **URL**: `/lsp/assignment/{lspResponseId}/vehicle-details`
- **Function**: Submit vehicle details for confirmed response
- **Path Param**: lspResponseId (required)
- **Request Body**: VehicleDetailsRequest (vehicleNumber, driverName, driverContact)
- **Response**: 201 Created - LspAssignment object

### Submit Vehicle Details (by Tender Number)
- **Method**: POST
- **URL**: `/lsp/assignment/{tenderNo}/vehicle-details`
- **Function**: Submit vehicle details using tender number
- **Path Param**: tenderNo (required)
- **Request Body**: VehicleDetailsRequest (vehicleNumber, driverName, driverContact)
- **Response**: 201 Created - LspAssignment object

### Get Assignment by Tender Number
- **Method**: GET
- **URL**: `/lsp/assignment/tender/{tenderNo}`
- **Function**: Get assignment details by tender number
- **Path Param**: tenderNo (required)
- **Response**: 200 OK - LspAssignment object

### Get Assignment by Response ID
- **Method**: GET
- **URL**: `/lsp/assignment/{lspResponseId}`
- **Function**: Get assignment details by response ID
- **Path Param**: lspResponseId (required)
- **Response**: 200 OK - LspAssignment object

### Get All LSP Assignments
- **Method**: GET
- **URL**: `/lsp/assignment/company/{companyName}`
- **Function**: Get all assignments for LSP company
- **Path Param**: companyName (required)
- **Response**: 200 OK - List of LspAssignment objects
