package com.security.village.webservice.retrofit.response;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * Created by fruitware on 12/24/15.
 */
@JsonIgnoreProperties (ignoreUnknown = true)
public class Image implements Serializable {
    public Image() {
    }

    private Formats formats;

    public Formats getFormats() {
        return formats;
    }

    public void setFormats(Formats formats) {
        this.formats = formats;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Formats implements Serializable{
        public Formats(){}

        private String original;
        private String smallThumb;
        private String mediumThumb;
        private String bigThumb;

        public String getBigThumb() {
            return bigThumb;
        }

        public String getMediumThumb() {
            return mediumThumb;
        }

        public String getOriginal() {
            return original;
        }

        public String getSmallThumb() {
            return smallThumb;
        }

        public void setBigThumb(String bigThumb) {
            this.bigThumb = bigThumb;
        }

        public void setMediumThumb(String mediumThumb) {
            this.mediumThumb = mediumThumb;
        }

        public void setOriginal(String original) {
            this.original = original;
        }

        public void setSmallThumb(String smallThumb) {
            this.smallThumb = smallThumb;
        }
    }
}
