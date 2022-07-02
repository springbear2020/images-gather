package edu.whut.springbear.gather.service;

import edu.whut.springbear.gather.pojo.Response;
import edu.whut.springbear.gather.pojo.Upload;
import edu.whut.springbear.gather.pojo.User;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Spring-_-Bear
 * @datetime 2022-07-01 11:08 Friday
 */
@Service
public interface TransferService {
    /**
     * Judge the three image files format, whether is it null or not image format
     *
     * @param healthImage   Health image
     * @param scheduleImage Schedule image
     * @param closedImage   Closed image
     * @return null - All images format are correct
     */
    Response judgeThreeImagesFormat(MultipartFile healthImage, MultipartFile scheduleImage, MultipartFile closedImage);

    /**
     * Create the user's today file save directory,
     * if the directory exists or create it successfully then return the directory's relative path,
     * or return null
     *
     * @param userWithStudent User info with student
     * @param realPath        The real path of '/' parse by the server(context path)
     * @return The user today path or null
     */
    String createUserTodayDirectory(User userWithStudent, String realPath);

    /**
     * Save the the three image files to the disk,
     * if save all images successfully then return a Upload object for controller updating the access path,
     * or return null to the controller
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
    Upload saveImagesToCloud(Upload upload, String realPath);
}
