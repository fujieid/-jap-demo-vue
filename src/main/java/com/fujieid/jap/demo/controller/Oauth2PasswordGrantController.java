package com.fujieid.jap.demo.controller;

import com.fujieid.jap.core.JapUserService;
import com.fujieid.jap.core.result.JapResponse;
import com.fujieid.jap.demo.config.JapConfigContext;
import com.fujieid.jap.oauth2.OAuthConfig;
import com.fujieid.jap.oauth2.Oauth2EndpointMethodType;
import com.fujieid.jap.oauth2.Oauth2GrantType;
import com.fujieid.jap.oauth2.Oauth2Strategy;
import me.zhyd.oauth.utils.UuidUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 需要依赖 jap-oauth2 模块
 *
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com)
 * @version 1.0.0
 * @date 2021/1/12 14:07
 * @since 1.0.0
 */
@RestController
@RequestMapping("/oauth2")
public class Oauth2PasswordGrantController implements InitializingBean {

    @Resource(name = "oauth2")
    private JapUserService japUserService;
    private Oauth2Strategy oauth2Strategy;


    @RequestMapping("/login/password/jai")
    public JapResponse renderAuth(HttpServletRequest request, HttpServletResponse response) throws IOException {
        JapConfigContext.strategy = "oauth2_password_Grant";
        OAuthConfig config = new OAuthConfig();
        config.setPlatform("jai")
                .setState(UuidUtils.getUUID())
                .setClientId("test")
                .setClientSecret("test")
                .setCallbackUrl("http://sso.jap.com:8080/callback/oauth")
                // 密码模式，不需要授权端链接
//                .setAuthorizationUrl("http://localhost:8081/oauth/authorize")
                .setTokenUrl("http://localhost:8081/oauth/token")
                .setUserinfoUrl("http://localhost:8081/oauth/userinfo")
                .setScopes(new String[]{"read", "write"})
                // GrantType 设为 password
                .setGrantType(Oauth2GrantType.password)
                .setAccessTokenEndpointMethodType(Oauth2EndpointMethodType.GET)
                .setUserInfoEndpointMethodType(Oauth2EndpointMethodType.GET)
                // 指定账号密码
                .setUsername("test")
                .setPassword("123456");
        return oauth2Strategy.authenticate(config, request, response);
    }

    /**
     * 初始化 bean 时对 SimpleStrategy 进行初始化，适用于启用了 SSO 的情况，如果没有启用 SSO，则非强制使用该方式初始化
     *
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        oauth2Strategy = new Oauth2Strategy(japUserService, JapConfigContext.getConfig());
    }
}
