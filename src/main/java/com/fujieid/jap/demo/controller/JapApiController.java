package com.fujieid.jap.demo.controller;

import com.fujieid.jap.core.result.JapResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 模拟资源 API 服务
 *
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com)
 * @version 1.0.0
 * @date 2021-02-26 11:14
 * @since 1.0.0
 */
@RestController
@RequestMapping("/api")
public class JapApiController {

    @GetMapping("/user")
    public JapResponse user(HttpServletRequest request, HttpServletResponse response) {
        return JapResponse.success("登录成功，此为 user 接口返回的数据");
    }
}
