package com.green.movie_demo.feign;

import com.green.movie_demo.session.WXSession;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Component("wxCode2SessionFeignClient")
@FeignClient(url = "${WX-session-url}", name = "WXSession")
public interface WXCode2SessionFeignClient
{
    @GetMapping("${WX-session-url}")
    public WXSession code2Session(@RequestParam Map<String, Object>requestMap);
}
