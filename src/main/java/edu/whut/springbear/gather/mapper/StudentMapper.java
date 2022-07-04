package edu.whut.springbear.gather.mapper;

import edu.whut.springbear.gather.pojo.Student;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * @author Spring-_-Bear
 * @datetime 2022-06-30 15:52 Thursday
 */
@Repository
public interface StudentMapper {
    @Select("select * from t_student where id = #{studentId}")
    Student getStudentById(@Param("studentId") Integer studentId);
}
