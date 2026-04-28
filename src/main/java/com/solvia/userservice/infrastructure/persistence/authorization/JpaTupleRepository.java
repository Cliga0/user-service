package com.solvia.userservice.infrastructure.persistence.authorization;

import com.solvia.userservice.domain.authorization.*;
import org.springframework.stereotype.Repository;

@Repository
public class JpaTupleRepository implements TupleRepository {

    private final SpringDataTupleRepository repository;

    public JpaTupleRepository(SpringDataTupleRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean exists(String subject, Relation relation, Resource resource) {
        return repository.existsBySubjectAndRelationAndResource(
                subject,
                relation.name(),
                resource.toKey()
        );
    }
}