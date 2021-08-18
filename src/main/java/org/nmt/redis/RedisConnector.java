package org.nmt.redis;

import redis.clients.jedis.Jedis;

public class RedisConnector {
    private static final String host = "localhost";
    private static final int port = 6379;

    public Jedis connect(){
        Jedis jedis = new Jedis(host,port);
        System.out.println("Connect Redis successfully!");
        return jedis;
    }
}
