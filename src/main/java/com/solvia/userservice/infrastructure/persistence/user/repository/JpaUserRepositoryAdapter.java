package com.solvia.userservice.infrastructure.persistence.user.repository;

import com.solvia.userservice.application.port.out.UserRepository;
import com.solvia.userservice.domain.model.aggregate.User;
import com.solvia.userservice.domain.model.vo.contact.Email;
import com.solvia.userservice.domain.model.vo.identity.UserId;
import com.solvia.userservice.infrastructure.persistence.user.entity.UserEntity;
import com.solvia.userservice.infrastructure.persistence.user.entity.UserMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

@Repository
@Transactional
public class JpaUserRepositoryAdapter implements UserRepository {

    private final JpaUserRepository jpaRepository;

    public JpaUserRepositoryAdapter(JpaUserRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    // =========================
    // SAVE
    // =========================

    @Override
    public User save(User user) {
        UserEntity entity = UserMapper.toEntity(user);
        UserEntity saved = jpaRepository.save(entity);
        return UserMapper.toDomain(saved);
    }

    // =========================
    // FIND BY ID
    // =========================

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findById(UserId id) {
        return jpaRepository.findById(id.value())
                .map(UserMapper::toDomain);
    }

    // =========================
    // FIND BY EMAIL (🔥 MANQUANT)
    // =========================

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByEmail(Email email) {

        Objects.requireNonNull(email, "Email must not be null");

        return jpaRepository.findByEmail(email.value())
                .map(UserMapper::toDomain);
    }

    // =========================
    // EXISTS
    // =========================

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(UserId id) {
        return jpaRepository.existsById(id.value());
    }

    // =========================
    // DELETE
    // =========================

    @Override
    public void delete(User user) {
        jpaRepository.deleteById(user.id().value());
    }
}