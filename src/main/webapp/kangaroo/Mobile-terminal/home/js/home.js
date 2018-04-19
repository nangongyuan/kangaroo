/*
 * @Author: shn 
 * @Date: 2018-04-11 18:31:34 
 * @Last Modified by: shn
 * @Last Modified time: 2018-04-12 16:10:53
 */
var pagenum = 1;
var s;
var e;
var t = true;

$(function () {
    ajax_data_home();
    console.log(1);
    $("#home-content").css("height", document.documentElement.clientHeight - 60);

    $("#home-content").on('scroll', function () {
        
        if ($(this).height() + Math.ceil($(this)[0].scrollTop + 2) >= $(this)[0].scrollHeight) {
            pagenum++;
            ajax_data_home();
        }

    });

});

var now = new Date();
var myYear = now.getFullYear();
var myWeek = now.getDay();
var myDay = now.getDate();
var myMonth = now.getMonth();

function is(data) {

    if (myYear == data.getFullYear() && myMonth == data.getMonth()) {
        var d = myWeek;
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


    if (date_format.getFullYear() > myYear) {
        console.log(date_format.getYear() + ":" + myYear)
        date_format_f = date_format_f + date_format.getFullYear() + "年";
    }

    let y;
    let t = is(date_format);

    if (myDay == date_format.getDate() && myMonth == date_format.getMonth() && myYear == date_format.getFullYear()) {
        y = "今日";
    } else if (t) {
        y = week;
    } else {
        y = date_format.getMonth() + 1 + "月" + date_format.getDate() + "日";
    }

    date_format_f = date_format_f + y + "&nbsp;" + date_format.getHours() + ":" + date_format.getMinutes();

    return date_format_f;
}

function ajax_data_home() {
    if (t == true) {
        $.ajax({
            type: 'GET',
            url: 'http://' + ip + '/activity/recommend?pagenum=' + pagenum + '&pagesize=4',
            beforeSend: function () {
                $("#icon_load").css("display", "block");
            },
            success: function (json) {

                $("#icon_load").css("display", "none");
                
                if (json.success == true) {
                    if (json.data.rows.length < 4) {
                        $("#have_data").css("display", "none");
                        $("#no_data").css("display", "block");                    
                    }
                    
                    for (let i = 0; i < json.data.rows.length; i++) {
                        let status = "未开始";
                        s = new Date(json.data.rows[i].startTime);
                        e = new Date(json.data.rows[i].endTime);

                        let sd = date_Format(s);
                        let ed = date_Format(e);
                        if (json.data.rows[i].status == 0) {
                            status = "正在进行";
                        } else if (json.data.rows[i].status == 2) {
                            status = "已结束";
                        }

                        $("#home-content ul").append(
                            "<div class='row' style='position:relative;top:0px;'  id='activity-home-" + json.data.rows[i].id + "' onclick='show_activity_info(0,this)'>" +
                            "<div class='col-xs-12' style='padding:10px;padding-bottom:0px;margin-bottom:-13px;'>"

                            +
                            "<div class='panel panel-default'>" +
                            "<div class='panel-heading' style='padding:0;height:200px;overflow:hidden'>" +
                            "<img src='http://" + ip + "/advertisings/" + json.data.rows[i].pic + "' style='width:100%;height:auto'>" +
                            "</div>" +
                            "<div class='panel-body'>" +
                            "<p class='title'>" + json.data.rows[i].name + "</p>" +
                            "<p class='address'>" + json.data.rows[i].area + "  |  " + json.data.rows[i].site + "</p>" +
                            "<p class='address'>" + sd + "&#12288;至&#12288;" + ed + "<span class='status'>" + status + "</span></p>" +
                            "</div>" +
                            "</div>"

                            +
                            "</div>" +
                            "</div>"
                        );
                    }

                } else {
                    $("#have_data").css("display", "none");
                    $("#icon_load").css("display", "none");
                    $("#no_data").css("display", "block");
                    t = false;
                }
            },
        });
    }
}