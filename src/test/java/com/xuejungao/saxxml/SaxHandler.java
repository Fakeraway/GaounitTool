package com.xuejungao.saxxml;


import com.xuejungao.entity.Assert;
import com.xuejungao.entity.Call;
import com.xuejungao.entity.Result;
import com.xuejungao.entity.SQL;
import com.xuejungao.entity.TestCase;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SaxHandler extends DefaultHandler {


    // 定义对象
    private TestCase testCase;
    // 声明call对象
    private Call call;
    // 定义vales
    private String value = "";
    // 定义map集合用来接收参数
    private Map<String,String> map = new HashMap<>();
    // 定义map的key变量
    private String mapRequestKey= "";
    // 定义成员对象
    private Assert anAssert;
    // 定义期望返回数据的结果对象
    private Result result;
    // SQL 语句期望值
    private SQL sql;
    // 定义 类型用来在有换行特殊情况进行判断
    private String name;
    private String JsonRsult="";

    // 定义列表
    private List<TestCase> testCaseList = new ArrayList<TestCase>();

    // 定义一个对外提供对象的方法
    public  List<TestCase> getTestCaseList(){

        return testCaseList;
    }


    @Override
    public void startDocument() throws SAXException {
        super.startDocument();
    }


    @Override
    public void endDocument() throws SAXException {
        super.endDocument();
    }


    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);

        name = qName;
        // 进行判断实例化
        if(qName.equals("case")){

            testCase = new TestCase();

            // 获取属性个数
            int number = attributes.getLength();
            // 使用for循环
            for (int i=0;i<number;i++){

                if(attributes.getQName(i).equals("id")){

                    testCase.setId(attributes.getValue(i));
                }

                if(attributes.getQName(i).equals("desc")){

                    testCase.setDesc(attributes.getValue(i));
                }

                if(attributes.getQName(i).equals("tag")){

                    testCase.setTag(attributes.getValue(i));
                }
            }
        }


        // 判断 call
        if(qName.equals("call")){

            // 实例化对象
            call = new Call();
            // 参数全部遍历完成,我们需要将map设置为空,因为下一条测试用例也有参数,不能带有上一条测试用例的参数
            map = new HashMap<>();


            // 获取属性
            // 获取属性个数
            int number = attributes.getLength();
            // 使用for循环
            for (int i=0;i<number;i++){

                if(attributes.getQName(i).equals("service")){

                    call.setService(attributes.getValue(i));
                }

            }

        }

        // 往下继续判断
        if(qName.equals("param")){

            // 获取属性
            int number = attributes.getLength();

            for (int i=0;i<number;i++){

                if(attributes.getQName(i).equals("name")){

                    // 获取值加入到map集合
                    mapRequestKey= attributes.getValue(i);
                }
            }

        }

        // 断言内容的获取
        if(qName.equals("Assert")){

            // 实例化对象
            anAssert = new Assert();
        }

        // 判断返回结果
        if(qName.equals("result")){


            //实例化
            result = new Result();

            // 获取属性
            int number = attributes.getLength();

            // 使用for循环
            for (int i=0;i<number;i++){

                if(attributes.getQName(i).equals("statue")){

                    result.setStatue(attributes.getValue(i));
                }

            }
        }

        // SQL
        if(qName.equals("SQL")){

            // 实例化对象
            sql = new SQL();

            // 获取属性
            int number = attributes.getLength();

            for (int i=0;i<number;i++){

                if(attributes.getQName(i).equals("database")){

                    sql.setDatabase(attributes.getValue(i));
                }
            }

        }


        // 开始每一条测试用例之前将我们以前的期望值设置为""
        if(qName.equals("JsonRsult")){

            JsonRsult = "";
        }

    }



    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);

        // 在遍历标签结束的时候通过key将值加进去
        if(qName.equals("param")){

            String str1 = value.replaceAll(" ", "");
            String str2 = str1.replaceAll("\r\n|\r|\n", "");

            // 加入值
            map.put(mapRequestKey,str2);

        }

        // call
        if(qName.equals("call")){

            // 将map加入到call里面
            call.setMap(map);


            System.out.println("当前map是:"+map);

        }


        // print
        if(qName.equals("print")){

            // 设置为true 表示在控制台输出日志
            testCase.setPrint(true);
        }


        // JsonRsult
        if(qName.equals("JsonRsult")){

            String str1 = JsonRsult.replaceAll(" ", "");
            String str2 = str1.replaceAll("\r\n|\r|\n", "");
            // 设置属性
            result.setJsonRsult(str2);
        }


        // exe_sql
        if(qName.equals("exe_sql")){

            sql.setExe_sql(value);
        }

        // sql_result
        if(qName.equals("sql_result")){

            sql.setSql_result(value);
        }

        // SQL
        if(qName.equals("Assert")){

            anAssert.setSql(sql);
        }

        // result
        if(qName.equals("result")){

            anAssert.setmResult(result);
        }

        // Assert
        if(qName.equals("Assert")){

            testCase.setAnAssert(anAssert);
        }

        // call
        if(qName.equals("call")){

            testCase.setCall(call);
        }
        // case
        if(qName.equals("case")){

            // 加入到列表里面
            testCaseList.add(testCase);
        }


    }


    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length);


        value = new String(ch,start,length);

        // 打印 value
        System.out.print("获取的节点是:"+value+"=="+length);

        // 判断是不是 JsonRsult
        if(name.equals("JsonRsult")){

            JsonRsult += value;
        }


    }
}
