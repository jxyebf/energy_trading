package com.example.energy_trading.Util.callback;


import androidx.annotation.NonNull;

/**

 */

public interface IGlobalCallback<T> {

    void executeCallback(@NonNull T args);
}
