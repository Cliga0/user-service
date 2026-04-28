package com.solvia.userservice.application.query.model;

import com.solvia.userservice.domain.model.vo.identity.UserId;

import java.io.Serializable;

public record GetUserQuery(
        UserId userId
) implements Serializable {}