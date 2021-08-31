<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
    <style>
        #dlgShop{
            padding: 10px
        }
    </style>
    <link rel="stylesheet" href="/resources/layui/css/layui.css">

    <script src="/resources/wangEditor.min.js"></script>


    <script type="text/html" id="toolbar">
        <div class="layui-btn-container">
            <button class="layui-btn layui-btn-sm" id="btnAdd" onclick="showCreate()">添加</button>
        </div>
    </script>

</head>
<body>


<div class="layui-container">
    <blockquote class="layui-elem-quote">商铺列表</blockquote>
	<!-- 数据表格 -->
    <table id="grdShop" lay-filter="grdShop"></table>
</div>
<!--表单内容-->
<div id="dialog" style="padding: 10px;display: none">
    <form class="layui-form" >
        <div class="layui-form-item">
			<!-- 图书类别 -->
            <select id="categoryId" name="categoryId" lay-verify="required" lay-filter=
			"categoryId">
                <option value=""></option>
                <option value="1">餐饮</option>
				<option value="2">娱乐</option>
				<option value="3">交友</option>
				<option value="4">其他</option>
            </select>

        </div>
        <div class="layui-form-item">
			<!-- 书名 -->
            <input type="text" id="shopName" name="shopName" required lay-verify="required" placeholder="请输入商铺"
                   autocomplete="off" class="layui-input">
        </div>


        <div class="layui-form-item">
			<!-- 子标题 -->
            <input type="text" id="subTitle" name="subTitle" required lay-verify="required" placeholder="请输入子标题"
                   autocomplete="off" class="layui-input">
        </div>

        <div class="layui-form-item">
			<!-- 作者 -->
            <input type="text" id="owner" name="owner" required lay-verify="required" placeholder="请输入拥有者信息"
                   autocomplete="off" class="layui-input">
        </div>

        <div style="margin-top: 30px;font-size: 130%">商铺介绍(默认第一图将作为商铺封面)</div>
        <div class="layui-form-item" >
			<!-- wangEditor编辑器 -->
            <div id="editor" style="width: 100%">

            </div>
        </div>
		<!-- 图书编号 -->
        <input id="shopId" type="hidden">
		<!-- 当前表单操作类型,create代表新增 update代表修改 -->
        <input id="optype"  type="hidden">
        <div class="layui-form-item" style="text-align: center">
			<!-- 提交按钮 -->
            <button class="layui-btn" lay-submit="" lay-filter="btnSubmit">立即提交</button>
        </div>
    </form>
</div>
<script src="/resources/layui/layui.all.js"></script>
<script>

    var table = layui.table; //table数据表格对象
    var $ = layui.$; //jQuery
    var editor = null; //wangEditor富文本编辑器对象
    //初始化图书列表
    table.render({
        elem: '#grdShop'  //指定div
        , id : "shopList" //数据表格id
        , toolbar: "#toolbar" //指定工具栏,包含新增添加
        , url: "/management/shop/list" //数据接口
        , page: true //开启分页
        , cols: [[ //表头
            {field: 'shopName', title: '商铺名', width: '300'}
            , {field: 'subTitle', title: '子标题', width: '200'}
            , {field: 'owner', title: '拥有者', width: '200'}
            , {type: 'space', title: '操作', width: '200' , templet : function(d){
					//为每一行表格数据生成"修改"与"删除"按钮,并附加data-id属性代表图书编号
                    return "<button class='layui-btn layui-btn-sm btn-update'  data-id='" + d.shopId + "' data-type='update' onclick='showUpdate(this)'>修改</button>" +
                        "<button class='layui-btn layui-btn-sm btn-delete'  data-id='" + d.shopId + "'   onclick='showDelete(this)'>删除</button>";
                }
            }
        ]]
    });
	//显示更新图书对话框
	//obj对应点击的"修改"按钮对象
    function showUpdate(obj){
		//弹出"编辑图书"对话框
        layui.layer.open({
            id: "dlgShop", //指定div
            title: "编辑图书", //标题
            type: 1, 
            content: $('#dialog').html(), //设置对话框内容,复制自dialog DIV
            area: ['820px', '730px'], //设置对话框宽度高度
            resize: false //是否允许调整尺寸
        })

        var shopId = $(obj).data("id"); //获取"修改"按钮附带的图书编号
        $("#dlgShop #shopId").val(shopId); //为表单隐藏域赋值,提交表单时用到

        editor = new wangEditor('#dlgShop #editor'); //初始化富文本编辑器
        editor.customConfig.uploadImgServer = '/management/shop/upload' //设置图片上传路径
        editor.customConfig.uploadFileName = 'img'; //图片上传时的参数名
        editor.create(); //创建wangEditor
        $("#dlgShop #optype").val("update"); //设置当前表单提交时提交至"update"更新地址

		//发送ajax请求,获取对应图书信息
        $.get("/management/shop/id/" + shopId , {} , function(json){
			//文本框回填已有数据
            $("#dlgShop #shopName").val(json.data.shopName);//书名
            $("#dlgShop #subTitle").val(json.data.subTitle); //子标题
            $("#dlgShop #owner").val(json.data.owner);//作者
            $("#dlgShop #categoryId").val(json.data.categoryId); //分类选项
            editor.txt.html(json.data.description); //设置图文内容
            layui.form.render();//重新渲染LayUI表单
        } , "json")



    }
	//显示新增图书对话框
    function showCreate(){
		//弹出"新增图书"对话框
        layui.layer.open({
            id: "dlgShop",
            title: "新增店铺",
            type: 1,
            content: $('#dialog').html(),
            area: ['820px', '730px'],
            resize: false
        })
		//初始化wangEditor
        editor = new wangEditor('#dlgShop #editor');
        editor.customConfig.uploadImgServer = '/management/shop/upload';//设置图片上传地址
        editor.customConfig.uploadFileName = 'img';//设置图片上传参数
        editor.create();//创建wangEditor

        layui.form.render(); //LayUI表单重新
        $("#dlgShop #optype").val("create");//设置当前表单提交时提交至"create"新增地址

    };

	//对话框表单提交
    layui.form.on('submit(btnSubmit)', function(data){
		//获取表单数据
        var formData = data.field;
		
		//判断是否包含至少一副图片,默认第一图作为封面显示
        var description = editor.txt.html();
        if(description.indexOf("img") == -1){
            layui.layer.msg('请放置一副图片作为封面');
            return false;
        }
		//获取当前表单要提交的地址
		//如果是新增数据则提交至create
		//如果是更新数据则提交至update
        var optype = $("#dlgShop #optype").val();
		
        if(optype == "update"){
			//更新数据时,提交时需要附加图书编号
            formData.shopId=$("#dlgShop #shopId").val();
        }
		//附加图书详细描述的图文html
        formData.description = description;
		//向服务器发送请求
        $.post("/management/shop/" + optype , formData , function(json){
            if(json.code=="0"){
				//处理成功,关闭对话框,刷新列表,提示操作成功
                layui.layer.closeAll();
                table.reload('shopList');
                layui.layer.msg('数据操作成功,图书列表已刷新');
            }else{
				//处理失败,提示错误信息
                layui.layer.msg(json.msg);
            }
        } ,"json")
        return false;
    });
	//删除图书
    function showDelete(obj){
		//获取当前点击的删除按钮中包含的图书编号
        var shopId = $(obj).data("id");
		//利用layui的询问对话框进行确认
        layui.layer.confirm('确定要执行删除操作吗?', {icon: 3, title:'提示'}, function(index){
					
				//确认按钮后发送ajax请求,包含图书编号
				$.get("/management/shop/delete/" + shopId, {}, function (json) {
					if(json.code=="0"){
						//删除成功刷新表格
						table.reload('shopList');
						//提示操作成功
						layui.layer.msg('数据操作成功,图书列表已刷新');
						//关闭对话框
						layui.layer.close(index);
					}else{
						//处理失败,提示错误信息
						layui.layer.msg(json.msg);
					}
				}, "json");
			
        });

    }

</script>
</body>
</html>