package com.zhwl.demo.common.interceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * @author yxs
 * @desc 拦截器配置
 * @date 2019/3/21 15:50
 */
@Configuration
public class MyWebMvcConfigurer extends WebMvcConfigurationSupport {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new PermissionInterceptor())
                .excludePathPatterns("/swagger-ui.html","/index.html", "/swagger-resources/**", "/webjars/**", "/v2/**", "/swagger-ui.html/**", "/**/favicon.ico")
                .excludePathPatterns("/email/send","/common/getAppVersion","/common/executeAlgorithm")
                .excludePathPatterns("/user/login", "/user/logincheck", "/user/logout","/user/addsguser","/user/refreshSession","/Virtual/select","/Virtual/random",
                        "/user/selectUser","/card/**","/image/**","/question/**","/user/findPass","/setting/**","/common/getAppVersion","/notice/**",
                        "/good/**","/cookie/**","/user/selectz","/rate/**","/goodOrder/**","/Mercarigood/**","/user/loginPhone",
                        "/MerOrder/**","/user/selectAlluser","/account/**","/contact/**","/yahooOrder/**","/fenlei/**","/phone/**","/img/**")
                .excludePathPatterns("/h5/**","/","/images/**","/js/**","/css/**","/fonts/**","/pay/**","/user/payYjBalance","/user/jdyj","/user/**",
                        "/txRecord/**","/common/download","/upload","/collect/**","/user/updataAdmin","/admin/**","/liulan/**")
                .addPathPatterns("/**","/upload");
        super.addInterceptors(registry);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
        registry.addResourceHandler("/favicon.ico")
                .addResourceLocations("classpath:/favicon.ico");
        registry.addResourceHandler("/upload/**")
                .addResourceLocations("file:.\\file\\upload\\");
        registry.addResourceHandler("/h5/**")
                .addResourceLocations("classpath:/static/h5/");
        registry.addResourceHandler("/images/**")
                .addResourceLocations("classpath:/static/images/");
        registry.addResourceHandler("/img/**")
                .addResourceLocations("classpath:/static/img/");
        registry.addResourceHandler("/js/**")
                .addResourceLocations("classpath:/static/js/");
        registry.addResourceHandler("/css/**")
                .addResourceLocations("classpath:/static/css/");
        registry.addResourceHandler("/fonts/**")
                .addResourceLocations("classpath:/static/fonts/");
    }

}