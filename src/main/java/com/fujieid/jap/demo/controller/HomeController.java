package com.fujieid.jap.demo.controller;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fujieid.jap.core.JapUser;
import com.fujieid.jap.core.context.JapAuthentication;
import com.fujieid.jap.core.result.JapResponse;
import com.fujieid.jap.demo.config.JapConfigContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 首页控制器
 *
 * @author L.cm
 */
@Controller
@ResponseBody
public class HomeController {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @RequestMapping("/getLoginInfo")
    public JapResponse getLoginInfo(HttpServletRequest request, HttpServletResponse response) {
        return toIndex(request, response);
    }

    private JapResponse toIndex(HttpServletRequest request, HttpServletResponse response) {
        JapUser japUser = JapAuthentication.getUser(request, response);
        Map<String, Object> result = new HashMap<>();
        String userJson = null;
        if (null != japUser) {
            userJson = claimsToJson(japUser);
        }
        result.put("strategy", JapConfigContext.strategy);
        result.put("sso", JapConfigContext.sso);
        result.put("userJson", userJson);
        return JapResponse.success(result);
    }

    @RequestMapping("/logout")
    public JapResponse logout(HttpServletRequest request, HttpServletResponse response) {
        JapAuthentication.logout(request, response);
        return JapResponse.success();
    }

    @RequestMapping("/checkToken")
    public JapResponse checkToken(String token) {
        Map<String, Object> result = JapAuthentication.checkToken(token);
        return JapResponse.success(result);
    }

    @RequestMapping("/enableSso")
    public JapResponse enableSso(HttpServletRequest request, HttpServletResponse response) {
        JapConfigContext.sso = !JapConfigContext.sso;
        return toIndex(request, response);
    }

    private String claimsToJson(JapUser japUser) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            // null替换为""
            mapper.getSerializerProvider().setNullValueSerializer(new JsonSerializer<Object>() {
                @Override
                public void serialize(Object arg0, JsonGenerator arg1, SerializerProvider arg2) throws IOException {
                    arg1.writeString("");
                }
            });
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(japUser);
        } catch (JsonProcessingException jpe) {
            log.error("Error parsing claims to JSON", jpe);
        }
        return "Error parsing claims to JSON.";
    }

}
