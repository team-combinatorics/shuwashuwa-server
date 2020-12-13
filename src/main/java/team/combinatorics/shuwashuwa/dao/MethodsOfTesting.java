package team.combinatorics.shuwashuwa.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
public interface MethodsOfTesting {
    /**
     * 清空所有表，自增的id也会归零
     * 超级管理员会添加
     */
    void truncateAllTables();
}
