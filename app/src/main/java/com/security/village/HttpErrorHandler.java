package com.security.village;

import retrofit.RetrofitError;

/**
 * Created by fruitware on 12/27/15.
 */
public class HttpErrorHandler {
    public static String handleError(RetrofitError error){
        try {
            switch (error.getResponse().getStatus()) {
                case 400:
                    if(error.getBody().toString().contains("order_must_be_paid_if_status_done")) {
                        return "Заявка должна быть оплачена";
                    }
                    if(error.getBody().toString().contains("GEN-WRONG-ARGS")) {
                        return "Одно или более полей неправильно заполнены";
                    }
                    if(error.getBody().toString().contains("token_invalid") || error.getBody().toString().contains("token_expired") || error.getBody().toString().contains("token_not_provided"))
                        return "token_invalid";
                case 401:
                    if(error.getBody().toString().contains("token_invalid") || error.getBody().toString().contains("token_expired") || error.getBody().toString().contains("token_not_provided"))
                        return "token_invalid";
                case 403:
                    return "Нет прав на совершение данного действия";
                case 404:
                    return "Объект не найден";
            }
            if(error.getResponse().getStatus() >= 500)
                return "Ведутся технические работы. Приносим свои извинения за неудобства";
            else
                return null;
        } catch (Exception e) {
            e.printStackTrace();
            return "Отсутствует соединие с интернетом";
        }
    }
}
