<%@page language="java" import="java.util.*" contentType="text/html; charset=utf-8" %>
<html>
<body>
<h2>Hello World!</h2>

SpringMVC 上传文件
<form name="form1" action="/manage/product/upload.do" method="post" enctype="multipart/form-data">
    <input type="file" name="upload_file" />
    <<input type="submit" value="上传文件" />
</form>

富文本图片上传
<form name="form1" action="/manage/product/richtext_img_upload.do.do" method="post" enctype="multipart/form-data">
    <input type="file" name="upload_file" />
    <<input type="submit" value="富文本图片上传" />
</form>

</body>
</html>
