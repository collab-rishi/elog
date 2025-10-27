-- Drop the entire database if it already exists
DROP DATABASE IF EXISTS ELogisticsTendering;

-- Create and select the new database
CREATE DATABASE ELogisticsTendering;
USE ELogisticsTendering;

-- USERS table
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    company_name VARCHAR(255) NOT NULL UNIQUE,
    location VARCHAR(255),
    mobile_number VARCHAR(20) NOT NULL,
    password VARCHAR(255) NOT NULL,
    role ENUM('LSP', '3PL') NOT NULL,
    fcm_token VARCHAR(512),

    CONSTRAINT chk_role CHECK (role IN ('LSP', '3PL')),
    CONSTRAINT chk_mobile_number CHECK (mobile_number REGEXP '^[6-9][0-9]{9}$'),
    CONSTRAINT chk_password CHECK (
        password REGEXP '^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!]).{8,}$'
    )
);

-- TENDERS table (Created by 3PL)
CREATE TABLE tenders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    tender_no VARCHAR(50) NOT NULL UNIQUE,
    source_location VARCHAR(255) NOT NULL,
    destination_location VARCHAR(255) NOT NULL,
    pickup_date DATE NOT NULL,
    drop_date DATE NOT NULL,
    weight DECIMAL(10, 2) NOT NULL,
    special_instructions TEXT,
    tender_status ENUM('ACTIVE', 'PENDING', 'COMPLETED') DEFAULT 'ACTIVE',
    created_by VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    tender_price DECIMAL(10, 2) NOT NULL,

    CONSTRAINT fk_tender_creator FOREIGN KEY (created_by) REFERENCES users(company_name) ON DELETE CASCADE,
    CONSTRAINT chk_status CHECK (tender_status IN ('ACTIVE', 'PENDING', 'COMPLETED'))
);

-- LSP_RESPONSES table
CREATE TABLE lsp_responses (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    tender_no VARCHAR(50) NOT NULL,
    lsp_company_name VARCHAR(255) NOT NULL,

    -- Copied fields from tenders
    source_location VARCHAR(255) NOT NULL,
    destination_location VARCHAR(255) NOT NULL,
    pickup_date DATE NOT NULL,
    drop_date DATE NOT NULL,
    weight DECIMAL(10, 2) NOT NULL,
    special_instructions TEXT,
    tender_price DECIMAL(10, 2) NOT NULL,
    created_by_company_name VARCHAR(255) NOT NULL,

    -- Response fields
    estimated_arrival_date DATE,
    bid_price DECIMAL(10, 2),
    lsp_message TEXT,
    response_status ENUM('ACTIVE', 'INPROCESS', 'COMPLETED', 'PENDING') DEFAULT 'ACTIVE',
    selection_status ENUM('PENDING', 'CONFIRMED', 'REJECTED') DEFAULT 'PENDING',

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_lsp_user FOREIGN KEY (lsp_company_name) REFERENCES users(company_name) ON DELETE CASCADE,
    CONSTRAINT fk_lsp_creator FOREIGN KEY (created_by_company_name) REFERENCES users(company_name) ON DELETE CASCADE,
    CONSTRAINT fk_lsp_tender FOREIGN KEY (tender_no) REFERENCES tenders(tender_no) ON DELETE CASCADE
);

-- LSP_ASSIGNMENTS table
CREATE TABLE lsp_assignments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    lsp_response_id BIGINT NOT NULL UNIQUE,
    tender_no VARCHAR(50) NOT NULL,
    assigned_by_3pl VARCHAR(255) NOT NULL,
    lsp_company_name VARCHAR(255) NOT NULL,

    vehicle_number VARCHAR(50) NOT NULL,
    driver_name VARCHAR(255) NOT NULL,
    driver_contact VARCHAR(20) NOT NULL,

    assigned_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_assignment_response FOREIGN KEY (lsp_response_id) REFERENCES lsp_responses(id) ON DELETE CASCADE,
    CONSTRAINT fk_assignment_tender FOREIGN KEY (tender_no) REFERENCES tenders(tender_no) ON DELETE CASCADE,
    CONSTRAINT fk_assignment_lsp FOREIGN KEY (lsp_company_name) REFERENCES users(company_name) ON DELETE CASCADE,
    CONSTRAINT fk_assignment_3pl FOREIGN KEY (assigned_by_3pl) REFERENCES users(company_name) ON DELETE CASCADE
);

-- Test Queries
SELECT * FROM lsp_responses;
SELECT * FROM lsp_assignments;
