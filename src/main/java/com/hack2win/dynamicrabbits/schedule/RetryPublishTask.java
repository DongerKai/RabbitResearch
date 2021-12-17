package com.hack2win.dynamicrabbits.schedule;

import com.hack2win.dynamicrabbits.constant.PublicStatusEnum;
import com.hack2win.dynamicrabbits.dynamic.entity.MessageRecord;
import com.hack2win.dynamicrabbits.dynamic.mapper.MessageRecordMapper;
import com.hack2win.dynamicrabbits.mq.service.MessagePublicService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 重发定时任务
 */
@Component
public class RetryPublishTask {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    MessageRecordMapper messageRecordMapper;

    @Autowired
    RetryPublishTask retryPublishTask;

    @Autowired
    MessagePublicService messagePublicService;



    @Scheduled(cron = "0 0 0 * * ? ")
    public void execute() {
        logger.info("start to get lock");
        //添加redis锁
        try {
            if (!stringRedisTemplate.opsForValue().setIfAbsent("retryPublish", Boolean.TRUE.toString())) {
                return;
            }

            stringRedisTemplate.expire("retryPublish", 3600, TimeUnit.SECONDS);
            logger.info("thread id:{},RetryPublishTask execute", Thread.currentThread().getId());
            //获取重发消息队列
            List<MessageRecord> retryMessageList = messageRecordMapper.selectRetryMessageRecordList();
            if (!CollectionUtils.isEmpty(retryMessageList)) {
                //将待重发的消息状态修改为发送中
                retryPublishTask.updateRetryMessageStatus(retryMessageList);
                for (MessageRecord messageRecord : retryMessageList) {
                    messagePublicService.publicMessage(messageRecord, "retry_" + messageRecord.getId());
                }
            }

        } catch (Exception exception) {
            logger.error("RetryPublishTask error:{}", exception);
        } finally {
            //释放redis锁
            stringRedisTemplate.delete("retryPublish");
        }
    }

    /**
     * 更新待重发的消息状态为发送中
     * @param retryMessageList 重发消息队列
     */
    @Transactional
    public void updateRetryMessageStatus(List<MessageRecord> retryMessageList) {
        Date date = new Date();
        for (MessageRecord messageRecord : retryMessageList) {
            messageRecord.setPublishStatus(PublicStatusEnum.TRANSMITTING.getCode());
            messageRecord.setModifyDate(date);
            messageRecordMapper.updateMessageRecord(messageRecord);
        }
    }
}
