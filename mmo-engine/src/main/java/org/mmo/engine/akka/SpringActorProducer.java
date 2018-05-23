package org.mmo.engine.akka;

import org.springframework.context.ApplicationContext;

import akka.actor.Actor;
import akka.actor.IndirectActorProducer;

/**
 * actor创建器 <br>
 * 通过spring bean 名字创建actor取代反射创建actor
 * 
 * @author JiangZhiYong
 * @mail 359135103@qq.com
 */
public class SpringActorProducer implements IndirectActorProducer {

	private final ApplicationContext applicationContext;
	/** actor bean 名字 */
	private final String actorBeanName;

	public SpringActorProducer(ApplicationContext applicationContext, String actorBeanName) {
		super();
		this.applicationContext = applicationContext;
		this.actorBeanName = actorBeanName;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Class<? extends Actor> actorClass() {
		return (Class<? extends Actor>) applicationContext.getType(actorBeanName);
	}

	@Override
	public Actor produce() {
		return (Actor) applicationContext.getBean(actorBeanName);
	}

}
