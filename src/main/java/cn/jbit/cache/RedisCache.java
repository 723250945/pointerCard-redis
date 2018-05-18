package cn.jbit.cache;

import cn.jbit.util.MD5;
import org.apache.ibatis.cache.Cache;
import org.apache.log4j.Logger;
import org.springframework.data.redis.connection.jedis.JedisConnection;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import redis.clients.jedis.exceptions.JedisConnectionException;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class RedisCache implements Cache {

	private static JedisConnectionFactory jedisConnectionFactory;

	private final String id;

	/**
	 * The {@code ReadWriteLock}.
	 */
	private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

	public RedisCache(final String id) {
		if (id == null) {
			throw new IllegalArgumentException("Cache instances require an ID");
		}
		System.out.println(">>>>>>>>>>>>>>>>>>>>>MybatisRedisCache:id=" + id);
		this.id = id;
	}


	public void clear() {
		JedisConnection connection = null;
		try {
			connection = jedisConnectionFactory.getConnection();
			connection.flushDb();
			connection.flushAll();
			System.out.println("clear=redis======>");
		} catch (JedisConnectionException e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				connection.close();
			}
		}
	}


	public String getId() {
		return this.id;
	}


	public Object getObject(Object key) {
		Object result = null;
		JedisConnection connection = null;
		try {
			key = MD5.getMD5Str(key.toString());
			connection = jedisConnectionFactory.getConnection();
			RedisSerializer<Object> serializer = new JdkSerializationRedisSerializer();
			result = serializer.deserialize(connection.get(serializer.serialize(key)));
			if (result == null) {
				removeObject(key);
				return null;
			}
			System.out.println("get-Redis-----key=:" + key);
		} catch (JedisConnectionException e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				connection.close();
			}
		}
		return result;
	}


	public ReadWriteLock getReadWriteLock() {
		return this.readWriteLock;
	}


	public int getSize() {
		int result = 0;
		JedisConnection connection = null;
		try {
			connection = jedisConnectionFactory.getConnection();
			result = Integer.valueOf(connection.dbSize().toString());
			System.out.println(this.id + "---->>>>getSize:" + result);
		} catch (JedisConnectionException e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				connection.close();
			}
		}
		return result;
	}


	public void putObject(Object key, Object value) {
		if (value.toString().equals("[]")) {
			System.out.println("key:="+key+"--- value=:" + value);
			return;
		}
		JedisConnection connection = null;
		try {
			RedisSerializer<Object> serializer = new JdkSerializationRedisSerializer();
			key = MD5.getMD5Str(key.toString());
			connection = jedisConnectionFactory.getConnection();
			connection.set(serializer.serialize(key), serializer.serialize(value));
			System.out.println("rdis-put--------key:=" + key + "  value:=" + value);
		} catch (JedisConnectionException e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				connection.close();
			}
		}
	}


	public Object removeObject(Object key) {
		JedisConnection connection = null;
		Object result = null;
		try {
			key = MD5.getMD5Str(key.toString());
			connection = jedisConnectionFactory.getConnection();
			RedisSerializer<Object> serializer = new JdkSerializationRedisSerializer();
			result = connection.expire(serializer.serialize(key), 0);
			System.out.println("remove-Redis-----" + this.id + " key" + key);
		} catch (JedisConnectionException e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				connection.close();
			}
		}
		return result;
	}

	public static void setJedisConnectionFactory(JedisConnectionFactory jedisConnectionFactory) {
		RedisCache.jedisConnectionFactory = jedisConnectionFactory;
	}

}
