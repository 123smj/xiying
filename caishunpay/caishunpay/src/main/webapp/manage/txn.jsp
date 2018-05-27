<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
    String path = request.getContextPath();
			String basePath = request.getScheme() + "://"
					+ request.getServerName() + ":" + request.getServerPort()
					+ path + "/";
%>

<!doctype html>
<html lang="en"><head>
    <title>万物交易管理平台</title>
    <meta content="IE=edge,chrome=1" http-equiv="X-UA-Compatible">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content=""><%--

    <link href='http://fonts.googleapis.com/css?family=Open+Sans:400,700' rel='stylesheet' type='text/css'>
    --%><link rel="stylesheet" type="text/css" href="lib/bootstrap/css/bootstrap.css">
    <link rel="stylesheet" href="lib/font-awesome/css/font-awesome.css">

    <script src="lib/jquery-1.11.1.min.js" type="text/javascript"></script>
    <script type="text/javascript" src="<%= request.getContextPath()%>/common_js/md5.js"></script>

    

    <link rel="stylesheet" type="text/css" href="<%= request.getContextPath()%>/stylesheets/theme.css">
    <link rel="stylesheet" type="text/css" href="<%= request.getContextPath()%>/stylesheets/comon.css">
    <link rel="stylesheet" type="text/css" href="<%= request.getContextPath()%>/stylesheets/premium.css">

	 <link href="<%= request.getContextPath()%>/build/toastr.css" rel="stylesheet" />
    <script src="<%= request.getContextPath()%>/build/toastr.min.js"></script>
</head>
<body class=" theme-blue">
<div class="table-responsive" style="width: 100%;">
<div style="width: 1500px;overflow:scroll-x">
<table class="table" width="1500px" >
  <thead>
    <tr>
      <th>#</th>
      <th width="80px" >交易日期</th>
      <th width="80px">交易时间</th>
      <th width="120px">卡号</th>
      <th width="80px">商户编号</th>
      <th width="100px">渠道商户号</th>
      <th width="120px">接入方交易号</th>
      <th width="120px">平台交易号</th>
      <th width="120px">上游交易号</th>
      <th width="80px">交易金额</th>
      <th width="80px">交易渠道</th>
      <th width="80px">交易类型</th>
      <th width="80px">应答码</th>
      <th width="120px">应答码描述</th>
      <th width="80px">是否T+0</th>
      <th width="80px">t0应答码</th>
      <th width="120px">t0应答码描述</th>
      <th style="width: 3.5em;"></th>
    </tr>
  </thead>
  
  <%--<tbody>
    <tr>
      <td>1</td>
      <td>Mark</td>
      <td>Tompson</td>
      <td>the_mark7</td>
      <td>
          <a href="user.html"><i class="fa fa-pencil"></i></a>
          <a href="#myModal" role="button" data-toggle="modal"><i class="fa fa-trash-o"></i></a>
      </td>
    </tr>
    <tr>
      <td>2</td>
      <td>Ashley</td>
      <td>Jacobs</td>
      <td>ash11927</td>
      <td>
          <a href="user.html"><i class="fa fa-pencil"></i></a>
          <a href="#myModal" role="button" data-toggle="modal"><i class="fa fa-trash-o"></i></a>
      </td>
    </tr>
    <tr>
      <td>3</td>
      <td>Audrey</td>
      <td>Ann</td>
      <td>audann84</td>
      <td>
          <a href="user.html"><i class="fa fa-pencil"></i></a>
          <a href="#myModal" role="button" data-toggle="modal"><i class="fa fa-trash-o"></i></a>
      </td>
    </tr>
    <tr>
      <td>4</td>
      <td>John</td>
      <td>Robinson</td>
      <td>jr5527</td>
      <td>
          <a href="user.html"><i class="fa fa-pencil"></i></a>
          <a href="#myModal" role="button" data-toggle="modal"><i class="fa fa-trash-o"></i></a>
      </td>
    </tr>
    <tr>
      <td>5</td>
      <td>Aaron</td>
      <td>Butler</td>
      <td>aaron_butler</td>
      <td>
          <a href="user.html"><i class="fa fa-pencil"></i></a>
          <a href="#myModal" role="button" data-toggle="modal"><i class="fa fa-trash-o"></i></a>
      </td>
    </tr>
    <tr>
      <td>6</td>
      <td>Chris</td>
      <td>Albert</td>
      <td>cab79</td>
      <td>
          <a href="user.html"><i class="fa fa-pencil"></i></a>
          <a href="#myModal" role="button" data-toggle="modal"><i class="fa fa-trash-o"></i></a>
      </td>
    </tr>
  </tbody>
--%></table>
</div></div>

</body></html>