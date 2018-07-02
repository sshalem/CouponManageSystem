package shabtay.coupon.system.redis;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import shabtay.coupon.system.common.ConstantList;

@Configuration
@EnableCaching
public class RedisConfig extends CachingConfigurerSupport {

	
	// This is spring's redisTemplate with 
	
	@Bean
	public JedisConnectionFactory redisConnectionFactory() {
		JedisConnectionFactory redis = new JedisConnectionFactory();
		// Defaults
		redis.setHostName("localhost");
		redis.setPort(6379);
		return redis;
	}

	@Bean
	public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisCf) {
		RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<Object, Object>();
		redisTemplate.setConnectionFactory(redisCf);
		return redisTemplate;
	}

	@Bean
	public CacheManager cacheManager(RedisTemplate redisTemplate) {
		RedisCacheManager cacheManager = new RedisCacheManager(redisTemplate);
		
		// REDIS_SERVER_EXPIRATION_TIME:
		// I have @cachaeable decorators for get methods (like getCoupons).
		// Once GetCupons is invoked, at begging the data is fetched from the DB
		// after that , every time that I will invoke getCoupons ,data will be fetched from REDIS server ,
		// till time for the REDIS server will expire which is the time that I defined here:
		// "cacheManager.setDefaultExpiration(ConstantList.REDIS_SERVER_EXPIRATION_TIME);"
		// Once the time is expired (for instance 10sec) , on the next time that getCoupons will be 
		// Invoked , the data again will be feteched from DB (till time expires again )
		cacheManager.setDefaultExpiration(ConstantList.REDIS_SERVER_EXPIRATION_TIME);
		cacheManager.setUsePrefix(true);
		return cacheManager;
	}

}
