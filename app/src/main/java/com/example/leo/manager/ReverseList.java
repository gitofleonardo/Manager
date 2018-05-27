package com.example.leo.manager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class ReverseList<T> extends ArrayList<T>
{

    public ReverseList(Collection<T> c) { super(c);   }

    public Iterable<T> reversed()
    {
        return new Iterable<T>()      //第一个匿名的内部类
        {
            @Override
            public Iterator<T> iterator()     //Iterable里面的必须实现的方法
            {
                return new Iterator<T>()      //返回值是第二个匿名内部类
                {
                    int current = size() - 1;   //这个size是ArrayList里面的函数，继承过来的

                    @Override
                    public boolean hasNext()
                    {
                        return current > -1;
                    }

                    @Override
                    public T next()
                    {
                        return get(current--);  //这个是向前推进的
                    }

                    @Override
                    public void remove()
                    {
                        throw new UnsupportedOperationException();
                    }

                };

            }

        };
    }

}
