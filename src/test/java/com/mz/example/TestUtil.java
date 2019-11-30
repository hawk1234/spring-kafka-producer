package com.mz.example;

import com.mz.example.service.customization.MessageType;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.springframework.util.StreamUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.UUID;

@UtilityClass
public class TestUtil {

    public static Matcher<String> isUUID(){
        return new TypeSafeMatcher<String>() {

            @Override
            @SuppressWarnings("all")
            public boolean matchesSafely(String uuid) {
                try {
                    UUID.fromString(uuid);
                    return true;
                } catch (IllegalArgumentException ex) {
                    return false;
                }
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("UUID format");
            }
        };
    }

    public static Matcher<String> isLong(){
        return new TypeSafeMatcher<String>() {

            @Override
            public boolean matchesSafely(String number) {
                try {
                    Long.valueOf(number);
                    return true;
                } catch (NumberFormatException ex) {
                    return false;
                }
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("Long value");
            }
        };
    }

    public static Charset utf8(){
        return Charset.forName("UTF-8");
    }

    public static String loadJSONTemplate(@NonNull String fileName) {
        return loadMessageTemplate(MessageType.JSON, fileName);
    }

    public static String loadXMLTemplate(@NonNull String fileName) {
        return loadMessageTemplate(MessageType.XML, fileName);
    }

    public static String loadRequestTemplate(@NonNull String fileName) {
        String filePath = String.join(File.separator, "templates", "request", fileName);
        return loadTemplateForPath(filePath);
    }

    public static String loadMessageTemplate(@NonNull MessageType fileType, @NonNull String fileName) {
        String filePath = String.join(File.separator, "templates", "message", fileType.name().toLowerCase(), fileName);
        return loadTemplateForPath(filePath);
    }

    private static String loadTemplateForPath(@NonNull String filePath) {
        try {
            return StreamUtils.copyToString(TestUtil.class.getClassLoader().getResourceAsStream(filePath), Charset.forName("UTF-8"));
        }catch(IOException ex){
            throw new AssertionError("Unable to read test file: "+filePath, ex);
        }
    }
}
