package com.rappytv.globaltags.wrapper;

import com.rappytv.globaltags.wrapper.enums.GlobalIcon;
import com.rappytv.globaltags.wrapper.enums.GlobalPosition;
import com.rappytv.globaltags.wrapper.http.ApiRequest;
import com.rappytv.globaltags.wrapper.model.PlayerInfo;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Consumer;

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
                Routes.getVersion(),
                null
        ).sendRequestSync((response) -> {
            String version = response.body() != null
                    ? response.body().has("version")
                        ? response.body().get("version").getAsString()
                        : null
                    : null;
            consumer.accept(new ApiResponse<>(
                    response.successful(),
                    version
            ));
        });
    }

    public void getInfo(Consumer<PlayerInfo<T>> consumer) {
        getInfo(api.getClientUUID(), consumer);
    }

    public void getInfo(UUID uuid, Consumer<PlayerInfo<T>> consumer) {
        // Temporary
        consumer.accept(null);
//        ApiRequest request = new ApiRequest(
//                Method.GET,
//                Routes.player(uuid),
//                api.getAuthorizationHeader()
//        ) {
//            @Override
//            public Map<String, Object> getBody() {
//                return null;
//            }
//        };
//        request.sendAsyncRequest((response) -> {
//            if(!request.isSuccessful()) {
//                consumer.accept(null);
//                return;
//            }
//            consumer.accept(new PlayerInfo<T>(
//                    uuid,
//                    request.responseBody.tag,
//                    request.responseBody.position,
//                    request.responseBody.icon,
//                    request.responseBody.referred,
//                    request.responseBody.referrals,
//                    request.responseBody.roles,
//                    request.responseBody.ban
//            ));
//        });
    }
//
//    public void setTag(String tag, Consumer<ApiResponse> consumer) {
//        setTag(api.getClientUUID(), tag, consumer);
//    }
//
//    public void setTag(UUID uuid, String tag, Consumer<ApiResponse> consumer) {
//        ApiRequest request = new ApiRequest(
//                Method.POST,
//                Routes.player(uuid),
//                api.getAuthorizationHeader()
//        ) {
//            @Override
//            public Map<String, Object> getBody() {
//                return Map.of("tag", tag);
//            }
//        };
//        request.sendAsyncRequest((response -> {
//            if(!request.isSuccessful()) {
//                consumer.accept(new ApiResponse(false, request.getError()));
//                return;
//            }
//            api.getCache().clear();
//            api.getCache().resolveSelf((info) -> consumer.accept(new ApiResponse(true, request.getMessage())));
//        }));
//    }
//
//    public void setPosition(GlobalPosition position, Consumer<ApiResponse> consumer) {
//        setPosition(api.getClientUUID(), position, consumer);
//    }
//
//    public void setPosition(UUID uuid, GlobalPosition position, Consumer<ApiResponse> consumer) {
//        ApiRequest request = new ApiRequest(
//                Method.POST,
//                Routes.setPosition(uuid),
//                api.getAuthorizationHeader()
//        ) {
//            @Override
//            public Map<String, Object> getBody() {
//                return Map.of("position", position.name());
//            }
//        };
//        request.sendAsyncRequest((response) -> {
//            if(!request.isSuccessful()) {
//                consumer.accept(new ApiResponse(false, request.getError()));
//                return;
//            }
//            api.getCache().clear();
//            api.getCache().resolveSelf((info) -> consumer.accept(new ApiResponse(true, request.getMessage())));
//        });
//    }
//
//    public void setIcon(GlobalIcon icon, Consumer<ApiResponse> consumer) {
//        setIcon(api.getClientUUID(), icon, consumer);
//    }
//
//    public void setIcon(UUID uuid, GlobalIcon icon, Consumer<ApiResponse> consumer) {
//        ApiRequest request = new ApiRequest(
//                Method.POST,
//                Routes.setIcon(uuid),
//                api.getAuthorizationHeader()
//        ) {
//            @Override
//            public Map<String, Object> getBody() {
//                return Map.of("icon", icon.name());
//            }
//        };
//        request.sendAsyncRequest((response) -> {
//            if(!request.isSuccessful()) {
//                consumer.accept(new ApiResponse(false, request.getError()));
//                return;
//            }
//            api.getCache().clear();
//            api.getCache().resolveSelf((info) -> consumer.accept(new ApiResponse(true, request.getMessage())));
//        });
//    }
//
//    public void resetTag(Consumer<ApiResponse> consumer) {
//        resetTag(api.getClientUUID(), consumer);
//    }
//
//    public void resetTag(UUID uuid, Consumer<ApiResponse> consumer) {
//        ApiRequest request = new ApiRequest(
//                Method.DELETE,
//                Routes.player(uuid),
//                api.getAuthorizationHeader()
//        ) {
//            @Override
//            public Map<String, Object> getBody() {
//                return emptyBody;
//            }
//        };
//        request.sendAsyncRequest((response) -> {
//            if(!request.isSuccessful()) {
//                consumer.accept(new ApiResponse(false, request.getError()));
//                return;
//            }
//            api.getCache().clear();
//            api.getCache().resolveSelf((info) -> consumer.accept(new ApiResponse(true, request.getMessage())));
//        });
//    }
//
//    public void referPlayer(UUID uuid, Consumer<ApiResponse> consumer) {
//        ApiRequest request = new ApiRequest(
//                Method.POST,
//                Routes.referPlayer(uuid),
//                api.getAuthorizationHeader()
//        ) {
//            @Override
//            public Map<String, Object> getBody() {
//                return emptyBody;
//            }
//        };
//        request.sendAsyncRequest((response) -> {
//            if(!request.isSuccessful()) {
//                consumer.accept(new ApiResponse(false, request.getError()));
//                return;
//            }
//            api.getCache().clear();
//            api.getCache().resolveSelf((info) -> consumer.accept(new ApiResponse(true, request.getMessage())));
//        });
//    }
//
//    public void reportPlayer(UUID uuid, String reason, Consumer<ApiResponse> consumer) {
//        ApiRequest request = new ApiRequest(
//                Method.POST,
//                Routes.reportPlayer(uuid),
//                api.getAuthorizationHeader()
//        ) {
//            @Override
//            public Map<String, Object> getBody() {
//                return Map.of("reason", reason);
//            }
//        };
//        request.sendAsyncRequest((response) -> {
//            if(!request.isSuccessful()) {
//                consumer.accept(new ApiResponse(false, request.getError()));
//                return;
//            }
//            consumer.accept(new ApiResponse(true, request.getMessage()));
//        });
//    }
//
//    public void banPlayer(UUID uuid, String reason, Consumer<ApiResponse> consumer) {
//        ApiRequest request = new ApiRequest(
//                Method.POST,
//                Routes.ban(uuid),
//                api.getAuthorizationHeader()
//        ) {
//            @Override
//            public Map<String, Object> getBody() {
//                return Map.of("reason", reason);
//            }
//        };
//        request.sendAsyncRequest((response) -> {
//            if(!request.isSuccessful()) {
//                consumer.accept(new ApiResponse(false, request.getError()));
//                return;
//            }
//            api.getCache().clear();
//            api.getCache().resolveSelf((info) -> consumer.accept(new ApiResponse(true, request.getMessage())));
//        });
//    }
//
//    public void unbanPlayer(UUID uuid, Consumer<ApiResponse> consumer) {
//        ApiRequest request = new ApiRequest(
//                Method.DELETE,
//                Routes.ban(uuid),
//                api.getAuthorizationHeader()
//        ) {
//            @Override
//            public Map<String, Object> getBody() {
//                return emptyBody;
//            }
//        };
//        request.sendAsyncRequest((response) -> {
//            if(!request.isSuccessful()) {
//                consumer.accept(new ApiResponse(false, request.getError()));
//                return;
//            }
//            api.getCache().clear();
//            api.getCache().resolveSelf((info) -> consumer.accept(new ApiResponse(true, request.getMessage())));
//        });
//    }
//
//    public void editBan(UUID uuid, Suspension suspension, Consumer<ApiResponse> consumer) {
//        ApiRequest request = new ApiRequest(
//                Method.PUT,
//                Routes.ban(uuid),
//                api.getAuthorizationHeader()
//        ) {
//            @Override
//            public Map<String, Object> getBody() {
//                Objects.requireNonNull(suspension.getReason(), "Reason must not be null");
//                return Map.of("reason", suspension.getReason(), "appealable", suspension.isAppealable());
//            }
//        };
//        request.sendAsyncRequest((response) -> {
//            if(!request.isSuccessful()) {
//                consumer.accept(new ApiResponse(false, request.getError()));
//                return;
//            }
//            api.getCache().clear();
//            api.getCache().resolveSelf((info) -> consumer.accept(new ApiResponse(true, request.getMessage())));
//        });
//    }
//
//    public void appealBan(String reason, Consumer<ApiResponse> consumer) {
//        ApiRequest request = new ApiRequest(
//                Method.POST,
//                Routes.appealBan(api.getClientUUID()),
//                api.getAuthorizationHeader()
//        ) {
//            @Override
//            public Map<String, Object> getBody() {
//                return Map.of("reason", reason);
//            }
//        };
//        request.sendAsyncRequest((response) -> {
//            if(!request.isSuccessful()) {
//                consumer.accept(new ApiResponse(false, request.getError()));
//                return;
//            }
//            consumer.accept(new ApiResponse(true, request.getMessage()));
//        });
//    }
//
//    public void linkDiscord(Consumer<ApiResponse> consumer) {
//        ApiRequest request = new ApiRequest(
//                Method.POST,
//                Routes.discordConnection(api.getClientUUID()),
//                api.getAuthorizationHeader()
//        ) {
//            @Override
//            public Map<String, Object> getBody() {
//                return emptyBody;
//            }
//        };
//        request.sendAsyncRequest((response) -> {
//            if(!request.isSuccessful()) {
//                consumer.accept(new ApiResponse(false, request.getError()));
//                return;
//            }
//            api.getCache().clear();
//            api.getCache().resolveSelf((info) -> consumer.accept(new ApiResponse(true, request.responseBody.code)));
//        });
//    }
//
//    public void unlinkDiscord(Consumer<ApiResponse> consumer) {
//        ApiRequest request = new ApiRequest(
//                Method.DELETE,
//                Routes.discordConnection(api.getClientUUID()),
//                api.getAuthorizationHeader()
//        ) {
//            @Override
//            public Map<String, Object> getBody() {
//                return emptyBody;
//            }
//        };
//        request.sendAsyncRequest((response) -> {
//            if(!request.isSuccessful()) {
//                consumer.accept(new ApiResponse(false, request.getError()));
//                return;
//            }
//            api.getCache().clear();
//            api.getCache().resolveSelf((info) -> consumer.accept(new ApiResponse(true, request.getMessage())));
//        });
//    }

    public record ApiResponse<T>(boolean successful, T data) {}
}
