package org.task.cache;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class UserCache {

    // <ACCESS_TOKEN, USER>
    //NOTE: memory leak, need to erase authentications, because they don't even equal to each other
    //will work for current implementation
    private final Map<String, Authentication> users = new ConcurrentHashMap<>();

    public Optional<Authentication> getAuthentication(String accessToken) {
        return Optional.ofNullable(users.get(accessToken));
    }

    public void eraseToken(String accessToken) {
        users.remove(accessToken);
    }

    public void putAccessToken(String accessToken, Authentication user) {
        users.put(accessToken, user);
    }

}
