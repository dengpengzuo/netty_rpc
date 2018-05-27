package com.ezweb.demo;

import com.ezweb.demo.simple.Hello;
import com.ezweb.demo.simple.HelloAsync;
import com.ezweb.demo.simple.TimeResult;
import com.ezweb.engine.client.NettyClient;
import com.ezweb.engine.log.Log4j2System;
import com.ezweb.engine.rpc.RpcProtocolCode;
import com.ezweb.engine.rpc.client.AsyncRpcClient;
import com.ezweb.engine.rpc.client.RpcClient;
import com.ezweb.engine.rpc.serialize.kryo.KryoSerializationImpl;
import com.ezweb.engine.rpc.server.RpcProtocolCodeImpl;
import com.google.common.collect.Lists;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;

/**
 * @author : zuodp
 * @version : 1.10
 */
public class ClientBootstrap {

	private static final Logger logger = LoggerFactory.getLogger(ClientBootstrap.class);

	public static void main(String[] args) throws Exception {
		new Log4j2System("client").init(null);

		ExecutorService async_pool = Executors.newFixedThreadPool(10, new DefaultThreadFactory("biz_async", true));
		// 同步写入并取回这个条结果.

		NettyClient socket_client = new NettyClient();
		try {
			socket_client.connect("localhost", 9000);
			RpcProtocolCode protocol = new RpcProtocolCodeImpl(new KryoSerializationImpl());

			{
				RpcClient rpcClient = new RpcClient();

				rpcClient.setProtocol(protocol);
				rpcClient.setNettyClient(socket_client);

				Hello helloProxy = rpcClient.createRef("/v2", Hello.class);

				for (int i = 0; i < 10; ++i) {
					try {
						TimeResult timeResult = helloProxy.say(Lists.newArrayList(
								"interface say",
								"interface say",
								"interface say",
								"interface say",
								"interface say",
								"interface say",
								"interface say",
								"interface say",
								"interface say",
								"interface say",
								"interface say",
								"interface say",
								"interface say",
								"interface say",
								"interface say",
								"interface say",
								"interface say",
								"interface say",
								"interface say",
								"interface say",
								"interface say",
								"sdfjaklsfasdfasdsf"
						), System.currentTimeMillis());
						logger.info("timeResult.num = {}, {}", i, timeResult.getTime());
						TimeUnit.SECONDS.sleep(31L);
					} catch (Exception e) {
						logger.info("同步调用，返回异常:", e);
					}
				}
			}
			/*
			{
				AsyncRpcClient rpcClient = new AsyncRpcClient(8);

				rpcClient.setProtocol(protocol);
				rpcClient.setNettyClient(socket_client);

				HelloAsync helloProxy = rpcClient.createRef("/v2", HelloAsync.class);

				int j = 0;
				for (int i = 0; i < 128; ++i) {

					CompletableFuture<TimeResult> timeResultFuture = helloProxy.say("interface say", System.currentTimeMillis());
					++j;
					class ConsumerImpl implements BiConsumer<TimeResult, Throwable> {
						private final int _num;

						public ConsumerImpl(int _num) {
							this._num = _num;
						}

						@Override
						public void accept(TimeResult timeResult, Throwable throwable) {
							if (throwable != null) {
								// 这儿的throwable是CompletableException.
								logger.error("timeResultFuture.num = {}:{}", _num, throwable.getMessage());
							} else {
								// System.out.println(Thread.currentThread().getName());
								logger.info("timeResultFuture.num = {}, {}", _num, timeResult.getTime());
							}
						}
					}
					timeResultFuture.whenCompleteAsync(new ConsumerImpl(i), async_pool);
					if (j % 32 == 0) TimeUnit.SECONDS.sleep(1L); // 并发32个.
				}
			}
			TimeUnit.SECONDS.sleep(2L);
			*/
		} finally {
			socket_client.close();
		}
	}
}
