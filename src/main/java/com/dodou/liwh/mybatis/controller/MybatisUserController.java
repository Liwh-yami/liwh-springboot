package com.dodou.liwh.mybatis.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dodou.liwh.amqp.boot.hello.HelloSender;
import com.dodou.liwh.mybatis.dao.model.MpUser;
import com.dodou.liwh.mybatis.service.MpUserService;
import com.dodou.liwh.mybatis.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: Liwh
 * @ClassName: UserController
 * @Description:
 * @version: 1.0.0
 * @date: 2018-11-08 10:31 AM
 */
@RestController
public class MybatisUserController {

    @Autowired
    private MpUserService mpUserService;
    @Autowired
    private TestService testService;
    @Autowired
    HelloSender helloSender;

    @RequestMapping(value = "/user/{id}", produces = {"application/json;charset=utf-8"})
    public MpUser queryById(@PathVariable("id") Long id) {
        MpUser mpUser = mpUserService.queryById(id);
        return mpUser;
    }

    @RequestMapping(value = "/addUser", produces = {"application/json;charset=utf-8"})
    public void addUser() {
        mpUserService.insterTransactional();
    }

    @RequestMapping(value = "/page", produces = {"application/json;charset=utf-8"})
    public Page page(@RequestParam(value = "current", required = false, defaultValue = "1") Integer current
            , @RequestParam(value = "size", required = false, defaultValue = "3") Integer size
    ) {
        Page page = mpUserService.queryUserByPage(current, size);
        return page;
    }

    @RequestMapping(value = "/async")
    public String batchAdd() {
        try {
            testService.test();
        } catch (Exception e) {
            System.out.println(e.getMessage()+"--------------------------");
            return "fail:" + e.getMessage();
        }
        return "success";
    }

    @RequestMapping(value = "/mq")
    public void mq() {
       helloSender.send();
    }
}
