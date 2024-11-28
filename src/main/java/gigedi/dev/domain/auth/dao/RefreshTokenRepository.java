package gigedi.dev.domain.auth.dao;

import org.springframework.data.repository.CrudRepository;

import gigedi.dev.domain.auth.domain.RefreshToken;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, Long> {}
