package com.solvia.userservice.domain.authorization;

public interface TupleRepository {

    boolean exists(String subject, Relation relation, Resource resource);

}