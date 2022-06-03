package edu.whut.bear.gather.dao;

import edu.whut.bear.gather.pojo.Upload;
import org.springframework.stereotype.Repository;

/**
 * @author Spring-_-Bear
 * @datetime 6/3/2022 12:09 AM
 */
@Repository
public interface UploadDao {
    /**
     * Save the upload record of the user
     *
     * @param upload File upload record
     * @return 1 - Save successfully
     */
    int saveUpload(Upload upload);
}
