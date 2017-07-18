package com.uottawa.benjaminmacdonald.quicktings.Interfaces;

/**
 * Created by thomas on 17/07/17.
 */
public interface CompletionCallable<E> {
    int ADDED = 0;
    int REMOVED = 1;
    int UPDATED = 2;

    void onComplete(E item, int resultCode);

    void onFinalize();
}
