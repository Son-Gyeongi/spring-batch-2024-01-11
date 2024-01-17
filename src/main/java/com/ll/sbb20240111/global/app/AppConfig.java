package com.ll.sbb20240111.global.app;

import org.springframework.beans.factory.annotation.Value;

public class AppConfig {
    private static String activeProfile;

    // spring.profiles.active 값을 받아와서 activeProfile에 설정하는 메서드
    @Value("${spring.profiles.active}")
    public void setActiveProfile(String value) {
        activeProfile = value;
    }

    // 현재 프로파일이 'prod'가 아닌지 확인하는 메서드
    public static boolean isNotProd() {
        return isProd() == false;
    }

    // 현재 프로파일이 'prod'인지 확인하는 메서드
    public static boolean isProd() {
        return activeProfile.equals("prod");
    }

    // 현재 프로파일이 'dev'가 아닌지 확인하는 메서드
    public static boolean isNotDev() {
        return isDev() == false;
    }

    // 현재 프로파일이 'dev'인지 확인하는 메서드
    public static boolean isDev() {
        return activeProfile.equals("dev");
    }

    // 현재 프로파일이 'dev'가 아닌지 확인하는 메서드
    public static boolean isNotTest() {
        return isDev() == false;
    }

    // 현재 프로파일이 'test'인지 확인하는 메서드
    public static boolean isTest() {
        return activeProfile.equals("test");
    }
}
