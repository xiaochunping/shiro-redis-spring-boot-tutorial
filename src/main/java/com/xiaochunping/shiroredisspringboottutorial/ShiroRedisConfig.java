package com.xiaochunping.shiroredisspringboottutorial;

import org.apache.shiro.mgt.SessionsSecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import com.xiaochunping.shiro.RedisCacheManager;
import com.xiaochunping.shiro.RedisSessionDAO;
import com.xiaochunping.shiroredisspringboottutorial.realm.ExampleRealm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.List;

@Configuration
@PropertySource("classpath:application.properties")
public class ShiroRedisConfig {

    // Here is an example of inject redisSessionDAO and redisCacheManager by yourself if you've created your own sessionManager or securityManager
    @Autowired
    RedisSessionDAO redisSessionDAO;

    @Autowired
    RedisCacheManager redisCacheManager;

    @Bean
    public ExampleRealm myShiroRealm() {
        ExampleRealm myShiroRealm = new ExampleRealm();
        //myShiroRealm.setCredentialsMatcher(hashedCredentialsMatcher());;
        return myShiroRealm;
    }

    @Bean
    public SessionManager sessionManager() {
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();

        // inject redisSessionDAO
        sessionManager.setSessionDAO(redisSessionDAO);
        return sessionManager;
    }

    @Bean
    public SessionsSecurityManager securityManager(List<Realm> realms, SessionManager sessionManager) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager(realms);

        securityManager.setRealm(myShiroRealm());

        // inject redisCacheManager
        securityManager.setCacheManager(redisCacheManager);

        //inject sessionManager
        securityManager.setSessionManager(sessionManager);

        return securityManager;
    }

}
