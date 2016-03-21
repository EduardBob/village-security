package com.security.village.webservice.retrofit.response;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * Created by fwmobile on 3/11/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Profile implements Serializable {

    private Data1 data;

    public Profile(){}

    public Data1 getData() {
        return data;
    }

    public void setData(Data1 data) {
        this.data = data;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Data1{
        private String id;
        private String first_name;
        private String last_name;
        private String email;
        private String phone;
        private boolean activated;
        private Building building;

        public Data1(){}

        public Building getBuilding() {
            return building;
        }

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

        public void setId(String id) {
            this.id = id;
        }

        public void setActivated(boolean activated) {
            this.activated = activated;
        }

        public void setBuilding(Building building) {
            this.building = building;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public void setFirst_name(String first_name) {
            this.first_name = first_name;
        }

        public void setLast_name(String last_name) {
            this.last_name = last_name;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Building{
        private DataBuilding data;

        public Building(){}

        public DataBuilding getData() {
            return data;
        }

        public void setData(DataBuilding data) {
            this.data = data;
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class DataBuilding{

            private String id;
            private String address;
            private Village village;

            public DataBuilding(){}

            public String getId() {
                return id;
            }

            public String getAddress() {
                return address;
            }

            public Village getVillage() {
                return village;
            }

            public void setId(String id) {
                this.id = id;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            public void setVillage(Village village) {
                this.village = village;
            }

            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class Village{
                private Data data;

                public Village(){}

                public Data getData() {
                    return data;
                }

                public void setData(Data data) {
                    this.data = data;
                }
            }

            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class Data{
                private String name;
                private String shop_name;
                private String shop_address;
                private String service_payment_info;
                private String service_bottom_text;
                private String product_payment_info;
                private String product_bottom_text;
                private String product_unit_step_kg;
                private String product_unit_step_bottle;
                private String product_unit_step_piece;
                private boolean active;

                public Data(){}

                public String getName() {
                    return name;
                }

                public String getProduct_bottom_text() {
                    return product_bottom_text;
                }

                public String getProduct_payment_info() {
                    return product_payment_info;
                }

                public String getProduct_unit_step_bottle() {
                    return product_unit_step_bottle;
                }

                public String getProduct_unit_step_kg() {
                    return product_unit_step_kg;
                }

                public String getProduct_unit_step_piece() {
                    return product_unit_step_piece;
                }

                public String getService_bottom_text() {
                    return service_bottom_text;
                }

                public String getService_payment_info() {
                    return service_payment_info;
                }

                public String getShop_address() {
                    return shop_address;
                }

                public String getShop_name() {
                    return shop_name;
                }

                public void setActive(boolean active) {
                    this.active = active;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public void setProduct_bottom_text(String product_bottom_text) {
                    this.product_bottom_text = product_bottom_text;
                }

                public void setProduct_payment_info(String product_payment_info) {
                    this.product_payment_info = product_payment_info;
                }

                public void setProduct_unit_step_bottle(String product_unit_step_bottle) {
                    this.product_unit_step_bottle = product_unit_step_bottle;
                }

                public void setProduct_unit_step_kg(String product_unit_step_kg) {
                    this.product_unit_step_kg = product_unit_step_kg;
                }

                public void setProduct_unit_step_piece(String product_unit_step_piece) {
                    this.product_unit_step_piece = product_unit_step_piece;
                }

                public void setService_bottom_text(String service_bottom_text) {
                    this.service_bottom_text = service_bottom_text;
                }

                public void setService_payment_info(String service_payment_info) {
                    this.service_payment_info = service_payment_info;
                }

                public void setShop_address(String shop_address) {
                    this.shop_address = shop_address;
                }

                public void setShop_name(String shop_name) {
                    this.shop_name = shop_name;
                }
            }
        }
    }
}
