package com.fingerchar.dao.ext;

import org.apache.ibatis.annotations.Param;

public interface FcSystemExtMapper {

    Integer update(@Param("key") String key, @Param("value") String value);
}
