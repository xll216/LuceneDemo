package com.ssm.lucene;

import org.apache.commons.io.FileUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.FSDirectory;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;

/**
 * Created by 蓝鸥科技有限公司  www.lanou3g.com.
 */
public class Index {

    /**
     * 创建索引
     * 构建存放索引库的目录对象，即FSDirectory
     * 创建目标文档的分词器对象
     * 创建IndexWriter写入流对象
     * 创建Document文档对象
     * 将Docment文档对象写入对应的磁盘
     * 关闭IndexWriter写入流对象
     **/
    public void index() {
        IndexWriter indexWriter = null;

        try {

            //1.构建索引库目录
            FSDirectory directory = FSDirectory.open(
                    FileSystems.getDefault().getPath(
                            "/Users/lilyxiao/1005/06_code/LuceneDemo/index"));

            //2.创建分词器
            Analyzer analyzer = new StandardAnalyzer();

            //创建IndexWriterConfig配置对象
            IndexWriterConfig indexWriterConfig = new
                    IndexWriterConfig(analyzer);

            //3.构建IndexWriter对象
            indexWriter = new IndexWriter(
                    directory, indexWriterConfig);

            //在写入索引前先删除所有的索引
            indexWriter.deleteAll();


            //4.构建目标文件
            File destFile = new File(
                    "/Users/lilyxiao/1005/06_code/LuceneDemo/data");

            //遍历目录路径下的所有文件
            for (File file : destFile.listFiles()) {
                //5.构建Document对象
                Document doc = new Document();

                //文件名称
                String fileName = file.getName();

                //文件的内容
                String content = FileUtils.readFileToString(
                        file, "utf-8");

                //创建文档域
                TextField fileNameField = new TextField(
                        "fileName",//文档域的自定义名称
                        fileName, //文档域对应的内容
                        TextField.Store.YES);//文档域的内容是否要持久化存储

                TextField contentField = new TextField(
                        "content",
                        content,
                        TextField.Store.YES);

                //将要存储的内容加入到文档对象中
                doc.add(fileNameField);
                doc.add(contentField);

                //6.写入document对象
                indexWriter.addDocument(doc);
            }


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (indexWriter != null) {
                try {
                    indexWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

}
