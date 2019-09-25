# bigfile
大文件分片断点上传

#最原始的大文件上传，该方法不好用，太复杂了，逻辑也不好,upload.ftl
http://localhost:39090/bigfile/file/open

#修改后的大文件上传,可以用于实际使用,uploadFile.ftl
http://localhost:39090/bigfile/uploadFile/open

#用于测试直接上传大文件的，不分片的操作，测试上传时间，test.ftl
http://192.168.193.62:39090/bigfile/test/open

