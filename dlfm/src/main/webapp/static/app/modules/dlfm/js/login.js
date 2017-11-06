function initGlobal() {

    $('.loginbox').css({'position': 'absolute', 'left': ($(window).width() - 692) / 2});
    $(window).resize(function () {
        $('.loginbox').css({'position': 'absolute', 'left': ($(window).width() - 692) / 2});
    });


    $("#txtpd").focus(function () {
        $(this).css("display", "none");
        $("#txtpwd").css("display", "block");
        $("#txtpwd").val("");
        $("#txtpwd").focus()
    });
    $("#txtpwd").blur(function () {
        if ($(this).val() == "") {
            $("#txtpd").css("display", "block");
            $(this).css("display", "none");
            $("#txtpd").val("请输入密码");
        }
    });
}

function submitForm() {
    if ($("#txtname").val() == "请输入用户名") {
        alert("用户名不能为空！");
        return ;
    }
    if ($("#txtpwd").val() == "请输入密码") {
        alert("密码不能为空！");
        return ;
    }
    jQuery.post(url,data,success(data, textStatus, jqXHR),dataType)
}