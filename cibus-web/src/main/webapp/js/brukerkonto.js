$(document).ready(function() {
    $("#registerUserForm").ajaxForm({
        beforeSubmit: function(formData, jqForm, options) {
            jQuery.each(formData, function(i, val) {
                if (val.value == "") {
                    formShowError("Alle feltene må fylles inn!");
                    return false;
                }
            });
            if ($("#formPasswordConfirm").val() != $("#formPassword").val()) {
                formShowError("Passordet og passordbekreftelsen er ikke lik!");
                return false;
            }
            if (!isValidEMail($("#formEMail").val())) {
                formShowError("E-postadressen er ikke på gyldig form");
                return false;
            }
            return false;
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
    })

    $("#editUserForm").ajaxForm({
    	beforeSubmit: function(formData, jqForm, options) {
			if (!isValidEMail($("#formEMail").val())) {
				formShowError("E-postadressen er ikke på gyldig form");
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
})
