package edu.whut.bear.gather.dao;

import edu.whut.bear.gather.pojo.Upload;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Spring-_-Bear
 * @datetime 6/6/2022 9:12 PM
 */
@Repository
public interface UploadDao {
    /**
     * 保存文件上传记录（上传到七牛云平台）
     *
     * @param upload 文件上传记录
     * @return 1 - 保存成功
     */
    int saveUpload(Upload upload);

    /**
     * 批量插入文件上传记录并返回自增 ID
     *
     * @param uploadList 上传记录集合
     * @return 受影响的行数
     */
    int saveUploadBatch(List<Upload> uploadList);
}
