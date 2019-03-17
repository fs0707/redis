package org.java.service.impl;

import org.java.dao.InfMapper;
import org.java.entity.Info;
import org.java.service.InfService;
import org.java.util.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.JedisCluster;

import java.util.List;
import java.util.Map;

@Service
public class InfServiceImpl implements InfService {



    @Autowired
    private InfMapper mapper;

    @Autowired
    private JedisCluster jedisCluster;


    @Override
    public List<Info> getList(Integer currPage, Integer size) {
              //计算开始下标
        int startIndex=(currPage-1)*size;

        String json = jedisCluster.hget("springboot_thymeleaf_all", "getList");
        List<Info> list=null;
        if(json==null){ //25
            //缓存中没有数据，要读取数据库
            synchronized (this){
                //双重判断
                json = jedisCluster.hget("springboot_thymeleaf_all", "getList");
                if(json ==null){
                    System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@从数据库加载数据");
                    list = mapper.getList(startIndex, size);
                    //把数据存放到json
                    jedisCluster.hset("springboot_thymeleaf_all", "getList", JsonUtils.objectToJson(list));
                }else{
                    System.out.println("---------------------------------------------从缓存中加载");
                    list = JsonUtils.jsonToList(json, Info.class);
                }

            }
        }else{
            System.out.println("----从缓存中加载 ");
            //从缓存中加载
            list = JsonUtils.jsonToList(json, Info.class );
        }

        return list;
    }

    @Override
    public int getCount() {
        return mapper.getCount();
    }

    @Transactional
    @Override
    public void del(Integer id) {
        mapper.del(id);
    }

    @Transactional
    @Override
    public void add(Map m) {
        mapper.add(m);
    }
    @Override
    public void update(Map m) {
        mapper.update(m);
    }

    @Override
    public Info find(Integer id) {
        return mapper.find(id);
    }

}
