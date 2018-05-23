package org.mmo.engine.akka;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import akka.actor.Extension;
import akka.actor.Props;

/**
 * an Akka Extension is used to add additional functionality to the ActorSystem.
 * The SpringExtension uses Akka Props to create actors with the
 * SpringActorProducer
 * {@link http://www.linkedin.com/pulse/spring-boot-akka-part-1-aliaksandr-liakh}
 * {@link https://github.com/aliakh/demo-akka-spring/tree/master/demo-java-akka-spring}
 * 
 * @author JiangZhiYong
 * @mail 359135103@qq.com
 */
@Component
public class SpringExtension implements Extension {
	private ApplicationContext applicationContext;

	public void initialize(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	public Props props(String actorBeanName) {
		return Props.create(SpringActorProducer.class, applicationContext, actorBeanName);
	}
}
