package com.solvia.userservice.infrastructure.authorization;

import com.solvia.userservice.domain.authorization.TupleRepository;
import com.solvia.userservice.domain.authorization.Relation;
import com.solvia.userservice.domain.authorization.Resource;

public class InMemoryTupleRepository implements TupleRepository {

    @Override
    public boolean exists(String subject, Relation relation, Resource resource) {
        return true;
    }
}