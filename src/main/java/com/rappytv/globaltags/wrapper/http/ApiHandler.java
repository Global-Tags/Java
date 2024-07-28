package com.rappytv.globaltags.wrapper.http;

import com.rappytv.globaltags.wrapper.GlobalTagsAPI;
import com.rappytv.globaltags.wrapper.enums.GlobalIcon;
import com.rappytv.globaltags.wrapper.enums.GlobalPosition;
import com.rappytv.globaltags.wrapper.model.PlayerInfo;

import java.util.*;
import java.util.function.Consumer;

/**
 * A class containing all requests to the <a href="https://github.com/Global-Tags/API">GlobalTagAPI</a>
 * @param <T>
 */
@SuppressWarnings("unused")
public class ApiHandler<T> {

    /**
     * See <a href="https://github.com/elysiajs/elysia/issues/495">elysiajs/elysia#495</a> for more info.
     */
    private static final Map<String, Object> emptyBody = Map.of("body", "placeholder body");

    private final GlobalTagsAPI<T> api;

    /**
     * Instantiates a new ApiHandler
     * @param api The corresponding api where it's being implemented
     */
    public ApiHandler(GlobalTagsAPI<T> api) {
        this.api = api;
    }

    /**
     * A request to get the api version
     * @param consumer The action to be executed on response.
     */
    public void getVersion(Consumer<ApiResponse<String>> consumer) {
        new ApiRequest<>(
                api,
                "GET",
                Routes.getVersion()
        ).sendRequestSync((response) -> consumer.accept(new ApiResponse<>(
                response.successful(),
                response.body().version
        )));
    }

    /**
     * A request to get the player info of {@link GlobalTagsAPI#getClientUUID()}
     * @param consumer The action to be executed on response.
     */
    public void getInfo(Consumer<PlayerInfo<T>> consumer) {
        getInfo(api.getClientUUID(), consumer);
    }

    /**
     * A request to get the player info of a specific uuid
     * @param uuid The uuid to get the info of
     * @param consumer The action to be executed on response.
     */
    public void getInfo(UUID uuid, Consumer<PlayerInfo<T>> consumer) {
        new ApiRequest<>(
                api,
                "GET",
                Routes.player(uuid)
        ).sendRequestSync((response) -> {
            if(!response.successful()) {
                consumer.accept(null);
                return;
            }
            consumer.accept(new PlayerInfo<>(
                    api,
                    uuid,
                    response.body().tag,
                    response.body().position,
                    response.body().icon,
                    response.body().referred,
                    response.body().referrals,
                    response.body().roles,
                    response.body().suspension
            ));
        });
    }

    /**
     * A request to update the tag of {@link GlobalTagsAPI#getClientUUID()}
     * @param tag The new tag you want to set
     * @param consumer The action to be executed on response.
     */
    public void setTag(String tag, Consumer<ApiResponse<String>> consumer) {
        setTag(api.getClientUUID(), tag, consumer);
    }

    /**
     * A request to update the tag of a specific uuid
     * @param uuid The uuid you want to update the tag of
     * @param tag The new tag you want to set
     * @param consumer The action to be executed on response.
     */
    public void setTag(UUID uuid, String tag, Consumer<ApiResponse<String>> consumer) {
        new ApiRequest<>(
                api,
                "POST",
                Routes.player(uuid),
                Map.of("tag", tag)
        ).sendRequestSync((response) -> {
            if(!response.successful()) {
                consumer.accept(new ApiResponse<>(false, response.body().error));
                return;
            }
            api.getCache().clear();
            api.getCache().resolveSelf((info) ->
                    consumer.accept(new ApiResponse<>(true, response.body().message))
            );
        });
    }

    /**
     * A request to update the {@link GlobalPosition} of {@link GlobalTagsAPI#getClientUUID()}
     * @param position The new position you want to set
     * @param consumer The action to be executed on response.
     */
    public void setPosition(GlobalPosition position, Consumer<ApiResponse<String>> consumer) {
        setPosition(api.getClientUUID(), position, consumer);
    }

    /**
     * A request to update the {@link GlobalPosition} of a specific uuid
     * @param uuid The uuid you want to update the position of
     * @param position The new position you want to set
     * @param consumer The action to be executed on response.
     */
    public void setPosition(UUID uuid, GlobalPosition position, Consumer<ApiResponse<String>> consumer) {
        new ApiRequest<>(
                api,
                "POST",
                Routes.setPosition(uuid),
                Map.of("position", position.name())
        ).sendRequestSync((response) -> {
            if(!response.successful()) {
                consumer.accept(new ApiResponse<>(false, response.body().error));
                return;
            }
            api.getCache().clear();
            api.getCache().resolveSelf((info) ->
                    consumer.accept(new ApiResponse<>(true, response.body().message))
            );
        });
    }

    /**
     * A request to update the global icon of {@link GlobalTagsAPI#getClientUUID()}
     * @param icon The new icon you want to set
     * @param consumer The action to be executed on response.
     */
    public void setIcon(GlobalIcon icon, Consumer<ApiResponse<String>> consumer) {
        setIcon(api.getClientUUID(), icon, consumer);
    }

    /**
     * A request to update the global icon of a specific uuid
     * @param uuid The uuid you want to update the icon of
     * @param icon The new icon you want to set
     * @param consumer The action to be executed on response.
     */
    public void setIcon(UUID uuid, GlobalIcon icon, Consumer<ApiResponse<String>> consumer) {
        new ApiRequest<>(
                api,
                "POST",
                Routes.setIcon(uuid),
                Map.of("icon", icon.name())
        ).sendRequestSync((response) -> {
            if(!response.successful()) {
                consumer.accept(new ApiResponse<>(false, response.body().error));
                return;
            }
            api.getCache().clear();
            api.getCache().resolveSelf((info) ->
                    consumer.accept(new ApiResponse<>(true, response.body().message))
            );
        });
    }

    /**
     * A request to clear the tag of {@link GlobalTagsAPI#getClientUUID()}
     * @param consumer The action to be executed on response.
     */
    public void resetTag(Consumer<ApiResponse<String>> consumer) {
        resetTag(api.getClientUUID(), consumer);
    }

    /**
     * A request to clear the tag of a specific uuid
     * @param uuid The uuid you want to clear the tag of
     * @param consumer The action to be executed on response.
     */
    public void resetTag(UUID uuid, Consumer<ApiResponse<String>> consumer) {
        new ApiRequest<>(
                api,
                "DELETE",
                Routes.player(uuid),
                emptyBody
        ).sendRequestSync((response) -> {
            if(!response.successful()) {
                consumer.accept(new ApiResponse<>(false, response.body().error));
                return;
            }
            api.getCache().clear();
            api.getCache().resolveSelf((info) ->
                    consumer.accept(new ApiResponse<>(true, response.body().message))
            );
        });
    }

    /**
     * A request to mark a specific uuid as the inviter of {@link GlobalTagsAPI#getClientUUID()}
     * @param uuid The uuid you want to mark as the inviter
     * @param consumer The action to be executed on response.
     */
    public void referPlayer(UUID uuid, Consumer<ApiResponse<String>> consumer) {
        new ApiRequest<>(
                api,
                "POST",
                Routes.referPlayer(uuid),
                emptyBody
        ).sendRequestSync((response) -> {
            if(!response.successful()) {
                consumer.accept(new ApiResponse<>(false, response.body().error));
                return;
            }
            api.getCache().clear();
            api.getCache().resolveSelf((info) ->
                    consumer.accept(new ApiResponse<>(true, response.body().message))
            );
        });
    }

    /**
     * A request to report a specific uuid
     * @param uuid The uuid you want to report
     * @param reason The reason why you want to report the uuid
     * @param consumer The action to be executed on response.
     */
    public void reportPlayer(UUID uuid, String reason, Consumer<ApiResponse<String>> consumer) {
        new ApiRequest<>(
                api,
                "POST",
                Routes.reportPlayer(uuid),
                Map.of("reason", reason)
        ).sendRequestSync((response) -> {
            if(!response.successful()) {
                consumer.accept(new ApiResponse<>(false, response.body().error));
                return;
            }
            api.getCache().clear();
            api.getCache().resolveSelf((info) ->
                    consumer.accept(new ApiResponse<>(true, response.body().message))
            );
        });
    }

    /**
     * A request to ban a specific uuid
     * @param uuid The uuid you want to ban
     * @param reason The reason for the ban
     * @param consumer The action to be executed on response.
     */
    public void banPlayer(UUID uuid, String reason, Consumer<ApiResponse<String>> consumer) {
        new ApiRequest<>(
                api,
                "POST",
                Routes.ban(uuid),
                Map.of("reason", reason)
        ).sendRequestSync((response) -> {
            if(!response.successful()) {
                consumer.accept(new ApiResponse<>(false, response.body().error));
                return;
            }
            api.getCache().clear();
            api.getCache().resolveSelf((info) -> {
                System.out.println("eee");
                consumer.accept(new ApiResponse<>(true, response.body().message));
            });
        });
    }

    /**
     * A request to unban a specific uuid
     * @param uuid The uuid you want to unban
     * @param consumer The action to be executed on response.
     */
    public void unbanPlayer(UUID uuid, Consumer<ApiResponse<String>> consumer) {
        new ApiRequest<>(
                api,
                "DELETE",
                Routes.ban(uuid),
                emptyBody
        ).sendRequestSync((response) -> {
            if(!response.successful()) {
                consumer.accept(new ApiResponse<>(false, response.body().error));
                return;
            }
            api.getCache().clear();
            api.getCache().resolveSelf((info) ->
                    consumer.accept(new ApiResponse<>(true, response.body().message))
            );
        });
    }

    /**
     * A request to edit the ban of a specific uuid
     * @param uuid The uuid you want to edit the ban of
     * @param suspension The new {@link PlayerInfo.Suspension} object
     * @param consumer The action to be executed on response.
     */
    public void editBan(UUID uuid, PlayerInfo.Suspension suspension, Consumer<ApiResponse<String>> consumer) {
        Objects.requireNonNull(suspension.getReason(), "Reason must not be null");
        new ApiRequest<>(
                api,
                "PUT",
                Routes.ban(uuid),
                Map.of("reason", suspension.getReason(), "appealable", suspension.isAppealable())
        ).sendRequestSync((response) -> {
            if(!response.successful()) {
                consumer.accept(new ApiResponse<>(false, response.body().error));
                return;
            }
            api.getCache().clear();
            api.getCache().resolveSelf((info) ->
                    consumer.accept(new ApiResponse<>(true, response.body().message))
            );
        });
    }

    /**
     * A request to appeal the ban of {@link GlobalTagsAPI#getClientUUID()}
     * @param reason The reason why {@link GlobalTagsAPI#getClientUUID()} should be unbanned
     * @param consumer The action to be executed on response.
     */
    public void appealBan(String reason, Consumer<ApiResponse<String>> consumer) {
        new ApiRequest<>(
                api,
                "POST",
                Routes.appealBan(api.getClientUUID()),
                Map.of("reason", reason)
        ).sendRequestSync((response) -> {
            if(!response.successful()) {
                consumer.accept(new ApiResponse<>(false, response.body().error));
                return;
            }
            consumer.accept(new ApiResponse<>(true, response.body().message));
        });
    }

    /**
     * A request to get the discord linking code of {@link GlobalTagsAPI#getClientUUID()}. Implementation Note: Please don't show the code; Only copy it to the clipboard
     * @param consumer The action to be executed on response.
     */
    public void linkDiscord(Consumer<ApiResponse<String>> consumer) {
        new ApiRequest<>(
                api,
                "POST",
                Routes.discordConnection(api.getClientUUID()),
                emptyBody
        ).sendRequestSync((response) -> {
            if(!response.successful()) {
                consumer.accept(new ApiResponse<>(false, response.body().error));
                return;
            }
            consumer.accept(new ApiResponse<>(true, response.body().code));
        });
    }

    /**
     * A request to unlink the discord account of {@link GlobalTagsAPI#getClientUUID()}
     * @param consumer The action to be executed on response.
     */
    public void unlinkDiscord(Consumer<ApiResponse<String>> consumer) {
        new ApiRequest<>(
                api,
                "DELETE",
                Routes.discordConnection(api.getClientUUID()),
                emptyBody
        ).sendRequestSync((response) -> {
            if(!response.successful()) {
                consumer.accept(new ApiResponse<>(false, response.body().error));
                return;
            }
            api.getCache().clear();
            api.getCache().resolveSelf((info) ->
                    consumer.accept(new ApiResponse<>(true, response.body().message))
            );
        });
    }

    /**
     * An inline class containing response data for these requests
     * @param successful If the request was successful
     * @param data The data returned
     * @param <T> The type of the returned data
     */
    public record ApiResponse<T>(boolean successful, T data) {}
}
