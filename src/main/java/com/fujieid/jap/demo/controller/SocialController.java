package com.fujieid.jap.demo.controller;

import com.fujieid.jap.core.JapUserService;
import com.fujieid.jap.core.result.JapResponse;
import com.fujieid.jap.demo.config.JapConfigContext;
import com.fujieid.jap.social.SocialConfig;
import com.fujieid.jap.social.SocialStrategy;
import me.zhyd.oauth.config.AuthConfig;
import me.zhyd.oauth.utils.UuidUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 需要依赖 jap-social 模块
 *
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com)
 * @version 1.0.0
 * @date 2021/1/12 14:07
 * @since 1.0.0
 */
@RestController
@RequestMapping("/social")
public class SocialController implements InitializingBean {

    @Resource(name = "social")
    private JapUserService japUserService;
    private SocialStrategy socialStrategy;

    @RequestMapping("/login/gitee")
    public JapResponse renderAuth(HttpServletRequest request, HttpServletResponse response) {
        JapConfigContext.strategy = "social";
        return socialStrategy.authenticate(new SocialConfig()
                .setPlatform("wechat_open")
                .setState(UuidUtils.getUUID())
                .setJustAuthConfig(AuthConfig.builder()
                        .clientId("39c8b2aba936a1638cb245b899b24ba8f3ad798e6bb4a08ab645d7a1571b41a2")
                        .clientSecret("c621f97d07854826fc48bb6d095951fd73b354b352ea3073a08ad20304a1f3f2")
                        .redirectUri("http://sso.jap.com:8080/callback/social")
                        .build()), request, response);
    }

    /**
     * 初始化 bean 时对 SimpleStrategy 进行初始化，适用于启用了 SSO 的情况，如果没有启用 SSO，则非强制使用该方式初始化
     *
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        socialStrategy = new SocialStrategy(japUserService, JapConfigContext.getConfig());

    }
}
