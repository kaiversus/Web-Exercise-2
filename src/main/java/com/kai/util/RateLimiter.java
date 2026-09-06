package com.kai.util;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RateLimiter {

	private static final Map<String, Deque<Long>> HITS = new ConcurrentHashMap<>();
	private static final int MAX_KEYS = 10000;

	public static synchronized boolean allow(String key, int maxHits, long windowSeconds) {
		long now = System.currentTimeMillis();
		long windowStart = now - windowSeconds * 1000L;

		if (HITS.size() > MAX_KEYS) {
			HITS.entrySet().removeIf(e -> e.getValue().isEmpty()
					|| e.getValue().peekLast() < windowStart);
		}

		Deque<Long> queue = HITS.computeIfAbsent(key, k -> new ArrayDeque<>());
		while (!queue.isEmpty() && queue.peekFirst() < windowStart) {
			queue.pollFirst();
		}
		if (queue.size() >= maxHits) {
			return false;
		}
		queue.addLast(now);
		return true;
	}

	public static synchronized long retryAfterSeconds(String key, long windowSeconds) {
		Deque<Long> queue = HITS.get(key);
		if (queue == null || queue.isEmpty()) {
			return 0;
		}
		long elapsed = (System.currentTimeMillis() - queue.peekFirst()) / 1000L;
		return Math.max(windowSeconds - elapsed, 0);
	}

	public static synchronized void reset(String key) {
		HITS.remove(key);
	}
}
