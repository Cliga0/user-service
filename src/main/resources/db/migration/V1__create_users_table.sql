-- ==================================================================================================
--  Flyway Migration: V1__create_users_table.sql
-- --------------------------------------------------------------------------------------------------
--  Description : Création de la table "users"
--                Contexte : User Service (Bounded Context Identity / User Management)
--                Architecture : DDD + Clean Architecture + JPA Persistence Model
--                Objectif : Stockage des agrégats User (write-side)
-- --------------------------------------------------------------------------------------------------
--  Auteur       : Solvia Platform Team
--  Date         : 2026-05-02
--  Compatible   : PostgreSQL 15+
--  Encoding     : UTF-8
--  Flyway Scope : Baseline V1
-- ==================================================================================================

-- ================================================================================================
-- Schéma
-- ================================================================================================
SET search_path TO public;

-- ================================================================================================
-- Table: users
-- ================================================================================================
CREATE TABLE IF NOT EXISTS users (

    -- --------------------------------------------------------------------------------------------
    -- 1. Identity (Aggregate Root)
    -- --------------------------------------------------------------------------------------------
                                     id                  UUID PRIMARY KEY,                              -- Identifiant unique du User (Aggregate Root)

                                     external_auth_id    VARCHAR(255),                                  -- ID provider externe (Keycloak / Auth provider)

-- --------------------------------------------------------------------------------------------
-- 2. Person (Domain core identity)
-- --------------------------------------------------------------------------------------------
    first_name          VARCHAR(150) NOT NULL,                         -- Prénom
    last_name           VARCHAR(150) NOT NULL,                         -- Nom
    birth_date          DATE NOT NULL,                                 -- Date de naissance
    gender              VARCHAR(20) NOT NULL,                          -- Gender (enum string)

-- --------------------------------------------------------------------------------------------
-- 3. Contact
-- --------------------------------------------------------------------------------------------
    email               VARCHAR(255),                                  -- Email (nullable selon stratégie métier)
    phone_number        VARCHAR(50),                                   -- Téléphone international format

-- --------------------------------------------------------------------------------------------
-- 4. Business attributes
-- --------------------------------------------------------------------------------------------
    role                VARCHAR(50) NOT NULL,                          -- USER, ADMIN, MERCHANT, etc.
    balance             NUMERIC(19,2) NOT NULL DEFAULT 0,              -- Solde utilisateur (wallet / ledger)
    loyalty_points      INTEGER NOT NULL DEFAULT 0,                    -- Points fidélité

-- --------------------------------------------------------------------------------------------
-- 5. Lifecycle / Status
-- --------------------------------------------------------------------------------------------
    status              VARCHAR(30) NOT NULL,                          -- ACTIVE, INACTIVE, SUSPENDED, DELETED

-- --------------------------------------------------------------------------------------------
-- 6. Audit (DDD / production-grade traceability)
-- --------------------------------------------------------------------------------------------
    created_at          TIMESTAMP(3) NOT NULL,                         -- Création (UTC)
    updated_at          TIMESTAMP(3) NOT NULL,                         -- Dernière modification (UTC)
    created_by          VARCHAR(100) NOT NULL,                         -- ActorId (system/user/service)
    updated_by          VARCHAR(100) NOT NULL                          -- ActorId (last modifier)
    );

-- ================================================================================================
-- Indexes (performance + scale)
-- ================================================================================================

-- Email lookup (login / identity search)
CREATE INDEX IF NOT EXISTS idx_users_email
    ON users (email);

-- External auth lookup (Keycloak / OAuth2 mapping)
CREATE INDEX IF NOT EXISTS idx_users_external_auth
    ON users (external_auth_id);

-- Role-based filtering (admin dashboards / segmentation)
CREATE INDEX IF NOT EXISTS idx_users_role
    ON users (role);

-- Status filtering (active users, soft delete strategy)
CREATE INDEX IF NOT EXISTS idx_users_status
    ON users (status);

-- Audit queries (debug / observability / compliance)
CREATE INDEX IF NOT EXISTS idx_users_created_at
    ON users (created_at);

-- ================================================================================================
-- Constraints (data integrity)
-- ================================================================================================

ALTER TABLE users
    ADD CONSTRAINT chk_users_gender
        CHECK (gender IN ('MALE', 'FEMALE', 'OTHER', 'UNKNOWN'));

ALTER TABLE users
    ADD CONSTRAINT chk_users_status
        CHECK (status IN ('ACTIVE', 'INACTIVE', 'SUSPENDED', 'DELETED'));

ALTER TABLE users
    ADD CONSTRAINT chk_users_balance_non_negative
        CHECK (balance >= 0);

ALTER TABLE users
    ADD CONSTRAINT chk_users_loyalty_points_non_negative
        CHECK (loyalty_points >= 0);

-- ================================================================================================
-- Comments (DDD documentation embedded in DB)
-- ================================================================================================

COMMENT ON TABLE users IS
    'User aggregate root table for User Service bounded context. '
    'Write-side persistence model aligned with DDD and Clean Architecture.';

COMMENT ON COLUMN users.id IS 'Aggregate Root ID (UUID).';
COMMENT ON COLUMN users.external_auth_id IS 'External authentication provider identifier.';
COMMENT ON COLUMN users.first_name IS 'User first name (domain value object).';
COMMENT ON COLUMN users.last_name IS 'User last name (domain value object).';
COMMENT ON COLUMN users.birth_date IS 'User birth date.';
COMMENT ON COLUMN users.gender IS 'Gender enum representation.';
COMMENT ON COLUMN users.email IS 'Email address (nullable depending on onboarding flow).';
COMMENT ON COLUMN users.phone_number IS 'International phone number.';
COMMENT ON COLUMN users.role IS 'User role within the system.';
COMMENT ON COLUMN users.balance IS 'User financial balance (ledger-like field).';
COMMENT ON COLUMN users.loyalty_points IS 'User loyalty points.';
COMMENT ON COLUMN users.status IS 'Lifecycle status of the user aggregate.';
COMMENT ON COLUMN users.created_at IS 'Creation timestamp (UTC).';
COMMENT ON COLUMN users.updated_at IS 'Last update timestamp (UTC).';
COMMENT ON COLUMN users.created_by IS 'Actor who created the record.';
COMMENT ON COLUMN users.updated_by IS 'Actor who last updated the record.';