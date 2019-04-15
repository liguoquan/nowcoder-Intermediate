package com.nowcoder;

import com.nowcoder.model.User;
import com.nowcoder.util.JedisAdapter;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ToutiaoApplication.class)
public class JedisTests {
    @Autowired
    JedisAdapter jedisAdapter;

    @Test
    public void testObject(){
        User user = new User();
        user.setPassword("pwd");
        user.setHeadUrl("http://image.nowcoder.com/head/100t.png");
        user.setSalt("salt");
        user.setName("user1");

        jedisAdapter.setObject("user1",user);
        User u1 = jedisAdapter.getObject("user1", User.class);

        System.out.println(ToStringBuilder.reflectionToString(u1));
    }
}
