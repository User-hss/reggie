package com.hss.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hss.reggie.common.R;
import com.hss.reggie.pojo.User;
import com.hss.reggie.service.UserService;
import com.hss.reggie.utils.SMSUtils;
import com.hss.reggie.utils.ValidateCodeUtils;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;
    /**
     * 手机端登录
     */
    @PostMapping("/login")
    public R<User> login(@RequestBody Map map,HttpSession session){
        String phone = (String) map.get("phone");
        String code= (String) map.get("code");
        if (StringUtils.isNotEmpty(phone)) {
            String o = (String) session.getAttribute(phone);
            if (StringUtils.isNotEmpty(o)&&o.equals(code)) {
                //登录成功，检查是否为新用户
                LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(User::getPhone,phone);
                User user = userService.getOne(queryWrapper);
                //新用户自动注册
                if (user == null) {
                    user = new User();//防止空指针异常
                    user.setPhone(phone);
                    userService.save(user);
                }
                session.setAttribute("user",user.getId());//将用户id存入session
                return R.success(user);
            }
        }
        return R.error("登录失败");
    }
    /**
     * 发送验证码
     */
    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session){
        String phone = user.getPhone();
        if(StringUtils.isNotEmpty(phone)){
            //String code = ValidateCodeUtils.generateValidateCode4String(4);//生成随机4位验证码(有英文)
            String code=ValidateCodeUtils.generateValidateCode(4).toString();
            log.info("验证码=>{}",code);
            //发送短信...
            //SMSUtils.sendMessage("阿里云短信测试","SMS_154950909",phone,code);
            session.setAttribute(phone, code);
            return R.success("发送短信成功");
        }
        return R.error("发送短信失败");
    }
    /**
     * 退出登录
     */
    @PostMapping("/logout")
    public R<String> logout(HttpSession session) {
        session.removeAttribute("user");
        return R.success("退出成功");
    }
}
