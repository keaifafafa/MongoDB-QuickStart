package com.fafa.mongodb;

import com.fafa.mongodb.entity.User;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * mongoTemplate 灵活性可以，但是比较繁琐
 */
@SpringBootTest
class MongodbApplicationTests1 {
    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 添加
     */
    @Test
    void createUser() {
        User user = new User();
        user.setAge(26);
        user.setName("test3");
        user.setEmail("7852534455@qq.com");
        User user1 = mongoTemplate.insert(user);
        System.out.println(user1);
    }

    /**
     * 查询所有数据
     */
    @Test
    void findAll() {
        List<User> all = mongoTemplate.findAll(User.class);
        System.out.println(all);
    }

    /**
     * 根据id查询用户
     */
    @Test
    void getById() {
        User user = mongoTemplate.findById("6203b02535ccb5202c275416", User.class);
        System.out.println(user);
    }

    /**
     * 条件查询
     */
    @Test
    public void findUserList() {
        Query query = new Query(Criteria
                .where("name").is("test")
                .and("age").is(20));
        List<User> userList = mongoTemplate.find(query, User.class);
        System.out.println(userList);
    }

    /**
     * 模糊查询
     */
    @Test
    public void findUsersLikeName() {
        String name = "est";
        String regex = String.format("%s%s%s", "^.*", name, ".*$");
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Query query = new Query(Criteria.where("name").regex(pattern));
        List<User> userList = mongoTemplate.find(query, User.class);
        System.out.println(userList);
    }

    /**
     * 分页查询
     * 使用正则表达式
     */
    @Test
    public void findUsersPage() {
        String name = "est";
        int pageNo = 1;
        int pageSize = 10;

        Query query = new Query();
        String regex = String.format("%s%s%s", "^.*", name, ".*$");
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        query.addCriteria(Criteria.where("name").regex(pattern));
        int totalCount = (int) mongoTemplate.count(query, User.class);

        List<User> userList = mongoTemplate.
                find(query.skip((pageNo - 1) * pageSize)
                        .limit(pageSize), User.class);

        Map<String, Object> pageMap = new HashMap<>();
        pageMap.put("list", userList);
        pageMap.put("totalCount", totalCount);
        System.out.println(pageMap);
    }

    /**
     * 修改
     */
    @Test
    public void updateUser() {
        // 先查询
        User user = mongoTemplate.findById("6203b02535ccb5202c275416", User.class);
        // 设置值
        user.setName("test_1");
        user.setAge(25);
        user.setEmail("493220990@qq.com");
        // 调用方法实现修改
        Query query = new Query(Criteria.where("_id").is(user.getId()));
        Update update = new Update();
        update.set("name", user.getName());
        update.set("age", user.getAge());
        update.set("email", user.getEmail());
        UpdateResult result = mongoTemplate.upsert(query, update, User.class);
        // 影响的行数
        long count = result.getModifiedCount();
        System.out.println(count);
    }

    /**
     * 删除操作
     */
    @Test
    public void delete() {
        Query query =
                new Query(Criteria.where("_id").is("6203b02535ccb5202c275416"));
        DeleteResult result = mongoTemplate.remove(query, User.class);
        // 影响的行数
        long count = result.getDeletedCount();
        System.out.println(count);
    }


}
