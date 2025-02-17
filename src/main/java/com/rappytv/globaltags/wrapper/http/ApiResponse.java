package com.rappytv.globaltags.wrapper.http;

/**
 * An lightweight class containing response data for requests
 *
 * @param <T> The type of the data
 */
public class ApiResponse<T> {

    private final boolean successful;
    private final T data;
    private final String error;

    /**
     * Constructs a new ApiResponse instance
     *
     * @param successful If the request was successful
     * @param data       The data returned if available
     * @param error      The error returned if available
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
     *
     * @return an error if available
     */
    public String getError() {
        return this.error;
    }

    @Override
    public String toString() {
        return "ApiResponse{" +
                "successful=" + this.successful +
                ", data=" + this.data +
                ", error='" + this.error + '\'' +
                '}';
    }
}