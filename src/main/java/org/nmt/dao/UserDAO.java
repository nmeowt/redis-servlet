package org.nmt.dao;

import org.nmt.model.Key;
import org.nmt.model.User;
import org.nmt.redis.RedisConnector;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserDAO {
    private static final RedisConnector redisConnector = new RedisConnector();

    public User create(User user) {
        Jedis jedis = redisConnector.connect();
        long userId = jedis.incr(Key.USER_IDS.key());
        user.setId(userId);
        Pipeline pipeline = jedis.pipelined();
        pipeline.lpush(Key.USER_ALL.key(), String.valueOf(userId));
        Map<String, String> userProperties = new HashMap<>();
        userProperties.put("id", String.valueOf(userId));
        userProperties.put("name", user.getName());
        userProperties.put("address", user.getAddress());
        pipeline.hmset(Key.USER_DATA.formatted(String.valueOf(userId)), userProperties);
        pipeline.sync();
        return user;
    }

    public List<User> list() {
        Jedis jedis = redisConnector.connect();
        List<User> users = new ArrayList<>();
        //Get all user ids from the redis list using LRANGE
        List<String> allUserIds = jedis.lrange(Key.USER_ALL.key(), 0, -1);
        if (allUserIds != null && !allUserIds.isEmpty()) {
            List<Response<Map<String, String>>> responseList = new ArrayList<Response<Map<String, String>>>();

            Pipeline pipeline = jedis.pipelined();
            for (String userId : allUserIds) {
                //call HGETALL for each user id
                responseList.add(pipeline.hgetAll(Key.USER_DATA.formatted(String.valueOf(userId))));
            }
            pipeline.sync();
            //iterate over the pipelined results
            for (Response<Map<String, String>> properties : responseList) {
                User user = new User();
                user.setId(Integer.valueOf(properties.get().get("id")));
                user.setName(properties.get().get("name"));
                user.setAddress(properties.get().get("address"));
                users.add(user);
            }
        }
        return users;
    }
}
