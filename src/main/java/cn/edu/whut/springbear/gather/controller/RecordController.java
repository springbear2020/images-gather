package cn.edu.whut.springbear.gather.controller;

import cn.edu.whut.springbear.gather.pojo.People;
import cn.edu.whut.springbear.gather.pojo.Response;
import cn.edu.whut.springbear.gather.pojo.Upload;
import cn.edu.whut.springbear.gather.pojo.User;
import cn.edu.whut.springbear.gather.service.PeopleService;
import cn.edu.whut.springbear.gather.service.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Date;

/**
 * @author Spring-_-Bear
 * @datetime 2022-08-11 15:04 Thursday
 */
@RestController
public class RecordController {
    @Autowired
    private PeopleService peopleService;
    @Autowired
    private RecordService recordService;

    @GetMapping("/record/upload/today.do")
    public Response getUserUploadToday(HttpSession session) {
        User user = (User) session.getAttribute("user");
        // Only the student and monitor have the upload record
        if (user.getUserType() > User.TYPE_MONITOR) {
            return Response.info("当前用户账号不存在上传记录");
        }

        // Keep the user info in the session scope with people information
        People people = peopleService.queryPeople(user.getId());
        user.setPeople(people);
        session.setAttribute("user", user);

        Upload upload = recordService.getStudentUpload(user.getId(), new Date(), Upload.STATUS_COMPLETED);
        if (upload == null) {
            return Response.info("暂无您的今日已完成记录").put("name", people.getName()).put("userType", user.getUserType());
        }
        return Response.success("今日已完成记录查询成功").put("name", people.getName()).put("userType", user.getUserType()).put("upload", upload);
    }
}
