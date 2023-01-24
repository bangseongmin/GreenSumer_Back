package org.swyg.greensumer.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.stereotype.Repository;
import org.swyg.greensumer.dto.User;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class UserCacheRepository {

    private final RedisTemplate<String, Object> redisTemplate;
    private final static Duration USER_CACHE_TTL = Duration.ofDays(3);

    private ValueOperations<String, Object> valueOperations;

    @PostConstruct
    public void init() {
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<User>(User.class));
        this.valueOperations = redisTemplate.opsForValue();
    }

    public void setUser(User user){
        if(Objects.isNull(user) || Objects.isNull(user.getUsername())){
            log.error("Required Values must not be null");
            return;
        }

        try{
            String key = getKey(user.getUsername());
            valueOperations.set(key, user, USER_CACHE_TTL);
            log.info("[UserCacheRepository save - success]  id: {}", user.getId());
        }catch (Exception e){
            log.error("[UserCacheRepository save - error] {}", e.getMessage());
        }
    }

    public Optional<User> getUser(String username){
        String key = getKey(username);
        User user = (User) valueOperations.get(key);
        log.info("Get data from Redis {}, {}", key, user);
        return Optional.ofNullable(user);
    }

    public String getKey(String username){
        return "USER:"+username;
    }

}
