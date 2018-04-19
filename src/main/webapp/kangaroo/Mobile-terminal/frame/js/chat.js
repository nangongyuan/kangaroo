/*
 * @Author: shn 
 * @Date: 2018-04-11 18:30:51 
 * @Last Modified by: shn
 * @Last Modified time: 2018-04-12 21:41:17
 */

screenFuc();
var myself_headpic = "";
// var myself_id = "";
var send_img;

$(function () {

    $("#chat-fasong").css("left", window.screen.width - 100)

    let url = "http://" + ip + "/users";

    $.ajax({
        url: url,
        type: 'GET',
        success: function (json) {
            var success = $.trim(json.success);
            if (success == 'true') {
                myself_headpic = json.data.headpic;
                myself_id = json.data.id;
                myself_name = json.data.name;
            } else {

            }
        },
        error: function () {}
    });

    $(".emoji-picker-image").attr("src", "img/white.png"); //表情背景图初始化，不然有灰色边框
})

function screenFuc() {
    var topHeight = $(".chatBox-head").innerHeight(); //聊天头部高度
    //屏幕小于768px时候,布局change
    var winWidth = $(window).innerWidth();
    if (winWidth <= 768) {
        var totalHeight = $(window).height(); //页面整体高度
        $(".chatBox-info").css("height", totalHeight - topHeight);
        var infoHeight = $(".chatBox-info").innerHeight(); //聊天头部以下高度
        //中间内容高度
        $(".chatBox-content").css("height", infoHeight - 46);
        $(".chatBox-content-demo").css("height", infoHeight - 110);

        $(".chatBox-list").css("height", totalHeight - topHeight);
        $(".chatBox-kuang").css("height", totalHeight - topHeight);
    } else {
        $(".chatBox-info").css("height", 495);
        $(".chatBox-content").css("height", 448);
        $(".chatBox-content-demo").css("height", 448);
        $(".chatBox-list").css("height", 495);
        $(".chatBox-kuang").css("height", 495);
    }
}
(window.onresize = function () {
    screenFuc();
})();

$(".chat-return").click(function () {
    $(".chatBox-head-one").toggle(1);
    $(".chatBox-head-two").toggle(1);
    $(".chatBox-list").fadeToggle(1);
    $(".chatBox-kuang").fadeToggle(1);
});

//      发送信息
$("#chat-fasong").click(function () {
    let myDate = new Date();

    let textContent = "";
    textContent = $(".div-textarea").html();
    textContent = textContent.replace(/\"/g, "'");
    let date = myDate.toLocaleString();
    if (textContent != "") {
        $(".chatBox-content-demo").append("<div class=\"clearfloat\">" +
            "<div class=\"author-name\"><small class=\"chat-date\" style=\"float:right;font-size:75%\">" + date + "</small> </div><br>" +
            "<div class=\"right\"> <div class=\"chat-message\"> " + textContent + " </div> " +
            "<div class=\"chat-avatars\"><img src='" + "http://" + ip + "/headpics/" + myself_headpic + "' alt=\"头像\" /></div> </div> </div>");
        //发送后清空输入框
        $(".div-textarea").html("");
        //聊天框默认最底部
        $(document).ready(function () {
            $("#chatBox-content-demo").scrollTop($("#chatBox-content-demo")[0].scrollHeight);
        });
    }

    textContent = change_bq_img_c(textContent);
    textContent = textContent.replace(/\"/g, "'");

    if (textContent != "") {
        let json_send = '{"type":2,"data":"' + textContent + '","receiver":' + receive + '}';
        send(json_send);
    }

});

//      发送表情
$("#chat-biaoqing").click(function () {
    $(".biaoqing-photo").toggle();
});

$(document).click(function () {
    $(".biaoqing-photo").css("display", "none");
    $(".room-biaoqing-photo").css("display", "none");
});

$("#chat-biaoqing").click(function (event) {
    event.stopPropagation(); //阻止事件
});

$(".emoji-picker-image-chat").each(function () {
    $(this).click(function () {
        var bq = $(this).parent().html();
        $(".div-textarea").append($.trim(bq));
    })
});

//      发送图片
function selectImg(pic) {
    let myDate = new Date();
    let date = myDate.toLocaleString();

    if (!pic.files || !pic.files[0]) {
        return;
    }

    var formData = new FormData();
    formData.append("file", document.getElementById("chat_inputImage").files[0]);
    
    let url = "http://" + ip + "/chatimages";
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
                send_img = encodeURIComponent(json.data.name);
                
                var reader = new FileReader();
                reader.onload = function (evt) {
                    var images = evt.target.result;
                    $(".chatBox-content-demo").append("<div class='clearfloat'>" +
                        "<div class='author-name'><small class='chat-date'>" + date + "</small> </div> " +
                        "<div class='right'> <div class='chat-message'><img src=" + images + " style='width:220px;height:auto'></div> " +
                        "<div class='chat-avatars'><img src='" + "http://" + ip + '/headpics/' + myself_headpic + "' alt='头像' /></div> </div> </div>");
                    //聊天框默认最底部
                    $("#chatBox-content-demo").scrollTop($("#chatBox-content-demo")[0].scrollHeight);
                    $.hideLoading();
                    $.toast("发送成功", 1000);

                };
                reader.readAsDataURL(pic.files[0]);

                let json_send = '{"type":3,"data":"' + send_img + '","receiver":' + receive + '}'
                send(json_send);

            } else {
                $.hideLoading();
                $.toast("发送失败,图片过大", "cancel", 1000);
            }

        },
        error: function () {
            $.hideLoading();
            $.toast("发送失败", "cancel", 1000);
        },
        beforeSend: function () {
            $.showLoading();
        },
    });


}

$(".chat-close").click(function () {
    $(".chatBox").animate({
        left: '102%',
        opacity: '0'
    }, 250);
    $("#frame-main").animate({
        opacity: '1'
    }, 250);
    $("#frame-main").css("display", "block");

    $(".chatBox-send").hide()

});

//格式化表情
function change_bq_img_c(temp){
    do{
        str = temp;
        temp = str.replace("<img class='emoji-picker-image emoji-picker-image-chat' style='background-position: ",'{/');
        temp = temp.replace(";' src='img/white.png'>","/}");
    }while(str!=temp);
    
    return temp;
}

$(".div-textarea").keydown(function (e) {
    if (e.keyCode == 13) {
        event.preventDefault();
        $("#chat-fasong").click();
    }
});