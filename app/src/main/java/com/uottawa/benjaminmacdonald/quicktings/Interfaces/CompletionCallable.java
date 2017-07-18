package com.uottawa.benjaminmacdonald.quicktings.Interfaces;

import com.uottawa.benjaminmacdonald.quicktings.Classes.ShoppingCart;

/**
 * Created by thomas on 17/07/17.
 */
public interface CompletionCallable {
    int ADDED = 0;
    int REMOVED = 1;
    int UPDATED = 2;

    void onComplete(ShoppingCart.CartItem item, int resultCode);

    void onFinalize();
}
