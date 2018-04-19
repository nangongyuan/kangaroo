/*
 * @Author: shn 
 * @Date: 2018-04-11 18:31:50 
 * @Last Modified by: shn
 * @Last Modified time: 2018-04-12 15:41:33
 */
$(function () {
    $(".update-popup-img-div").click(function () {
        $("#update-popup-img-file").trigger("click");
    })

    //上传活动宣传图
    $("#update-popup-img-file").change(function () {
        var formData = new FormData();
        formData.append("file", document.getElementById("update-popup-img-file").files[0]);

        let url = "http://" + ip + "/advertising";
        $.ajax({
            url: url,
            type: "POST",
            data: formData,
            /**
             *必须false才会自动加上正确的Content-Type
             */
            contentType: false,
            /**
             * 必须false才会避开jQuery对 formdata 的默认处理
             * XMLHttpRequest会对 formdata 进行正确的处理
             */
            processData: false,
            success: function (json) {
                console.log(json.success);
                if (json.success == true) {
                    $.hideLoading();
                    $.toast("上传成功", 1000);
                    activity_pic = json.data.name;
                    $(".update-popup-img").attr('src', 'http://' + ip + "/advertisings/" + activity_pic);
                } else {
                    $.hideLoading();
                    $.toast("上传失败,图片过大", "cancel", 1000);
                }

            },
            error: function () {
                console.log("上传失败")
            },
            beforeSend: function () {
                $.showLoading();
            },
        });
    })


})

function update_activity_info(obj) {
    let id = obj.id.split("-");
    $.actions({
        actions: [{
            text: "修改",
            onClick: function () {
                $("#update-notrun-div").popup();
                $.ajax({
                    type: "GET",
                    url: "http://" + ip + "/activity/" + id[2] + "/detail",
                    success: function (json) {
                        $.hideLoading(json);
                        if (json.success == true) {
                            select_li_id = json.data.typeid;
                            activity_pic = json.data.pic;
                            $(".update-popup-img").attr("src", "http://" + ip + "/advertisings/" + json.data.pic)
                            $(".update-activity-title").html(json.data.name);
                            $(".update-activity-intro").html(json.data.intro);
                            $("#update-activity-intro-length").html(json.data.intro.length);
                            $("#update-city-picker").val(json.data.area);
                            $(".update-activity-site").html(json.data.site);
                            $(".update-activity-max").val(json.data.max);
                            $(".update-type-picker").val(json.data.typeName);
                            $(".update-activity-button").attr("id", "update-activity-button-" + id[2])

                            $("#update-datetime-picker-s").datetimePicker({
                                value: date_Format2(new Date(json.data.startTime)),
                                min: mynyr_s,
                                onClose: function () {
                                    mynyr_e = $("#update-datetime-picker-s").val();
                                    mynyr_e = mynyr_e.substring(0, mynyr_e.length - 6);
                                    $("#update-datetime-picker-e").removeAttr("disabled");
                                    console.log(mynyr_e)
                                },
                                toolbar: false,
                            });
                            $("#update-datetime-picker-e").datetimePicker({
                                value: date_Format2(new Date(json.data.endTime)),
                                min: mynyr_e,
                                toolbar: false,
                                onClose: function () {
                                    if (mynyr_e < mynyr_s) {
                                        console.log(mynyr_e)
                                    }
                                },
                            });

                            $("#update-city-picker").cityPicker({
                                // showDistrict: false,
                                toolbar: false,

                            });

                        } else {
                            $.toast("获取活动信息失败", "cancel", 1000);
                        }
                    },
                    error: function () {
                        $.hideLoading();
                        $.toast("获取活动信息失败", "cancel", 1000);
                    },
                    beforeSend: function () {
                        $.showLoading();
                    }
                })
            }
        }, {
            text: "删除",
            onClick: function () {
                $.confirm("您确定要删除此次活动吗", function () {
                    $.ajax({
                        type: "POST",
                        url: "http://192.168.1.110/activity/" + id[2] + "/del",
                        success: function (json) {
                            $.hideLoading();
                            if (json.success == true) {
                                $("#ublish-notrun").trigger("click");
                            } else {
                                $.toast("删除活动失败", "cancel", 1000);
                            }
                            $("#publish-notrun").trigger("click");
                        },
                        error: function () {
                            $.hideLoading();
                            $.toast("删除活动失败", "cancel", 1000);
                        },
                        beforeSend: function () {
                            $.showLoading();
                        }
                    })
                    //点击确认后的回调函数
                }, function () {
                    //点击取消后的回调函数
                });

            }
        }]
    });
}

function update_activity_button(obj) {
    let id = obj.id.split("-");

    url = 'http://' + ip + '/activity/put?id=' + id[3] +
        "&name=" + $(".update-activity-title").html() +
        "&intro=" + $(".update-activity-intro").val() +
        "&typeid=" + select_li_id +
        "&pic=" + activity_pic +
        "&area=" + $("#update-city-picker").val() +
        "&s=" + $("#update-datetime-picker-s").val() +
        "&e=" + $("#update-datetime-picker-e").val() +
        "&site=" + $(".update-activity-site").html() +
        "&max=" + $(".update-activity-max").val() +
        "&password=" + $(".update-activity-password").val();
    console.log(url);

    $.ajax({
        type: 'POST',
        url: url,
        success: function (json) {
            $.hideLoading();
            if (json.success == true) {
                $.toast("修改活动成功", 1000);
                $.closePopup();
            } else {
                $.toast("修改活动失败", "cancel", 1000);
            }
        },
        beforeSend: function () {
            $.showLoading();
        },
        error: function () {
            $.hideLoading();
            $.toast("修改活动失败", "cancel", 1000);
        },
    })
}

function update_activity_button_close() {
    $.closePopup();
}

//获取活动类型名称及id
var activity_number;
var activity_type = new Array;
var activity_pic;
//上传图片自适应居中
var select_li_id;
var select_li_name;

function select_li(objct) {
    select_li_id = objct.id;
    select_li_name = objct.innerHTML;
    $(".update-type-picker").val(select_li_name);
    $.closeModal();
}

function date_Format2(formatdate) {
    let i = formatdate.getFullYear() + "-" + (formatdate.getMonth() + 1) + "-" + formatdate.getDate() + " " + formatdate.getHours() + ":" + formatdate.getMinutes()
    return i;
}

function show_update_type() {
    $.modal({
        title: '点击选择活动类型',
        text: '<div style="height:150px;overflow-y:scroll">' +
            '<ul style="padding:0;height:100%;" id="update-type-ul-id"></ul>' +
            '</div>',
        buttons: [{
            text: "取消",
            className: "default",
            onClick: function () {
                $.closeModal();
            }
        }, ]
    });

    $.ajax({
        type: 'GET',
        url: 'http://' + ip + '/activity-type',
        success: function (json) {
            $.hideLoading();
            if (json.success == true) {
                activity_number = json.data.length;
                for (let i = 0; i < activity_number; i++) {
                    $("#update-type-ul-id").append("<li id='" + json.data[i].id + "'onclick='select_li(this)' style='font-size: 17px;line-height: 1.5;'>" + json.data[i].name + "</li>");
                }
            } else {
                $.toast("获取活动类型失败", "cancel", 1000);
            }

        },
        beforeSend: function () {
            $.showLoading("正在获取活动类型");
        },
        error: function () {
            $.hideLoading();
            $.toast("获取活动类型失败", "cancel", 1000);
        },
    });
}