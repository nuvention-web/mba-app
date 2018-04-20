function validateEmail($email) {
 var emailReg = /^([\w-\.]+@([\w-]+\.)+[\w-]{2,4})?$/;
 return emailReg.test( $email );
}

function showError(text) {
  $("#error").slideDown();
  $("#error").text(text);
}

$(document).ready(function() {
  $("#email").focus();
});

$("#email").keyup(function() {
  var val = $("#email").val();
  if(val != "" && validateEmail(val)) {
    $("#form-hidden").slideDown();
  } else {
    $("#form-hidden").slideUp();
  }
});

$("#email").change(function() {
  var email = $("#email").val();
  var utm_source = $("#utm_source").val();
  var utm_medium = $("#utm_medium").val();
  var utm_campaign = $("#utm_campaign").val();
  var utm_term = $("#utm_term").val();
  var utm_content = $("#utm_content").val();

  if(email != "" && validateEmail(email)) {
    $.post("save_email.php",
      {
        email: email,
        utm_source: utm_source,
        utm_medium: utm_medium,
        utm_campaign: utm_campaign,
        utm_term: utm_term,
        utm_content: utm_content
      }
    ).done();
  }
});

$("#signup").submit(function(e) {
  var fname = $("#fname").val();
  var lname = $("#lname").val();
  var dob = $("#dob").val();
  var occ = $("#occ").val();
  var email = $("#email").val();
  var utm_source = $("#utm_source").val();
  var utm_medium = $("#utm_medium").val();
  var utm_campaign = $("#utm_campaign").val();
  var utm_term = $("#utm_term").val();
  var utm_content = $("#utm_content").val();

  e.preventDefault();
  if(!validateEmail(email)) {
      console.log(email);
      showError("Please check that your email is correct.");
      return;
  }
  if(fname == "" || lname == "" || dob == "" || occ == "") {
      showError("Please check that you have filled out all required fields.");
      return;
  }

  $.post( "process.php",
    {
      fname: fname,
      lname: lname,
      dob: dob,
      occ: occ,
      email: email,
      utm_source: utm_source,
      utm_medium: utm_medium,
      utm_campaign: utm_campaign,
      utm_term: utm_term,
      utm_content: utm_content
    }
  ).done(function(data) {
    $("#signup").slideUp();
    $("#success").slideDown();
  }).fail(function(data) {
    showError("Unable to sign you up. Please check your information and try again.");
  });

});
