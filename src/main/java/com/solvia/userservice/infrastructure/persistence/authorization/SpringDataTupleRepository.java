package com.solvia.userservice.infrastructure.persistence.authorization;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataTupleRepository
        extends JpaRepository<TupleEntity, String> {

    boolean existsBySubjectAndRelationAndResource(
            String subject,
            String relation,
            String resource
    );
}