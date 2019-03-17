package org.java.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

import java.util.HashSet;
import java.util.Set;


@Configuration//标识，当前类是一个配置类，作用类似于applicationCOntext.xml会在程序启动时，马上运行
public class JedisClient {
    @Value("${spring.redis.cluster.nodes}")
    private String redisClusterNodes;


    /**
     * 获得JedisCluster
     * @return
     * 加载@Bean的方法，它返回的对象，会在程序启动时，就直接加载到内存中----------相当于以前在applicationContext.xml文件中注册的对象，会自动加载
     */
    @Bean
    public JedisCluster getJedisCluter(){
        //存放所有redis服务器
        Set<HostAndPort> nodes = new HashSet<>();
        //按,分割，获得每一台主机的地址与端口
        String[] hps = redisClusterNodes.split(",");

        //对数组迭代，分别获得每一个ip与端口，创建一个HostAndPort放到set集合中
        for(String hp:hps){
            String  host = hp.split(":")[0];//主机地址
            int  port = Integer.parseInt(hp.split(":")[1]);//主机地址

            HostAndPort hostAndPort = new HostAndPort(host,port );
            nodes.add(hostAndPort);//添加主机
        }
        JedisCluster jedisCluster = new JedisCluster(nodes);
        return jedisCluster;
    }
}
