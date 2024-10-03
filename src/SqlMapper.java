import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface SqlMapper {
    @Select("${sql}")
    List<Map<String, Object>> executeQuery(@Param("sql") String sql);
}