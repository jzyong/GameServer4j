package org.mmo.manage.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

	/**
	 * http://localhost:8014/
	 * @return
	 */
	@RequestMapping("/")
	public String index() {
		return "hello !";
	}
}
