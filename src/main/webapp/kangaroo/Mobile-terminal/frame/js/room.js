/*
 * @Author: shn 
 * @Date: 2018-04-11 18:31:09 
 * @Last Modified by: shn
 * @Last Modified time: 2018-04-12 15:39:16
 */
var mySwiper = '';

$(function () {
    $("#room").css("height", document.documentElement.clientHeight)
    $(".no-vote").css("height", document.documentElement.clientHeight);
    $(".no-vote").css("width", document.documentElement.clientWidth);
    $(".no-vote").css("line-height", document.documentElement.clientHeight + "px");
    $("#room-vote-div").css("height", document.documentElement.clientHeight);    
    $("#room-vote").css("height", document.documentElement.clientHeight - 48);
    $("#room-install").css("height", document.documentElement.clientHeight);
    $("#room-vote").css("width", document.documentElement.clientWidth);
    $(".roomBox-send").css("top", document.documentElement.clientHeight - 45);
    $(".roomBox-content").css("height", document.documentElement.clientHeight - 48 - 45 - 30);
    $(".roomBox-content").css("width", document.documentElement.clientWidth - 20);
    $("#room-chat").css("height", document.documentElement.clientHeight);
    $("#room-gift").css("height", document.documentElement.clientHeight);
    $(".room-button").css("width", document.documentElement.clientWidth)
    $(".weui-cells").css("width", document.documentElement.clientWidth)

    $(".div-room-textarea").keydown(function (e) {
        if (e.keyCode == 13) {
            event.preventDefault();
            $("#room-fasong").click();
        }
    });

    $(".room-back").click(function () {

        $("#room").animate({
            left: "102%",
            opacity: 0
        }, 200);
        $("#frame-main").animate({
            opacity: 1
        }, 200);
        $("#frame-main").css("display", "block");

        setTimeout(function () {
            $("#room").css("display", "none");
        }, 200);

        $(".roomBox-content").html("");
        mySwiper.destroy(false);

        room_websocket.close();

        $("#room-gift").find(".weui-cell").remove();
    });

    $("#room-fasong").click(function () {
        if (speak_type == 1) {
            let textContent = "";
            textContent = $(".div-room-textarea").html();
            textContent = change_bq_img(textContent);
            textContent = textContent.replace(/\"/g, "'");
            
            $(".roomBox-content").scrollTop($(".roomBox-content")[0].scrollHeight + 75);
            
            //发送后清空输入框
            send_room_barrage('{"type":0,"data":"' + textContent + '","receiver":' + now_activity_id + '}')
            $(".div-room-textarea").html("");
        } else {
            $(".div-room-textarea").html("");
            $.toptip('全员禁言中', 'warning');
        }
    })

    $("#room-biaoqing").click(function () {
        $(".room-biaoqing-photo").toggle();
    });

    $("#room-biaoqing").click(function (event) {
        event.stopPropagation(); //阻止事件
    });

    $(".emoji-picker-image-room").each(function () {
        $(this).click(function () {
            var bq = $(this).parent().html();
            $(".div-room-textarea").append($.trim(bq));
        })
    });

    // 获取文本域文字长度并显示
    $('#room-poll-popup-textarea').on('keyup', function () {
        var _length = $(this).val().length;
        $("#room-poll-popup-textarea-length").html(_length);
    });


    // 获取开关状态
    $("#switchDX").click(function () {

        if ($("#switchDX").val() == "off") {

            $("#switchDX").val("on");
            console.log("开关1当前状态：" + $("#switchDX").val());
            multiselect = 1;

        } else {

            $("#switchDX").val("off");
            console.log("开关1当前状态：" + $("#switchDX").val());
            multiselect = 0;

        }

    })

    // 添加更多选项
    $("#add-option").click(function () {
        if ($("#room-poll-popup-option-" + (poll_option_num - 1)).val() == "" || $("#room-poll-popup-option-" + (poll_option_num - 2)).val() == "") {
            $.toptip('请将选项填写完整', 'error', 500);
        } else {
            $("#room-poll-popup-option").append(
                "<div class='weui-cell'>" +
                "<div class='weui-cell__hd'>" +
                "<label class='weui-label'>选项" + poll_option_num + "</label>" +
                "</div>" +
                "<div class='weui-cell__bd'>" +
                "<input class='weui-input' type='text' placeholder='' id='room-poll-popup-option-" + poll_option_num + "'>" +
                "</div>" +
                "</div>"
            )
            poll_option_num++;
        }
    })

    //关闭投票popup
    $("#room-poll-popup-button-cancel").click(function () {
        $.closePopup();
    })

    //发布投票信息
    $("#room-poll-popup-button-confirm").click(function () {
        if ($("#room-poll-popup-option-" + (poll_option_num - 1)).val() == "" || $("#room-poll-popup-option-" + (poll_option_num - 2)).val() == "") {
            $.toptip('请将选项填写完整', 'error', 500);
        } else {
            let option = '{"name":"' + $("#room-poll-popup-option-1").val() + '"},{"name":"' + $("#room-poll-popup-option-2").val() + '"},';
            for (let i = 3; i < poll_option_num; i++) {
                option = option + '{"name":"' + $("#room-poll-popup-option-" + i).val() + '"},'
            }

            option = option.substring(0, option.length - 1);

            msg = '{"type":5,"data":{"name":"' + $("#room-poll-popup-textarea").val() + '","multiselect":' + multiselect +
                ',"voteGroups":[' + option + ']},"receiver":' + now_activity_id + '}'

            $.closePopup();

            send_room_barrage(msg);

            $("#room-poll-popup-option-1").val("");
            $("#room-poll-popup-option-2").val("");
            $("#room-poll-popup-textarea").val("");

            for(let i = poll_option_num-1;i>2;i--){
                $("#room-poll-popup-option-" + i).parent().parent().remove();
            }

        }
    })

    //关闭抽奖popup
    $("#room-gift-popup-button-cancel").click(function () {
        $.closePopup();
    })

    //发布抽奖信息
    $("#room-gift-popup-button-confirm").click(function () {
        let r = /^\+?[1-9][0-9]*$/;

        if ($("#room_gift_number").val() == "" || $("#room_gift_name").val() == "" || $("#room_gift_draw_name").val() == "") {
            $.toptip('请将选项填写完整', 'error', 500);
        } else {
            if ($("#room_gift_number").val() < 0 || !r.test($("#room_gift_number").val())) {
                $.toptip('请输入合理的礼物数量', 'error', 500);
            } else {
                msg = '{"type":4,"receiver":' + now_activity_id + ',"data":{"name":"' + $("#room_gift_draw_name").val() + '","prizename":"' + $("#room_gift_name").val() + '","number":' + $("#room_gift_number").val() + ' }}'

                $.closePopup();

                send_room_barrage(msg);
            }
        }
    })

    //初始化发言模式picker
    $("#room-speak").select({
        title: "",
        items: ["全员禁言仅房主发言", "无限制"],
        onClose: function () {
            let status = 0;
            if (this.items[0].checked == false) {
                status = 1;
            } else {
                status = 0;
            }
            msg = '{"type":12,"receiver":' + now_activity_id + ',"data":' + status + '}'
            send_room_barrage(msg);
        }
    });

    // 上传图片
    $("#room_file").change(function () {

        var formData = new FormData();
        formData.append("file", document.getElementById("room_file").files[0]);

        let url = "http://" + ip + "/displaypic";
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
                $.hideLoading();
                if (json.success == true) {
                    $.toast("上传成功", 1000);
                    msg = '{"type":1,"receiver":' + now_activity_id + ',"data":"' + json.data.name + '"}'
                    send_room_barrage(msg);
                } else {
                    $.toast("图片太大,上传失败", "cancel", 1000);
                }

            },
            error: function () {
                $.hideLoading();
                $.toast("上传失败", "cancel", 1000);
            },
            beforeSend: function () {
                $.showLoading();
            },
        });
    });

    //用户分享图片
    $("#room-tupian").click(function () {
        $("#room-inputImage").trigger("click");
    })

    $("#room-inputImage").change(function () {

        let formData = new FormData();
        formData.append("file", document.getElementById("room-inputImage").files[0]);

        let url = "http://" + ip + "/displaypic";
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
                $.hideLoading();
                if (json.success == true) {
                    $.toast("上传成功", 1000);
                    msg = '{"type":23,"receiver":' + now_activity_id + ',"data":"' + json.data.name + '"}'
                    send_room_barrage(msg);
                } else {
                    $.toast("图片太大,上传失败", "cancel", 1000);
                }

            },
            error: function () {
                $.hideLoading();
                $.toast("上传失败", "cancel", 1000);
            },
            beforeSend: function () {
                $.showLoading();
            },
        });
    });

})

var starRating = 0;
var poll_option_num = 3;
var multiselect = 1;
var speak_type = 1;
var random = 0; //0普通红包 1随机红包

// 打开投票popub
function room_poll() {
    $("#room-poll-popup").popup();
    // $("#room-speak").select("close");
}

// 打开抽奖popub
function room_gift() {
    $("#room-gift-popup").popup();
    // $("#room-speak").select("close");

}

// 打开发言模式picker
function room_speak() {
    $("#room-speak").select("open");
}

// 显示海报
function room_show_poster() {
    msg = '{"type":2,"receiver":' + now_activity_id + '}'
    send_room_barrage(msg);
}

// 显示签到
function room_show_sign() {
    msg = '{"type":3,"receiver":' + now_activity_id + '}'
    send_room_barrage(msg);
}

// 显示投票结果
function room_show_poll() {
    $("#room-show-poll").select("open");
}

// 发起评分
function room_score() {
    msg = '{"type":7,"receiver":' + now_activity_id + '}'
    send_room_barrage(msg);
}

// 上传图片
function room_up_img() {
    $("#room_file").trigger("click");
}

// 取消分享图片
function room_down_img() {
    msg = '{"type":24,"receiver":' + now_activity_id + '}'
    send_room_barrage(msg);
}

//发起签到
function room_send_sign() {
    msg = '{"type":25,"receiver":' + now_activity_id + ',"data":{"latitude":29.9116600000,"longitude":121.6102200000}}'
    send_room_barrage(msg);
}

//用户点击签到
function room_click_sign() {
    msg = '{"type":26,"receiver":' + now_activity_id + ',"data":{"latitude":29.9116600000,"longitude":121.6102200000}}'
    send_room_barrage(msg);
}

// 发红包
function room_redpaper() {
    $.modal({
        title: "发红包",
        text: "<div><a href='javascript:;' class='weui-btn weui-btn_plain-primary' id='room-send-redpacket-select-1' onclick='room_send_redpacket_select_1()'>拼手气红包</a>" +
            "<a href='javascript:;' class='weui-btn weui-btn_plain-primary' id='room-send-redpacket-select-2' onclick='room_send_redpacket_select_2()'>普通红包</a></div>" +
            "<div class='weui-cells'  id='room_send_redpacket' style='overflow:inherit !important'>" +
            "<div class='weui-cell'  style='font-size:17px'>" +
            "<div class='weui-cell__hd'><label class='weui-label' id='room-send-redpacket-label1'>总金额</label></div>" +
            "<input class='weui-input' type='number' placeholder='输入金额' style='width:150px;margin-right:10px;text-align:right;font-size:15px' id='room-send-redpacket-money'>" +
            "<div style='position:absolute;right:10px;'>元</div>" +
            "</div>" +
            "<div class='weui-cell'  style='font-size:17px'>" +
            "<div class='weui-cell__hd'><label class='weui-label'>红包个数</label></div>" +
            "<input class='weui-input' type='number' placeholder='' style='width:150px;margin-right:10px;text-align:right;font-size:15px' id='room-send-redpacket-number'>" +
            "<div style='position:absolute;right:10px;'>个</div>" +
            "</div>" +
            "<div class='weui-cell'  style='font-size:17px'>" +
            "<div class='weui-cell__hd'><label class='weui-label'>祝福文字</label></div>" +
            "<input class='weui-input' type='text' placeholder='节日快乐' style='width:150px;margin-right:10px;text-align:right' id='room-send-redpacket-title'>" +
            "</div>" +
            "</div>",
        buttons: [{
                text: "支付宝",
                className: "pay-btn",
                onClick: function () {
                    let title = "节日快乐";

                    if($("#room-send-redpacket-title").val() != ""){
                        title = $("#room-send-redpacket-title").val();
                    }

                    if (random == 0) {
                        msg = '{"type":6,"receiver":' + now_activity_id +
                            ',"data":{"rptitle":"' + title +
                            '","money":' + $("#room-send-redpacket-money").val() * $("#room-send-redpacket-number").val() +
                            ',"number":' + $("#room-send-redpacket-number").val() +
                            ',"random":' + random + '}}'
                    } else {
                        msg = '{"type":6,"receiver":' + now_activity_id +
                            ',"data":{"rptitle":"' + $("#room-send-redpacket-title").val() +
                            '","money":' + $("#room-send-redpacket-money").val() +
                            ',"number":' + $("#room-send-redpacket-number").val() +
                            ',"random":' + random + '}}'
                    }

                    $(".romm_red_packet_modal_title").html(title);
                    
                    send_room_barrage(msg);
                    random = 1;
                }
            },
            {
                text: "微信",
                className: "pay-btn",
                onClick: function () {
                    let title = "节日快乐";
                    
                    if($("#room-send-redpacket-title").val() != ""){
                        title = $("#room-send-redpacket-title").val();
                    }

                    if (random == 0) {
                        msg = '{"type":6,"receiver":' + now_activity_id +
                            ',"data":{"rptitle":"' + title +
                            '","money":' + $("#room-send-redpacket-money").val() * $("#room-send-redpacket-number").val() +
                            ',"number":' + $("#room-send-redpacket-number").val() +
                            ',"random":' + random + '}}'
                    } else {
                        msg = '{"type":6,"receiver":' + now_activity_id +
                            ',"data":{"rptitle":"' + $("#room-send-redpacket-title").val() +
                            '","money":' + $("#room-send-redpacket-money").val() +
                            ',"number":' + $("#room-send-redpacket-number").val() +
                            ',"random":' + random + '}}'
                    }

                    $(".romm_red_packet_modal_title").html(title);
                    
                    send_room_barrage(msg);
                    random = 1;                    
                }
            },
            {
                text: "取消",
                className: "default",
                onClick: function () {
                    random = 1;                    
                }
            },
        ]
    });

    $("#room-send-redpacket-number").attr("placeholder", "当前" + room_people_num + "人在线")

    $("#room-send-redpacket-money").change(function () {
        if ($("#room-send-redpacket-money").val() < 0.01) {
            $("#room-send-redpacket-money").val("");
            $("#room-send-redpacket-money").attr("placeholder", "金额必须大于0.01")
        }
    })

    $("#room-send-redpacket-number").change(function () {
        if ($("#room-send-redpacket-number").val() > room_people_num) {
            $("#room-send-redpacket-number").val("");
            $("#room-send-redpacket-number").attr("placeholder", "人数必须小于在线人数")
        }
    })

}

// 投票栏单选列表及按钮
var checked_id;

function initPanel(id) {
    let $outer = $(id);

    $outer.find('label').click(function () {

        $outer.find('.weui-icon-checked-radius').hide();
        $(this).find('.weui-icon-checked-radius').show();
        $(this).find('input[type="radio"]').attr('checked', 'checked');
        checked_id = $(this).find('input[type="radio"]').attr('id');

    });

    $outer.find('a').click(function () {
        let _checked_id = checked_id.substring(1, checked_id.length);
        $(this).html("已投票");
        $(this).css("pointer-events", "none");
        $(this).css("background-color", "#e8e8e8");
        $(this).css("color", "#000");
        $outer.find('label').css("pointer-events", "none");

        msg = '{"type":9,"receiver":' + now_activity_id + ',"data":[' + _checked_id + ']}'
        send_room_barrage(msg);


    });
}

// 投票栏多选列表及按钮
function initPanelCheck(id) {
    let $outers = $(id);

    $outers.find('label').click(function () {

        if ($(this).find('.weui-icon-checked-check').css("display") == "none") {
            $(this).find('.weui-icon-checked-check').show();
            $(this).find('input[type="checkbox"]').attr('checked', 'checked');
        } else {
            $(this).find('.weui-icon-checked-check').hide();
            $(this).find('input[type="checkbox"]').removeAttr('checked');
        }
    
        checked_id = $(this).find('input[type="checkbox"]').attr('id');

    });

    $outers.find('a').click(function () {
        // let _checked_id = checked_id.substring(1, checked_id.length);
        $(this).html("已投票");
        $(this).css("pointer-events", "none");
        $(this).css("background-color", "#e8e8e8");
        $(this).css("color", "#000");
        $outers.find('label').css("pointer-events", "none");
        let a = "";

        $outers.find('label').find('input[type="checkbox"]').each(function () {
            if ($(this).is(':checked')) {
                let b = $(this).attr('id');
                b = b.substring(1, b.length);
                a = a + "," + b;
            }
        })

        a = a.substring(1, a.length);

        msg = '{"type":9,"receiver":' + now_activity_id + ',"data":[' + a + ']}'
        send_room_barrage(msg);

        // msg = '{"type":9,"receiver":' + now_activity_id + ',"data":[' + _checked_id + ']}'
        // send_room_barrage(msg);

    });
}

// 设置普通红包
function room_send_redpacket_select_1() {
    $("#room-send-redpacket-select-1").css("border-bottom", "1px solid rgb(192, 29, 29)");
    $("#room-send-redpacket-select-2").css("border-bottom", "1px solid #FFF");
    $("#room-send-redpacket-select-1").css("color", "rgb(192, 29, 29)");
    $("#room-send-redpacket-select-2").css("color", "rgb(161, 161, 161)");
    $("#room-send-redpacket-label1").html("总金额");
    random = 1;
}
// 设置拼手气红包
function room_send_redpacket_select_2() {
    $("#room-send-redpacket-select-2").css("border-bottom", "1px solid rgb(192, 29, 29)");
    $("#room-send-redpacket-select-1").css("border-bottom", "1px solid #FFF");
    $("#room-send-redpacket-select-2").css("color", "rgb(192, 29, 29)");
    $("#room-send-redpacket-select-1").css("color", "rgb(161, 161, 161)");
    $("#room-send-redpacket-label1").html("单个金额");
    random = 0;
}

// 抽取红包
function room_red_packet_modal_img() {
    msg = '{"type":19,"receiver":' + now_activity_id + ',"data":' + room_redpacket_data + '}'
    send_room_barrage(msg);
    $(".chuai").removeAttr("onclick");
}

//关闭红包modal
function close_packet_modal() {
    event.returnValue = false; // 取消事件的默认行为
    event.cancelBubble = true; // 阻止事件冒泡
    $("#romm_red_packet_modal").fadeOut(300);
}

//格式化表情
function change_bq_img(temp){
    do{
        str = temp;
        temp = str.replace('<img class="emoji-picker-image emoji-picker-image-room" style="background-position:','{/');
        temp = temp.replace(';" src="img/white.png">','/}');
    }while(str!=temp);
    return temp;
}