package com.mycompany.servicewatch.protocol;
//converter that turns Java objects into JSON text and back.
import com.google.gson.Gson;
//Lets you choose settings before creating the converter.
import com.google.gson.GsonBuilder;
//The error your code throws when it cannot read the text as an expected message.
import com.google.gson.JsonSyntaxException;
//Gives you the STRICT setting for checking JSON syntax.
import com.google.gson.Strictness;

public class ProtocolJson {
//new GsonBuilder()creates the builder to configure setting
    private static final Gson gson = new GsonBuilder()
            //require valid JSON syntax
            .setStrictness(Strictness.STRICT)
            //builds the configured Gson converter
            .create();
//prepares text to send (convert from object to string)
    public static String toJson(ProtocolMessage message) {
        return gson.toJson(message);
    }
//read the text recieved and turn to object
    public static ProtocolMessage fromJson(String line) {
        //line is string need to convert to type of ProtocolMessage class
        ProtocolMessage message = gson.fromJson(line, ProtocolMessage.class);
        
        if (message == null || message.getType() == null) {
            throw new JsonSyntaxException("Expected a message with a known type");
        }

        return message;
    }
}