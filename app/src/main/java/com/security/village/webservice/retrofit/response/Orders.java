package com.security.village.webservice.retrofit.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonIgnoreType;

import java.io.Serializable;

/**
 * Created by fruitware on 12/24/15.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class Orders implements Serializable {
    public Data[] data;

    public Order order;

    public Orders(){

    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Order getOrder() {
        return order;
    }

    public void setData(Data[] data) {
        this.data = data;
    }

    public Data[] getData() {
        return data;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Order implements Serializable{
        public Data data;

        public Order(){}

        public Data getData() {
            return data;
        }

        public void setData(Data data) {
            this.data = data;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Data implements Serializable {
        private String id;
        private String price;
        private String perform_date;
        private String perform_time;
        private String payment_type;
        private String status;
        private String payment_status;
        private String added_from;
        private String phone;
        private String admin_comment;
        private String comment;
        private Service service;
        private User user;

        public Data(){}

        public String getAdmin_comment() {
            return admin_comment;
        }

        public String getPhone() {
            return phone;
        }

        public User getUser() {
            return user;
        }

        public String getPerform_date() {
            return perform_date;
        }

        public String getPerform_time() {
            return perform_time;
        }

        public String getStatus() {
            return status;
        }

        public Service getService() {
            return service;
        }

        public String getId() {
            return id;
        }

        public String getAdded_from() {
            return added_from;
        }

        public String getComment() {
            return comment;
        }

        public String getPayment_status() {
            return payment_status;
        }

        public String getPayment_type() {
            return payment_type;
        }

        public String getPrice() {
            return price;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public void setAdmin_comment(String admin_comment) {
            this.admin_comment = admin_comment;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setAdded_from(String added_from) {
            this.added_from = added_from;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public void setPayment_status(String payment_status) {
            this.payment_status = payment_status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public void setPayment_type(String payment_type) {
            this.payment_type = payment_type;
        }

        public void setService(Service service) {
            this.service = service;
        }

        public void setPerform_date(String perform_date) {
            this.perform_date = perform_date;
        }

        public void setPerform_time(String perform_time) {
            this.perform_time = perform_time;
        }

        public void setUser(User user) {
            this.user = user;
        }

        /*@Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {

        }

        public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
            public Data createFromParcel(Parcel in) {
                return new Data();
            }

            public Data[] newArray(int size) {
                return new Data[size];
            }
        };*/
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Service implements Serializable{
        private ServiceData data;

        public Service(){}

        public ServiceData getData() {
            return data;
        }

        public void setData(ServiceData data) {
            this.data = data;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ServiceData implements Serializable{
        private String id;
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
    public static class Category implements Serializable{
        private ServiceData data;

        public Category(){}

        public ServiceData getData() {
            return data;
        }

        public void setData(ServiceData data) {
            this.data = data;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class User implements Serializable{
        private UserData data;

        public User(){}

        public UserData getData() {
            return data;
        }

        public void setData(UserData data) {
            this.data = data;
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class UserData implements Serializable{
            private String id;
            private String first_name;
            private String last_name;
            private String email;
            private String phone;
            private boolean activated;

            public UserData(){}

            public String getEmail() {
                return email;
            }

            public String getFirst_name() {
                return first_name;
            }

            public String getId() {
                return id;
            }

            public String getLast_name() {
                return last_name;
            }

            public String getPhone() {
                return phone;
            }

            public void setActivated(boolean activated) {
                this.activated = activated;
            }

            public void setEmail(String email) {
                this.email = email;
            }

            public void setFirst_name(String first_name) {
                this.first_name = first_name;
            }

            public void setId(String id) {
                this.id = id;
            }

            public void setLast_name(String last_name) {
                this.last_name = last_name;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }
        }

    }
}


