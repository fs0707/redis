package org.java.service;

import org.java.entity.Info;

import java.util.List;
import java.util.Map;

public interface InfService {
    /**
     *
     * @param currPage 当前页
     * @param size  每页显示几条数据
     * @return
     */
    public List<Info> getList(Integer currPage, Integer size);


    public int getCount();

    public void del(Integer id);

    public void add(Map m);

    public void update(Map m);

    public Info find(Integer id);
}
