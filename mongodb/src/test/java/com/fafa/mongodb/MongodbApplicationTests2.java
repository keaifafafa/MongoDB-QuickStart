package com.fafa.mongodb;

import com.fafa.mongodb.entity.User;
import com.fafa.mongodb.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;

import java.util.List;


/**
 * MongoRepository 更为简洁，灵活性不够
 *
 * @author Sire
 * @version 1.0
 * @date 2022-02-09 21:12
 */
@SpringBootTest
public class MongodbApplicationTests2 {

    @Autowired
    private UserRepository userRepository;

    /**
     * 添加
     */
    @Test
    void createUser() {
        User user = new User();
        user.setAge(20);
        user.setName("张三");
        user.setEmail("3332200@qq.com");
        User user1 = userRepository.save(user);
    }

    /**
     * 查询所有数据
     */
    @Test
    void findAll() {
        List<User> userList = userRepository.findAll();
        System.out.println(userList);
    }

    /**
     * 根据id查询用户
     */
    @Test
    void getById() {
        // 这里需要加 get 才能得到实体对象
        User user = userRepository.findById("6203c27ba6ff53409574774a").get();
        System.out.println(user);
    }

    /**
     * 条件查询
     */
    @Test
    public void findUserList() {
        User user = new User();
        user.setName("张三");
        user.setAge(20);
        // 选择器（例子）
        Example<User> userExample = Example.of(user);
        // 将例子传入
        List<User> users = userRepository.findAll(userExample);
        System.out.println(users);
    }

    /**
     * 模糊查询
     */
    @Test
    public void findUsersLikeName() {
        //创建匹配器，即如何使用查询条件
        ExampleMatcher matcher = ExampleMatcher.matching() //构建对象
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING) //改变默认字符串匹配方式：模糊查询
                .withIgnoreCase(true); //改变默认大小写忽略方式：忽略大小写
        User user = new User();
        user.setName("三");
        Example<User> userExample = Example.of(user, matcher);
        List<User> userList = userRepository.findAll(userExample);
        System.out.println(userList);

    }

    /**
     * 分页查询
     */
    @Test
    public void findUsersPage() {
        Sort sort = Sort.by(Sort.Direction.DESC, "age");
        // 0为第一页
        Pageable pageable = PageRequest.of(0, 10, sort);
        // 创建匹配器，即如何使用查询条件
        ExampleMatcher matcher = ExampleMatcher.matching() //构建对象
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING) //改变默认字符串匹配方式：模糊查询
                .withIgnoreCase(true); //改变默认大小写忽略方式：忽略大小写
        User user = new User();
        user.setName("三");
        //创建实例
        Example<User> example = Example.of(user, matcher);
        Page<User> pages = userRepository.findAll(example, pageable);
        System.out.println(pages);
    }

    /**
     * 修改
     */
    @Test
    public void updateUser() {
        User user = userRepository.findById("6203c27ba6ff53409574774a").get();
        user.setName("张三_1");
        user.setAge(25);
        user.setEmail("883220990@qq.com");
        // 由于对象存在id，所以保存即为更新
        User save = userRepository.save(user);
        System.out.println(save);
    }

    /**
     * 删除操作
     */
    @Test
    public void delete() {
        userRepository.deleteById("6203c27ba6ff53409574774a");
    }
}
