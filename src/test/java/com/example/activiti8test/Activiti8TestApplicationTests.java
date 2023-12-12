package com.example.activiti8test;


import com.example.activiti8test.utils.SecurityUtil;
import org.activiti.api.process.model.ProcessDefinition;
import org.activiti.api.process.runtime.ProcessRuntime;
import org.activiti.api.runtime.shared.query.Page;
import org.activiti.api.runtime.shared.query.Pageable;
import org.activiti.engine.*;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class Activiti8TestApplicationTests {

    /**
     * 使用配置类的方式创建表
     * 结果是：表可以成功创建，但是运行结果是失败的
     */
    @Test
    public void createByProcessEngineConfiguration() {
        ProcessEngineConfiguration processEngineConfiguration = ProcessEngineConfiguration.createStandaloneProcessEngineConfiguration();
        processEngineConfiguration.setJdbcDriver("com.mysql.cj.jdbc.Driver")
                .setJdbcUrl("jdbc:mysql://localhost:3306/activiti8?nullCatalogMeansCurrent=true")
                .setJdbcUsername("root")
                .setJdbcPassword("shuizhiyve1!");
        ProcessEngine processEngine = processEngineConfiguration.buildProcessEngine();
        System.out.println("processEngine = " + processEngine);
    }

    /**
     * 使用默认方法进行建表
     */
    @Test
    void test() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        System.out.println("processEngine = " + processEngine);
    }

    /**
     * 因为使用了整合框架，所以可以直接<br>
     * 使用注入的方式来使用相关的类
     */

    @Autowired
    private ProcessEngine processEngine;

    @Autowired
    private ProcessRuntime processRuntime;

//    @Autowired
//    private TaskRuntime taskRuntime;

//    @Autowired
//    private RuntimeService runtimeService;

    @Autowired
    private SecurityUtil securityUtil;

    /**
     * 进行流程部署
     */
    @Test
    public void deploy(){
        securityUtil.logInAs("system");
        RepositoryService repositoryService = processEngine.getRepositoryService();
        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource("processes/leave.bpmn20.xml")
                .name("请假申请流程")
                .deploy();
        System.out.println("deployment.getId() = " + deployment.getId());
        System.out.println("deployment.getName() = " + deployment.getName());
    }

    /**
     * 查看流程定义
     */
    @Test
    public void contextLoads() {
        securityUtil.logInAs("system");
        Page<ProcessDefinition> processDefinitionPage =
                processRuntime.processDefinitions(Pageable.of(0, 10));
        System.out.println("可用的流程定义数量：" + processDefinitionPage.getTotalItems());
        for (ProcessDefinition pd : processDefinitionPage.getContent()) {
            System.out.println("流程定义：" + pd);
        }
    }

    /**
     * 启动流程实例
     */
    @Test
    public void startProcessInstance(){
        RuntimeService runtimeService = processEngine.getRuntimeService();
        ProcessInstance processInstance = runtimeService.
                startProcessInstanceByKey("leave", "1001");
        System.out.println("processInstance.getBusinessKey() = " + processInstance.getBusinessKey());
    }

    /**
     * 查询流程实例
     */
    @Test
    public void queryProcessInstance(){
        RuntimeService runtimeService = processEngine.getRuntimeService();
        List<ProcessInstance> leave = runtimeService.createProcessInstanceQuery()
                .processDefinitionKey("leave")
                .list();
        for (ProcessInstance processInstance : leave) {
            System.out.println("processInstance.getBusinessKey() = " + processInstance.getBusinessKey());
            System.out.println("processInstance.getName() = " + processInstance.getName());
            System.out.println("processInstance.getProcessInstanceId() = " + processInstance.getProcessInstanceId());
        }
    }

    /**
     * 查询某个候选人小组拥有流程实例，若是此时用户属于该候选人小组，则拥有处理该任务的权力，可以选择完成任务
     */
    @Test
    public void queryTask(){
        TaskService taskService = processEngine.getTaskService();
        List<Task> list = taskService.createTaskQuery()
                .processDefinitionKey("leave")
                .taskCandidateGroup("deptLeader")
                .list();
        for (Task task : list) {
            System.out.println("task.getProcessInstanceId() = " + task.getProcessInstanceId());
        }
    }

    /**
     * 处理流程任务——即完成流程中间节点<br>
     * 目前处理的是请假流程，需要检查处理人是不是属于对应的
     * 候选人分组
     */
    @Test
    public void completeProcess(){

    }











}
