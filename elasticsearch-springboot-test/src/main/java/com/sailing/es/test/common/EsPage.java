package com.sailing.es.test.common;

import lombok.Data;
import lombok.ToString;

import java.util.List;
import java.util.Map;

/**
 * @Auther: Administrator
 * @Date: 2019/7/2 23:17
 * @Description:
 */
@Data
@ToString
public class EsPage {

    //当前页
    private int currentPage;

    //每页显示多少条
    private int pageSize;

    //总页数
    private int pageCount;

    //总记录数
    private int recordCount;

    //本页的数据列表
    private List<Map<String,Object>> recordList;

    // 页码列表的开始索引（包含）
    private int beginPageIndex;

    //页码列表的结束索引（包含）
    private int endPageIndex;

    /**
     * 只接受4个必要的属性，会自动计算出其他3个属性
     * @param currentPage
     * @param pageSize
     * @param recordCount
     * @param recordList
     */
    public EsPage(int currentPage,int pageSize,int recordCount,List<Map<String,Object>> recordList){
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.recordCount = recordCount;
        this.recordList = recordList;

        //计算总页码
        pageCount = (recordCount+pageSize-1)/pageSize;

        //计算beginPageIndex和endPageIndex
        //>>总页数不大于10页，则全部显示
        if(pageCount<=10){
            beginPageIndex = 1;
            endPageIndex = pageCount;
        }else{// 总页数多于10页，则显示当前页附近的共10个页码
            //当前面的页码不足4个时，则显示前10个页码
            if(beginPageIndex<1){
                beginPageIndex = 1;
                endPageIndex = 10;
            }
            //当后面的页码不足5个时，则显示后10个页码
            if(endPageIndex>pageCount){
                endPageIndex = pageCount;
                beginPageIndex = pageCount-10+1;
            }
        }
    }


}

