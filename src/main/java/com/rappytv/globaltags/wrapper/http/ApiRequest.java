package com.rappytv.globaltags.wrapper.http;

import com.google.gson.Gson;
import com.rappytv.globaltags.wrapper.GlobalTagsAPI;
import org.jetbrains.annotations.NotNull;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.function.Consumer;

/**
 * A utility request class.
 */
public class ApiRequest {

    private final static Gson gson = new Gson();
    private final static HttpClient client = HttpClient.newHttpClient();

    private final String method;
    private final String path;
    private final GlobalTagsAPI<?> api;
    private final Map<String, Object> body;

    /**
     * Builds a new request without body
     * @param api An api instance
     * @param method The method
     * @param path The request path, use {@link Routes}
     */
    public ApiRequest(GlobalTagsAPI<?> api, String method, String path) {
        this(api, method, path, null);
    }

    /**
     * Builds a new request
     * @param api An api instance
     * @param method The method
     * @param path The request path, use {@link Routes}
     * @param body The request body
     */
    public ApiRequest(GlobalTagsAPI<?> api, String method, String path, Map<String, Object> body) {
        this.api = api;
        this.method = method;
        this.path = path;
        this.body = body;
    }

    /**
     * Send the request
     * @param consumer A consumer which gets called when the API responded
     */
    public void sendRequestSync(Consumer<@NotNull Response> consumer) {
        try {
            HttpRequest request = getBuilder()
                    .uri(new URI(api.getUrls().getApiBase() + path))
                    .method(method, getBodyPublisher())
                    .build();

            client.sendAsync(request, HttpResponse.BodyHandlers.ofString()).thenAccept(response -> {
                ResponseBody body = gson.fromJson(response.body(), ResponseBody.class);
                consumer.accept(new Response(
                        response.statusCode() >= 200 && response.statusCode() < 300,
                        response.statusCode(),
                        body
                ));
            }).exceptionally(throwable -> {
                throwable.printStackTrace();
                consumer.accept(new Response(false, -1, getExceptionBody(throwable)));
                return null;
            });
        } catch (Exception e) {
            e.printStackTrace();
            consumer.accept(new Response(false, -1, getExceptionBody(e)));
        }
    }

    /**
     * Get a builder already containing all needed headers
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
     * Get the {@link HttpRequest.BodyPublisher} from the body parameter
     * @return The {@link HttpRequest.BodyPublisher} from the body parameter
     */
    private HttpRequest.BodyPublisher getBodyPublisher() {
        if(body == null || body.isEmpty()) return HttpRequest.BodyPublishers.noBody();
        return HttpRequest.BodyPublishers.ofString(gson.toJson(body));
    }

    /**
     * Get an error body from a throwable
     * @param throwable The throwable
     * @return An error body from the throwable
     */
    private ResponseBody getExceptionBody(Throwable throwable) {
        return gson.fromJson("{ \"error\": \"" + throwable.getLocalizedMessage() + "\" }", ResponseBody.class);
    }

    /**
     * A record which passes a lightweight API response to consumers
     * @param successful If the request was successful
     * @param statusCode The response status code
     * @param body The response body
     */
    public record Response(boolean successful, int statusCode, @NotNull ResponseBody body) {}
}
