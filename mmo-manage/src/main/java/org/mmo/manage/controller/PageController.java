package org.mmo.manage.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 页面
 *
 * @author jzy
 * @mail 359135103@qq.com
 */
@Controller
public class PageController {

    /**
     *
     * http://127.0.0.1:7022/index
     * 参考： https://zhuanlan.zhihu.com/p/103089477
     *
     * @param model
     * @return
     */
    @GetMapping("/index")
    public String index(Model model) {
        model.addAttribute("message", "hello world");
        return "index";
    }
}
