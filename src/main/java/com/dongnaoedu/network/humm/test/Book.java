package com.dongnaoedu.network.humm.test;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author heian
 * @create 2020-03-09-10:52 上午
 * @description
 */
public class Book {

    private String name;
    private int price;

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Book book = (Book) o;

        if (price != book.price) return false;
        return name != null ? name.equals(book.name) : book.name == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + price;
        return result;
    }
}
