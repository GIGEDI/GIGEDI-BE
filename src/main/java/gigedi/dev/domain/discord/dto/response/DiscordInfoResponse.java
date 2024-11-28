package gigedi.dev.domain.discord.dto.response;

import gigedi.dev.domain.discord.domain.Discord;

public record DiscordInfoResponse(Long discordId, String discordEmail) {
    public static DiscordInfoResponse from(Discord discord) {
        return new DiscordInfoResponse(discord.getDiscordId(), discord.getEmail());
    }
}
