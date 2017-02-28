package com.attribes.push2beat.interfaces;

/**
 * Author: Uzair Qureshi
 * Date:  2/24/17.
 * Description:
 */

public interface MyCallBacks<T> {
    void onSuccess(T data);
    void onFailure(String message);
}
