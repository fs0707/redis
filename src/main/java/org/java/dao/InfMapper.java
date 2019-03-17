package org.java.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.java.entity.Info;

import java.util.List;
import java.util.Map;

@Mapper
public interface InfMapper {

    /**
     *
     * @param startIndex  开始下标
     * @param size  查询几条数据
     * @return
     */
    public List<Info> getList(@Param("startIndex") Integer startIndex, @Param("size") Integer size);


    /**
     * 统计总数
     * @return
     */
    public int getCount();
    public void add(Map m);

    public void del(@Param("id") Integer id);

    public Info find(@Param("id") Integer id);
    public void update(Map m);

}
