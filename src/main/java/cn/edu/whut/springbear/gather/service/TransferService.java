package cn.edu.whut.springbear.gather.service;

import cn.edu.whut.springbear.gather.pojo.People;
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
     * if save all images save successfully then return a Upload object contains the images' access url or return null
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
     * if all images upload successfully to the Qiniu cloud server then return Upload
     * which the cloud images' access url from the Qiniu cloud or return original Upload object
     *
     * @param upload   Upload created by edu.whut.springbear.gather.service.TransferService#saveImageFilesToDisk(...)
     * @param realPath The real path of '/' parse by the server(context path)
     * @return Upload
     */
    Upload pushImagesToQiniu(Upload upload, String realPath);

    /**
     * Create a new file named README.txt contains the student list (unLogin, unUpload, completed),
     */
    boolean createReadmeFile(String realPath, String dateStr, People people);

    /**
     * Compress the specified directory, if process something going wrong then return null
     * or return the absolute path of the compress file
     *
     * @return Null or compress file absolute path
     */
    String compressDirectory(String realPath, String dateStr, People people);
}
