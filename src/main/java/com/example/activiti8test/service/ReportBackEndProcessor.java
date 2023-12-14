package com.example.activiti8test.service;

import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.springframework.stereotype.Service;

import java.io.Serial;

/**
 * @projectName: Activiti8Test
 * @package: com.example.activiti8test.service
 * @className: ReportBackEndProcessor
 * @author: KiMa
 * @description:
 * @date: 2023-12-14 11:09
 * @version: 1.0
 */
@Slf4j
@Service(value = "reportBackEndProcessor")
public class ReportBackEndProcessor implements TaskListener {

    @Serial
    private static final long serialVersionUID = 9184463817487884824L;

    @Override
    public void notify(DelegateTask delegateTask) {
        log.info("完成");
    }
}

