$(document).ready(function() {

	// Shuffling (see http://yelotofu.com/2008/08/jquery-shuffle-plugin/)
	(function($){
		  $.fn.shuffleSet = function() {
		    var parents = $.map(this, function(o) { return o.parentNode; });
		    $.each(parents, function(i, o) {
		      while (o.childNodes[0])
		        o.removeChild(o.childNodes[0]);
	    	});
		    var shuffled = $.shuffle(this);
		    $.each(parents, function(i, o) {
		      o.appendChild(shuffled[i]);
		    });
		    return shuffled;
	      };

		  $.fn.shuffle = function() {
		    return this.each(function(){
		      var items = $(this).children();
		      return (items.length)
		        ? $(this).html($.shuffle(items))
		        : this;
		    });
		  };

		  $.shuffle = function(arr) {
		    for(
		      var j, x, i = arr.length; i;
		      j = parseInt(Math.random() * i),
		      x = arr[--i], arr[i] = arr[j], arr[j] = x
		    );
		    return arr;
		  }
		})(jQuery);
	
	// Shuffling of partner logos
	$(".linkList tr td").shuffleSet();

	// For registration and editing user forms
	
    function formShowSuccess() {
    	$(".formError").hide();
		$(".formSuccess").show();
    }
    
    function formShowError(text) {
		$(".formSuccess").hide();
		$(".formError").show();
		$(".formError").text(text);
    }

    // http://www.regular-expressions.info/email.html
    function isValidEMail(email) {
    	var pattern = new RegExp(/(?:[a-z0-9!#$%&'*+\/=?^_`{|}~-]+(?:\.[a-z0-9!#$%&'*+\/=?^_`{|}~-]+)*|"(?:[\x01-\x08\x0b\x0c\x0e-\x1f\x21\x23-\x5b\x5d-\x7f]|\\[\x01-\x09\x0b\x0c\x0e-\x7f])*")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\x01-\x08\x0b\x0c\x0e-\x1f\x21-\x5a\x53-\x7f]|\\[\x01-\x09\x0b\x0c\x0e-\x7f])+)\])/i);
    	return pattern.test(email);
    }
    
    $("#editUserForm").ajaxForm({
    	beforeSubmit: function(formData, jqForm, options) {
			if (!isValidEMail($("#formEMail").val())) {
				formShowError("E-postadressen valideres feil!");
				return false;
			}
    	},
        success: function() {
    		formShowSuccess()
        }, 
    	error: function() {
    		formShowError("Feil i lagring, er du fortsatt logget inn?");
        }
    });

    $("#registerUserForm").ajaxForm({
    	beforeSubmit: function(formData, jqForm, options) {
    		var filled = true;
    		jQuery.each(formData, function(i, val) {
    			if (val.value == "") filled = false;
    		});
    		if (!filled) {
    			formShowError("Alle feltene må fylles inn!");
    			return false;
    		}
    		if ($("#formPasswordConfirm").val() != $("#formPassword").val()) {
    			formShowError("Passord og passordbekreftelsen er ikke lik!");
    			return false;
    		}
    		if (!isValidEMail($("#formEMail").val())) {
    			formShowError("E-postadressen valideres feil!");
    			return false;
    		}
    	},
        success: function() {
    		formShowSuccess();
        }, 
    	error: function(request, textStatus, errorThrown) {
        	if (request.status == 400)
        		formShowError("Bruker med det brukernavn har allerede blitt registrert");
        	else
        		formShowError("Feil i registrering");
        }
    });

	// Toggle login visibility
	$("#toggleLogin").click(function() {$("#loginBox").slideToggle(); return false;});

	// Toggle search input
	var defaultSearch = "Søk i java.no";
	$("input.search_input").val(defaultSearch);
	$("input.search_input").focus(function() {if ($("input.search_input").val() == defaultSearch) {$("input.search_input").val("");}});
	$("input.search_input").blur(function() {if ($("input.search_input").val() === "") {$("input.search_input").val(defaultSearch);}});

	// Function to unset an attribute  
	$.fn.unset = function(a) {
	  return this.each(function(){
	    if ( a && a.constructor == String ) {
	      var fix = {
	        'for': 'htmlFor',
	        'text': 'cssText',
	        'class': 'className',
	        'float': 'cssFloat'
	      };
	      a = (fix[a] && fix[a].replace && fix[a]) || a;
	      this[a] = null;
	      if ( this.removeAttribute ) this.removeAttribute(a);
	    }
	  });
	}

	// Cookie handling
	function createCookie(name,value,days) {
		if (days) {
			var date = new Date();
			date.setTime(date.getTime()+(days*24*60*60*1000));
			var expires = "; expires="+date.toGMTString();
		}
		else var expires = "";
		document.cookie = name+"="+value+expires+"; path=/";
	}
	
	function readCookie(name) {
		var nameEQ = name + "=";
		var ca = document.cookie.split(';');
		for(var i=0;i < ca.length;i+=1) {
			var c = ca[i];
			while (c.charAt(0)==' ') c = c.substring(1,c.length);
			if (c.indexOf(nameEQ) == 0) return c.substring(nameEQ.length,c.length);
		}
		return null;
	}
	
	function eraseCookie(name) {
		createCookie(name,"",-1);
	}

	// Switch between meeting tabs
	$(".announcements ul li").click(function() {
		$(".announcements > div.container").remove();
		$(".announcements > ul > li").unset("class");
		this.setAttribute("class", "current");
		var elem = this.getElementsByTagName("div")[0].cloneNode(true);
		elem.removeAttribute("style");
		this.parentNode.parentNode.appendChild(elem);
		createCookie("city", this.getAttribute("id"), 5000);
	});

	// Sett den byen som sist var valgt
	var city = readCookie("city");
	if (city == null) {
		city = "osloMoteTab";
	}
	$("#" + city).click();

});
