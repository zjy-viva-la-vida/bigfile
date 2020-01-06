<html>
<head>
    <meta charset="utf-8">
    <title>HTML5大文件分片上传示例</title>
    <script src="http://cdn.bootcss.com/jquery/1.12.4/jquery.min.js"></script>
    <script src="../static/js/md5.js"></script>

    <script type="text/javascript">

        var shardSize = 1 * 1024 * 1024;    //以10MB为一个分片
        var succeed = 0;
        var dataBegin;  //开始时间
        var dataEnd;    //结束时间

        var page = {
            init: function () {
                $("#upload").click(function () {
                    dataBegin = new Date();
                    succeed = 0;
                    $("#output").text("");
                    $("#useTime").text("");
                    $("#fileId").text("");
                    $("#param").text("上传过程：");

                    checkBeforeUpload();
                });
            }
        };
        $(function () {
            page.init();
        });

        function checkBeforeUpload () {
            var file = $("#file")[0].files[0];  //文件对象
            //构造一个表单，FormData是HTML5新增的
            var form = new FormData();
            var r = new FileReader();
            r.readAsBinaryString(file);
            $(r).load(function(e){
                var blob = e.target.result;
                /* 该方法对于文件较大的话会很耗内存，浏览器有奔溃风险，建议文件分块不要设置过大
                var md5 = hex_md5(blob);*/

                var random = Math.floor(Math.random()*100000000);
                /*var md5 = random;*/
                var md5 = "75904340";
                var name = file.name;
                form.append("md5", md5);
                form.append("name", name);
                //Ajax提交
                $.ajax({
                    url: "checkBeforeUpload",
                    type: "POST",
                    data: form,
                    async: true,        //异步
                    processData: false,  //很重要，告诉jquery不要对form进行处理
                    contentType: false,  //很重要，指定为false才能形成正确的Content-Type
                    success: function(data){
                        var fileId = data.Result.fileId;
                        var code = data.Code;
                        console.log("code",code);
                        if (code == "0") {
                            //没有上传过文件
                            readyUpload(fileId,md5,data.Result.date);
                        } else{
                            //文件已经上传过
                            alert("文件已经上传过,不需要重复上传！！");
                        }
                    },error: function(XMLHttpRequest, textStatus, errorThrown) {
                        alert("服务器出错!");
                    }
                })
            })
        };

        function readyUpload(fileId, md5, date) {
            var file = $("#file")[0].files[0];  //文件对象
            var name = file.name;
            var size = file.size;
            var shardCount = Math.ceil(size / shardSize);  //文件切割的总片数

            for(let index = 1;index <= shardCount;index++){
                //构造一个表单，FormData是HTML5新增的,不用let的话会造成ajax不是根据for循环调用后端而变成了异步调用
                let form = new FormData();
                form.append("fileId", fileId);
                form.append("md5", md5);
                form.append("name", name);
                form.append("size", size);
                form.append("total", shardCount);  //总片数
                //当前是第几片
                //计算每一片的起始与结束位置
                let firstPart = index -1;
                let start = firstPart * shardSize;
                let end = Math.min(size, start + shardSize);
                //切割的文件数据
                let data = file.slice(start, end);
                let r = new FileReader();
                r.readAsBinaryString(data);
                console.info("判断ajax异步调用，index：",index)
                let nowIndex = index;
                $(r).load(function (e) {
                    let bolb = e.target.result;
                    //生成切割文件的md5
                    let partMd5 = hex_md5(bolb);
                    /*let partMd5 = "3222222222";*/
                    form.append("partMd5", partMd5);
                    form.append("index", nowIndex);

                    console.info("partMd5",partMd5,"nowIndex",nowIndex);
                    //判断分片的文件是否已上传过，上传过就不重复上传

                    /*form.append("data", data);
                    upload(form,nowIndex,shardCount,fileId)*/

                    $.ajax({
                        url: "checkPartFile",
                        type: "POST",
                        data: form,
                        async: true,        //异步
                        processData: false,  //很重要，告诉jquery不要对form进行处理
                        contentType: false,  //很重要，指定为false才能形成正确的Content-Type
                        success: function(result){
                            var code = result.Code;
                            $("#param").append("<br/>" + "action==check " + "&nbsp;&nbsp;");
                            $("#param").append("index==" + nowIndex);
                            //显示提示信息
                            $("#fileId").text("文件ID：" + fileId);
                            dataEnd = new Date();
                            $("#useTime").text("共耗时：" + (dataEnd.getTime() - dataBegin.getTime())/1000 + "秒");
                            if(code == 0){
                                //分片文件未上传过，可以上传
                                form.append("data", data);
                                upload(form,nowIndex,shardCount,fileId)
                            }else{
                                //不是0的情况下可以标记上传分片文件成功
                                ++succeed;
                                $("#output").text(succeed + " / " + shardCount)
                                if (succeed  == shardCount) {
                                    dataEnd = new Date();
                                    $("#fileId").text("文件ID：" + fileId);
                                    $("#useTime").text("共耗时：" + (dataEnd.getTime() - dataBegin.getTime())/1000 + "秒");
                                    $("#param").append("<br/>" + "上传成功！");
                                }
                            }
                        },error: function(XMLHttpRequest, textStatus, errorThrown) {
                            alert("服务器出错!");
                        }
                    })
                });
            }
        }


        function  upload(form,index,shardCount,fileId) {
            console.info("开始上传分片文件，index：",index)
            //Ajax提交
            $.ajax({
                url: "uploadPartFile",
                type: "POST",
                data: form,
                async: true,        //异步
                processData: false,  //很重要，告诉jquery不要对form进行处理
                contentType: false,  //很重要，指定为false才能形成正确的Content-Type
                success: function (data) {
                    console.log("上传分片文件index：" + index + "，result:",data)
                    var code = data.Code;
                    if(code == 0){
                        $("#param").append("<br/>" + "action==upload " + "&nbsp;&nbsp;");
                        $("#param").append("index==" + index);
                        ++succeed;
                        $("#output").text(succeed + " / " + shardCount)

                        //显示提示信息
                        $("#fileId").text("文件ID：" + fileId);
                        dataEnd = new Date();
                        $("#useTime").text("共耗时：" + (dataEnd.getTime() - dataBegin.getTime())/1000 + "秒");

                        if (succeed  == shardCount) {
                            dataEnd = new Date();
                            // $("#fileId").append(fileId);
                            // $("#useTime").append((dataEnd.getTime() - dataBegin.getTime())/1000);
                            // $("#useTime").append("s")
                            $("#param").append("<br/>" + "上传成功！");
                        }
                    }

                }, error: function (XMLHttpRequest, textStatus, errorThrown) {
                    alert("服务器出错!");
                }
            });
        }




    </script>
</head>

<body>




<input type="file" id="file" />
<button id="upload">开始上传</button>
<br/><br/>
<span style="font-size:16px">上传进度：</span><span id="output" style="font-size:16px"></span>
<span id="useTime" style="font-size:16px;margin-left:20px;"></span>
<span id="fileId" style="font-size:16px;margin-left:20px;"></span>
<br/><br/>
<span id="param" style="font-size:16px"></span>

</body>
</html>