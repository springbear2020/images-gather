package cn.edu.whut.springbear.gather.controller;

import cn.edu.whut.springbear.gather.pojo.People;
import cn.edu.whut.springbear.gather.pojo.Response;
import cn.edu.whut.springbear.gather.pojo.Upload;
import cn.edu.whut.springbear.gather.pojo.User;
import cn.edu.whut.springbear.gather.service.ClassService;
import cn.edu.whut.springbear.gather.service.RecordService;
import cn.edu.whut.springbear.gather.service.TransferService;
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
    private ClassService classService;

    @PostMapping("/transfer.do")
    public Response upload(@RequestParam("healthImage") MultipartFile healthImage, @RequestParam("scheduleImage") MultipartFile scheduleImage, @RequestParam("closedImage") MultipartFile closedImage,
                           HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user.getUserType() > User.TYPE_MONITOR) {
            return Response.error("当前用户账号禁止上传");
        }
        People people = user.getPeople();

        // Real path of webapp directory
        String realPath = session.getServletContext().getRealPath("/");
        // images-gather/school/grade/class/2022-08-11
        String userTodayDirectoryPath = "images-gather/" + people.getSchool() + "/" + people.getGrade() + "/" + people.getClassName() + "/" + DateUtils.parseDate(new Date()) + "/";
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
        if (!recordService.updateUploadImagesUrl(upload)) {
            return Response.error("更新上传记录失败");
        }

        return Response.success(people.getName() + "，今日【两码一查】已上传");
    }

    @GetMapping("/transfer.do")
    public ResponseEntity<byte[]> classUploadFilesDownload(@RequestParam("date") String dateStr, HttpSession session) {
        User user = (User) session.getAttribute("user");
        // Student don't have the privilege to download the files
        if (user.getUserType() < User.TYPE_MONITOR) {
            return new ResponseEntity<>(null, null, HttpStatus.NON_AUTHORITATIVE_INFORMATION);
        }

        // e.g: E:\images-gather\target\images-gather-1.0-SNAPSHOT\
        String realPath = session.getServletContext().getRealPath("/");
        // Create new file named README.txt contains the student list (unLogin, unUpload, completed)
        if (!transferService.createReadmeFile(realPath, dateStr, user.getPeople())) {
            return new ResponseEntity<>(null, null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        // Compress the file user needed from the specified directory
        String compressFilePath = transferService.compressDirectory(realPath, dateStr, user.getPeople());
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

    @PostMapping("/transfer/class.do")
    public Response classDataBatchImport(@RequestParam("file") MultipartFile file, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user.getUserType() != User.TYPE_ADMIN) {
            return Response.error("权限不足，禁止导入班级");
        }

        // Real path of project webapp directory
        String realPath = session.getServletContext().getRealPath("/WEB-INF/excel/");
        String fileAbsolutePath = transferService.saveExcelFile(realPath, file);
        if (fileAbsolutePath == null) {
            return Response.error("请上传正确格式的 Excel 文件");
        }
        if (fileAbsolutePath.isEmpty()) {
            return Response.error("文件保存本地磁盘失败");
        }

        // Read row data from the excel file and generate bean list
        Converter converter = new SheetBeanConverter(fileAbsolutePath);
        List<People> peopleList = converter.sheetConvertBean(People.class);
        if (peopleList == null || peopleList.size() < 1) {
            return Response.info("文件中不存在有效的班级数据");
        }
        // Class data import
        int affectedRows = classService.classDataInsertBatch(peopleList);
        return Response.success("共 " + peopleList.size() + " 条，成功导入 " + affectedRows + " 条");
    }
}
