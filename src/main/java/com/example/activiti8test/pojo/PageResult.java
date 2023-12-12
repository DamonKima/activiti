package com.example.activiti8test.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * @projectName: Activiti8Test
 * @package: com.example.activiti8test.pojo
 * @className: PageResult
 * @author: KiMa
 * @description: 分宜结果处理类
 * @date: 2023-12-12 16:46
 * @version: 1.0
 */
@Data
@AllArgsConstructor
public class PageResult<T> {

    /**
     * 总数据量
     */
    private long totalCount;

    /**
     * 数据列表
     */
    private List<T> dataList;

}
