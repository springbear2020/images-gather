package cn.edu.whut.springbear.gather.service;

import cn.edu.whut.springbear.gather.pojo.Upload;
import cn.edu.whut.springbear.gather.pojo.User;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Spring-_-Bear
 * @datetime 2022-08-08 16:40 Monday
 */
public interface TransferService {
    /**
     * Save the the three image files to the disk,
     * if save all images successfully then return a Upload object for controller updating the access path,
     * or return null
     *
     * @param userWithStudent User info with student
     * @param realPath        The real path of '/' parse by the server(context path)
     * @param healthImage     Health image file
     * @param scheduleImage   Schedule image file
     * @param closedImage     Closed image file
     * @return Upload or null
     */
    Upload saveImageFilesToDisk(User userWithStudent, String realPath, String userTodayPath, MultipartFile healthImage, MultipartFile scheduleImage, MultipartFile closedImage);

    /**
     * Save the three image files to the Qiniu cloud,
     * if all images upload and saved successfully by the Qiniu cloud server then return Upload for controller
     * to update the access url of the images from Qiniu cloud,
     * or return the original Upload from the method params
     *
     * @param upload   Upload created by edu.whut.springbear.gather.service.TransferService#saveImageFilesToDisk(...)
     * @param realPath The real path of '/' parse by the server(context path)
     * @return Upload
     */
    Upload saveImagesToQiniuCloud(Upload upload, String realPath);

    /**
     * Compress the specified directory, if failed return null
     *
     * @param user     User to create and identify directory
     * @param realPath The context real path
     * @param date     Date string
     * @return Null or file path
     */
    String compressDirectory(User user, String realPath, String date);

    /**
     * Create new file named README.txt contains the student list (unLogin, unUpload, completed)
     */
    boolean generateReadmeFile(User user, String realPath, String date);
}
