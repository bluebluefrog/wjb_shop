<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>OA系统</title>
    <link rel="stylesheet" href="/resources/layui/css/layui.css">
    <style>
        body{
            background-color: #f2f2f2;
        }
        .oa-container {
            /*background-color: white;*/
            position: absolute;
            width: 400px;
            height: 350px;
            top: 50%;
            left: 50%;
            padding: 20px;
            margin-left: -200px;
            margin-top: -175px;
        }
        #username,#password{
            text-align: center;
            font-size: 24px;
        }
    </style>
</head>
<body>
<div class="oa-container">
    <h1 style="text-align: center;margin-bottom: 20px">OA System</h1>
    <form class="layui-form">
        <div class="layui-form-item">
            <input type="text" lay-verify="required" id="username" name="username" placeholder="请输入用户名" autocomplete="off" class="layui-input">
        </div>

        <div class="layui-form-item">
        <input type="password" lay-verify="required" id="password" name="password" placeholder="请输入密码" autocomplete="off" class="layui-input">
        </div>

        <div class="layui-form-item">
            <button class="layui-btn layui-btn-fluid" lay-submit lay-filter="login">登陆</button>
        </div>
    </form>
</div>
<script src="/resources/layui/layui.all.js"></script>
<script>
    //submit(login)submit中数值代表是lay-filter=""所谓的id，点击哪个id后触发
    layui.form.on("submit(login)",function (formdata) {//formdata参数包含了当前要提交的表单数据
        console.log(formdata);
        //layui对jquery进行了内置包含ajax
        layui.$.ajax({
            url : "/management/user/check_login",
            data : formdata.field, //提交表单数据
            type : "post",
            dataType : "json" ,
            success : function(json){
                console.log(json);
                if(json.code == "0"){ //登录校验成功
                    // layui.layer.msg("登录成功");
                    //跳转url
                    window.location.href=json.redirect_url;
                }else{
                    layui.layer.msg(json.msg);
                }
            }
        })
        return false;//submit提交时间返回true则表单提交，false阻止表单提交
    })
</script>
</body>
</html>