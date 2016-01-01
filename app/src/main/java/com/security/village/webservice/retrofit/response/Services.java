package com.security.village.webservice.retrofit.response;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * Created by fruitware on 12/24/15.
 */
@JsonIgnoreProperties (ignoreUnknown = true)
public class Services implements Serializable {
    public Data[] data;

    public Services(){

    }

    public void setData(Data[] data) {
        this.data = data;
    }

    public Data[] getData() {
        return data;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Data {
        private String id;
        private String title;
        private int price;
        private String text;
        private Category category;

        public Data(){}

        public Category getCategory() {
            return category;
        }

        public String getId() {
            return id;
        }

        public int getPrice() {
            return price;
        }

        public String getText() {
            return text;
        }

        public String getTitle() {
            return title;
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setPrice(int price) {
            this.price = price;
        }

        public void setText(String text) {
            this.text = text;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public void setCategory(Category category) {
            this.category = category;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ServiceData{private String id;
        private int price;
        private String title;
        private String comment_label;
        private String text;
        private Image image;
        private Category category;

        public ServiceData(){}

        public Category getCategory() {
            return category;
        }

        public Image getImage() {
            return image;
        }

        public int getPrice() {
            return price;
        }

        public String getComment_label() {
            return comment_label;
        }

        public String getId() {
            return id;
        }

        public String getText() {
            return text;
        }

        public String getTitle() {
            return title;
        }

        public void setCategory(Category category) {
            this.category = category;
        }

        public void setComment_label(String comment_label) {
            this.comment_label = comment_label;
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setImage(Image image) {
            this.image = image;
        }

        public void setPrice(int price) {
            this.price = price;
        }

        public void setText(String text) {
            this.text = text;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Category{
        private ServiceData data;

        public Category(){}

        public ServiceData getData() {
            return data;
        }

        public void setData(ServiceData data) {
            this.data = data;
        }
    }
}
