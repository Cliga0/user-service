package com.solvia.userservice.infrastructure.persistence.authorization;

import jakarta.persistence.*;

@Entity
@Table(name = "auth_tuples",
        indexes = {
                @Index(name = "idx_tuple_lookup", columnList = "subject,relation,resource")
        })
public class TupleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String subject;
    private String relation;
    private String resource;

    protected TupleEntity() {}

    public TupleEntity(String subject, String relation, String resource) {
        this.subject = subject;
        this.relation = relation;
        this.resource = resource;
    }

    // getters
}