/*
 * @Author: shn 
 * @Date: 2018-04-11 18:32:02 
 * @Last Modified by:   shn 
 * @Last Modified time: 2018-04-11 18:32:02 
 */
var colors = ["#A98DEB", "#E58167", "#ECBA89", "#EBB5C4", "#B2D97B", "#9BD1F2"];
var temp;
var m = new Map();

function ShowHobby() {
    let ScreenWidth = $(document.body).outerWidth(true);
    var z;
    if (ScreenWidth >= 410 && ScreenWidth < 760) {
        z = 4;
    } else if (ScreenWidth >= 760) {
        z = 8;
    } else {
        z = 3;
    }
    $.ajax({
        type: 'GET',
        url: 'http://' + ip + '/activity-type',
        success: function (data, textStatus, jqXHR) {
            temp = data.data;
            var changeone;
            for (var i = 0; i < temp.length; i++) {
                if (i % z == 0) {
                    ul = document.createElement("ul");
                    document.getElementById("J_List").appendChild(ul);
                }
                colorIndex = Math.floor(Math.random() * 6);
                m.set('' + temp[i].id, colorIndex);
                ul.innerHTML += '<div><span id=' + temp[i].id + ' onClick="labelClick(this)" style="color:' + colors[colorIndex] + '" title="未选">' + temp[i].name + '</span></div>';
            }

        }
    });
}

function labelClick(e) {
    if ("未选" == e.title) {
        e.style.color = '#FFFFFF';
        e.style.background = colors[m.get(e.id)];
        e.title = "已选";
    } else {
        e.style.color = colors[m.get(e.id)];
        e.style.background = '#FFFFFF';
        e.title = "未选";
    }
};

function createResult() {
    var str = '';
    for (var i = 0; i < temp.length; i++) {
        var tb = document.getElementById(temp[i].id);
        if (tb.title == '已选') {
            str += temp[i].id + ",";
        }
    }
    console.log(str);
    return str;
};