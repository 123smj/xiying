<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="com.manage.bean.OprInfo,com.trade.bean.*,com.manage.bean.*"%>

<%
String menuTree = (String)request.getSession().getAttribute("menuTree");
List<TradeDetailBean> list = (List<TradeDetailBean>)request.getAttribute("tradeList");
%>
<%@ include file="include.jsp" %>
<!doctype html>
<html lang="en"><head>
    <meta charset="utf-8">
    <title>彩顺网络科技平台</title>
    <meta content="IE=edge,chrome=1" http-equiv="X-UA-Compatible">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content=""><%--

    <link href='http://fonts.googleapis.com/css?family=Open+Sans:400,700' rel='stylesheet' type='text/css'>
    --%><link rel="stylesheet" type="text/css" href="<%= request.getContextPath()%>/lib/bootstrap/css/bootstrap.css">
    <link rel="stylesheet" href="<%= request.getContextPath()%>/lib/font-awesome/css/font-awesome.css">
    

    <link rel="stylesheet" type="text/css" href="<%= request.getContextPath()%>/stylesheets/theme.css">
    <link rel="stylesheet" type="text/css" href="<%= request.getContextPath()%>/stylesheets/comon.css">
    <link rel="stylesheet" type="text/css" href="<%= request.getContextPath()%>/stylesheets/premium.css">

</head>
<body class=" theme-blue">

    <!-- Demo page code -->

    <script type="text/javascript">
        $(function() {
            var match = document.cookie.match(new RegExp('color=([^;]+)'));
            if(match) var color = match[1];
            if(color) {
                $('body').removeClass(function (index, css) {
                    return (css.match (/\btheme-\S+/g) || []).join(' ')
                })
                $('body').addClass('theme-' + color);
            }

            $('[data-popover="true"]').popover({html: true});
            
        });
    </script>
    <style type="text/css">
        #line-chart {
            height:300px;
            width:800px;
            margin: 0px auto;
            margin-top: 1em;
        }
        .navbar-default .navbar-brand, .navbar-default .navbar-brand:hover { 
            color: #fff;
        }
    </style>

    <script type="text/javascript">
        $(function() {
            var uls = $('.sidebar-nav > ul > *').clone();
            uls.addClass('visible-xs');
            $('#main-menu').append(uls.clone());
        });
    </script>

    <!-- Le HTML5 shim, for IE6-8 support of HTML5 elements -->
    <!--[if lt IE 9]>
      <script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
    <![endif]-->

    <!-- Le fav and touch icons -->
    <link rel="apple-touch-icon-precomposed" sizes="144x144" href="../assets/ico/apple-touch-icon-144-precomposed.png">
    <link rel="apple-touch-icon-precomposed" sizes="114x114" href="../assets/ico/apple-touch-icon-114-precomposed.png">
    <link rel="apple-touch-icon-precomposed" sizes="72x72" href="../assets/ico/apple-touch-icon-72-precomposed.png">
    <link rel="apple-touch-icon-precomposed" href="../assets/ico/apple-touch-icon-57-precomposed.png">
  

  <!--[if lt IE 7 ]> <body class="ie ie6"> <![endif]-->
  <!--[if IE 7 ]> <body class="ie ie7 "> <![endif]-->
  <!--[if IE 8 ]> <body class="ie ie8 "> <![endif]-->
  <!--[if IE 9 ]> <body class="ie ie9 "> <![endif]-->
  <!--[if (gt IE 9)|!(IE)]><!--> 
   
  <!--<![endif]-->

    <div class="navbar navbar-default" role="navigation">
        <div class="navbar-header">
          <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target=".navbar-collapse">
            <span class="sr-only">Toggle navigation</span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </button>
          <a class="" href="index.html"><span class="navbar-brand"><span class="fa"></span> 翼快宝支付 - 线上管理平台</span></a></div>

        <div class="navbar-collapse collapse" style="height: 1px;">
          <ul id="main-menu" class="nav navbar-nav navbar-right">
            <li class="dropdown hidden-xs">
                <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                    <span class="glyphicon glyphicon-user padding-right-small" style="position:relative;top: 3px;"> <%=oprInfo.getOpr_name() %></span> 
                    <i class="fa fa-caret-down"></i>
                </a>
                
              <ul class="dropdown-menu">
                <li><a href="<%= request.getContextPath()%>/manage/update_pwd.jsp">修改密码</a></li>
                <li class="divider"></li>
                <li><a tabindex="-1" href="<%= request.getContextPath()%>/manage/loginOut">退出登录</a></li>
              </ul>
              
            </li>
          </ul>

        </div>
      </div>
      <div id="loading" class="loading" style="">Loading pages...</div>
    

    <div id="mainDiv" class="sidebar-nav">
    <%--<ul>
    <li><a href="manage/main.jsp#" data-target=".dashboard-menu" class="nav-header " data-toggle="collapse"><i class="fa fa-fw fa-dashboard"></i>商户管理<i class="fa fa-collapse"></i></a></li>
    <li><ul class="dashboard-menu nav nav-list collapse in">
    		<li><a href="/manage/mcht_add.jsp" target="main_iframe"><span class="fa fa-caret-right"></span> 商户新增</a></li>
            <li><a href="/manage/mcht_query.jsp" target="main_iframe"><span class="fa fa-caret-right"></span> 商户查询</a></li>
            <li><a href="/manage/mcht_update.jsp" target="main_iframe"><span class="fa fa-caret-right"></span> 商户修改</a></li>
    </ul></li>

	</ul>
    --%></div>
<iframe class="content"  width="82%"  height="auto" scrolling="no" src="<%= request.getContextPath()%><%if("1".equals(oprInfo.getOpr_type())){%>/manage/tradeQuery4Admin.jsp<%}else{%>/manage/tradeQuery.jsp<%} %>" id="main_iframe" name="main_iframe"></iframe>
		
    <script type="text/javascript">
        $("[rel=tooltip]").tooltip();
        $(function() {
            $('.demo-cancel-click').click(function(){return false;});
        });
        $('.collapse').collapse();

        function getTree() {
            // Some logic to retrieve, or generate tree structure
            var data = <%=menuTree%>;
            return data;
        }
        var obj = {};
        obj.text = "123";
        $('#mainDiv').treeview({
            data: getTree(),         
            levels: 5,
            multiSelect: false,//是否可选择多个节点
            highlightSelected: false,
            onNodeSelected: function(event, data) {
            	if("" != $.trim(data.pageUrl)) {//菜单则跳转
            		window.parent.main_iframe.location.href = "<%= request.getContextPath()%>" + data.pageUrl;
                }
            	else {//目录则折叠
            		$('#mainDiv').treeview('toggleNodeExpanded', [ data.nodeId, { silent: false } ]);
                }
            	$('#mainDiv').treeview('toggleNodeSelected', [ data.nodeId, { silent: false } ]);
            }
        });
    </script>
    
  
</body></html>
