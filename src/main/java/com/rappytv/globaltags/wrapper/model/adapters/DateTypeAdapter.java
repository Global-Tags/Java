package com.rappytv.globaltags.wrapper.model.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.Date;

public class DateTypeAdapter extends TypeAdapter<Date> {

    @Override
    public void write(JsonWriter out, Date date) throws IOException {
        if (date == null) {
            out.nullValue();
        } else {
            out.value(date.getTime());
        }
    }

    @Override
    public Date read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
        }
        return new Date(in.nextLong());
    }
}
