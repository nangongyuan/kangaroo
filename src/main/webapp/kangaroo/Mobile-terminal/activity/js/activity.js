/*
 * @Author: shn 
 * @Date: 2018-04-11 18:30:11 
 * @Last Modified by: shn
 * @Last Modified time: 2018-04-12 16:29:19
 */


var pagenum = 1;
var have_data = true;
var activity_number;



$(function () {

    // 初始化分类
    $(".sort-div").css("width", document.documentElement.clientWidth - 70)
    $(".activity-content").css("height", document.documentElement.clientHeight - 60 - 44);
    $(".activity-content").css("width" , document.documentElement.clientWidth)
    $(".activity-sort").css("width",document.documentElement.clientWidth);
    //获取活动类型名称及id

    var activity_pic;

    $.ajax({
        type: 'GET',
        url: 'http://' + ip + '/activity-type',
        success: function (json) {
            activity_number = json.data.length;
            for (let i = 0; i < activity_number; i++) {
                $("#aa").append("<span id='" + json.data[i].id + "'onclick='select_sort(this)' class='sort-lable'>" + json.data[i].name + "</span>");
            }
        }
    });

    $("input").keyup(function () {
        $(".activity-content ul").html("");

        let url = "http://" + ip + "/activity/search?wd=" + $("#searchInput").val() + "&pagenum=1&pagesize=6";
        ajax_data(url);
        pagenum = 1;

        $("#activity-content").on('scroll', function () {
            
            if (have_data == true) {
                if ($(this).height() + Math.ceil($(this)[0].scrollTop + 2) >= $(this)[0].scrollHeight) {
                    pagenum++;
                    url = "http://" + ip + "/activity/search?wd=" + $("#searchInput").val() + "&pagenum=" + pagenum + "&pagesize=6"
                    ajax_data(url);
                }
            }
        })
    })

    $("#0").trigger("click");

});


function select_sort(typeid) {
    pagenum = 1;
    have_data = true;
    let url = "";

    $(".activity-content ul").html("");

    for (let i = 0; i < activity_number + 1; i++) {
        if (i == typeid.id) {
            $("#" + i).css("color", "rgb(250, 186, 90)");
        } else {
            $("#" + i).css("color", "rgb(70, 69, 69)");
        }
    }
    
    url = 'http://' + ip + '/activity/type/' + typeid.id + '?pagenum=' + pagenum + '&pagesize=6'
    ajax_data(url);

    $("#activity-content").on('scroll', function () {
        
        if (have_data == true) {
            if ($(this).height() + Math.ceil($(this)[0].scrollTop + 2) >= $(this)[0].scrollHeight) {
                pagenum++;
                url = 'http://' + ip + '/activity/type/' + typeid.id + '?pagenum=' + pagenum + '&pagesize=6'
                ajax_data(url);
            }
        }
    });
}


function ajax_data(url) {
    console.log(url)
    
    $.ajax({
        type: 'GET',
        url: url,
        beforeSend: function () {
            $("#icon-load").css("display", "block");
        },
        success: function (json) {
            $("#icon-load").css("display", "none");

            if (json.success == true) {
                if (json.data.rows.length < 6) {
                    $("#no-data").css("display", "block");
                    $("#have-data").hide()
                    $("#icon-load").hide()
                    have_data = false;
                    console.log(json.data.rows.length);
                    
                }

                for (let i = 0; i < 5; i = i + 2) {
                    if (i < json.data.rows.length) {
                        let status = "";
                        let content2 = "<div style='margin-top:10px;width:100%;opacity:0;z-index:-1' >" +
                            "<div class='second-box'>" +
                            "<div class='activity-panel panel-default'>" +
                            "<div class='activity-panel-heading'>" +
                            "<img src='' style='width:100%;height:100%'>" +
                            "</div>" +
                            "<div class='panel-body' style='padding:4px;'>" +
                            "<span class='title'>1</span>" +
                            "<p class='address'>2</p>"
                            "<span style='float:right'>3</span></p>" +
                            "</div>" +
                            "</div>" +
                            "</div>";

                        if (i < json.data.rows.length - 1) {
                            let status2 = "";
                            s2 = new Date(json.data.rows[i + 1].startTime);
                            let sd2 = date_Format(s2);
                            if (json.data.rows[i + 1].status == 0) {
                                status2 = "<img src='../activity/img/running.png' class='running_img'>";
                            } else if (json.data.rows[i + 1].status == 2) {
                                status2 = "<img src='../activity/img/ending.png' class='ending_img'>";
                            }else{
                                status2 = "<img src='../activity/img/not-started.png' class='nstart_img'>";                                
                            }

                            let name2 = json.data.rows[i + 1].name;
                            if (name2.length >= 9) {
                                name2 = name2.slice(0, 8) + "...";
                            }

                            let site2 = json.data.rows[i + 1].site;
                            if (site2.length >= 5) {
                                site2 = site2.slice(0, 4) + "...";
                            }

                            content2 = "<div style='margin-top:10px;width:100%;z-index:-1'>" +
                                "<div class='second-box' id='activity-activity-" + json.data.rows[i+1].id + "' onclick='show_activity_info(0,this)'>" +
                                status2 +
                                "<div class='activity-panel panel-default'>" +
                                "<div class='activity-panel-heading'>" +
                                "<img src='http://" + ip + "/advertisings/" + json.data.rows[i+1].pic + "' style='width:100%;height:auto'>" +
                                "</div>" +
                                "<div class='panel-body' style='padding:4px;'>" +
                                "<span class='title'>" + name2 + "</span>" +
                                "<p class='address'>" + site2 +
                                "<span style='float:right'>" + sd2 + "</span></p>" +
                                "</div>" +
                                "</div>" +
                                "</div>"
                        }

                        s = new Date(json.data.rows[i].startTime);

                        let sd = date_Format(s);

                        if (json.data.rows[i].status == 0) {
                            status = "<img src='../activity/img/running.png' class='running_img'>";
                        } else if (json.data.rows[i].status == 2) {
                            status = "<img src='../activity/img/ending.png' class='ending_img'>";
                        } else{
                            status = "<img src='../activity/img/not-started.png' class='nstart_img'>";                                                            
                        }

                        let name = json.data.rows[i].name;
                        if (name.length >= 9) {
                            name = name.slice(0, 8) + "...";
                        }

                        let site = json.data.rows[i].site;
                        if (site.length >= 5) {
                            site = site.slice(0, 4) + "...";
                        }

                        $(".activity-content ul").append(
                            "<div style=z-index:-1'>" +
                            "<div class='first-box'  id='activity-activity-" + json.data.rows[i].id + "' onclick='show_activity_info(0,this)'>" +
                            status +
                            "<div class='activity-panel panel-default'>" +
                            "<div class='activity-panel-heading'>" +
                            "<img src='http://" + ip + "/advertisings/" + json.data.rows[i].pic + "' style='width:100%;height:auto'>" +
                            "</div>" +
                            "<div class='panel-body' style='padding:4px;'>" +
                            "<span class='title'>" + name + "</span>" +
                            "<p class='address'>" + site +
                            "<span style='float:right'>" + sd + "</span></p>" +
                            "</div>" +
                            "</div>" +
                            "</div>" +
                            content2

                        )
                    }
                }

            } else {
                $("#have-data").css("display", "none");
                $("#icon-load").css("display", "none");
                $("#no-data").css("display", "block");
                have_data = false;
            }
        },
    });
}

function is(data) {

    if (now.getFullYear() == data.getFullYear() && now.getMonth() == data.getMonth()) {
        var d = now.getDay();
        if (d == 0) d = 7;
        var d1 = data.getDate();
        var d2 = myDay;
        if (d1 == d2)
            return true;
        else if (d1 > d2 && d1 - d2 <= 7 - d)
            return true;
        else if (d1 < d2 && d2 - d1 <= d - 1)
            return true;
    }

    return false;
}


function date_Format(date_format) {
    let week;
    let date_format_f = "";
    if (date_format.getDay() == 0) {
        week = "本周日";
    } else if (date_format.getDay() == 1) {
        week = "本周一";
    } else if (date_format.getDay() == 2) {
        week = "本周二";
    } else if (date_format.getDay() == 3) {
        week = "本周三";
    } else if (date_format.getDay() == 4) {
        week = "本周四";
    } else if (date_format.getDay() == 5) {
        week = "本周五";
    } else if (date_format.getDay() == 6) {
        week = "本周六";
    }


    if (date_format.getYear() > myYear) {
        return "非本年";
    }

    let y;
    let t = is(date_format);

    if (myDay == date_format.getDate()) {
        y = "今日";
    } else if (t) {
        y = week;
    } else {
        y = date_format.getMonth() + 1 + "月" + date_format.getDate() + "日";
    }

    date_format_f = date_format_f + y + "&nbsp;" + date_format.getHours() + ":" + date_format.getMinutes();

    return date_format_f;
}