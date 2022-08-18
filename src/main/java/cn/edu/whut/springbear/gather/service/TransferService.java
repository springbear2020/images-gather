package cn.edu.whut.springbear.gather.service;

import cn.edu.whut.springbear.gather.pojo.Upload;
import cn.edu.whut.springbear.gather.pojo.User;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Spring-_-Bear
 * @datetime 2022-08-11 16:19 Thursday
 */
public interface TransferService {
    /**
     * Save the the three image files to the local physical disk,
     * if save all images save successfully then return a Upload object contains the images' access url
     *
     * @param user          User
     * @param realPath      The real path of '/' parse by the server(webapp)
     * @param healthImage   Health image file
     * @param scheduleImage Schedule image file
     * @param closedImage   Closed image file
     * @return Upload or null
     */
    Upload saveImageFilesToDisk(User user, String realPath, String userTodayPath, MultipartFile healthImage, MultipartFile scheduleImage, MultipartFile closedImage);

    /**
     * Upload the three image files to the Qiniu cloud,
     * if all images upload successfully to the Qiniu cloud server then return Upload
     * which including the cloud images' access url from the Qiniu cloud
     *
     * @param upload   Upload created by edu.whut.springbear.gather.service.TransferService#saveImageFilesToDisk(...)
     * @param realPath The real path of '/' parse by the server(webapp)
     * @return Upload
     */
    Upload pushImagesToQiniu(Upload upload, String realPath);

    /**
     * Create a new file named README.txt contains the user list (unLogin, unUpload, completed)
     */
    boolean createReadmeFile(String realPath, String dateStr, User user);

    /**
     * Compress the specified directory, if process something going wrong then return null
     * or return the absolute path of the compress file
     *
     * @return Null or compress file absolute path
     */
    String compressDirectory(String realPath, String dateStr, User user);

    /**
     * Save the user list excel file uploaded by admin
     */
    String saveUploadExcelFile(String realPath, MultipartFile excelFile);
}
