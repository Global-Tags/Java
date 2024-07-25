package com.rappytv.globaltags.wrapper.http;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.rappytv.globaltags.wrapper.GlobalTagsAPI;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.function.Consumer;

public class ApiRequest<T> {

    private final static Gson gson = new Gson();
    private final static HttpClient client = HttpClient.newHttpClient();

    private final String method;
    private final String path;
    private final GlobalTagsAPI<T> api;
    private final Map<String, Object> body;

    public ApiRequest(GlobalTagsAPI<T> api, String method, String path, Map<String, Object> body) {
        this.api = api;
        this.method = method;
        this.path = path;
        this.body = body;
    }

    public void sendRequestSync(Consumer<@NotNull Response> consumer) {
        try {
            HttpRequest request = getBuilder()
                    .uri(new URI(api.getApiBase() + path))
                    .method(method, getBodyPublisher())
                    .build();

            client.sendAsync(request, HttpResponse.BodyHandlers.ofString()).thenAccept(response -> {
                JsonObject object = gson.fromJson(response.body(), JsonObject.class);
                consumer.accept(new Response(
                        response.statusCode() >= 200 && response.statusCode() < 300,
                        response.statusCode(),
                        object
                ));
            });
        } catch (Exception e) {
            consumer.accept(new Response(false, -1, null));
            e.printStackTrace();
        }
    }

    private HttpRequest.Builder getBuilder() {
        return HttpRequest.newBuilder()
                .header("Content-Type", "application/json")
                .header("Authorization", api.getAuthorizationHeader())
                .header("X-Agent", api.getAgent());
    }

    private HttpRequest.BodyPublisher getBodyPublisher() {
        if(body == null || body.isEmpty()) return HttpRequest.BodyPublishers.noBody();
        return HttpRequest.BodyPublishers.ofString(gson.toJson(body));
    }

    public record Response(boolean successful, int statusCode, @Nullable JsonObject body) {}
}
