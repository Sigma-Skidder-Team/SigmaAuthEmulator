package com.mentalfrostbyte.utils;

import java.io.*;

public class FileUtil {

    //from: https://sentry.io/answers/inputstream-to-string/
    public static String toString_ByteArrayOutputStream(InputStream stream) throws IOException {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        byte[] buffer = new byte[1024];

        int readBytes = stream.read(buffer);

        while(readBytes != -1){
            outputStream.write(buffer, 0, readBytes);
            readBytes = stream.read(buffer);
        }

        return outputStream.toString();
    }

    public static String readInputStream(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) != -1) {
            byteArrayOutputStream.write(buffer, 0, length);
        }
        return byteArrayOutputStream.toString("UTF-8");  // Convert the byte array to a string
    }

}
