<!doctype html>
<html>
<head>
    <meta name="layout" content="main"/>
    <asset:javascript src="application.js"/>
    <asset:stylesheet src="application.css"/>
    <title>Mapping</title>

    <asset:link rel="icon" href="favicon.ico" type="image/x-ico"/>
</head>

<body>
<content tag="nav">
    <li><a href="javascript:toggleRunning()" id="btnControl">START</a></li>
</content>

<div id="content" role="main" style="text-align: center">

    <canvas id="tutorial" width="1200" height="900" style="background-color: #f4f4f4;margin-top:20px;"></canvas>

</div>
<script language="JavaScript" type="text/javascript">

    $(document).ready(function () {
        updateMap();
        toggleRunning();
    });

    function updateMap() {

        $.ajax('${createLink(action: 'mapJSON')}', {
            success: function (data) {
                drawMap(data);
                setTimeout(updateMap, 500);
            },
            error: function () {
                $('#notification-bar').text('An error occurred');
                setTimeout(updateMap, 500);
            }
        });

    }

    var ctx;

    var colors = [];

    function drawMap(data) {

//        if (colors.length == 0) {
//            var o = Math.round, r = Math.random, s = 255;
//            for (var i = 0; i <= 255; i++)
//                colors[i] = 'rgba(' + o(r() * s) + ',' + o(r() * s) + ',' + o(r() * s) + ',' + r().toFixed(1) + ')';
//        }

        $('#tutorial').css('background', '#eeeeee').width(data.height * 2).height(data.width * 2);
        var canvas = document.getElementById('tutorial');
        if (!ctx)
            ctx = canvas.getContext('2d');

        ctx.clearRect(0, 0, canvas.width, canvas.height);
//        ctx.fillStyle = 'rgb(0, 100, 100)';
//        ctx.fillRect(0 + 600 - 5, 0 + 450 - 5, 10, 10);

//        var o = Math.round, r = Math.random, s = 255;
        for (var i = 0; i < data.width; i++) {
            for (var j = 0; j < data.height; j++) {
                var point = data.data[j * data.width + i];
//                var opacity = 0;
//            if (point.z <= 255)
//                opacity = 255 - point.z;
//            ctx.fillStyle = colors[opacity];
                if (point != -1) {
                    ctx.fillStyle = (point == 0 ? 'rgb(255,255,255)' : 'rgb(0,0,0)');
                    ctx.fillRect((data.height - j) * 2, (data.width - i) * 2, 3, 3);
                }
            }
        }
        ctx.fillStyle = ('rgb(255,0,255)');
        ctx.fillRect(data.height, data.width, 5, 5);
    }

    var running = true;

    function toggleRunning() {
        $.get(running ? "${createLink(action: 'stop')}" : "${createLink(action: 'start')}", function (data) {
            if (data == '1') {
                running = true;
                $('#btnControl').html('STOP');
            }
            else {
                running = false;
                $('#btnControl').html('START');
            }
        });
    }
</script>

</body>
</html>
