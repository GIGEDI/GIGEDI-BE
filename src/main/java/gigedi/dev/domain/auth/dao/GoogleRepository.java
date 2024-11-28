package gigedi.dev.domain.auth.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import gigedi.dev.domain.auth.domain.Google;

public interface GoogleRepository extends JpaRepository<Google, Long> {}
