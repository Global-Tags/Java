package com.rappytv.globaltags.wrapper.model.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.rappytv.globaltags.wrapper.enums.GlobalPermission;

import java.io.IOException;

public class GlobalPermissionTypeAdapter extends TypeAdapter<GlobalPermission> {

    @Override
    public void write(JsonWriter out, GlobalPermission permission) throws IOException {
        out.value(permission.name());
    }

    @Override
    public GlobalPermission read(JsonReader in) throws IOException {
        return GlobalPermission.valueOf(in.nextString().toUpperCase());
    }
}
