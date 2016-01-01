package com.security.village;

import org.codehaus.jackson.map.ObjectMapper;

/**
 * Created by fruitware on 12/23/15.
 */
public class ObjectMap {
    private static ObjectMapper ourInstance = new ObjectMapper();

    public static ObjectMapper getInstance() {
        return ourInstance;
    }

    private ObjectMap() {
    }
}
