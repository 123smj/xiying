<!doctype html>
<html lang="en"><head>
    <meta charset="utf-8">
    <title>Bootstrap Admin</title>
    <meta content="IE=edge,chrome=1" http-equiv="X-UA-Compatible">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content=""><%--

    <link href='http://fonts.googleapis.com/css?family=Open+Sans:400,700' rel='stylesheet' type='text/css'>
    --%><link rel="stylesheet" type="text/css" href="<%= request.getContextPath()%>/lib/bootstrap/css/bootstrap.css">
    <link rel="stylesheet" href="<%= request.getContextPath()%>/lib/font-awesome/css/font-awesome.css">

    <script src="<%= request.getContextPath()%>/lib/jquery-1.11.1.min.js" type="text/javascript"></script>

    

    <link rel="stylesheet" type="text/css" href="<%= request.getContextPath()%>/stylesheets/theme.css">
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
    <link rel="shortcut icon" href="../assets/ico/favicon.ico">
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
    

    <div class="content">
        

<div class="row">
  <div class="col-md-4">
    <br>
    <div id="myTabContent" class="tab-content">
      <div class="tab-pane active in" id="home">
      <form id="tab">
        <div class="form-group">
        <label>Username</label>
        <input type="text" value="jsmith" class="form-control">
        </div>
        <div class="form-group">
        <label>First Name</label>
        <input type="text" value="John" class="form-control">
        </div>
        <div class="form-group">
        <label>Last Name</label>
        <input type="text" value="Smith" class="form-control">
        </div>
        <div class="form-group">
        <label>Email</label>
        <input type="text" value="jsmith@yourcompany.com" class="form-control">
        </div>

        <div class="form-group">
          <label>Address</label>
          <textarea value="Smith" rows="3" class="form-control">2817 S 49th
Apt 314
San Jose, CA 95101</textarea>
        </div>

        <div class="form-group">
            <label>Time Zone</label>
            <select name="DropDownTimezone" id="DropDownTimezone" class="form-control">
              <option value="-12.0">(GMT -12:00) Eniwetok, Kwajalein</option>
              <option value="-11.0">(GMT -11:00) Midway Island, Samoa</option>
              <option value="-10.0">(GMT -10:00) Hawaii</option>
              <option value="-9.0">(GMT -9:00) Alaska</option>
              <option selected="selected" value="-8.0">(GMT -8:00) Pacific Time (US &amp; Canada)</option>
              <option value="-7.0">(GMT -7:00) Mountain Time (US &amp; Canada)</option>
              <option value="-6.0">(GMT -6:00) Central Time (US &amp; Canada), Mexico City</option>
              <option value="-5.0">(GMT -5:00) Eastern Time (US &amp; Canada), Bogota, Lima</option>
              <option value="-4.0">(GMT -4:00) Atlantic Time (Canada), Caracas, La Paz</option>
              <option value="-3.5">(GMT -3:30) Newfoundland</option>
              <option value="-3.0">(GMT -3:00) Brazil, Buenos Aires, Georgetown</option>
              <option value="-2.0">(GMT -2:00) Mid-Atlantic</option>
              <option value="-1.0">(GMT -1:00 hour) Azores, Cape Verde Islands</option>
              <option value="0.0">(GMT) Western Europe Time, London, Lisbon, Casablanca</option>
              <option value="1.0">(GMT +1:00 hour) Brussels, Copenhagen, Madrid, Paris</option>
              <option value="2.0">(GMT +2:00) Kaliningrad, South Africa</option>
              <option value="3.0">(GMT +3:00) Baghdad, Riyadh, Moscow, St. Petersburg</option>
              <option value="3.5">(GMT +3:30) Tehran</option>
              <option value="4.0">(GMT +4:00) Abu Dhabi, Muscat, Baku, Tbilisi</option>
              <option value="4.5">(GMT +4:30) Kabul</option>
              <option value="5.0">(GMT +5:00) Ekaterinburg, Islamabad, Karachi, Tashkent</option>
              <option value="5.5">(GMT +5:30) Bombay, Calcutta, Madras, New Delhi</option>
              <option value="5.75">(GMT +5:45) Kathmandu</option>
              <option value="6.0">(GMT +6:00) Almaty, Dhaka, Colombo</option>
              <option value="7.0">(GMT +7:00) Bangkok, Hanoi, Jakarta</option>
              <option value="8.0">(GMT +8:00) Beijing, Perth, Singapore, Hong Kong</option>
              <option value="9.0">(GMT +9:00) Tokyo, Seoul, Osaka, Sapporo, Yakutsk</option>
              <option value="9.5">(GMT +9:30) Adelaide, Darwin</option>
              <option value="10.0">(GMT +10:00) Eastern Australia, Guam, Vladivostok</option>
              <option value="11.0">(GMT +11:00) Magadan, Solomon Islands, New Caledonia</option>
              <option value="12.0">(GMT +12:00) Auckland, Wellington, Fiji, Kamchatka</option>
            </select>
          </div>
        </form>
      </div>

      <div class="tab-pane fade" id="profile">

        <form id="tab2">
          <div class="form-group">
            <label>New Password</label>
            <input type="password" class="form-control">
          </div>
          <div>
              <button class="btn btn-primary">Update</button>
          </div>
        </form>
      </div>
    </div>

    <div class="btn-toolbar list-toolbar">
      <button class="btn btn-primary"><i class="fa fa-save"></i> Save</button>
      <a href="#myModal" data-toggle="modal" class="btn btn-danger">Delete</a>
    </div>
  </div>
</div>

        </div>


    <script src="lib/bootstrap/js/bootstrap.js"></script>
    <script type="text/javascript">
        $("[rel=tooltip]").tooltip();
        $(function() {
            $('.demo-cancel-click').click(function(){return false;});
        });
    </script>
    
  
</body></html>
