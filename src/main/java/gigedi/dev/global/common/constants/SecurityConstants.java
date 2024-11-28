package gigedi.dev.global.common.constants;

public final class SecurityConstants {
    public static final String CODE_KEY = "code";
    public static final String CLIENT_ID_KEY = "client_id";
    public static final String CLIENT_SECRET_KEY = "client_secret";
    public static final String REDIRECT_URI_KEY = "redirect_uri";
    public static final String GRANT_TYPE_KEY = "grant_type";
    public static final String REISSUE_GRANT_TYPE_VALUE = "refresh_token";
    public static final String REFRESH_TOKEN = "refresh_token";
    public static final String LOGIN_GRANT_TYPE_VALUE = "authorization_code";

    public static final String GOOGLE_ID_TOKEN_URL = "https://oauth2.googleapis.com/token";
    public static final String GOOGLE_ISSUER = "https://accounts.google.com";
    public static final String GOOGLE_JWK_SET_URL = "https://www.googleapis.com/oauth2/v3/certs";
    public static final String GOOGLE_TOKEN_REISSUE_URL = "https://oauth2.googleapis.com/token";
    public static final String GOOGLE_WITHDRAWAL_URL =
            "https://accounts.google.com/o/oauth2/revoke?token=";

    public static final String DISCORD_HOST = "discord.com";
    public static final String DISCORD_TOKEN_URL = "https://discord.com/api/oauth2/token";
    public static final String DISCORD_USER_INFO_URL = "https://discord.com/api/users/@me";
    public static final String DISCORD_CREATE_DM_CHANNEL_URL =
            "https://discord.com/api/v9/users/@me/channels";
    public static final String DISCORD_GUILD_URL = "https://discord.com/api/v10/guilds";
    public static final String DISCORD_DISCONNECT_URL =
            "https://discord.com/api/oauth2/token/revoke";
    public static final String DISCORD_SEND_DM_URL = "/api/channels/{channelId}/messages";

    public static final String FIGMA_HOST = "api.figma.com";
    public static final String FIGMA_GET_ID_TOKEN_URL = "https://www.figma.com/api/oauth/token";
    public static final String FIGMA_GET_USER_INFO_URL = "https://api.figma.com/v1/me";
    public static final String FIGMA_TOKEN_REISSUE_URL = "/v1/oauth/refresh";
    public static final String FIGMA_FILE_INFO_URL = "/v1/files/{fileId}";

    public static final String TOKEN_ROLE_NAME = "role";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String BOT_TOKEN_PREFIX = "Bot ";
    public static final String BASIC_TOKEN_PREFIX = "Basic ";
    public static final String HTTPS_SCHEME = "https";
    public static final String NONE = "";

    private SecurityConstants() {
        throw new AssertionError();
    }
}
