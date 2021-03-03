package com.fujieid.jap;

import com.baomidou.kisso.security.token.SSOToken;

class JapDemoVueApplicationTests {

    public static void main(String[] args) {
        SSOToken ssoToken = new SSOToken()
                .setId("11111")
                .setIssuer("username")
                .setTime(System.currentTimeMillis());
        for (int i = 0; i < 10; i++) {
            System.out.println(ssoToken.getToken());
            System.out.println(ssoToken.getToken());
            System.out.println(ssoToken.getToken().equals(ssoToken.getToken()));
        }
    }
}
