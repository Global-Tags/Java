package com.rappytv.globaltags.wrapper.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.net.http.HttpRequest;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

@SuppressWarnings("unused")
public class MultipartData {

    private String boundary;
    private HttpRequest.BodyPublisher bodyPublisher;

    private MultipartData() {
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public HttpRequest.BodyPublisher getBodyPublisher() {
        return this.bodyPublisher;
    }

    public String getContentType() {
        return "multipart/form-data; boundary=" + this.boundary;
    }

    public static class Builder {

        private final String boundary;
        private final Charset charset = StandardCharsets.UTF_8;
        private final List<MimedFile> files = new ArrayList<>();
        private final Map<String, String> texts = new LinkedHashMap<>();

        private Builder() {
            this.boundary = new BigInteger(128, new Random()).toString();
        }

        public Builder addFile(String name, Path path, String mimeType) {
            this.files.add(new MimedFile(name, path, mimeType));
            return this;
        }

        public Builder addText(String name, String text) {
            this.texts.put(name, text);
            return this;
        }

        public MultipartData build() throws IOException {
            MultipartData mimeMultipartData = new MultipartData();
            mimeMultipartData.boundary = this.boundary;

            byte[] newline = "\r\n".getBytes(this.charset);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            for (MimedFile file : this.files) {
                byteArrayOutputStream.write(("--" + this.boundary).getBytes(this.charset));
                byteArrayOutputStream.write(newline);
                byteArrayOutputStream.write(("Content-Disposition: form-data; name=\"" + file.name + "\"; filename=\"" + file.path.getFileName() + "\"").getBytes(this.charset));
                byteArrayOutputStream.write(newline);
                byteArrayOutputStream.write(("Content-Type: " + file.mimeType).getBytes(this.charset));
                byteArrayOutputStream.write(newline);
                byteArrayOutputStream.write(newline);
                byteArrayOutputStream.write(Files.readAllBytes(file.path));
                byteArrayOutputStream.write(newline);
            }
            for (Map.Entry<String, String> entry : this.texts.entrySet()) {
                byteArrayOutputStream.write(("--" + this.boundary).getBytes(this.charset));
                byteArrayOutputStream.write(newline);
                byteArrayOutputStream.write(("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"").getBytes(this.charset));
                byteArrayOutputStream.write(newline);
                byteArrayOutputStream.write(newline);
                byteArrayOutputStream.write(entry.getValue().getBytes(this.charset));
                byteArrayOutputStream.write(newline);
            }
            byteArrayOutputStream.write(("--" + this.boundary + "--").getBytes(this.charset));

            mimeMultipartData.bodyPublisher = HttpRequest.BodyPublishers.ofByteArray(byteArrayOutputStream.toByteArray());
            return mimeMultipartData;
        }

        public static class MimedFile {

            private final String name;
            private final Path path;
            private final String mimeType;

            public MimedFile(String name, Path path, String mimeType) {
                this.name = name;
                this.path = path;
                this.mimeType = mimeType;
            }

            public String getName() {
                return this.name;
            }

            public Path getPath() {
                return this.path;
            }

            public String getMimeType() {
                return this.mimeType;
            }
        }
    }
}