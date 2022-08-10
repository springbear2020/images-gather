package cn.edu.whut.springbear.gather.controller;

import cn.edu.whut.springbear.gather.pojo.Response;
import cn.edu.whut.springbear.gather.pojo.Student;
import cn.edu.whut.springbear.gather.pojo.User;
import cn.edu.whut.springbear.gather.service.StudentService;
import cn.edu.whut.springbear.gather.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.regex.Pattern;

/**
 * @author Spring-_-Bear
 * @datetime 2022-08-08 21:16 Monday
 */
@RestController
public class StudentController {
    @Autowired
    private StudentService studentService;
    @Autowired
    private UserService userService;

    @PutMapping("/student.do")
    public Response updateStudent(@RequestParam String newSex, @RequestParam String newPhone, @RequestParam String newEmail, HttpSession session) {
        // Verify the data entered by the user
        if (!Pattern.matches("^1[3-9]\\d{9}$", newPhone)) {
            return Response.error("手机号格式不正确，请重新输入");
        }
        if (!Pattern.matches("\\w+@\\w+\\.[a-z]+(\\.[a-z]+)?", newEmail)) {
            return Response.error("邮箱地址格式不正确，请重新输入");
        }

        User user = (User) session.getAttribute("user");

        // Update the student info
        Student student = user.getStudent();
        if (student == null) {
            return Response.error("请登录您的学生账号");
        }
        if (!studentService.updateStudentInfo(newSex, newPhone, newEmail, student.getId())) {
            return Response.error("学生信息更新失败");
        }
        // Update the user info
        if (!userService.updateUserInfo(newPhone, newEmail, user.getId())) {
            return Response.error("用户信息更新失败");
        }

        // Set the latest information of user
        user.setPhone(newPhone);
        user.setEmail(newEmail);
        student.setSex(newSex);
        student.setPhone(newPhone);
        student.setEmail(newEmail);
        user.setStudent(student);
        session.setAttribute("user", user);
        return Response.success("个人信息更新成功");
    }

    @GetMapping("/student.do")
    public Response getStudentWithUser(HttpSession session) {
        User user = (User) session.getAttribute("user");
        Student student = studentService.getStudentByUserId(user.getId());
        user.setStudent(student);
        session.setAttribute("user", user);
        return Response.success("查询学生信息成功").put("item", user);
    }
}
