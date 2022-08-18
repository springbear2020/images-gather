package cn.edu.whut.springbear.gather.controller;

import cn.edu.whut.springbear.gather.pojo.entity.Response;
import cn.edu.whut.springbear.gather.pojo.Upload;
import cn.edu.whut.springbear.gather.pojo.User;
import cn.edu.whut.springbear.gather.service.RecordService;
import cn.edu.whut.springbear.gather.service.TransferService;
import cn.edu.whut.springbear.gather.service.UserService;
import cn.edu.whut.springbear.gather.util.DateUtils;
import cn.edu.whut.springbear.gather.util.FileUtils;
import cn.edu.whut.springbear.gather.util.poi.Converter;
import cn.edu.whut.springbear.gather.util.poi.SheetBeanConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

/**
 * @author Spring-_-Bear
 * @datetime 2022-08-11 16:12 Thursday
 */
@RestController
public class TransferController {
    @Autowired
    private TransferService transferService;
    @Autowired
    private RecordService recordService;
    @Autowired
    private UserService userService;

    /**
     * Student, monitor
     */
    @PostMapping("/transfer.do")
    public Response uploadThreeImages(@RequestParam("healthImage") MultipartFile healthImage, @RequestParam("scheduleImage") MultipartFile scheduleImage, @RequestParam("closedImage") MultipartFile closedImage, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user.getUserType() > User.TYPE_MONITOR) {
            return Response.info("当前用户账号无需上传两码一查");
        }

        // Real path of webapp directory
        String realPath = session.getServletContext().getRealPath("/");
        // e.g: images-gather/school/grade/class/2022-08-11/
        String userTodayDirectoryPath = "images-gather/" + user.getSchool() + "/" + user.getGrade() + "/" + user.getClassName() + "/" + DateUtils.parseDate(new Date()) + "/";
        if (!FileUtils.createDirectory(realPath + userTodayDirectoryPath)) {
            return Response.error("今日图片保存目录创建失败");
        }
        // Save the image files to the physical disk
        Upload upload = transferService.saveImageFilesToDisk(user, realPath, userTodayDirectoryPath, healthImage, scheduleImage, closedImage);
        if (upload == null) {
            return Response.error("图片文件保存本地磁盘失败");
        }
        // Upload the image files to the Qiniu cloud
        upload = transferService.pushImagesToQiniu(upload, realPath);

        // Update the upload record of the user
        if (!recordService.updateUpload(upload)) {
            return Response.error("更新上传记录失败");
        }

        // TODO Python images identify
        return Response.success("今日【两码一查】已上传");
    }

    /**
     * >= Monitor
     */
    @GetMapping("/transfer.do")
    @SuppressWarnings("all")
    public ResponseEntity<byte[]> classUploadZipFileDownload(@RequestParam("date") String dateStr, HttpSession session) {
        User user = (User) session.getAttribute("user");
        // Student don't have the privilege to download the files
        if (user.getUserType() < User.TYPE_MONITOR) {
            return new ResponseEntity<>(null, null, HttpStatus.NON_AUTHORITATIVE_INFORMATION);
        }

        // e.g: E:\images-gather\target\images-gather-1.0-SNAPSHOT\
        String realPath = session.getServletContext().getRealPath("/");
        // Create new file named README.txt contains the student list (unLogin, unUpload, completed)
        if (!transferService.createReadmeFile(realPath, dateStr, user)) {
            return new ResponseEntity<>(null, null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        // Compress the file user needed from the specified directory
        String compressFilePath = transferService.compressDirectory(realPath, dateStr, user);
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
            return new ResponseEntity<>(null, null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // Response headers
        MultiValueMap<String, String> headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment;filename=" + dateStr + ".zip");
        return new ResponseEntity<>(byteData, headers, HttpStatus.OK);
    }

    /**
     * Admin
     */
    @PostMapping("/transfer/class.do")
    public Response classUsersBatchInsert(@RequestParam("file") MultipartFile file, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user.getUserType() != User.TYPE_ADMIN) {
            return Response.error("权限不足，禁止批量导入用户");
        }

        // e.g: E:\images-gather\target\images-gather-5.0\WEB-INF\excel-class-upload\
        String realPath = session.getServletContext().getRealPath("/WEB-INF/excel-class-upload/");
        String fileAbsolutePath = transferService.saveUploadExcelFile(realPath, file);
        if (fileAbsolutePath == null) {
            return Response.error("请上传正确格式的 Excel 文件");
        }
        if (fileAbsolutePath.isEmpty()) {
            return Response.error("Excel 文件保存本地磁盘失败");
        }

        // Read row data from the excel file and generate bean list
        Converter converter = new SheetBeanConverter(fileAbsolutePath);
        List<User> userList = converter.sheetConvertBean(User.class);
        if (userList == null || userList.size() < 1) {
            return Response.info("文件中不存在有效的用户数据");
        }
        // Class data import
        int affectedRows = userService.saveUsersBatch(userList);
        return Response.success("共 " + userList.size() + " 条用户数据，成功导入 " + affectedRows + " 条");
    }
}
