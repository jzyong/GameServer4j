package org.jzy.game.manage.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

	/**
	 * http://localhost:7061/
	 * @return
	 */
	@RequestMapping("/")
	public String index() {
		return "hello !";
	}
}
