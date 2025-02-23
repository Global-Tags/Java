package com.rappytv.globaltags.wrapper.http;

import com.rappytv.globaltags.wrapper.GlobalTagsAPI;
import com.rappytv.globaltags.wrapper.enums.ConnectionType;
import com.rappytv.globaltags.wrapper.enums.GlobalIcon;
import com.rappytv.globaltags.wrapper.enums.GlobalPosition;
import com.rappytv.globaltags.wrapper.enums.ReferralLeaderboardType;
import com.rappytv.globaltags.wrapper.http.schemas.*;
import com.rappytv.globaltags.wrapper.model.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;

/**
 * A class containing all requests to the <a href="https://github.com/Global-Tags/API">GlobalTagAPI</a>
 *
 * @param <T> The same as the T value of your {@link GlobalTagsAPI} instance.
 */
@SuppressWarnings("unused")
public class ApiHandler<T> {

    /**
     * See <a href="https://github.com/elysiajs/elysia/issues/495">elysiajs/elysia#495</a> for more info.
     */
    private static final Map<String, Object> emptyBody = Map.of("data", "placeholder data");

    private final GlobalTagsAPI<T> api;

    /**
     * Instantiates a new ApiHandler
     *
     * @param api The corresponding api where it's being implemented
     */
    public ApiHandler(GlobalTagsAPI<T> api) {
        this.api = api;
    }

    /**
     * A request to get the api version
     *
     * @param consumer The action to be executed on response.
     */
    public void getApiInfo(@NotNull Consumer<ApiResponse<ApiInfo>> consumer) {
        Objects.requireNonNull(consumer);
        new ApiRequest<>(
                this.api,
                "GET",
                Routes.getApiInfo(),
                ApiInfo.class
        ).sendRequestAsync((response) -> {
            if (!response.isSuccessful()) {
                consumer.accept(new ApiResponse<>(false, null, response.getError()));
                return;
            }
            consumer.accept(new ApiResponse<>(true, response.getData(), null));
        });
    }

    /**
     * A request to get the current referral leaderboards
     *
     * @param consumer The action to be executed on response.
     */
    public void getReferralLeaderboards(@NotNull Consumer<ApiResponse<Map<ReferralLeaderboardType, List<ReferralLeaderboardEntry>>>> consumer) {
        Objects.requireNonNull(consumer);
        new ApiRequest<>(
                this.api,
                "GET",
                Routes.getReferralLeaderboards(),
                ReferralLeaderboardsSchema.class
        ).sendRequestAsync((response) -> {
            if (!response.isSuccessful()) {
                consumer.accept(new ApiResponse<>(false, null, response.getError()));
                return;
            }
            ReferralLeaderboardsSchema schemas = response.getData();
            Map<ReferralLeaderboardType, List<ReferralLeaderboardEntry>> leaderboards = new HashMap<>();
            List<ReferralLeaderboardEntry> totalEntries = new ArrayList<>();
            List<ReferralLeaderboardEntry> currentMonthEntries = new ArrayList<>();
            try {
                for (int i = 0; i < schemas.totalLeaderboard.length; i++) {
                    ReferralLeaderboardsSchema.ReferralLeaderboardEntrySchema entry = schemas.totalLeaderboard[i];
                    totalEntries.add(new ReferralLeaderboardEntry(
                            i + 1,
                            UUID.fromString(entry.uuid),
                            entry.totalReferrals,
                            entry.currentMonthReferrals
                    ));
                }
                for (int i = 0; i < schemas.currentMonthLeaderboard.length; i++) {
                    ReferralLeaderboardsSchema.ReferralLeaderboardEntrySchema entry = schemas.currentMonthLeaderboard[i];
                    currentMonthEntries.add(new ReferralLeaderboardEntry(
                            i + 1,
                            UUID.fromString(entry.uuid),
                            entry.totalReferrals,
                            entry.currentMonthReferrals
                    ));
                }
            } catch (IllegalArgumentException ignored) {
            }
            leaderboards.put(ReferralLeaderboardType.TOTAL, totalEntries);
            leaderboards.put(ReferralLeaderboardType.CURRENT_MONTH, currentMonthEntries);
            consumer.accept(new ApiResponse<>(true, leaderboards, null));
        });
    }

    /**
     * A request to get the player info of {@link GlobalTagsAPI#getClientUUID()}
     *
     * @param consumer The action to be executed on response.
     */
    public void getInfo(@NotNull Consumer<ApiResponse<PlayerInfo<T>>> consumer) {
        Objects.requireNonNull(this.api.getClientUUID(), "client uuid cannot be null");
        this.getInfo(this.api.getClientUUID(), consumer);
    }

    /**
     * A request to get the player info of a specific uuid
     *
     * @param uuid     The uuid to get the info of
     * @param consumer The action to be executed on response.
     */
    public void getInfo(@NotNull UUID uuid, @NotNull Consumer<ApiResponse<PlayerInfo<T>>> consumer) {
        Objects.requireNonNull(uuid);
        new ApiRequest<>(
                this.api,
                "GET",
                Routes.player(uuid),
                PlayerInfoSchema.class
        ).sendRequestAsync((response) -> {
            if (!response.isSuccessful()) {
                consumer.accept(new ApiResponse<>(false, null, response.getError()));
                return;
            }
            PlayerInfoSchema body = response.getData();
            consumer.accept(new ApiResponse<>(
                    true,
                    new PlayerInfo<>(
                            this.api,
                            uuid,
                            body.tag,
                            body.position,
                            body.icon,
                            body.referrals,
                            body.roleIcon,
                            body.roles,
                            body.permissions,
                            body.ban
                    ),
                    null
            ));
        });
    }

    /**
     * A request to get the tag history of {@link GlobalTagsAPI#getClientUUID()}
     *
     * @param consumer The action to be executed on response.
     */
    public void getTagHistory(@NotNull Consumer<ApiResponse<List<TagHistoryEntry>>> consumer) {
        Objects.requireNonNull(this.api.getClientUUID(), "client uuid cannot be null");
        this.getTagHistory(this.api.getClientUUID(), consumer);
    }

    /**
     * A request to get the tag history of a specific uuid
     *
     * @param uuid     The uuid to get the tag history of
     * @param consumer The action to be executed on response.
     */
    public void getTagHistory(@NotNull UUID uuid, @NotNull Consumer<ApiResponse<List<TagHistoryEntry>>> consumer) {
        Objects.requireNonNull(uuid);
        new ApiRequest<>(
                this.api,
                "GET",
                Routes.tagHistory(uuid),
                TagHistoryEntry[].class
        ).sendRequestAsync((response) -> {
            if (!response.isSuccessful()) {
                consumer.accept(new ApiResponse<>(false, null, response.getError()));
                return;
            }
            consumer.accept(new ApiResponse<>(
                    true,
                    Arrays.asList(response.getData()),
                    null
            ));
        });
    }

    /**
     * A request to update the tag of {@link GlobalTagsAPI#getClientUUID()}
     *
     * @param tag      The new tag you want to set
     * @param consumer The action to be executed on response.
     */
    public void setTag(@NotNull String tag, @NotNull Consumer<ApiResponse<String>> consumer) {
        Objects.requireNonNull(this.api.getClientUUID(), "client uuid cannot be null");
        Objects.requireNonNull(tag);
        this.setTag(this.api.getClientUUID(), tag, consumer);
    }

    /**
     * A request to update the tag of a specific uuid
     *
     * @param uuid     The uuid you want to update the tag of
     * @param tag      The new tag you want to set
     * @param consumer The action to be executed on response.
     */
    public void setTag(@NotNull UUID uuid, @NotNull String tag, @NotNull Consumer<ApiResponse<String>> consumer) {
        Objects.requireNonNull(uuid);
        Objects.requireNonNull(tag);
        Objects.requireNonNull(consumer);
        new ApiRequest<>(
                this.api,
                "POST",
                Routes.player(uuid),
                Map.of("tag", tag),
                MessageSchema.class
        ).sendRequestAsync((response) -> {
            if (!response.isSuccessful()) {
                consumer.accept(new ApiResponse<>(false, null, response.getError()));
                return;
            }
            this.api.getCache().renew(uuid, (info) ->
                    consumer.accept(new ApiResponse<>(true, response.getData().message, null))
            );
        });
    }

    /**
     * A request to update the {@link GlobalPosition} of {@link GlobalTagsAPI#getClientUUID()}
     *
     * @param position The new position you want to set
     * @param consumer The action to be executed on response.
     */
    public void setPosition(@NotNull GlobalPosition position, @NotNull Consumer<ApiResponse<String>> consumer) {
        Objects.requireNonNull(this.api.getClientUUID(), "client uuid cannot be null");
        Objects.requireNonNull(position);
        this.setPosition(this.api.getClientUUID(), position, consumer);
    }

    /**
     * A request to update the {@link GlobalPosition} of a specific uuid
     *
     * @param uuid     The uuid you want to update the position of
     * @param position The new position you want to set
     * @param consumer The action to be executed on response.
     */
    public void setPosition(@NotNull UUID uuid, @NotNull GlobalPosition position, @NotNull Consumer<ApiResponse<String>> consumer) {
        Objects.requireNonNull(uuid);
        Objects.requireNonNull(position);
        Objects.requireNonNull(consumer);
        new ApiRequest<>(
                this.api,
                "POST",
                Routes.setPosition(uuid),
                Map.of("position", position.name()),
                MessageSchema.class
        ).sendRequestAsync((response) -> {
            if (!response.isSuccessful()) {
                consumer.accept(new ApiResponse<>(false, null, response.getError()));
                return;
            }
            this.api.getCache().renew(uuid, (info) ->
                    consumer.accept(new ApiResponse<>(true, response.getData().message, null))
            );
        });
    }

    /**
     * A request to update the global icon of {@link GlobalTagsAPI#getClientUUID()}
     *
     * @param icon     The new icon you want to set
     * @param consumer The action to be executed on response.
     */
    public void setIcon(@NotNull GlobalIcon icon, @NotNull Consumer<ApiResponse<String>> consumer) {
        Objects.requireNonNull(this.api.getClientUUID(), "client uuid cannot be null");
        Objects.requireNonNull(icon);
        this.setIcon(this.api.getClientUUID(), icon, consumer);
    }

    /**
     * A request to update the global icon of a specific uuid
     *
     * @param uuid     The uuid you want to update the icon of
     * @param icon     The new icon you want to set
     * @param consumer The action to be executed on response.
     */
    public void setIcon(@NotNull UUID uuid, @NotNull GlobalIcon icon, @NotNull Consumer<ApiResponse<String>> consumer) {
        Objects.requireNonNull(uuid);
        Objects.requireNonNull(icon);
        Objects.requireNonNull(consumer);
        new ApiRequest<>(
                this.api,
                "POST",
                Routes.setIcon(uuid),
                Map.of("icon", icon.name()),
                MessageSchema.class
        ).sendRequestAsync((response) -> {
            if (!response.isSuccessful()) {
                consumer.accept(new ApiResponse<>(false, null, response.getError()));
                return;
            }
            this.api.getCache().renew(uuid, (info) ->
                    consumer.accept(new ApiResponse<>(true, response.getData().message, null))
            );
        });
    }

    /**
     * A request to update the role icon visibility of {@link GlobalTagsAPI#getClientUUID()}
     *
     * @param visible  If the icon should be visible or not
     * @param consumer The action to be executed on response.
     */
    public void setRoleIconVisibility(boolean visible, @NotNull Consumer<ApiResponse<String>> consumer) {
        Objects.requireNonNull(this.api.getClientUUID(), "client uuid cannot be null");
        this.setRoleIconVisibility(this.api.getClientUUID(), visible, consumer);
    }

    /**
     * A request to update the role icon visibility of a specific uuid
     *
     * @param uuid     The uuid you want to update the role icon visibility of
     * @param visible  If the icon should be visible or not
     * @param consumer The action to be executed on response.
     */
    public void setRoleIconVisibility(@NotNull UUID uuid, boolean visible, @NotNull Consumer<ApiResponse<String>> consumer) {
        Objects.requireNonNull(uuid);
        Objects.requireNonNull(consumer);
        new ApiRequest<>(
                this.api,
                "PATCH",
                Routes.roleIconVisiblity(uuid),
                Map.of("visible", visible),
                MessageSchema.class
        ).sendRequestAsync((response) -> {
            if (!response.isSuccessful()) {
                consumer.accept(new ApiResponse<>(false, null, response.getError()));
                return;
            }
            this.api.getCache().renew(uuid, (info) ->
                    consumer.accept(new ApiResponse<>(true, response.getData().message, null))
            );
        });
    }

    /**
     * A request to clear the tag of {@link GlobalTagsAPI#getClientUUID()}
     *
     * @param consumer The action to be executed on response.
     */
    public void resetTag(@NotNull Consumer<ApiResponse<String>> consumer) {
        Objects.requireNonNull(this.api.getClientUUID(), "client uuid cannot be null");
        this.resetTag(this.api.getClientUUID(), consumer);
    }

    /**
     * A request to clear the tag of a specific uuid
     *
     * @param uuid     The uuid you want to clear the tag of
     * @param consumer The action to be executed on response.
     */
    public void resetTag(@NotNull UUID uuid, @NotNull Consumer<ApiResponse<String>> consumer) {
        Objects.requireNonNull(uuid);
        Objects.requireNonNull(consumer);
        new ApiRequest<>(
                this.api,
                "DELETE",
                Routes.player(uuid),
                emptyBody,
                MessageSchema.class
        ).sendRequestAsync((response) -> {
            if (!response.isSuccessful()) {
                consumer.accept(new ApiResponse<>(false, null, response.getError()));
                return;
            }
            this.api.getCache().renew(uuid, (info) ->
                    consumer.accept(new ApiResponse<>(true, response.getData().message, null))
            );
        });
    }

    /**
     * A request to get a player's watchlist status
     *
     * @param uuid     The uuid you want to get the watchlist status of
     * @param consumer The action to be executed on response.
     */
    public void getWatchlistStatus(@NotNull UUID uuid, @NotNull Consumer<ApiResponse<Boolean>> consumer) {
        Objects.requireNonNull(uuid);
        Objects.requireNonNull(consumer);
        new ApiRequest<>(
                this.api,
                "GET",
                Routes.watchlist(uuid),
                emptyBody,
                WatchlistSchema.class
        ).sendRequestAsync((response) -> {
            if (!response.isSuccessful()) {
                consumer.accept(new ApiResponse<>(false, null, response.getError()));
                return;
            }
            consumer.accept(new ApiResponse<>(true, response.getData().watched, null));
        });
    }

    /**
     * A request to add a player to the watchlist
     *
     * @param uuid     The uuid you want to add to the watchlist
     * @param consumer The action to be executed on response.
     */
    public void updateWatchlistStatus(@NotNull UUID uuid, boolean watched, @NotNull Consumer<ApiResponse<String>> consumer) {
        Objects.requireNonNull(uuid);
        Objects.requireNonNull(consumer);
        new ApiRequest<>(
                this.api,
                "PATCH",
                Routes.watchlist(uuid),
                Map.of("watched", watched),
                MessageSchema.class
        ).sendRequestAsync((response) -> {
            if (!response.isSuccessful()) {
                consumer.accept(new ApiResponse<>(false, null, response.getError()));
                return;
            }
            consumer.accept(new ApiResponse<>(true, response.getData().message, null));
        });
    }

    /**
     * A request to get a player's API keys
     *
     * @param uuid     The uuid you want to get the API keys of
     * @param consumer The action to be executed on response
     */
    public void getApiKeys(@NotNull UUID uuid, @NotNull Consumer<ApiResponse<List<ApiKey>>> consumer) {
        Objects.requireNonNull(uuid);
        Objects.requireNonNull(consumer);
        new ApiRequest<>(
                this.api,
                "GET",
                Routes.apiKeys(uuid),
                emptyBody,
                ApiKey[].class
        ).sendRequestAsync((response) -> {
            if (!response.isSuccessful()) {
                consumer.accept(new ApiResponse<>(false, null, response.getError()));
                return;
            }
            consumer.accept(new ApiResponse<>(true, Arrays.asList(response.getData()), null));
        });
    }

    /**
     * A request to get a player's API key
     *
     * @param uuid     The uuid you want to get the API key of
     * @param name     The name of the API key
     * @param consumer The action to be executed on response.
     */
    public void getApiKey(@NotNull UUID uuid, @NotNull String name, @NotNull Consumer<ApiResponse<ApiKey>> consumer) {
        Objects.requireNonNull(uuid);
        Objects.requireNonNull(name);
        Objects.requireNonNull(consumer);
        new ApiRequest<>(
                this.api,
                "GET",
                Routes.apiKey(uuid, name),
                emptyBody,
                ApiKey.class
        ).sendRequestAsync((response) -> {
            if (!response.isSuccessful()) {
                consumer.accept(new ApiResponse<>(false, null, response.getError()));
                return;
            }
            consumer.accept(new ApiResponse<>(true, response.getData(), null));
        });
    }

    /**
     * A request to create a player API key
     *
     * @param uuid     The uuid you want to create the API key for
     * @param name     The name of the API key
     * @param consumer The action to be executed on response.
     */
    public void createApiKey(@NotNull UUID uuid, @NotNull String name, @NotNull Consumer<ApiResponse<ApiKeyCreationSchema>> consumer) {
        Objects.requireNonNull(uuid);
        Objects.requireNonNull(name);
        Objects.requireNonNull(consumer);
        new ApiRequest<>(
                this.api,
                "POST",
                Routes.apiKeys(uuid),
                Map.of("name", name),
                ApiKeyCreationSchema.class
        ).sendRequestAsync((response) -> {
            if (!response.isSuccessful()) {
                consumer.accept(new ApiResponse<>(false, null, response.getError()));
                return;
            }
            consumer.accept(new ApiResponse<>(true, response.getData(), null));
        });
    }

    /**
     * A request to regenerate a player's API key
     *
     * @param uuid     The uuid you want to regenerate the API key of
     * @param name     The name of the API key
     * @param consumer The action to be executed on response.
     */
    public void regenerateApiKey(@NotNull UUID uuid, @NotNull String name, @NotNull Consumer<ApiResponse<ApiKeyRegenSchema>> consumer) {
        Objects.requireNonNull(uuid);
        Objects.requireNonNull(name);
        Objects.requireNonNull(consumer);
        new ApiRequest<>(
                this.api,
                "PATCH",
                Routes.apiKey(uuid, name),
                emptyBody,
                ApiKeyRegenSchema.class
        ).sendRequestAsync((response) -> {
            if (!response.isSuccessful()) {
                consumer.accept(new ApiResponse<>(false, null, response.getError()));
                return;
            }
            consumer.accept(new ApiResponse<>(true, response.getData(), null));
        });
    }

    /**
     * A request to delete a player's API key
     *
     * @param uuid     The uuid you want to delete the API key of
     * @param name     The name of the API key
     * @param consumer The action to be executed on response.
     */
    public void deleteApiKey(@NotNull UUID uuid, @NotNull String name, @NotNull Consumer<ApiResponse<String>> consumer) {
        Objects.requireNonNull(uuid);
        Objects.requireNonNull(name);
        Objects.requireNonNull(consumer);
        new ApiRequest<>(
                this.api,
                "DELETE",
                Routes.apiKey(uuid, name),
                emptyBody,
                MessageSchema.class
        ).sendRequestAsync((response) -> {
            if (!response.isSuccessful()) {
                consumer.accept(new ApiResponse<>(false, null, response.getError()));
                return;
            }
            consumer.accept(new ApiResponse<>(true, response.getData().message, null));
        });
    }

    /**
     * A request to get all gift codes
     *
     * @param consumer The action to be executed on response
     */
    public void getGiftCodes(@NotNull Consumer<ApiResponse<List<GiftCode>>> consumer) {
        Objects.requireNonNull(consumer);
        new ApiRequest<>(
                this.api,
                "GET",
                Routes.giftCodes(),
                emptyBody,
                GiftCode[].class
        ).sendRequestAsync((response) -> {
            if (!response.isSuccessful()) {
                consumer.accept(new ApiResponse<>(false, null, response.getError()));
                return;
            }
            consumer.accept(new ApiResponse<>(true, Arrays.asList(response.getData()), null));
        });
    }

    /**
     * A request to get a specific gift code
     *
     * @param code     The gift code
     * @param consumer The action to be executed on response.
     */
    public void getGiftCode(@NotNull String code, @NotNull Consumer<ApiResponse<GiftCode>> consumer) {
        Objects.requireNonNull(code);
        Objects.requireNonNull(consumer);
        new ApiRequest<>(
                this.api,
                "GET",
                Routes.giftCode(code),
                emptyBody,
                GiftCode.class
        ).sendRequestAsync((response) -> {
            if (!response.isSuccessful()) {
                consumer.accept(new ApiResponse<>(false, null, response.getError()));
                return;
            }
            consumer.accept(new ApiResponse<>(true, response.getData(), null));
        });
    }

    /**
     * A request to create a gift code
     *
     * @param name     The name of the gift code
     * @param role     The gifted role
     * @param maxUses  The maximum number uses of the code
     * @param consumer The action to be executed on response.
     */
    public void createGiftCode(@NotNull String name, @NotNull String role, int maxUses, @NotNull Consumer<ApiResponse<GiftCodeCreationSchema>> consumer) {
        this.createGiftCode(name, role, maxUses, null, null, consumer);
    }

    /**
     * A request to create a gift code
     *
     * @param name         The name of the gift code
     * @param role         The gifted role
     * @param maxUses      The maximum number uses of the cod
     * @param giftDuration How long the gifted role should last in milliseconds
     * @param consumer     The action to be executed on response.
     */
    public void createGiftCode(@NotNull String name, @NotNull String role, int maxUses, @Nullable Long giftDuration, @NotNull Consumer<ApiResponse<GiftCodeCreationSchema>> consumer) {
        this.createGiftCode(name, role, maxUses, null, giftDuration, consumer);
    }

    /**
     * A request to create a gift code
     *
     * @param name           The name of the gift code
     * @param role           The gifted role
     * @param maxUses        The maximum number uses of the code
     * @param codeExpiration A date when the code should expire
     * @param consumer       The action to be executed on response.
     */
    public void createGiftCode(@NotNull String name, @NotNull String role, int maxUses, @Nullable Date codeExpiration, @NotNull Consumer<ApiResponse<GiftCodeCreationSchema>> consumer) {
        this.createGiftCode(name, role, maxUses, codeExpiration, null, consumer);
    }

    /**
     * A request to create a gift code
     *
     * @param name           The name of the gift code
     * @param role           The gifted role
     * @param maxUses        The maximum number uses of the code
     * @param codeExpiration A date when the code should expire
     * @param giftDuration   How long the gifted role should last in milliseconds
     * @param consumer       The action to be executed on response.
     */
    public void createGiftCode(@NotNull String name, @NotNull String role, int maxUses, @Nullable Date codeExpiration, @Nullable Long giftDuration, @NotNull Consumer<ApiResponse<GiftCodeCreationSchema>> consumer) {
        Objects.requireNonNull(name);
        Objects.requireNonNull(role);
        Objects.requireNonNull(consumer);
        Map<String, Object> body = new HashMap<>();
        body.put("name", name);
        body.put("role", role);
        body.put("max_uses", maxUses);
        if (codeExpiration != null) {
            body.put("code_expiration", codeExpiration.getTime());
        }
        if (giftDuration != null) {
            body.put("gift_duration", giftDuration);
        }

        new ApiRequest<>(
                this.api,
                "POST",
                Routes.giftCodes(),
                body,
                GiftCodeCreationSchema.class
        ).sendRequestAsync((response) -> {
            if (!response.isSuccessful()) {
                consumer.accept(new ApiResponse<>(false, null, response.getError()));
                return;
            }
            consumer.accept(new ApiResponse<>(true, response.getData(), null));
        });
    }

    /**
     * A request to redeem a gift code
     *
     * @param code     The gift code to redeem
     * @param consumer The action to be executed on response.
     */
    public void redeemGiftCode(@NotNull String code, @NotNull Consumer<ApiResponse<String>> consumer) {
        Objects.requireNonNull(code);
        Objects.requireNonNull(consumer);
        new ApiRequest<>(
                this.api,
                "POST",
                Routes.redeemGiftCode(code),
                emptyBody,
                MessageSchema.class
        ).sendRequestAsync((response) -> {
            if (!response.isSuccessful()) {
                consumer.accept(new ApiResponse<>(false, null, response.getError()));
                return;
            }
            consumer.accept(new ApiResponse<>(true, response.getData().message, null));
        });
    }

    /**
     * A request to delete a gift code
     *
     * @param code     The gift code to delete
     * @param consumer The action to be executed on response.
     */
    public void deleteGiftCode(@NotNull String code, @NotNull Consumer<ApiResponse<String>> consumer) {
        Objects.requireNonNull(code);
        Objects.requireNonNull(consumer);
        new ApiRequest<>(
                this.api,
                "DELETE",
                Routes.giftCode(code),
                emptyBody,
                MessageSchema.class
        ).sendRequestAsync((response) -> {
            if (!response.isSuccessful()) {
                consumer.accept(new ApiResponse<>(false, null, response.getError()));
                return;
            }
            consumer.accept(new ApiResponse<>(true, response.getData().message, null));
        });
    }

    /**
     * A request to mark a specific uuid as the inviter of {@link GlobalTagsAPI#getClientUUID()}
     *
     * @param uuid     The uuid you want to mark as the inviter
     * @param consumer The action to be executed on response.
     */
    public void referPlayer(@NotNull UUID uuid, @NotNull Consumer<ApiResponse<String>> consumer) {
        Objects.requireNonNull(uuid);
        Objects.requireNonNull(consumer);
        new ApiRequest<>(
                this.api,
                "POST",
                Routes.referPlayer(uuid),
                emptyBody,
                MessageSchema.class
        ).sendRequestAsync((response) -> {
            if (!response.isSuccessful()) {
                consumer.accept(new ApiResponse<>(false, null, response.getError()));
                return;
            }
            consumer.accept(new ApiResponse<>(true, response.getData().message, null));
        });
    }

    /**
     * A request to report a specific uuid
     *
     * @param uuid     The uuid you want to report
     * @param reason   The reason why you want to report the uuid
     * @param consumer The action to be executed on response.
     */
    public void reportPlayer(@NotNull UUID uuid, @NotNull String reason, @NotNull Consumer<ApiResponse<String>> consumer) {
        Objects.requireNonNull(uuid);
        Objects.requireNonNull(reason);
        Objects.requireNonNull(consumer);
        new ApiRequest<>(
                this.api,
                "POST",
                Routes.playerReports(uuid),
                Map.of("reason", reason),
                MessageSchema.class
        ).sendRequestAsync((response) -> {
            if (!response.isSuccessful()) {
                consumer.accept(new ApiResponse<>(false, null, response.getError()));
                return;
            }
            consumer.accept(new ApiResponse<>(true, response.getData().message, null));
        });
    }

    public void getReports(@NotNull UUID uuid, @NotNull Consumer<ApiResponse<List<PlayerReport>>> consumer) {
        Objects.requireNonNull(uuid);
        Objects.requireNonNull(consumer);
        new ApiRequest<>(
                this.api,
                "GET",
                Routes.playerReports(uuid),
                emptyBody,
                PlayerReport[].class
        ).sendRequestAsync((response) -> {
            if (!response.isSuccessful()) {
                consumer.accept(new ApiResponse<>(false, null, response.getError()));
                return;
            }
            consumer.accept(new ApiResponse<>(true, Arrays.asList(response.getData()), null));
        });
    }

    /**
     * A request to get a specific ban of a specific uuid
     *
     * @param uuid     The uuid you want to get the ban of
     * @param id       The id of the ban you want to get
     * @param consumer The action to be executed on response.
     */
    public void getBan(@NotNull UUID uuid, @NotNull String id, @NotNull Consumer<ApiResponse<BanInfo>> consumer) {
        Objects.requireNonNull(uuid);
        Objects.requireNonNull(id);
        Objects.requireNonNull(consumer);
        new ApiRequest<>(
                this.api,
                "GET",
                Routes.ban(uuid, id),
                emptyBody,
                BanInfo.class
        ).sendRequestAsync((response) -> {
            if (!response.isSuccessful()) {
                consumer.accept(new ApiResponse<>(false, null, response.getError()));
                return;
            }
            consumer.accept(new ApiResponse<>(true, response.getData(), null));
        });
    }

    /**
     * A request to get a list of all bans of a specific uuid
     *
     * @param uuid     The uuid you want to get the bans of
     * @param consumer The action to be executed on response.
     */
    public void getBans(@NotNull UUID uuid, @NotNull Consumer<ApiResponse<List<BanInfo>>> consumer) {
        Objects.requireNonNull(uuid);
        Objects.requireNonNull(consumer);
        new ApiRequest<>(
                this.api,
                "GET",
                Routes.bans(uuid),
                emptyBody,
                BanInfo[].class
        ).sendRequestAsync((response) -> {
            if (!response.isSuccessful()) {
                consumer.accept(new ApiResponse<>(false, null, response.getError()));
                return;
            }
            consumer.accept(new ApiResponse<>(true, Arrays.asList(response.getData()), null));
        });
    }

    /**
     * A request to ban a specific uuid
     *
     * @param uuid     The uuid you want to ban
     * @param reason   The reason for the ban
     * @param consumer The action to be executed on response.
     */
    public void banPlayer(@NotNull UUID uuid, @NotNull String reason, @NotNull Consumer<ApiResponse<String>> consumer) {
        this.banPlayer(uuid, reason, null, null, consumer);
    }

    /**
     * A request to ban a specific uuid
     *
     * @param uuid       The uuid you want to ban
     * @param reason     The reason for the ban
     * @param appealable If the user should be able to appeal the ban
     * @param consumer   The action to be executed on response.
     */
    public void banPlayer(@NotNull UUID uuid, @NotNull String reason, boolean appealable, @NotNull Consumer<ApiResponse<String>> consumer) {
        this.banPlayer(uuid, reason, appealable, null, consumer);
    }

    /**
     * A request to ban a specific uuid
     *
     * @param uuid     The uuid you want to ban
     * @param reason   The reason for the ban
     * @param duration The duration of the ban in milliseconds
     * @param consumer The action to be executed on response.
     */
    public void banPlayer(@NotNull UUID uuid, @NotNull String reason, long duration, @NotNull Consumer<ApiResponse<String>> consumer) {
        this.banPlayer(uuid, reason, null, duration, consumer);
    }

    /**
     * A request to ban a specific uuid
     *
     * @param uuid       The uuid you want to ban
     * @param reason     The reason for the ban
     * @param appealable If the user should be able to appeal the ban
     * @param duration   The duration of the ban in milliseconds
     * @param consumer   The action to be executed on response.
     */
    public void banPlayer(@NotNull UUID uuid, @NotNull String reason, @Nullable Boolean appealable, @Nullable Long duration, Consumer<ApiResponse<String>> consumer) {
        Objects.requireNonNull(uuid);
        Objects.requireNonNull(reason);
        Objects.requireNonNull(consumer);
        HashMap<String, Object> body = new HashMap<>();
        body.put("reason", reason);
        body.put("appealable", appealable);
        body.put("duration", duration);

        new ApiRequest<>(
                this.api,
                "POST",
                Routes.bans(uuid),
                body,
                MessageSchema.class
        ).sendRequestAsync((response) -> {
            if (!response.isSuccessful()) {
                consumer.accept(new ApiResponse<>(false, null, response.getError()));
                return;
            }
            this.api.getCache().renew(uuid, (info) ->
                    consumer.accept(new ApiResponse<>(true, response.getData().message, null))
            );
        });
    }

    /**
     * A request to unban a specific uuid
     *
     * @param uuid     The uuid you want to unban
     * @param consumer The action to be executed on response.
     */
    public void unbanPlayer(@NotNull UUID uuid, @NotNull Consumer<ApiResponse<String>> consumer) {
        Objects.requireNonNull(uuid);
        Objects.requireNonNull(consumer);
        new ApiRequest<>(
                this.api,
                "DELETE",
                Routes.bans(uuid),
                emptyBody,
                MessageSchema.class
        ).sendRequestAsync((response) -> {
            if (!response.isSuccessful()) {
                consumer.accept(new ApiResponse<>(false, null, response.getError()));
                return;
            }
            this.api.getCache().renew(uuid, (info) ->
                    consumer.accept(new ApiResponse<>(true, response.getData().message, null))
            );
        });
    }

    /**
     * A request to edit the ban of a specific uuid
     *
     * @param uuid       The uuid you want to edit the ban of
     * @param reason     The new reason for the ban
     * @param appealable If the ban should be appealable or not
     * @param consumer   The action to be executed on response.
     */
    public void editBan(@NotNull UUID uuid, @NotNull String reason, boolean appealable, @NotNull Consumer<ApiResponse<String>> consumer) {
        Objects.requireNonNull(uuid);
        Objects.requireNonNull(reason);
        Objects.requireNonNull(consumer);
        new ApiRequest<>(
                this.api,
                "PATCH",
                Routes.bans(uuid),
                Map.of("reason", reason, "appealable", appealable),
                MessageSchema.class
        ).sendRequestAsync((response) -> {
            if (!response.isSuccessful()) {
                consumer.accept(new ApiResponse<>(false, null, response.getError()));
                return;
            }
            this.api.getCache().renew(uuid, (info) ->
                    consumer.accept(new ApiResponse<>(true, response.getData().message, null))
            );
        });
    }

    /**
     * A request to appeal the ban of {@link GlobalTagsAPI#getClientUUID()}
     *
     * @param reason   The reason why {@link GlobalTagsAPI#getClientUUID()} should be unbanned
     * @param consumer The action to be executed on response.
     */
    public void appealBan(@NotNull String reason, @NotNull Consumer<ApiResponse<String>> consumer) {
        Objects.requireNonNull(reason);
        Objects.requireNonNull(consumer);
        new ApiRequest<>(
                this.api,
                "POST",
                Routes.appealBan(this.api.getClientUUID()),
                Map.of("reason", reason),
                MessageSchema.class
        ).sendRequestAsync((response) -> {
            if (!response.isSuccessful()) {
                consumer.accept(new ApiResponse<>(false, null, response.getError()));
                return;
            }
            consumer.accept(new ApiResponse<>(true, response.getData().message, null));
        });
    }

    /**
     * A request to get the discord linking code of {@link GlobalTagsAPI#getClientUUID()}. Implementation Note: Please don't show the code; Only copy it to the clipboard
     *
     * @param consumer The action to be executed on response.
     */
    public void linkDiscord(@NotNull Consumer<ApiResponse<String>> consumer) {
        Objects.requireNonNull(consumer);
        new ApiRequest<>(
                this.api,
                "POST",
                Routes.connection(this.api.getClientUUID(), ConnectionType.DISCORD),
                emptyBody,
                VerificationCodeSchema.class
        ).sendRequestAsync((response) -> {
            if (!response.isSuccessful()) {
                consumer.accept(new ApiResponse<>(false, null, response.getError()));
                return;
            }
            consumer.accept(new ApiResponse<>(true, response.getData().code, null));
        });
    }

    /**
     * A request to unlink the discord account of {@link GlobalTagsAPI#getClientUUID()}
     *
     * @param consumer The action to be executed on response.
     */
    public void unlinkDiscord(@NotNull Consumer<ApiResponse<String>> consumer) {
        Objects.requireNonNull(consumer);
        new ApiRequest<>(
                this.api,
                "DELETE",
                Routes.connection(this.api.getClientUUID(), ConnectionType.DISCORD),
                emptyBody,
                MessageSchema.class
        ).sendRequestAsync((response) -> {
            if (!response.isSuccessful()) {
                consumer.accept(new ApiResponse<>(false, null, response.getError()));
                return;
            }
            this.api.getCache().renewSelf((info) ->
                    consumer.accept(new ApiResponse<>(true, response.getData().message, null))
            );
        });
    }

    /**
     * A request to send an email verification to of {@link GlobalTagsAPI#getClientUUID()}.
     *
     * @param email    The email which should be linked
     * @param consumer The action to be executed on response.
     */
    public void linkEmail(@NotNull String email, @NotNull Consumer<ApiResponse<String>> consumer) {
        Objects.requireNonNull(email);
        Objects.requireNonNull(consumer);
        new ApiRequest<>(
                this.api,
                "POST",
                Routes.connection(this.api.getClientUUID(), ConnectionType.EMAIL),
                Map.of("email", email),
                MessageSchema.class
        ).sendRequestAsync((response) -> {
            if (!response.isSuccessful()) {
                consumer.accept(new ApiResponse<>(false, null, response.getError()));
                return;
            }
            consumer.accept(new ApiResponse<>(true, response.getData().message, null));
        });
    }

    /**
     * A request to unlink the email address of {@link GlobalTagsAPI#getClientUUID()}
     *
     * @param consumer The action to be executed on response.
     */
    public void unlinkEmail(@NotNull Consumer<ApiResponse<String>> consumer) {
        Objects.requireNonNull(consumer);
        new ApiRequest<>(
                this.api,
                "DELETE",
                Routes.connection(this.api.getClientUUID(), ConnectionType.EMAIL),
                emptyBody,
                MessageSchema.class
        ).sendRequestAsync((response) -> {
            if (!response.isSuccessful()) {
                consumer.accept(new ApiResponse<>(false, null, response.getError()));
                return;
            }
            consumer.accept(new ApiResponse<>(true, response.getData().message, null));
        });
    }

    /**
     * A request to verify the email with the received verification code.
     *
     * @param code     The verification code which was received via email
     * @param consumer The action to be executed on response.
     */
    public void verifyEmail(@NotNull String code, @NotNull Consumer<ApiResponse<String>> consumer) {
        Objects.requireNonNull(code);
        Objects.requireNonNull(consumer);
        new ApiRequest<>(
                this.api,
                "POST",
                Routes.verifyEmail(this.api.getClientUUID(), code),
                emptyBody,
                MessageSchema.class
        ).sendRequestAsync((response) -> {
            if (!response.isSuccessful()) {
                consumer.accept(new ApiResponse<>(false, null, response.getError()));
                return;
            }
            consumer.accept(new ApiResponse<>(true, response.getData().message, null));
        });
    }

    /**
     * A request to get all notes of {@link GlobalTagsAPI#getClientUUID()}
     *
     * @param consumer The action to be executed on response.
     */
    public void getNotes(@NotNull Consumer<ApiResponse<List<PlayerNote>>> consumer) {
        Objects.requireNonNull(this.api.getClientUUID());
        this.getNotes(this.api.getClientUUID(), consumer);
    }

    /**
     * A request to get all notes of a specific uuid
     *
     * @param uuid     The uuid of the player
     * @param consumer The action to be executed on response.
     */
    public void getNotes(@NotNull UUID uuid, @NotNull Consumer<ApiResponse<List<PlayerNote>>> consumer) {
        Objects.requireNonNull(uuid);
        Objects.requireNonNull(consumer);
        new ApiRequest<>(
                this.api,
                "GET",
                Routes.notes(uuid),
                emptyBody,
                PlayerNote[].class
        ).sendRequestAsync((response) -> {
            if (!response.isSuccessful()) {
                consumer.accept(new ApiResponse<>(false, null, response.getError()));
                return;
            }
            consumer.accept(new ApiResponse<>(true, Arrays.asList(response.getData()), null));
        });
    }

    /**
     * A request to create a note for {@link GlobalTagsAPI#getClientUUID()}
     *
     * @param note     The note which should be created
     * @param consumer The action to be executed on response.
     */
    public void createNote(@NotNull String note, @NotNull Consumer<ApiResponse<String>> consumer) {
        Objects.requireNonNull(this.api.getClientUUID());
        this.createNote(this.api.getClientUUID(), note, consumer);
    }

    /**
     * A request to create a note for a specific uuid
     *
     * @param uuid     The uuid of the player
     * @param note     The note which should be created
     * @param consumer The action to be executed on response.
     */
    public void createNote(@NotNull UUID uuid, @NotNull String note, @NotNull Consumer<ApiResponse<String>> consumer) {
        Objects.requireNonNull(uuid);
        Objects.requireNonNull(note);
        Objects.requireNonNull(consumer);
        new ApiRequest<>(
                this.api,
                "POST",
                Routes.notes(uuid),
                Map.of("note", note),
                MessageSchema.class
        ).sendRequestAsync((response) -> {
            if (!response.isSuccessful()) {
                consumer.accept(new ApiResponse<>(false, null, response.getError()));
                return;
            }
            consumer.accept(new ApiResponse<>(true, response.getData().message, null));
        });
    }

    /**
     * A request to get a specific note of {@link GlobalTagsAPI#getClientUUID()}
     *
     * @param noteId   The ID of the note to get
     * @param consumer The action to be executed on response.
     */
    public void getNote(String noteId, Consumer<ApiResponse<PlayerNote>> consumer) {
        Objects.requireNonNull(this.api.getClientUUID());
        this.getNote(this.api.getClientUUID(), noteId, consumer);
    }

    /**
     * A request to get a specific note of {@link GlobalTagsAPI#getClientUUID()}
     *
     * @param uuid     The uuid of the player
     * @param noteId   The ID of the note to get
     * @param consumer The action to be executed on response.
     */
    public void getNote(@NotNull UUID uuid, @NotNull String noteId, @NotNull Consumer<ApiResponse<PlayerNote>> consumer) {
        Objects.requireNonNull(uuid);
        Objects.requireNonNull(noteId);
        Objects.requireNonNull(consumer);
        new ApiRequest<>(
                this.api,
                "GET",
                Routes.note(uuid, noteId),
                emptyBody,
                PlayerNote.class
        ).sendRequestAsync((response) -> {
            if (!response.isSuccessful()) {
                consumer.accept(new ApiResponse<>(false, null, response.getError()));
                return;
            }
            consumer.accept(new ApiResponse<>(true, response.getData(), null));
        });
    }

    /**
     * Deletes a note of {@link GlobalTagsAPI#getClientUUID()}.
     *
     * @param noteId   The ID of the note to delete.
     * @param consumer The action to be executed on response.
     */
    public void deleteNote(@NotNull String noteId, @NotNull Consumer<ApiResponse<String>> consumer) {
        Objects.requireNonNull(this.api.getClientUUID());
        this.deleteNote(this.api.getClientUUID(), noteId, consumer);
    }

    /**
     * Deletes a note of a specific uuid.
     *
     * @param uuid     The UUID of the player whose note you want to delete.
     * @param noteId   The ID of the note to delete.
     * @param consumer The action to be executed on response.
     */
    public void deleteNote(@NotNull UUID uuid, @NotNull String noteId, @NotNull Consumer<ApiResponse<String>> consumer) {
        Objects.requireNonNull(uuid);
        Objects.requireNonNull(noteId);
        Objects.requireNonNull(consumer);
        new ApiRequest<>(
                this.api,
                "DELETE",
                Routes.note(uuid, noteId),
                emptyBody,
                MessageSchema.class
        ).sendRequestAsync((response) -> {
            if (!response.isSuccessful()) {
                consumer.accept(new ApiResponse<>(false, null, response.getError()));
                return;
            }
            consumer.accept(new ApiResponse<>(true, response.getData().message, null));
        });
    }
}
