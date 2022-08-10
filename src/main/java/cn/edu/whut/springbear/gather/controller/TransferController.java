package cn.edu.whut.springbear.gather.controller;

import cn.edu.whut.springbear.gather.pojo.*;
import cn.edu.whut.springbear.gather.service.RecordService;
import cn.edu.whut.springbear.gather.service.TransferService;
import cn.edu.whut.springbear.gather.util.DateUtils;
import cn.edu.whut.springbear.gather.util.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

/**
 * @author Spring-_-Bear
 * @datetime 2022-08-08 16:19 Monday
 */
@Controller
public class TransferController {
    @Autowired
    private TransferService transferService;
    @Autowired
    private RecordService recordService;

    @ResponseBody
    @PostMapping("/transfer.do")
    public Response upload(HttpSession session,
                           @RequestParam("healthImage") MultipartFile healthImage,
                           @RequestParam("scheduleImage") MultipartFile scheduleImage,
                           @RequestParam("closedImage") MultipartFile closedImage) {
        User user = (User) session.getAttribute("user");
        Student student = user.getStudent();
        if (student == null) {
            return Response.error("请登录您的学生账号");
        }

        // Create the user's file save directory of today
        String realPath = session.getServletContext().getRealPath("/");
        String userTodayDirectoryPath = "images-gather/" + student.getSchool()
                + "/" + student.getGrade() + "/" + student.getClassName() + "/" +
                DateUtils.parseDate(new Date()) + "/";
        if (!FileUtils.createDirectory(realPath + userTodayDirectoryPath)) {
            return Response.error("今日图片保存目录创建失败");
        }

        // Save the image files to the physical disk
        user.setStudent(student);
        Upload upload = transferService.saveImageFilesToDisk(user, realPath, userTodayDirectoryPath, healthImage, scheduleImage, closedImage);
        if (upload == null) {
            return Response.error("图片文件保存本地磁盘失败");
        }

        // Upload the image files to the Qiniu cloud
        upload = transferService.saveImagesToQiniuCloud(upload, realPath);

        // Update the upload record of the user
        if (!recordService.updateUpload(upload)) {
            return Response.error("更新上传记录失败");
        }

        return Response.success("今日【两码一查】已完成");
    }

    @GetMapping("/transfer.do")
    public ResponseEntity<byte[]> classUploadFilesDownload(@RequestParam("date") String date, HttpSession session) {
        User user = (User) session.getAttribute("user");
        // Student don't have the privilege to download the files
        if (user.getUserType() < 1) {
            return new ResponseEntity<>(null, null, HttpStatus.NON_AUTHORITATIVE_INFORMATION);
        }

        String realPath = session.getServletContext().getRealPath("/");
        // Create new file named README.txt contains the student upload list (unLogin, unUpload, completed)
        if (!transferService.generateReadmeFile(user, realPath, date)) {
            return new ResponseEntity<>(null, null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        // Compress the file user needed
        String compressFilePath = transferService.compressDirectory(user, realPath, date);
        if (compressFilePath == null) {
            return new ResponseEntity<>(null, null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        byte[] byteData;
        try {
            InputStream inputStream = new FileInputStream(compressFilePath);
            // Write byte data
            byteData = new byte[inputStream.available()];
            inputStream.read(byteData);
            inputStream.close();
        } catch (IOException e) {
            return new ResponseEntity<>(null, null, HttpStatus.NOT_FOUND);
        }

        // Response headers
        MultiValueMap<String, String> headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment;filename=" + date + ".zip");
        return new ResponseEntity<>(byteData, headers, HttpStatus.OK);
    }
}
