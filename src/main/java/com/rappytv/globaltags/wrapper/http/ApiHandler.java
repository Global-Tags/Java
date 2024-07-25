package com.rappytv.globaltags.wrapper.http;

import com.rappytv.globaltags.wrapper.GlobalTagsAPI;
import com.rappytv.globaltags.wrapper.enums.GlobalIcon;
import com.rappytv.globaltags.wrapper.enums.GlobalPosition;
import com.rappytv.globaltags.wrapper.model.PlayerInfo;

import java.util.*;
import java.util.function.Consumer;

@SuppressWarnings("unused")
public class ApiHandler<T> {

    // https://github.com/elysiajs/elysia/issues/495
    private static final Map<String, Object> emptyBody = Map.of("body", "placeholder body");

    private final GlobalTagsAPI<T> api;

    public ApiHandler(GlobalTagsAPI<T> api) {
        this.api = api;
    }

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

    public void getInfo(Consumer<PlayerInfo<T>> consumer) {
        getInfo(api.getClientUUID(), consumer);
    }

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

    public void setTag(String tag, Consumer<ApiResponse<String>> consumer) {
        setTag(api.getClientUUID(), tag, consumer);
    }

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

    public void setPosition(GlobalPosition position, Consumer<ApiResponse<String>> consumer) {
        setPosition(api.getClientUUID(), position, consumer);
    }

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

    public void setIcon(GlobalIcon icon, Consumer<ApiResponse<String>> consumer) {
        setIcon(api.getClientUUID(), icon, consumer);
    }

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

    public void resetTag(Consumer<ApiResponse<String>> consumer) {
        resetTag(api.getClientUUID(), consumer);
    }

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

    public record ApiResponse<T>(boolean successful, T data) {}
}
