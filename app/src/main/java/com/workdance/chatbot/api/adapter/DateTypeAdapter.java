package com.workdance.chatbot.api.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTypeAdapter extends TypeAdapter<Date> {

    private static final String DATE_FORMAT = "YYYY-MM-DD'T'hh:mm:ss.sss±hh:mm";
    private SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);

    @Override
    public void write(JsonWriter out, Date date) throws IOException {
        if (date == null) {
            out.nullValue();
        } else {
            out.value(dateFormat.format(date));
        }
    }

    @Override
    public Date read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
        }
        try {
            String dateStr = in.nextString();
            return dateFormat.parse(dateStr);
        } catch (ParseException e) {
            throw new IOException("Error parsing date: " + e.getMessage());
        }
    }
}