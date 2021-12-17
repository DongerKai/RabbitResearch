package com.hack2win.dynamicrabbits.dynamic.controller;

import com.hack2win.dynamicrabbits.dynamic.service.IAccessService;
import com.hack2win.dynamicrabbits.model.SendTopicVo;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


/**
 *  jjie shou
 *
 * @author DongerKai
 * @since 2021/12/11 11:34 ，1.0
 **/
@RequestMapping("/send")
@AllArgsConstructor
@Validated
@RestController
public class AccessController {
    @NonNull private IAccessService accessService;

    @PostMapping("/topic")
    public Object sendTopic(@Validated @RequestBody SendTopicVo sendTopic){
        return accessService.sendTopic(sendTopic);
    }

//    @PostMapping("/patch")
//    public Object sendBatchTopic() {
//
//    }


}
