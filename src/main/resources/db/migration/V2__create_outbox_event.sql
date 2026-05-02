-- ==================================================================================================
--  Flyway Migration:V2__create_outbox_event.sql
-- --------------------------------------------------------------------------------------------------
--  Description : Création de la table "outbox_event"
--                Contexte : Outbox Pattern (Transactional Messaging)
--                Objectif : Garantir la fiabilité de publication des événements domaine
-- --------------------------------------------------------------------------------------------------
--  Architecture  : DDD / Hexagonal / CQRS / Event-Driven Ready
--  Auteur        : Solvia Platform Team
--  Date          : 2026-05-02
--  DB            : PostgreSQL 15+
--  Encoding      : UTF-8
-- ==================================================================================================

-- ================================================================================================
-- Schema
-- ================================================================================================
SET search_path TO userservice;

-- ================================================================================================
-- Table: outbox_event
-- ================================================================================================
CREATE TABLE IF NOT EXISTS outbox_event (

    -- ============================================================================================
    -- 1. Identity
    -- ============================================================================================
                                            id                UUID PRIMARY KEY,

    -- ============================================================================================
    -- 2. Aggregate reference (DDD event source)
    -- ============================================================================================
                                            aggregate_type   VARCHAR(100) NOT NULL,
    aggregate_id     VARCHAR(100) NOT NULL,

    -- ============================================================================================
    -- 3. Event metadata
    -- ============================================================================================
    event_type       VARCHAR(150) NOT NULL,
    event_version    VARCHAR(20)  NOT NULL,

    tenant_id        VARCHAR(100) NOT NULL,
    trace_id         VARCHAR(100) NOT NULL,

    occurred_at      TIMESTAMP(3) NOT NULL,

    -- ============================================================================================
    -- 4. Payload (event body)
    -- ============================================================================================
    payload          TEXT NOT NULL,
    headers_json     TEXT,

    -- ============================================================================================
    -- 5. Processing state (Outbox lifecycle)
    -- ============================================================================================
    status           VARCHAR(20) NOT NULL, -- PENDING | PUBLISHED | FAILED

    retry_count      INT NOT NULL DEFAULT 0,
    next_retry_at    TIMESTAMP(3),

    -- ============================================================================================
    -- 6. Audit / processing timestamps
    -- ============================================================================================
    created_at       TIMESTAMP(3) NOT NULL DEFAULT NOW(),
    last_attempt_at   TIMESTAMP(3),
    published_at      TIMESTAMP(3)
    );

-- ================================================================================================
-- Indexes (CRITICAL for OUTBOX performance)
-- ================================================================================================

-- Worker polling (core performance path)
CREATE INDEX IF NOT EXISTS idx_outbox_status_created
    ON outbox_event (status, created_at);

-- Retry mechanism
CREATE INDEX IF NOT EXISTS idx_outbox_retry
    ON outbox_event (next_retry_at);

-- Multi-tenant isolation (future-proof)
CREATE INDEX IF NOT EXISTS idx_outbox_tenant
    ON outbox_event (tenant_id);

-- Aggregate tracing (debug / replay / observability)
CREATE INDEX IF NOT EXISTS idx_outbox_aggregate
    ON outbox_event (aggregate_type, aggregate_id);

-- Trace correlation (distributed tracing)
CREATE INDEX IF NOT EXISTS idx_outbox_trace
    ON outbox_event (trace_id);

-- ================================================================================================
-- Constraints (domain safety)
-- ================================================================================================

ALTER TABLE outbox_event
    ADD CONSTRAINT chk_outbox_status
        CHECK (status IN ('PENDING', 'PUBLISHED', 'FAILED'));

-- ================================================================================================
-- Comments (FAANG-level documentation)
-- ================================================================================================

COMMENT ON TABLE outbox_event IS
'Outbox Pattern table used to guarantee transactional consistency between database state and event publishing (Kafka / CDC / Messaging broker).';

COMMENT ON COLUMN outbox_event.aggregate_type IS
'Type of aggregate emitting the event (e.g. User, Order, Payment).';

COMMENT ON COLUMN outbox_event.aggregate_id IS
'Identifier of the aggregate instance producing the event.';

COMMENT ON COLUMN outbox_event.event_type IS
'Domain event name (e.g. UserCreated, UserUpdated).';

COMMENT ON COLUMN outbox_event.event_version IS
'Version of the event schema for backward compatibility.';

COMMENT ON COLUMN outbox_event.payload IS
'Serialized event payload (JSON). Must remain immutable once written.';

COMMENT ON COLUMN outbox_event.status IS
'Processing state: PENDING → PUBLISHED or FAILED (retryable).';

COMMENT ON COLUMN outbox_event.retry_count IS
'Number of retry attempts for failed delivery.';

-- ================================================================================================
-- Performance Note (IMPORTANT)
-- ================================================================================================
-- This table is designed for:
--  - High-frequency inserts (write path)
--  - Batch polling with SKIP LOCKED (multi-instance workers)
--  - Append-only pattern (no deletes recommended)
-- ================================================================================================