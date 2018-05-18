package org.mmo.db.test;

import org.junit.Test;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import scala.concurrent.Future;
import static org.junit.Assert.assertEquals;
import static scala.compat.java8.FutureConverters.*;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ExecutionException;

import static akka.pattern.Patterns.ask;

/**
 * 请求返回测试
 * 
 * @author JiangZhiYong
 * @mail 359135103@qq.com
 */
public class PongActorTest {
	ActorSystem system = ActorSystem.create();
	ActorRef actorRef = system.actorOf(Props.create(PongActor.class), "BruceWillis");

	@Test
	public void shouldReplyToPingWithPong() throws Exception {
		Future sFuture = ask(actorRef, "Ping", 1000);
		final CompletionStage<String> cs = toJava(sFuture);
		final CompletableFuture<String> jFuture = (CompletableFuture<String>) cs;
		assertEquals("Pong", jFuture.get(1000, TimeUnit.MILLISECONDS));
	}

	@Test(expected = ExecutionException.class)
	public void shouldReplyToUnknownMessageWithFailure() throws Exception {
		Future sFuture = ask(actorRef, "unknown", 1000);
		final CompletionStage<String> cs = toJava(sFuture);
		final CompletableFuture<String> jFuture = (CompletableFuture<String>) cs;
		jFuture.get(1000, TimeUnit.MILLISECONDS);
	}

	// Future Examples
	@Test
	public void shouldPrintToConsole() throws Exception {
		askPong("Ping").thenAccept(x -> System.out.println("replied with: " + x));
		Thread.sleep(100);
		// no assertion - just prints to console. Try to complete a CompletableFuture
		// instead.
	}

	@Test
	public void shouldTransform() throws Exception {
		char result = (char) get(askPong("Ping").thenApply(x -> x.charAt(0)));
		assertEquals('P', result);
	}

	/**
	 * There is was a bug with the scala-java8-compat library 0.3.0 - thenCompose
	 * throws exception https://github.com/scala/scala-java8-compat/issues/26
	 *
	 * I confirmed fixed in 0.6.0-SNAPSHOT (10 months later). Just in time for
	 * publishing!
	 */
	@Test
	public void shouldTransformAsync() throws Exception {
		CompletionStage cs = askPong("Ping").thenCompose(x -> askPong("Ping"));
		assertEquals(get(cs), "Pong");
	}

	@Test
	public void shouldEffectOnError() throws Exception {
		askPong("cause error").handle((x, t) -> {
			if (t != null) {
				System.out.println("Error: " + t);
			}
			return null;
		});
	}

	@Test
	public void shouldRecoverOnError() throws Exception {
		CompletionStage<String> cs = askPong("cause error").exceptionally(t -> {
			return "default";
		});

		String result = (String) get(cs);
	}

	@Test
	public void shouldRecoverOnErrorAsync() throws Exception {
		CompletionStage<String> cf = askPong("cause error")
				.handle((pong, ex) -> ex == null ? CompletableFuture.completedFuture(pong) : askPong("Ping"))
				.thenCompose(x -> x);
		assertEquals("Pong", get(cf));
	}

	@Test
	public void shouldChainTogetherMultipleOperations() {
		askPong("Ping").thenCompose(x -> askPong("Ping" + x)).handle((x, t) -> t != null ? "default" : x);
	}

	@Test
	public void shouldPrintErrorToConsole() throws Exception {
		askPong("cause error").handle((x, t) -> {
			if (t != null) {
				System.out.println("Error: " + t);
			}
			return null;
		});
		Thread.sleep(100);
	}

	// Helpers
	public Object get(CompletionStage cs) throws Exception {
		return ((CompletableFuture<String>) cs).get(1000, TimeUnit.MILLISECONDS);
	}

	public CompletionStage<String> askPong(String message) {
		Future sFuture = ask(actorRef, message, 1000);
		final CompletionStage<String> cs = toJava(sFuture);
		return cs;
	}
}
