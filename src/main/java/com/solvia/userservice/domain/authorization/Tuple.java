package com.solvia.userservice.domain.authorization;

import java.util.Objects;

public record Tuple(
        String subject,
        Relation relation,
        Resource resource
) {
    public Tuple {
        Objects.requireNonNull(subject, "Subject required");
        Objects.requireNonNull(relation, "Relation required");
        Objects.requireNonNull(resource, "Resource required");

        if (subject.isBlank()) {
            throw new IllegalArgumentException("Subject cannot be blank");
        }
    }
}