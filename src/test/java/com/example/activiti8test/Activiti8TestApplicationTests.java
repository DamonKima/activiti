package com.example.activiti8test;


import com.example.activiti8test.service.ReportBackEndProcessor;
import com.example.activiti8test.utils.SecurityUtil;
import org.activiti.api.process.model.ProcessDefinition;
import org.activiti.api.process.runtime.ProcessRuntime;
import org.activiti.api.runtime.shared.query.Page;
import org.activiti.api.runtime.shared.query.Pageable;
import org.activiti.api.task.runtime.TaskRuntime;
import org.activiti.engine.*;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

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
     * 因为使用了整合框架，所以可以直接使用注入的方式来使用相关的类
     */

    @Autowired
    private ProcessEngine processEngine;

    @Autowired
    private ProcessRuntime processRuntime;

    @Autowired
    private TaskRuntime taskRuntime;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private SecurityUtil securityUtil;

    /**
     * 进行流程部署
     */
    @Test
    public void deploy() {
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
    public void startProcessInstance() {
        RuntimeService runtimeService = processEngine.getRuntimeService();
        int i = 1000;
        Map<String, Object> claim = new HashMap<>();
        List<String> name = new LinkedList<>();
        name.add("Stefan");
        name.add("Damon");
        name.add("KiMa");
        for (String s : name) {
            claim.put("applyUserId", s);
            runtimeService.startProcessInstanceByKey("leave", Integer.toString(i), claim);
            i++;
        }
    }

    /**
     * 查询流程实例
     */
    @Test
    public void queryProcessInstance() {
        RuntimeService runtimeService = processEngine.getRuntimeService();
        List<ProcessInstance> leave = runtimeService.createProcessInstanceQuery()
                .processDefinitionKey("leave")
                .list();
        for (ProcessInstance processInstance : leave) {
            System.out.println("processInstance.getBusinessKey() = " + processInstance.getBusinessKey());
            System.out.println("processInstance.getName() = " + processInstance.getName());
            System.out.println("processInstance.getProcessInstanceId() = " + processInstance.getProcessInstanceId());
            System.out.println("**************************************");
        }
    }

    /**
     * @param :
     * @return void
     * @author KiMa
     * @description 删除任务实例
     * @date 2023-12-12 22:10
     */
    @Test
    public void deleteProcessInstance() {
        RuntimeService runtimeService = processEngine.getRuntimeService();
        List<ProcessInstance> leave = runtimeService.createProcessInstanceQuery()
                .processDefinitionKey("leave")
                .list();

        for (ProcessInstance processInstance : leave) {
            System.out.println("processInstance.getId() = " + processInstance.getId());
            runtimeService.deleteProcessInstance(processInstance.getProcessInstanceId(), "重置系统");
        }

    }

    /**
     * 查询某个候选人小组拥有流程实例，若是此时用户属于该候选人小组，则拥有处理该任务的权力，可以选择完成任务
     */
    @Test
    public void queryTask() {
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
     * @param : null
     * @return void
     * @author KiMa
     * @description 处理流程任务——即完成流程中间节点。完成部门领导审批任务，并且同意任务目前处理的是请假流程，需要检查处理人是不是属于对应的候选人分组
     * @date 2023-12-13 22:11
     */
    @Test
    public void deptLeaderCompleteTask() {
        // 假设当前是deptLeader组的leader在操作
        String candidateGroup = "deptLeader";
        String userId = "leader";
        // leader的意见是同意，并且放入map中
        Boolean deptLeaderApproved = true;
        HashMap<String, Object> claim = new HashMap<>();
        claim.put("deptLeaderApproved", deptLeaderApproved);
        /*
          完成任务
          0.登录用户
          1. 使用role获取该分组拥有的任务实例，实际场景中使用者会在
          前端指定一个任务实例id传给后端去完成任务的，而后端可以通过security管理
          传给前端任务实例的列表（这是大前提来的）
          2. 使用角色拾取任务
          3. 完成任务，需要及时传递使用到的变量
         */
        // 0.登录用户
        securityUtil.logInAs("leader");
        // 1. 使用role获取该分组拥有的任务实例，实际场景中使用者会在
        //    前端指定一个任务实例id传给后端去完成任务的，而后端可以通过security管理
        //    传给前端任务实例的列表（这是大前提来的）
        TaskService taskService = processEngine.getTaskService();
        List<Task> tasks = taskService.createTaskQuery()
                .taskCandidateGroup(candidateGroup)
                .list();

        // 如果已经使用分组候选人拾取了，那么只可以通过接收者来查询任务
//        Task leader = taskService.createTaskQuery()
//                .taskAssignee("leader")
//                .singleResult();
//        taskService.complete(leader.getId(),claim);

        ArrayList<String> taskIds = new ArrayList<>();

        // 2. 使用角色拾取任务
        // 使用用户id拾取任务
        // 在业务中：该用户是经过判断的，即是属于deptLeader分组中的用户
        for (Task task : tasks) {
            taskIds.add(task.getId());
            taskService.claim(task.getId(), userId);
        }

        // 3. 完成任务，需要及时传递使用到的变量
        // 因为任务已经拾取，所以需要使用候选人来获取tasks，这里就不重复获取，直接将taskid记录下来
        // 使用就好了
        for (String taskId : taskIds) {
            taskService.complete(taskId, claim);
        }
    }

    /**
     * @return void
     * @param: null
     * @author KiMa
     * @description 完成人事审批任务节点
     * @date 2023-12-13 22:08
     */
    @Test
    public void hrCompleteTask() {
        /*
        0.登录用户
        1.指定用户，指定用户组
        2.通过候选人组的方式拾取任务
        3.设置流程变量，设置local变量，将作用域限定在当前任务
        4.使用当前用户完成任务
         */
        // 0.登录用户:hr
        securityUtil.logInAs("hr");
        // 1.指定用户，指定用户组
        String userId = "hr";
        String candidateGroup = "hr";

        // 2.通过候选人组的方式拾取任务
//        TaskService taskService = processEngine.getTaskService();
//        List<Task> tasks = taskService.createTaskQuery()
//                .taskCandidateGroup(candidateGroup)
//                .list();
//        for (Task task : tasks) {
//            taskService.claim(task.getId(),userId);
//            System.out.println(task.getId());
//            System.out.println(task.getAssignee());
//        }

        // 通过assignee的方式获取任务列表，因为在拾取任务之后用候选人组获取的列表就失效了
        TaskService taskService = processEngine.getTaskService();
        List<Task> tasks = taskService.createTaskQuery()
                .taskAssignee("hr")
                .list();

        // 3.设置流程变量，设置local变量，将作用域限定在当前任务
        Boolean hrApproved = true;
        for (Task task : tasks) {
            taskService.setVariableLocal(task.getId(),"hrApproved",hrApproved);
        }

        // 4.完成任务
        for (Task task : tasks) {
            taskService.complete(task.getId());
        }
    }

    /**
     * @param :
     * @return void
     * @author KiMa
     * @description 假期申请人员销假
     * @date 2023-12-14 10:46
     */
    @Test
    public void applyUserReportBack(){
        /*
        1.登录用户Stefan
        2.获取任务列表
        3.在销假任务这个节点设置了一个taskListener，用于监听处理任务时是否设置reportBackEndProcessor
        为同意，或者不同意，因此需要先设置这个变量
            <userTask id="reportBack" name="销假" activiti:assignee="${applyUserId}">
              <extensionElements>
                <activiti:taskListener event="complete" delegateExpression="${reportBackEndProcessor}"/>
              </extensionElements>
            </userTask>
        4.完成销假——即complete任务即可
         */
        String userId = "KiMa";
        // 1.登录用户Stefan
        securityUtil.logInAs(userId);
        // 2.获取任务列表
        TaskService taskService = processEngine.getTaskService();
        Task stefanTask = taskService.createTaskQuery()
                .taskAssignee(userId)
                .singleResult();
        // 3.在销假任务这个节点设置了一个taskListener，用于监听处理任务时是否设置reportBackEndProcessor
        //   为同意，或者不同意，因此需要先设置这个变量
        taskService.setVariable(stefanTask.getId(),"reportBackEndProcessor",new ReportBackEndProcessor());
        // 4.完成销假——即complete任务即可
        taskService.complete(stefanTask.getId());
    }


}
