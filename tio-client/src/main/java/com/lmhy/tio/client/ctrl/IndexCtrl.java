package com.lmhy.tio.client.ctrl;

import com.lmhy.tio.client.TioClientCore;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexCtrl {
    @RequestMapping("")
    public String index(String msg){
        TioClientCore.send(msg);
        return "Hello World";
    }
}
