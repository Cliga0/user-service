package com.solvia.userservice.infrastructure.persistence.user.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * UserEntity
 *
 * <p>
 * Représentation persistence du User.
 *
 * <p>
 * Aucun comportement métier ici
 * Strictement technique (JPA)
 *
 * <p>
 * - Optimisé DB
 * - Flat structure
 * - Scalable
 */
@Entity
@Table(name = "users")
public class UserEntity {

    // =========================
    // IDENTITY
    // =========================

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "external_auth_id")
    private String externalAuthId;

    // =========================
    // PERSON
    // =========================

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @Column(name = "gender", nullable = false)
    private String gender;

    // =========================
    // CONTACT
    // =========================

    @Column(name = "email")
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    // =========================
    // ADDRESS (simplifié)
    // =========================

    @ElementCollection
    @CollectionTable(name = "user_addresses", joinColumns = @JoinColumn(name = "user_id"))
    private List<AddressEmbeddable> addresses = new ArrayList<>();

    // =========================
    // BUSINESS
    // =========================

    @Column(name = "role", nullable = false)
    private String role;

    @Column(name = "balance", nullable = false)
    private BigDecimal balance;

    @Column(name = "loyalty_points", nullable = false)
    private Integer loyaltyPoints;

    // =========================
    // METADATA (audit)
    // =========================

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Column(name = "created_by", nullable = false, updatable = false)
    private String createdBy;

    @Column(name = "updated_by", nullable = false)
    private String updatedBy;

    // =========================
    // CONSTRUCTORS
    // =========================

    protected UserEntity() {
        // JPA only
    }

    // =========================
    // GETTERS / SETTERS
    // =========================

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getExternalAuthId() { return externalAuthId; }
    public void setExternalAuthId(String externalAuthId) { this.externalAuthId = externalAuthId; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public LocalDate getBirthDate() { return birthDate; }
    public void setBirthDate(LocalDate birthDate) { this.birthDate = birthDate; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public List<AddressEmbeddable> getAddresses() { return addresses; }
    public void setAddresses(List<AddressEmbeddable> addresses) { this.addresses = addresses; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public BigDecimal getBalance() { return balance; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }

    public Integer getLoyaltyPoints() { return loyaltyPoints; }
    public void setLoyaltyPoints(Integer loyaltyPoints) { this.loyaltyPoints = loyaltyPoints; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }

    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }

    public String getUpdatedBy() { return updatedBy; }
    public void setUpdatedBy(String updatedBy) { this.updatedBy = updatedBy; }
}
