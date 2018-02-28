package com.ssm.lucene;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.*;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.io.StringReader;
import java.nio.file.FileSystems;

/**
 * Created by 蓝鸥科技有限公司  www.lanou3g.com.
 */
public class Search {

    /**
     * 根据关键字进行检索
     * <p>
     * 获得索引目录对象
     * 获得目录对象对应IndexReader流对象
     * 根据IndexReader创建IndexSearcher索引查询对象
     * 创建Query对象
     * 根据searcher搜索对应的索引即TopDocs
     * 得到TopDocs对应的ScopeDocs文档域对象
     * 根据searcher和ScopeDocs查询Document
     * 获得Document中包含的值
     **/
    public void search(String keyword) {

        IndexReader indexReader = null;

        try {
            //1.获得存储索引库的目录对象
            FSDirectory directory = FSDirectory.open(
                    FileSystems.getDefault().getPath(
                            "/Users/lilyxiao/1005/06_code/LuceneDemo/index"));

            //2.创建分词器
            Analyzer analyzer = new StandardAnalyzer();

            //3.获得索引库的reader对象
            indexReader = DirectoryReader.open(
                    directory);

            //4.创建IndexSearcher对象
            IndexSearcher indexSearcher = new IndexSearcher(
                    indexReader);

            //5.创建Query检索对象
            QueryParser queryParser = new QueryParser(
                    "content", analyzer);
            Query query = queryParser.parse(keyword);

            //6.根据索引查找索引对应的索引域 第一次查询
            //第一个参数：查询规则
            //第二个参数：返回的结果集最大条数
            TopDocs topDocs = indexSearcher.search(query, 2);

            //遍历符合条件的文档域
            for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
                //7.获取ScoreDoc的文档对象，即document对象
                //二次查询，在文档域中查找对应的Document对象
                Document doc = indexSearcher.doc(scoreDoc.doc);

                System.out.println("文件名字：" + doc.get("fileName"));
                System.out.println("文件内容：" + doc.get("content"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (indexReader != null) {
                try {
                    indexReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }


    /**
     * 结果插入高亮效果
     **/
    public void hiSearch(String keyword) {

        IndexReader indexReader = null;

        try {
            //1.获得存储索引库的目录对象
            FSDirectory directory = FSDirectory.open(
                    FileSystems.getDefault().getPath(
                            "/Users/lilyxiao/1005/06_code/LuceneDemo/index"));

            //2.创建分词器
            Analyzer analyzer = new StandardAnalyzer();

            //3.获得索引库的reader对象
            indexReader = DirectoryReader.open(
                    directory);

            //4.创建IndexSearcher对象
            IndexSearcher indexSearcher = new IndexSearcher(
                    indexReader);

            //5.创建Query检索对象
            QueryParser queryParser = new QueryParser(
                    "content", analyzer);
            Query query = queryParser.parse(keyword);

            //6.根据索引查找索引对应的索引域 第一次查询
            //第一个参数：查询规则
            //第二个参数：返回的结果集最大条数
            TopDocs topDocs = indexSearcher.search(query, 2);

            //创建高亮信息
            QueryScorer queryScorer = new QueryScorer(
                    query, "content");

            //将查询到的结果拆分
            Fragmenter fragmenter = new
                    SimpleSpanFragmenter(queryScorer, 10000);

            //创建插入的高亮对象
            SimpleHTMLFormatter formatter =
                    new SimpleHTMLFormatter(
                            "<font color='red'>",
                            "</font>");

            //创建高亮对象
            Highlighter highlighter = new Highlighter(
                    formatter, queryScorer);
            highlighter.setTextFragmenter(fragmenter);

            //遍历符合条件的文档域
            for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
                //7.获取ScoreDoc的文档对象，即document对象
                //二次查询，在文档域中查找对应的Document对象
                Document doc = indexSearcher.doc(scoreDoc.doc);
                //得到未高亮的原始内容
                String content = doc.get("content");

                //得到TokenStream流对象
                TokenStream stream = analyzer.tokenStream(
                        "content", new StringReader(content));

                //根据高亮规则往TokenStream中嵌入高亮标签
                String hContent = highlighter.getBestFragment(
                        stream, content);

                System.out.println("插入了高亮标签的内容：" + hContent);

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (indexReader != null) {
                try {
                    indexReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
