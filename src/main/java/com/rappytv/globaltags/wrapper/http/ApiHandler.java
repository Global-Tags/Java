package com.rappytv.globaltags.wrapper.http;

import com.rappytv.globaltags.wrapper.GlobalTagsAPI;
import com.rappytv.globaltags.wrapper.enums.ConnectionType;
import com.rappytv.globaltags.wrapper.enums.GlobalIcon;
import com.rappytv.globaltags.wrapper.enums.GlobalPosition;
import com.rappytv.globaltags.wrapper.enums.ReferralLeaderboardType;
import com.rappytv.globaltags.wrapper.http.schemas.*;
import com.rappytv.globaltags.wrapper.model.*;

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
    public void getApiInfo(Consumer<ApiResponse<ApiInfo>> consumer) {
        new ApiRequest<>(
                this.api,
                "GET",
                Routes.getApiInfo(),
                ApiInfo.class
        ).sendRequestAsync((response) -> {
            if(!response.isSuccessful()) {
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
    public void getReferralLeaderboards(Consumer<ApiResponse<Map<ReferralLeaderboardType, List<ReferralLeaderboardEntry>>>> consumer) {
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
    public void getInfo(Consumer<ApiResponse<PlayerInfo<T>>> consumer) {
        this.getInfo(this.api.getClientUUID(), consumer);
    }

    /**
     * A request to get the player info of a specific uuid
     *
     * @param uuid The uuid to get the info of
     * @param consumer The action to be executed on response.
     */
    public void getInfo(UUID uuid, Consumer<ApiResponse<PlayerInfo<T>>> consumer) {
        new ApiRequest<>(
                this.api,
                "GET",
                Routes.player(uuid),
                PlayerInfoSchema.class
        ).sendRequestAsync((response) -> {
            if(!response.isSuccessful()) {
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
    public void getTagHistory(Consumer<ApiResponse<List<TagHistoryEntry>>> consumer) {
        this.getTagHistory(this.api.getClientUUID(), consumer);
    }

    /**
     * A request to get the tag history of a specific uuid
     *
     * @param uuid The uuid to get the tag history of
     * @param consumer The action to be executed on response.
     */
    public void getTagHistory(UUID uuid, Consumer<ApiResponse<List<TagHistoryEntry>>> consumer) {
        new ApiRequest<>(
                this.api,
                "GET",
                Routes.tagHistory(uuid),
                TagHistoryEntry[].class
        ).sendRequestAsync((response) -> {
            if(!response.isSuccessful()) {
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
     * @param tag The new tag you want to set
     * @param consumer The action to be executed on response.
     */
    public void setTag(String tag, Consumer<ApiResponse<String>> consumer) {
        this.setTag(this.api.getClientUUID(), tag, consumer);
    }

    /**
     * A request to update the tag of a specific uuid
     *
     * @param uuid The uuid you want to update the tag of
     * @param tag The new tag you want to set
     * @param consumer The action to be executed on response.
     */
    public void setTag(UUID uuid, String tag, Consumer<ApiResponse<String>> consumer) {
        new ApiRequest<>(
                this.api,
                "POST",
                Routes.player(uuid),
                Map.of("tag", tag),
                MessageSchema.class
        ).sendRequestAsync((response) -> {
            if(!response.isSuccessful()) {
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
    public void setPosition(GlobalPosition position, Consumer<ApiResponse<String>> consumer) {
        this.setPosition(this.api.getClientUUID(), position, consumer);
    }

    /**
     * A request to update the {@link GlobalPosition} of a specific uuid
     *
     * @param uuid The uuid you want to update the position of
     * @param position The new position you want to set
     * @param consumer The action to be executed on response.
     */
    public void setPosition(UUID uuid, GlobalPosition position, Consumer<ApiResponse<String>> consumer) {
        new ApiRequest<>(
                this.api,
                "POST",
                Routes.setPosition(uuid),
                Map.of("position", position.name()),
                MessageSchema.class
        ).sendRequestAsync((response) -> {
            if(!response.isSuccessful()) {
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
     * @param icon The new icon you want to set
     * @param consumer The action to be executed on response.
     */
    public void setIcon(GlobalIcon icon, Consumer<ApiResponse<String>> consumer) {
        this.setIcon(this.api.getClientUUID(), icon, consumer);
    }

    /**
     * A request to update the global icon of a specific uuid
     *
     * @param uuid The uuid you want to update the icon of
     * @param icon The new icon you want to set
     * @param consumer The action to be executed on response.
     */
    public void setIcon(UUID uuid, GlobalIcon icon, Consumer<ApiResponse<String>> consumer) {
        new ApiRequest<>(
                this.api,
                "POST",
                Routes.setIcon(uuid),
                Map.of("icon", icon.name()),
                MessageSchema.class
        ).sendRequestAsync((response) -> {
            if(!response.isSuccessful()) {
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
    public void resetTag(Consumer<ApiResponse<String>> consumer) {
        this.resetTag(this.api.getClientUUID(), consumer);
    }

    /**
     * A request to clear the tag of a specific uuid
     *
     * @param uuid The uuid you want to clear the tag of
     * @param consumer The action to be executed on response.
     */
    public void resetTag(UUID uuid, Consumer<ApiResponse<String>> consumer) {
        new ApiRequest<>(
                this.api,
                "DELETE",
                Routes.player(uuid),
                emptyBody,
                MessageSchema.class
        ).sendRequestAsync((response) -> {
            if(!response.isSuccessful()) {
                consumer.accept(new ApiResponse<>(false, null, response.getError()));
                return;
            }
            this.api.getCache().renew(uuid, (info) ->
                    consumer.accept(new ApiResponse<>(true, response.getData().message, null))
            );
        });
    }

    /**
     * A request to add a player to the watchlist
     *
     * @param uuid The uuid you want to add to the watchlist
     * @param consumer The action to be executed on response.
     */
    public void watchPlayer(UUID uuid, Consumer<ApiResponse<String>> consumer) {
        new ApiRequest<>(
                this.api,
                "POST",
                Routes.watchPlayer(uuid),
                emptyBody,
                MessageSchema.class
        ).sendRequestAsync((response) -> {
            if(!response.isSuccessful()) {
                consumer.accept(new ApiResponse<>(false, null, response.getError()));
                return;
            }
            consumer.accept(new ApiResponse<>(true, response.getData().message, null));
        });
    }

    /**
     * A request to remove a player from the watchlist
     *
     * @param uuid The uuid you want to remove from the watchlist
     * @param consumer The action to be executed on response.
     */
    public void unwatchPlayer(UUID uuid, Consumer<ApiResponse<String>> consumer) {
        new ApiRequest<>(
                this.api,
                "POST",
                Routes.unwatchPlayer(uuid),
                emptyBody,
                MessageSchema.class
        ).sendRequestAsync((response) -> {
            if(!response.isSuccessful()) {
                consumer.accept(new ApiResponse<>(false, null, response.getError()));
                return;
            }
            consumer.accept(new ApiResponse<>(true, response.getData().message, null));
        });
    }

    /**
     * A request to mark a specific uuid as the inviter of {@link GlobalTagsAPI#getClientUUID()}
     *
     * @param uuid The uuid you want to mark as the inviter
     * @param consumer The action to be executed on response.
     */
    public void referPlayer(UUID uuid, Consumer<ApiResponse<String>> consumer) {
        new ApiRequest<>(
                this.api,
                "POST",
                Routes.referPlayer(uuid),
                emptyBody,
                MessageSchema.class
        ).sendRequestAsync((response) -> {
            if(!response.isSuccessful()) {
                consumer.accept(new ApiResponse<>(false, null, response.getError()));
                return;
            }
            consumer.accept(new ApiResponse<>(true, response.getData().message, null));
        });
    }

    /**
     * A request to report a specific uuid
     *
     * @param uuid The uuid you want to report
     * @param reason The reason why you want to report the uuid
     * @param consumer The action to be executed on response.
     */
    public void reportPlayer(UUID uuid, String reason, Consumer<ApiResponse<String>> consumer) {
        new ApiRequest<>(
                this.api,
                "POST",
                Routes.playerReports(uuid),
                Map.of("reason", reason),
                MessageSchema.class
        ).sendRequestAsync((response) -> {
            if(!response.isSuccessful()) {
                consumer.accept(new ApiResponse<>(false, null, response.getError()));
                return;
            }
            consumer.accept(new ApiResponse<>(true, response.getData().message, null));
        });
    }

    public void getReports(UUID uuid, Consumer<ApiResponse<List<PlayerReport>>> consumer) {
        new ApiRequest<>(
                this.api,
                "GET",
                Routes.playerReports(uuid),
                emptyBody,
                ReportSchema[].class
        ).sendRequestAsync((response) -> {
            if(!response.isSuccessful()) {
                consumer.accept(new ApiResponse<>(false, null, response.getError()));
                return;
            }

            List<PlayerReport> reports = new ArrayList<>();
            for(ReportSchema report : response.getData()) {
                reports.add(new PlayerReport(
                        report.id,
                        report.reason,
                        report.reportedTag,
                        report.by,
                        report.createdAt
                ));
            }
            consumer.accept(new ApiResponse<>(true, reports, null));
        });
    }

    /**
     * A request to ban a specific uuid
     *
     * @param uuid The uuid you want to ban
     * @param reason The reason for the ban
     * @param consumer The action to be executed on response.
     */
    public void banPlayer(UUID uuid, String reason, Consumer<ApiResponse<String>> consumer) {
        new ApiRequest<>(
                this.api,
                "POST",
                Routes.bans(uuid),
                Map.of("reason", reason),
                MessageSchema.class
        ).sendRequestAsync((response) -> {
            if(!response.isSuccessful()) {
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
     * @param uuid The uuid you want to unban
     * @param consumer The action to be executed on response.
     */
    public void unbanPlayer(UUID uuid, Consumer<ApiResponse<String>> consumer) {
        new ApiRequest<>(
                this.api,
                "DELETE",
                Routes.bans(uuid),
                emptyBody,
                MessageSchema.class
        ).sendRequestAsync((response) -> {
            if(!response.isSuccessful()) {
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
     * @param uuid The uuid you want to edit the ban of
     * @param suspension The new {@link PlayerInfo.Suspension} object
     * @param consumer The action to be executed on response.
     */
    public void editBan(UUID uuid, PlayerInfo.Suspension suspension, Consumer<ApiResponse<String>> consumer) {
        Objects.requireNonNull(suspension.getReason(), "Reason must not be null");
        new ApiRequest<>(
                this.api,
                "PUT",
                Routes.bans(uuid),
                Map.of("reason", suspension.getReason(), "appealable", suspension.isAppealable()),
                MessageSchema.class
        ).sendRequestAsync((response) -> {
            if(!response.isSuccessful()) {
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
     * @param reason The reason why {@link GlobalTagsAPI#getClientUUID()} should be unbanned
     * @param consumer The action to be executed on response.
     */
    public void appealBan(String reason, Consumer<ApiResponse<String>> consumer) {
        new ApiRequest<>(
                this.api,
                "POST",
                Routes.appealBan(this.api.getClientUUID()),
                Map.of("reason", reason),
                MessageSchema.class
        ).sendRequestAsync((response) -> {
            if(!response.isSuccessful()) {
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
    public void linkDiscord(Consumer<ApiResponse<String>> consumer) {
        new ApiRequest<>(
                this.api,
                "POST",
                Routes.connection(this.api.getClientUUID(), ConnectionType.DISCORD),
                emptyBody,
                VerificationCodeSchema.class
        ).sendRequestAsync((response) -> {
            if(!response.isSuccessful()) {
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
    public void unlinkDiscord(Consumer<ApiResponse<String>> consumer) {
        new ApiRequest<>(
                this.api,
                "DELETE",
                Routes.connection(this.api.getClientUUID(), ConnectionType.DISCORD),
                emptyBody,
                MessageSchema.class
        ).sendRequestAsync((response) -> {
            if(!response.isSuccessful()) {
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
     * @param email The email which should be linked
     * @param consumer The action to be executed on response.
     */
    public void linkEmail(String email, Consumer<ApiResponse<String>> consumer) {
        new ApiRequest<>(
                this.api,
                "POST",
                Routes.connection(this.api.getClientUUID(), ConnectionType.EMAIL),
                Map.of("email", email),
                MessageSchema.class
        ).sendRequestAsync((response) -> {
            if(!response.isSuccessful()) {
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
    public void unlinkEmail(Consumer<ApiResponse<String>> consumer) {
        new ApiRequest<>(
                this.api,
                "DELETE",
                Routes.connection(this.api.getClientUUID(), ConnectionType.EMAIL),
                emptyBody,
                MessageSchema.class
        ).sendRequestAsync((response) -> {
            if(!response.isSuccessful()) {
                consumer.accept(new ApiResponse<>(false, null, response.getError()));
                return;
            }
            consumer.accept(new ApiResponse<>(true, response.getData().message, null));
        });
    }

    /**
     * A request to verify the email with the received verification code.
     *
     * @param code The verification code which was received via email
     * @param consumer The action to be executed on response.
     */
    public void verifyEmail(String code, Consumer<ApiResponse<String>> consumer) {
        new ApiRequest<>(
                this.api,
                "POST",
                Routes.verifyEmail(this.api.getClientUUID(), code),
                emptyBody,
                MessageSchema.class
        ).sendRequestAsync((response) -> {
            if(!response.isSuccessful()) {
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
    public void getNotes(Consumer<ApiResponse<List<PlayerNote>>> consumer) {
        this.getNotes(this.api.getClientUUID(), consumer);
    }

    /**
     * A request to get all notes of a specific uuid
     *
     * @param uuid The uuid of the player
     * @param consumer The action to be executed on response.
     */
    public void getNotes(UUID uuid, Consumer<ApiResponse<List<PlayerNote>>> consumer) {
        new ApiRequest<>(
                this.api,
                "GET",
                Routes.notes(uuid),
                emptyBody,
                NoteSchema[].class
        ).sendRequestAsync((response) -> {
            if(!response.isSuccessful()) {
                consumer.accept(new ApiResponse<>(false, null, response.getError()));
                return;
            }
            List<PlayerNote> notes = new ArrayList<>();
            for(NoteSchema note : response.getData()) {
                notes.add(new PlayerNote(
                        note.id,
                        note.text,
                        note.author,
                        note.createdAt
                ));
            }
            consumer.accept(new ApiResponse<>(true, notes, null));
        });
    }

    /**
     * A request to create a note for {@link GlobalTagsAPI#getClientUUID()}
     *
     * @param note The note which should be created
     * @param consumer The action to be executed on response.
     */
    public void createNote(String note, Consumer<ApiResponse<String>> consumer) {
        this.createNote(this.api.getClientUUID(), note, consumer);
    }

    /**
     * A request to create a note for a specific uuid
     *
     * @param uuid The uuid of the player
     * @param note The note which should be created
     * @param consumer The action to be executed on response.
     */
    public void createNote(UUID uuid, String note, Consumer<ApiResponse<String>> consumer) {
        new ApiRequest<>(
                this.api,
                "POST",
                Routes.notes(uuid),
                Map.of("note", note),
                MessageSchema.class
        ).sendRequestAsync((response) -> {
            if(!response.isSuccessful()) {
                consumer.accept(new ApiResponse<>(false, null, response.getError()));
                return;
            }
            consumer.accept(new ApiResponse<>(true, response.getData().message, null));
        });
    }

    /**
     * A request to get a specific note of {@link GlobalTagsAPI#getClientUUID()}
     *
     * @param noteId The ID of the note to get
     * @param consumer The action to be executed on response.
     */
    public void getNote(String noteId, Consumer<ApiResponse<PlayerNote>> consumer) {
        this.getNote(this.api.getClientUUID(), noteId, consumer);
    }

    /**
     * A request to get a specific note of {@link GlobalTagsAPI#getClientUUID()}
     *
     * @param uuid The uuid of the player
     * @param noteId The ID of the note to get
     * @param consumer The action to be executed on response.
     */
    public void getNote(UUID uuid, String noteId, Consumer<ApiResponse<PlayerNote>> consumer) {
        new ApiRequest<>(
                this.api,
                "GET",
                Routes.note(uuid, noteId),
                emptyBody,
                NoteSchema.class
        ).sendRequestAsync((response) -> {
            if(!response.isSuccessful()) {
                consumer.accept(new ApiResponse<>(false, null, response.getError()));
                return;
            }
            NoteSchema body = response.getData();
            consumer.accept(new ApiResponse<>(
                    false,
                    new PlayerNote(
                        body.id,
                        body.text,
                        body.author,
                        body.createdAt
                    ),
                    null
            ));
        });
    }

    /**
     * Deletes a note of {@link GlobalTagsAPI#getClientUUID()}.
     *
     * @param noteId The ID of the note to delete.
     * @param consumer The action to be executed on response.
     */
    public void deleteNote(String noteId, Consumer<ApiResponse<String>> consumer) {
        this.deleteNote(this.api.getClientUUID(), noteId, consumer);
    }

    /**
     * Deletes a note of a specific uuid.
     *
     * @param uuid The UUID of the player whose note you want to delete.
     * @param noteId The ID of the note to delete.
     * @param consumer The action to be executed on response.
     */
    public void deleteNote(UUID uuid, String noteId, Consumer<ApiResponse<String>> consumer) {
        new ApiRequest<>(
                this.api,
                "DELETE",
                Routes.note(uuid, noteId),
                emptyBody,
                MessageSchema.class
        ).sendRequestAsync((response) -> {
            if(!response.isSuccessful()) {
                consumer.accept(new ApiResponse<>(false, null, response.getError()));
                return;
            }
            consumer.accept(new ApiResponse<>(true, response.getData().message, null));
        });
    }

    /**
     * An inline class containing response data for these requests
     * @param <T> The type of the data
     */
    public static class ApiResponse<T> {

        private final boolean successful;
        private final T data;
        private final String error;

        /**
         * Constructs a new ApiResponse instance
         *
         * @param successful If the request was successful
         * @param data The data returned if available
         * @param error The error returned if available
         */
        public ApiResponse(boolean successful, T data, String error) {
            this.successful = successful;
            this.data = data;
            this.error = error;
        }

        /**
         * Checks if the request was successful
         *
         * @return If the request was successful
         */
        public boolean isSuccessful() {
            return this.successful;
        }

        /**
         * Gets the data returned if available
         *
         * @return the data if available
         */
        public T getData() {
            return this.data;
        }

        /**
         * Get the error returned if available
         * @return an error if available
         */
        public String getError() {
            return this.error;
        }
    }
}
