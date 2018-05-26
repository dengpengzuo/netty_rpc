package com.ezweb.demo.simple;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author : zuodp
 * @version : 1.10
 */
public class HelloImpl implements Hello {
	private Logger logger = LoggerFactory.getLogger(HelloImpl.class);

	@Override
	public TimeResult say(String name, long curTime) {
		logger.info(" say ( {}, {} ).", name, curTime);
		TimeResult r = new TimeResult(name, System.currentTimeMillis() - curTime);
		if (r.getTime() % 2 == 0) throw new RuntimeException("打开数据库失败");
		return r;
	}
}
