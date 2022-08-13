<%--
  Created by IntelliJ IDEA.
  User: Zhaoy
  Date: 2022/3/25
  Time: 22:51
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <base href="http://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/"/>
    <script src="jquery/jquery-3.4.1.js" type="text/javascript"></script>
    <script src="layer/layer.js" type="text/javascript"></script>

    <script type="text/javascript">
        $(function (){
            // btn1
            //此方式可以在浏览器看到发送的请求体是Form Data(表单数据)
            $("#btn1").click(function (){

                $.ajax({
                    url: "test/one.html",//请求目标资源地址
                    type: "post",//请求方式
                    data: "array="+[5,8,12],//发送的请求参数
                    dataType: "text",//表示如何对待服务器返回的数据
                    success:function (response){
                        alert(response);
                    },
                    error:function (response){
                        alert(response);
                    }
                });
            });
            // btn2
            //此方式可以在浏览器看到发送的请求体是Request Payload(请求负载)
            $("#btn2").click(function () {
                // 准备要发送的数据
                var array = [5, 8, 12];
                console.log(array)
                // 必须将目标先转换成JSON字符串
                var arrayStr = JSON.stringify(array);
                $.ajax({
                    url: "test/two.html",
                    data: arrayStr,
                    type: "post",
                    dataType: "json",
                    contentType:"application/json; charset=utf-8",
                    // 告诉服务器当前请求的请求体是JSON格式
                    success: function (response) {
                        alert(response);
                    },
                    error: function (response) {
                        alert(response);
                    }
                });
            });
            // btn3
            // 传输复杂对象
            $("#btn3").click(function () {
                var student = {
                    "name":"Fall",
                    "id":21,
                    "address":{
                        "province":"浙江",
                        "city":"宁波"
                    },
                    "subjects":[
                        {
                            "subjectName":"Java",
                            "score":96
                        },
                        {
                            "subjectName":"Data Struct",
                            "score":93
                        }
                    ],
                    "map":{
                        "key1":"value1",
                        "key2":"value2"
                    }
                };
                //student end
                //将目标转换成json字符串
                var studentStr = JSON.stringify(student);
                $.ajax({
                    url: "send/compose/object.html",         //请求目标资源地址
                    type: "post",                       //请求方式
                    data: studentStr,                     //发送的请求参数
                    dataType: "text",                   //表示如何对待服务器返回的数据
                    contentType: "application/json;charset=UTF-8",  //告诉服务器端当前请求的请求体是JSON格式
                    success: function (response) {
                        alert(response);
                    },
                    error: function (response) {
                        alert(response);
                    }
                });
            });
            //btn4
            //使用ResultEntity，统一返回的格式
            $("#btn4").click(function () {

                var boss = {
                    "username":"xxxx",
                    "hobby":"sleep"
                }

                $.ajax({
                    url: "send/compose/object.json",
                    type: "post",
                    data: JSON.stringify(boss),
                    dataType: "json",
                    contentType: "application/json;charset=UTF-8",
                    success: function (response) {
                        console.log(response);
                    },
                    error: function (response) {
                        console.log(response);
                    }
                });

            });//btn4

            $("#btn5").click(function (){
                layer.msg("测试Layer组件！")
            });
            $("#btn6").click(function (){
                var testData = [1, 2, 3]
                $.ajax({
                    url: "exception_ajax.html",
                    type: "post",
                    data: JSON.stringify(testData),
                    dataType: "json",
                    contentType: "application/json;charset=UTF-8",
                    success: function (response) {
                        console.log(response);
                        alert(response);
                    },
                    error: function (response) {
                        console.log(response);
                        alert(response);
                    }
                });
            });
        });
    </script>

</head>
<body>

<%--${pageContext.request.serverName} <br>--%>
<%--${pageContext.request.serverPort} <br>--%>
<%--${pageContext.request.contextPath} <br>--%>

<%--一、无base标签--%>
<%--<a href="${pageContext.request.contextPath}/ssm.html">测试页面</a>--%>

<%--一、有base标签--%>
<a href="ssm.html">测试页面</a>
<br>
<h1>------------------------------------------------</h1>
<button id="btn1">传送json对象[简单字符串] -- 返回text</button>
<br>
<button id="btn2">传送json字符串[简单字符串] -- 返回json</button>
<br>
<button id="btn3">传送json字符串[复杂对象] -- 返回text</button>
<br>
<button id="btn4">传送json字符串[简单对象] -- 返回json</button>
<br>
<button id="btn5">测试layer组件</button>
<br>
<h1>------------------------------------------------</h1>
<a href="exception_common.html">测试异常跳转页面(普通页面请求)</a><br>
<br>
<button id="btn6">测试异常跳转页面(ajax请求)</button>
<h1>------------------------------------------------</h1>
<br><br>
<a href="admin/to/login/page.html">登录页面</a>

</body>
</html>
