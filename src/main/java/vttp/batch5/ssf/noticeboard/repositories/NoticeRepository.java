package vttp.batch5.ssf.noticeboard.repositories;

import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public class NoticeRepository {

	// TODO: Task 4
	// You can change the signature of this method by adding any number of parameters
	// and return any type
	// 
	/*
	 * Write the redis-cli command that you use in this method in the comment. 
	 * For example if this method deletes a field from a hash, then write the following
	 * redis-cli command 
	 * 	hdel myhashmap a_key
	 *
	 *
	 */

	@Autowired
	@Qualifier("notice")
	private RedisTemplate<String, Object> redisTemplate;

	//hset key "field" "value"
	public String insertNotices(JsonObject payload) {

		String key = payload.getString("id");

		redisTemplate.opsForHash().put("Notices", key, payload.toString());

		System.out.println("Notice saved into redis");

		System.out.println(key);

		return key;
	}



}
