package com.example.secondappfromgb;

public interface Callback <T>{

    void onSuccess (T data);

    void onError (Throwable exception);
}
