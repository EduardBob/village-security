package com.security.village.webservice.retrofit.response;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import java.io.Serializable;

/**
 * Created by fruitware on 12/23/15.
 */
@JsonIgnoreProperties (ignoreUnknown = true)
public class Token implements Serializable{
    public Token(){}

    @JsonProperty("data")
    public Data data;

    public void setData(Data data) {
        this.data = data;
    }

    public Data getData() {
        return data;
    }

    private class Data {
        public Data(){}

        public String token;

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }

    public String getToken(){
        return this.data.getToken();
    }
}
