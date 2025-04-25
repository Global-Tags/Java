package com.rappytv.globaltags.wrapper.model.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.rappytv.globaltags.wrapper.enums.GlobalIcon;
import com.rappytv.globaltags.wrapper.enums.GlobalPosition;

import java.io.IOException;

public class GlobalPositionTypeAdapter extends TypeAdapter<GlobalPosition> {

    @Override
    public void write(JsonWriter out, GlobalPosition position) throws IOException {
        out.value(position.name());
    }

    @Override
    public GlobalPosition read(JsonReader in) throws IOException {
        return GlobalPosition.valueOf(in.nextString().toUpperCase());
    }
}
