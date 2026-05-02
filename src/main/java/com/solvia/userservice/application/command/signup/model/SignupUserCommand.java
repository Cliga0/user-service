package com.solvia.userservice.application.command.signup.model;

import java.io.Serializable;
import java.time.LocalDate;

public record SignupUserCommand(
        String email,
        String phoneNumber,
        String firstName,
        String lastName,
        String password,
        String externalAuthId,
        String gender,
        LocalDate birthDate
) implements Serializable {}