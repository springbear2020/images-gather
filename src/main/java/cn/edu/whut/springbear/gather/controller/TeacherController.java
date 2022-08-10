package cn.edu.whut.springbear.gather.controller;

import cn.edu.whut.springbear.gather.pojo.LoginLog;
import cn.edu.whut.springbear.gather.pojo.Response;
import cn.edu.whut.springbear.gather.pojo.Teacher;
import cn.edu.whut.springbear.gather.pojo.User;
import cn.edu.whut.springbear.gather.service.RecordService;
import cn.edu.whut.springbear.gather.service.TeacherService;
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
 * @datetime 2022-08-10 09:17 Wednesday
 */
@RestController
public class TeacherController {
    @Autowired
    private TeacherService teacherService;
    @Autowired
    private UserService userService;
    @Autowired
    private RecordService recordService;

    @GetMapping("/teacher.do")
    public Response getTeacherWithUser(HttpSession session) {
        User user = (User) session.getAttribute("user");
        // Teacher's info
        Teacher teacher = teacherService.getTeacherByUserId(user.getId());
        user.setTeacher(teacher);
        session.setAttribute("user", user);
        // Get the user's latest login log
        LoginLog loginLog = recordService.getLatestLoginLog(user.getId());
        return Response.success("查询教师信息成功").put("user", user).put("loginLog", loginLog);
    }

    @PutMapping("/teacher.do")
    public Response updateTeacherInfo(@RequestParam String newSex, @RequestParam String newPhone, @RequestParam String newEmail, HttpSession session) {
        // Verify the data entered by the user
        if (!Pattern.matches("^1[3-9]\\d{9}$", newPhone)) {
            return Response.error("手机号格式不正确，请重新输入");
        }
        if (!Pattern.matches("\\w+@\\w+\\.[a-z]+(\\.[a-z]+)?", newEmail)) {
            return Response.error("邮箱地址格式不正确，请重新输入");
        }

        User user = (User) session.getAttribute("user");

        // Update the teacher info
        Teacher teacher = user.getTeacher();
        if (teacher == null) {
            return Response.error("请登录您的教师账号");
        }
        if (!teacherService.updateTeacherInfo(newSex, newPhone, newEmail, teacher.getId())) {
            return Response.error("教师信息更新失败");
        }

        // Update the user info
        if (!userService.updateUserInfo(newPhone, newEmail, user.getId())) {
            return Response.error("用户信息更新失败");
        }

        // Set the latest information of user
        user.setPhone(newPhone);
        user.setEmail(newEmail);
        teacher.setSex(newSex);
        teacher.setPhone(newPhone);
        teacher.setEmail(newEmail);
        user.setTeacher(teacher);
        session.setAttribute("user", user);
        return Response.success("个人信息更新成功");
    }
}
