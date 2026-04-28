package com.solvia.userservice.application.command.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

/**
 * CreateUserCommand
 * <p>
 * PURE INTENT OBJECT (CQRS / DDD)
 * <p>
 * Responsabilités :
 * - transporter une intention métier
 * - être immuable
 * - être indépendant du domaine
 * - être utilisable par REST / Kafka / Batch
 * <p>
 * ⚠️ Aucun comportement métier, aucune validation, aucun mapping
 */
public record CreateUserCommand(

    // =========================
    // IDENTITY
    // =========================
    UUID userId,
    String externalAuthId,

    // =========================
    // PERSON
    // =========================
    String firstName,
    String lastName,
    LocalDate birthDate,
    String gender,

    // =========================
    // CONTACT (raw input)
    // =========================
    String email,
    String phoneNumber,

    // =========================
    // BUSINESS
    // =========================
    String role,

    // =========================
    // AUDIT / SECURITY CONTEXT
    // =========================
    UUID actorId

) implements Serializable {}
