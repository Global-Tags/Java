# GlobalTags Java Wrapper

## Overview
The **GlobalTags Java Wrapper** provides an easy-to-use interface for interacting with the GlobalTags API. This wrapper simplifies the integration of custom player tags in a Minecraft mod, enabling developers to fetch tag data including the player's tag, icon, and roles. It offers various methods to authenticate with the API, handle cache, and translate color codes for text display.

## Features
- API interaction through a well-defined interface.
- Customizable color code translation for Minecraft text.
- Authentication support for different methods (e.g., token-based auth).
- Cache management to optimize performance.
- User agent customization for API identification.
- Multilingual support for API responses.

## Installation
To use this wrapper in your Java project, you can add it via Maven or Gradle. The package includes all necessary dependencies and models required to integrate the GlobalTags API into your mod.

Replace the `version` tag value with the current latest version: ![GitHub Tag](https://img.shields.io/github/v/tag/Global-Tags/Java?include_prereleases&label=%20)

### Maven:
```xml
<dependency>
  <groupId>com.rappytv.globaltags</groupId>
  <artifactId>GlobalTagsJava</artifactId>
  <version>VERSION</version>
</dependency>
```

### Gradle:
```groovy
implementation 'com.rappytv.globaltags:GlobalTagsJava:VERSION'
```

## Usage
To implement the wrapper, create a class that implements the `GlobalTagsAPI<T>` interface. This will give you access to a wide range of methods to interact with the GlobalTags API, such as fetching player information, translating color codes, handling authentication, and more.

The `T` generic in `GlobalTagsAPI<T>` represents the type used for colored text components. It allows flexibility in how you implement color formatting, whether as a simple `String`, a `TextComponent`, or another type. Please note that the `ApiHandler<T>` and the `PlayerInfo<T>` also need to use the same generic value as the `GlobalTagsAPI<T>`.

### Example Implementation:
```java
public class MyGlobalTagsAPI implements GlobalTagsAPI<String> {

    // Create these two objects so you don't instantiate a new object every method call
    private final ApiHandler<String> apiHandler = new ApiHandler<>(this);
    private final PlayerInfo.Cache<String> cache = new PlayerInfo.Cache<>(this);

    @Override
    public Agent getAgent() {
        // 1. argument - Wrapper name, 2. argument - Wrapper version, 3. argument - Minecraft version
        return new Agent("MyMod", "v1.0.0", "1.21");
    }

    @Override
    public String getLanguageCode() {
        // Send a language code to the api. If we have translations for this language, any api response will be localized.
        // This method does not need to be overridden. Just remove it if you don't plan to implement localized responses.
        return "en_us";
    }

    @Override
    public String translateColorCodes(String input) {
        // Implement color code translation logic here
        return input; // For simplicity, return input as is
    }

    @Override
    public UUID getClientUUID() {
        // Return the current client's UUID
        return UUID.randomUUID(); // For testing, generate a random UUID
    }

    @Override
    public PlayerInfo.Cache<String> getCache() {
        return cache;
    }

    @Override
    public ApiHandler<String> getApiHandler() {
        return apiHandler;
    }

    @Override
    public AuthProvider getAuthType() {
        // Define the authentication method
        return AuthProvider.YGGDRASIL; // Assuming minecraft session token based authentication
    }

    @Override
    public String getAuthorization() {
        // Return the authorization token
        return "my-api-token"; // Replace with the clients actual auth token
    }
}
```
Once you've implemented the GlobalTagsAPI<T> interface, you can utilize its methods to interact with the GlobalTags API. Here’s a guide on how to use it for key operations:
```java
public class Main {
    
    private static GlobalTagsAPI<String> api;

    public static void main(String[] args) {
        // Create an instance of your implementation and save it in some kind of field or attribute
        api = new MyGlobalTagsAPI();

        // Fetch a player's tag data, cache it and print it out once resolved
        api.getCache().resolve(uuid, System.out::println);

        // Fetch the client's tag data, cache it and print it out once resolved (The client's uuid is the uuid specified in GlobalTagsAPI#getClientUUID)
        api.getCache().resolveSelf(System.out::println);

        // Fetch a player's tag data without caching it and print it out once resolved
        api.getApiHandler().getInfo(uuid, System.out::println);

        // Get a player's tag from the cache (or null if it's not in the cache)
        System.out.println(api.getCache().get(uuid).getTag());

        // Report a player and log the response message
        api.getApiHandler().reportPlayer(uuid, "Racism", (response) -> System.out.println(response.data()));

        // Get a player's ban reason (Note: This will only work on accounts with the GlobalTags admin permissions)
        PlayerInfo<String> info = api.getCache().get(uuid);
        // Suspension#getReason will NOT be null while Suspension#isActive is true
        System.out.println(info.isSuspended() ? info.getSuspension().getReason() : "The user is not banned.");

        // Clear the cache
        api.getCache().clear();
    }
}
```
Also, everything is documented with javadocs so everything should be pretty self-explanatory. If you have any questions don't hesitate to create a [new issue](https://github.com/Global-Tags/Java/issues/new) or create a ticket on the [Discord Server](https://globaltags.xyz/discord).

## Caching
The wrapper includes a built-in caching mechanism that helps reduce redundant API calls. You can define how long the cache lives by overriding the `getCacheLiveDuration()` method and returning a time value in milliseconds. By default, the cache is cleared every 5 minutes.

## Authentication
To authenticate with the API, you need to provide an authorization token or other credentials depending on the authentication method (`AuthProvider`).
To create an own auth mechanism for the API please read [this page](https://github.com/Global-Tags/API/blob/master/CONTRIBUTING.md).

## Examples
You can see production examples here:
- [LabyMod](https://labymod.net/) Addon: [[GitHub](https://github.com/Global-Tags/LabyAddon/blob/master/api/src/main/java/com/rappytv/globaltags/api/GlobalTagAPI.java)]