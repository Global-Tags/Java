package com.rappytv.globaltags.wrapper.http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rappytv.globaltags.wrapper.GlobalTagsAPI;
import com.rappytv.globaltags.wrapper.enums.GlobalIcon;
import com.rappytv.globaltags.wrapper.enums.GlobalPermission;
import com.rappytv.globaltags.wrapper.enums.GlobalPosition;
import com.rappytv.globaltags.wrapper.http.schemas.ErrorSchema;
import com.rappytv.globaltags.wrapper.model.adapters.*;
import org.jetbrains.annotations.NotNull;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

/**
 * A utility request class with generic response parsing.
 *
 * @param <T> The return type
 */
public class ApiRequest<T> {

    private static final HttpClient client = HttpClient.newHttpClient();
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Date.class, new DateTypeAdapter())
            .registerTypeAdapter(GlobalIcon.class, new GlobalIconTypeAdapter())
            .registerTypeAdapter(GlobalPermission.class, new GlobalPermissionTypeAdapter())
            .registerTypeAdapter(GlobalPosition.class, new GlobalPositionTypeAdapter())
            .registerTypeAdapter(UUID.class, new UUIDTypeAdapter())
            .create();

    private final String method;
    private final String path;
    private final GlobalTagsAPI<?> api;
    private final HttpRequest.BodyPublisher body;
    private final String contentType;
    private final Class<T> responseType;

    /**
     * Builds a new request without data.
     *
     * @param api          An API instance
     * @param method       The method
     * @param path         The request path, use {@link Routes}
     * @param responseType The class type for parsing the response
     */
    public ApiRequest(GlobalTagsAPI<?> api, String method, String path, Class<T> responseType) {
        this.api = api;
        this.method = method;
        this.path = path;
        this.body = HttpRequest.BodyPublishers.noBody();
        this.contentType = null;
        this.responseType = responseType;
    }

    /**
     * Builds a new request with a json body.
     *
     * @param api          An API instance
     * @param method       The method
     * @param path         The request path, use {@link Routes}
     * @param body         The request data
     * @param responseType The class type for parsing the response
     */
    public ApiRequest(GlobalTagsAPI<?> api, String method, String path, Map<String, Object> body, Class<T> responseType) {
        this.api = api;
        this.method = method;
        this.path = path;
        this.body = HttpRequest.BodyPublishers.ofString(gson.toJson(body));
        this.contentType = "application/json";
        this.responseType = responseType;
    }

    /**
     * Builds a new request with a formdata body.
     *
     * @param api          An API instance
     * @param method       The method
     * @param path         The request path, use {@link Routes}
     * @param body         The request data
     * @param responseType The class type for parsing the response
     */
    public ApiRequest(GlobalTagsAPI<?> api, String method, String path, MultipartData body, Class<T> responseType) {
        this.api = api;
        this.method = method;
        this.path = path;
        this.body = body.getBodyPublisher();
        this.contentType = body.getContentType();
        this.responseType = responseType;
    }

    /**
     * Send the request.
     *
     * @param consumer A consumer which gets called when the API responds
     */
    public void sendRequestAsync(Consumer<@NotNull ApiResponse<T>> consumer) {
        try {
            HttpRequest.Builder builder = this.getBuilder()
                    .uri(new URI(this.api.getUrls().getApiBase() + this.path))
                    .method(this.method, this.body);

            if(this.contentType != null) {
                builder.header("Content-Type", this.contentType);
            }

            client.sendAsync(builder.build(), HttpResponse.BodyHandlers.ofString()).thenAccept(response -> {
                boolean success = response.statusCode() >= 200 && response.statusCode() < 300;
                if(!success) {
                    ErrorSchema body = gson.fromJson(response.body(), ErrorSchema.class);
                    consumer.accept(new ApiResponse<>(false, null, body.error));
                    return;
                }
                T parsedBody = gson.fromJson(response.body(), this.responseType);
                consumer.accept(new ApiResponse<>(
                        true,
                        parsedBody,
                        null
                ));
            }).exceptionally(throwable -> {
                consumer.accept(new ApiResponse<>(false, null, throwable.getLocalizedMessage()));
                return null;
            });
        } catch (Exception e) {
            consumer.accept(new ApiResponse<>(false, null, e.getLocalizedMessage()));
        }
    }

    /**
     * Get a builder already containing all needed headers.
     *
     * @return A builder already containing all needed headers
     */
    private HttpRequest.Builder getBuilder() {
        return HttpRequest.newBuilder()
                .header("Authorization", this.api.getAuthorizationHeader())
                .header("X-Language", this.api.getLanguageCode())
                .header("X-Agent", this.api.getAgent().toString());
    }
}
