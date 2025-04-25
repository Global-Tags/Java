package com.rappytv.globaltags.wrapper.model.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.rappytv.globaltags.wrapper.enums.GlobalIcon;

import java.io.IOException;

public class GlobalIconTypeAdapter extends TypeAdapter<GlobalIcon> {

    @Override
    public void write(JsonWriter out, GlobalIcon icon) throws IOException {
        out.value(icon.name());
    }

    @Override
    public GlobalIcon read(JsonReader in) throws IOException {
        return GlobalIcon.valueOf(in.nextString().toUpperCase());
    }
}
