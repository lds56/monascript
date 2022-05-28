package org.lds56.mona.core.engine;

/**
 * @Author: Rui Chen
 * @Date: 28 May 2022
 * @Description: This is description.
 */
public class Foo {

    public Foo(Integer bar) {
        this.bar = bar;
    }

    public Integer bar;

    public Integer incBar() {
        bar ++;
       return  bar;
    }

    public Integer getBar() {
        return bar;
    }
}
