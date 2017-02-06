<%@ page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>QuantifiedCare Login</title>
<style type="text/css">
.l-shadow {
	background: url(/resources/img/left-shadow.png) repeat-y;
	width: 6px
}

.r-shadow {
	background: url(/resources/img/right-shadow.png) repeat-y;
	width: 14px
}

.middle {
	background: url(/resources/img/bg_content_gradient_darker.png) repeat-x
}
</style>

<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>

</head>

<body style="margin: 0px auto">
	<center>
		<table width="1002" border="0" cellpadding="0" cellspacing="0">
			<tr>
				<td width="6" class="l-shadow">&nbsp;</td>
				<td class="middle" align="center" valign="top"><br />
					<h1 style="font-family: Verdana">Quantified Care Panel</h1> <br />
					<center>
						<form name="form" onsubmit="return checkForm(this);" action="<%=request.getContextPath() %>/users/reset_pwd" method="post">
							<table width="300" border="0" align="center" cellpadding="0"
								cellspacing="0">
								<tr>
									<td height="3" align="center" bgcolor="#ce3e3e"></td>
								</tr>
								<tr>
									<td height="1" align="center" bgcolor="#FFFFFF"></td>
								</tr>
								<tr>
									<td height="34" align="center" bgcolor="#bfcfdc"><div
											style="font-family: tahoma; font-size: 16px; color: #063557; font-weight: bold; text-align: center; padding: 10px">Reset Password</div>
									</td>
								</tr>
							</table>
							<div style="border: 1px solid #cccccc; background: #FFFFFF; width: 304px; margin: 0px auto; font-size: 12px; color: #333333; font-family: tahoma">
								<br />
								<div id="error_msg"></div>
								<table width="304" border="0" cellspacing="0" cellpadding="0">
								<tr align="center" style="color: red;"></tr>
									<tr>
										<td align="right" valign="top" style="padding: 5px 10px">New Password</td>
										<td align="left" valign="top" style="padding: 5px 10px">
											<input type="password" name="new_password" id="new_password" />
										</td>
									</tr>
									<tr>
										<td align="right" valign="top" style="padding: 5px 10px">Confirm Password</td>
										<td align="left" valign="top" style="padding: 5px 10px">
										<input type="password" name="confirm_password" id="confirm_password" /></td>
									</tr>
									
									<tr>
										<td style="padding: 5px 10px">&nbsp;</td>
										<td style="padding: 5px 10px">
										<input type="submit" name="submit" id="submit_btn" value="Reset Password" />
											<input name="user_id" type="hidden" value="${user_id}" />
										</td>
									</tr>
								</table>
								</div>
						</form>
						<br />
					</center> <br />
				</td>
				<td width="14" class="r-shadow">&nbsp;</td>
			</tr>
		</table>

	</center>

<script type="text/javascript"> 

 //This function is used for validation of username, password, confirm password and checkbox...
  $( document ).ready(function() {
  	
	  $("#submit_btn").click(function(){
		  
		  var new_pass = $("#new_password").val();
		  var confirm_pass = $("#confirm_password").val();
		  if(new_pass.length == 0 || confirm_pass.length == 0){
			  $("#error_msg").text('New password or confirm password should not be blank!');
			  return false;
	  	  } if(new_pass != confirm_pass){
	  		  $("#error_msg").text('New and confirm password should be same!');
			  return false;
	  	  } else if(!(new_pass.length >=6 && new_pass.length <= 15) || !(confirm_pass.length >=6 && confirm_pass.length <= 15)){
	  		  $("#error_msg").text('Password should be between 6 to 15 characters!');
			  return false;
	  	  }
	  });
  });
 </script>
 
 </body>
</html>
