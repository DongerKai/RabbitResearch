package com.hack2win.dynamicrabbits.dynamic.mapper;

import com.hack2win.dynamicrabbits.dynamic.entity.MessageRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MessageRecordMapper {
    /**
     * 查询所有消息记录
     * @return
     */
    List<MessageRecord> selectMessageRecordList();

    /**
     * 查询待重发的消息记录
     * @return
     */
    List<MessageRecord> selectRetryMessageRecordList();

    /**
     * 插入消息记录
     * @return
     */
    void insertMessageRecord(MessageRecord messageRecord);

    /**
     * 批量删除消息记录
     * @param idList
     */
    void deleteMessageRecordByIdList(@Param("idList") List<Long> idList);

    /**
     * 更新消息记录
     * @param messageRecord
     */
    void updateMessageRecord(MessageRecord messageRecord);
}
