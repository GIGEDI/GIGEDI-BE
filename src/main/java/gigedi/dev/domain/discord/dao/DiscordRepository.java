package gigedi.dev.domain.discord.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import gigedi.dev.domain.discord.domain.Discord;
import gigedi.dev.domain.member.domain.Member;

@Repository
public interface DiscordRepository extends JpaRepository<Discord, Long> {
    Optional<Discord> findByMember(Member member);
}
