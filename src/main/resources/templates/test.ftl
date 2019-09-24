<html>
<head>
    <meta charset="utf-8">
    <title>HTML5大文件分片上传示例</title>
    <script src="http://cdn.bootcss.com/jquery/1.12.4/jquery.min.js"></script>
    <script src="../static/js/md5.js"></script>
    <script type="text/javascript">
        var i = -1;
        var shardSize = 10 * 1024 * 1024;    //以1MB为一个分片
        var succeed = 0;
        var dataBegin;  //开始时间
        var dataEnd;    //结束时间
        var action = false;
        var page = {
            init: function () {
                $("#upload").click(function () {
                    dataBegin = new Date();
                    var file = $("#file")[0].files[0];  //文件对象
                    realUpload(file);
                });
            }
        };
        $(function () {
            page.init();
        });


        function realUpload(file) {
            var form = new FormData();
            name = file.name;
            size = file.size;
            form.append("file", file);
            form.append("name", name);
            form.append("size", size);
            $.ajax({
                url: "uploadAAA",
                type: "POST",
                data: form,
                async: true,        //异步
                processData: false,  //很重要，告诉jquery不要对form进行处理
                contentType: false,  //很重要，指定为false才能形成正确的Content-Type
                success: function (data) {
                    console.log("data:",data)

                }, error: function (XMLHttpRequest, textStatus, errorThrown) {
                    alert("服务器出错!");
                }
            });
        }
    </script>
</head>

<body>

<input type="file" id="file" />
<button id="upload">上传</button>
<br/><br/>
<span style="font-size:16px">上传进度：</span><span id="output" style="font-size:16px"></span>
<span id="useTime" style="font-size:16px;margin-left:20px;">上传时间：</span>
<span id="uuid" style="font-size:16px;margin-left:20px;">文件ID：</span>
<br/><br/>
<span id="param" style="font-size:16px">上传过程：</span>

</body>
</html>