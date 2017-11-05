var questionsObject = new Object();
var prevControl = null;
var firstError = null;

function setChoice(a) {
	$(a).prev("input").val(a.value);
}
$(function() {
	$("input[type='text']").focus(function() {
		$(this.parentNode).addClass("ui-focus");
	});
	$("input[type='text']").blur(function() {
		$(this.parentNode).removeClass("ui-focus");
	});
	$("input").bind("click", function(q) {
		var k = $(this).parents(".field")[0];
		if(window.isWeiXin && prevControl && prevControl != this && ($(prevControl).is("textarea") || $(prevControl).is("input:text"))) {
			$(prevControl).blur();
		}
		prevControl = this;
		if(k && k.removeError) {
			k.removeError();
		}
		q.preventDefault();
	});
	var b = false;
	var v = new Array();
	$(".field").each(function() {
		var A = $(this);
		A.bind("click", function() {
			if(this.removeError) {
				this.removeError();
			}
			if(window.scrollup) {
				scrollup.Stop();
			}
		});
		var I = A.attr("type");
		var H = getTopic(A);
		questionsObject[H] = A;
		/*单选*/
		if(I == "3") {
			$("div.ui-radio", A).bind("click", function(N) {
				var M = $(this).find("input[type='radio']")[0];
				if(M.disabled) { return; } window.hasAnswer = true;
				$(A).find("div.ui-radio").each(function() {
					$(this).find("input[type='radio']")[0].checked = false;
					$(this).find("a.jqradio").removeClass("jqchecked");
					$(this).removeClass("focuschoice");
				});
				M.checked = true;
				$(this).find("a.jqradio").addClass("jqchecked");
				$(this).addClass("focuschoice");
				N.preventDefault();
			});
			var L = A.attr("qingjing");
			if(L) { v.push(A); }
		} else if(I == "4") {
			$("div.ui-checkbox", A).bind("click", function(N) {
				var M = $(this).find("input[type='checkbox']")[0];
				if(M.disabled) { return; } M.checked = !M.checked;
				window.hasAnswer = true;
				if(M.checked) {
					$(this).find("a.jqcheck").addClass("jqchecked");
					$(this).addClass("focuschoice");
				} else {
					$(this).find("a.jqcheck").removeClass("jqchecked");
					$(this).removeClass("focuschoice");
				}
				N.preventDefault();
			});
		}
	});

	$("#ctlNext") != null && $("#ctlNext").on("click", function() {
		var k = validate();
		if(!k) { return; }
        var f = groupAnswer();
        var jsonString = JSON.stringify(f);
        console.log(jsonString);
        $.ajax({
            type: 'POST',
            url: ctxFront + '/survey/submit',
            dataType: 'json',
            data: {id:surveyId,userid:userid,jsonString:jsonString},
            async: true,
            success: function (data) {
                if (data != null) {
                   if(data.code==1){
                   	    setTimeout(function () {
                            $.alert("问卷提交成功!")
                        },200);

				   }else if(data.code == 2) {
                       setTimeout(function () {
                           $.alert(data.msg);
                       },250);
				   }else{
                       setTimeout(function () {
                           $.alert("问卷提交失败");
                       },200);
				   }
                }
            },
            error: function (xhr, type) {
                $.hideLoading();
                setTimeout(function () {
                    $.alert("问卷提交失败");
                },200);
            },
            beforeSend: function (xhr, settings) {
                $.showLoading();
            },
            complete: function (xhr, status) {
                $.hideLoading();
            }

        });
		
	
	});
});

function groupAnswer() {
    var f = new Array();
    var k = 0;
    $(".field").each(function() {
        var s = $(this);
        var y = new Object();
        var w = s.attr("type");
        var t = this.style.display != "none";
        if(t) {
            y.qid = s.data("qid");
            switch(w) {
                case "3":
                    $("input[type='radio']:checked", s).each(function() {
                        y.aid = $(this).val();
                    });
                    break;
                case "4":
                    var aids = [];
                    $("input[type='checkbox']:checked", s).each(function() {
                        aids.push($(this).val());
                    });
                    y.aid = aids.join(",");
                    break;
            }
            f[k++] = y;
        }
    });
    return f;
}

function removeError() {
	if(this.errorMessage) {
		this.errorMessage.innerHTML = "";
		this.removeError = null;
		this.style.border = "solid 2px #f7f7f7";
		if(this.errorControl) {
			this.errorControl.style.background = "white";
			this.errorControl = null;
		}
	}
}

function writeError(a, c, b) {
	a = $(a)[0];
	a.style.border = "solid 2px #ff9900";
	if(a.errorMessage) {
		a.errorMessage.innerHTML = c;
	} else {
		a.errorMessage = $(".errorMessage", $(a))[0];
		a.errorMessage.innerHTML = c;
	}
	a.removeError = removeError;
	if(a.errorControl) {
		a.errorControl.style.background = "#FBD5B5";
	}
	if(!firstError) {
		firstError = a;
	}
	return false;
}

function getTopic(a) {
	return $(a).attr("topic");
}

function validate() {
	var b = true;
	firstError = null;
	$(".field:visible").each(function() {

		var d = $(this),
			a = validateQ(d);
		if(!a) { b = false; }
	});
	if(!b) {
		if(firstError) {
			$("html, body").animate({ scrollTop: $(firstError).offset().top }, 600);
			$(".scrolltop").show();
			$(".scrolltop").click(function() {
				$("html, body").animate({ scrollTop: $(document).height() }, 600);
				$(".scrolltop").hide();
			});
		}
	} else {}
	return b;
}

function validateQ(n) {
	var g = $(n).attr("req"),
		k = $(n).attr("type"),
		m = false;
	var j = $(n)[0];
	var f = "";
	$(n).find("input:checked").each(function() {
		m = true;
	});

	if(!m && g == "1") {
		f = "此题是必答题";
		writeError(n, f, 1000);
	} else {
		$("span.error", $(n)).hide();
		$("div.field-label", $(n)).css("background", "");
	}
	if(f) {
		return false;
	}
	if(j.removeError) {
		j.removeError();
	}
	return true;
}