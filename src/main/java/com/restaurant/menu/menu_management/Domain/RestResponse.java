package com.restaurant.menu.menu_management.Domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

//Các API sẽ trả ra dạng format như này
public class RestResponse<T> {
    private int statusCode;
    private String error;

    // message có thể là string, hoặc arrayList
    private Object message;
    private T data;
}
