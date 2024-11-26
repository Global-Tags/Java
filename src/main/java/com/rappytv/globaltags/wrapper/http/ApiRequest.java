package com.rappytv.globaltags.wrapper.http;

import com.google.gson.Gson;
import com.rappytv.globaltags.wrapper.GlobalTagsAPI;
import com.rappytv.globaltags.wrapper.http.schemas.ErrorSchema;
import org.jetbrains.annotations.NotNull;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.function.Consumer;

/**
 * A utility request class with generic response parsing.
 *
 * @param <T> The return type
 */
public class ApiRequest<T> {

    private static final Gson gson = new Gson();
    private static final HttpClient client = HttpClient.newHttpClient();

    private final String method;
    private final String path;
    private final GlobalTagsAPI<?> api;
    private final Map<String, Object> body;
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
        this(api, method, path, null, responseType);
    }

    /**
     * Builds a new request.
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
        this.body = body;
        this.responseType = responseType;
    }

    /**
     * Send the request.
     *
     * @param consumer A consumer which gets called when the API responds
     */
    public void sendRequestAsync(Consumer<@NotNull ResponseBody<T>> consumer) {
        try {
            HttpRequest request = getBuilder()
                    .uri(new URI(api.getUrls().getApiBase() + path))
                    .method(method, getBodyPublisher())
                    .build();

            client.sendAsync(request, HttpResponse.BodyHandlers.ofString()).thenAccept(response -> {
                boolean success = response.statusCode() >= 200 && response.statusCode() < 300;
                if(!success) {
                    ErrorSchema body = gson.fromJson(response.body(), ErrorSchema.class);
                    consumer.accept(new ResponseBody<>(false, null, body.error));
                    return;
                }
                T parsedBody = gson.fromJson(response.body(), responseType);
                consumer.accept(new ResponseBody<>(
                        true,
                        parsedBody,
                        null
                ));
            }).exceptionally(throwable -> {
                consumer.accept(new ResponseBody<>(false, null, throwable.getLocalizedMessage()));
                return null;
            });
        } catch (Exception e) {
            consumer.accept(new ResponseBody<>(false, null, e.getLocalizedMessage()));
        }
    }

    /**
     * Get a builder already containing all needed headers.
     *
     * @return A builder already containing all needed headers
     */
    private HttpRequest.Builder getBuilder() {
        return HttpRequest.newBuilder()
                .header("Content-Type", "application/json")
                .header("Authorization", api.getAuthorizationHeader())
                .header("X-Language", api.getLanguageCode())
                .header("X-Agent", api.getAgent().toString());
    }

    /**
     * Get the {@link HttpRequest.BodyPublisher} from the data parameter.
     *
     * @return The {@link HttpRequest.BodyPublisher} from the data parameter
     */
    private HttpRequest.BodyPublisher getBodyPublisher() {
        if (body == null || body.isEmpty()) return HttpRequest.BodyPublishers.noBody();
        return HttpRequest.BodyPublishers.ofString(gson.toJson(body));
    }

    /**
     * A class which passes a lightweight API response to consumers.
     *
     * @param <T> The return type
     */
    public static class ResponseBody<T> {

        private final boolean successful;
        private final T data;
        private final String error;

        /**
         * Constructs a new response body.
         *
         * @param successful If the request was successful
         * @param data       The response data
         * @param error      An error message
         */
        public ResponseBody(boolean successful, T data, String error) {
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
            return successful;
        }

        /**
         * Gets the data returned if available
         *
         * @return the data if available
         */
        public T getData() {
            return data;
        }

        /**
         * Get the error returned if available
         * @return an error if available
         */
        public String getError() {
            return error;
        }
    }
}
