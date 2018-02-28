package com.ssm.lucene;

import org.junit.Test;

/**
 * Created by 蓝鸥科技有限公司  www.lanou3g.com.
 */
public class MainTest {

    /**
     * 创建索引的单元测试
     **/
    @Test
    public void index() {
        Index index = new Index();
        //创建索引
        index.index();
    }

    /**
     * 全文搜索
     **/
    @Test
    public void search() {
        Search search = new Search();
        search.search("毛泽东思想");
    }

    @Test
    public void hiSearch() {
        Search search = new Search();
        search.hiSearch("毛泽东思想");
    }
}
