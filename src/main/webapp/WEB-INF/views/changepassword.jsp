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
</head>

<body style="margin: 0px auto">
	<center>
		<table width="1002" border="0" cellpadding="0" cellspacing="0">
			<tr>
				<td width="6" class="l-shadow">&nbsp;</td>
				<td class="middle" align="center" valign="top"><br />
					<h1 style="font-family: Verdana">Quantified Care</h1> <br />
					<center>
						<form name="form" onsubmit="return checkForm(this);" action="./changepassword" method="post">
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

								<table width="304" border="0" cellspacing="0" cellpadding="0">
								<tr align="center" style="color: red;"></tr>
									<tr>
										<td align="right" valign="top" style="padding: 5px 10px">Old Password</td>
										<td align="left" valign="top" style="padding: 5px 10px">
											<input id="opwd" type="password" name="old_password" id="textfield" />
										</td>
									</tr>
									<tr>
										<td align="right" valign="top" style="padding: 5px 10px">New Password</td>
										<td align="left" valign="top" style="padding: 5px 10px">
											<input id="npwd" type="password" name="new_password" id="textfield" />
										</td>
									</tr>
									<tr>
										<td align="right" valign="top" style="padding: 5px 10px">Confirm Password</td>
										<td align="left" valign="top" style="padding: 5px 10px">
										<input id="cpwd" type="password" name="confirm_password" id="textfield" /></td>
									</tr>
									
									<tr>
										<td style="padding: 5px 10px">&nbsp;</td>
										<td style="padding: 5px 10px"><input type="submit"
											name="submit" id="button" value="Reset Password" />
											<input name="userid" type="hidden" value="${userid}" />
											<input name="token" type="hidden" value="${token}" />
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
  function checkForm(form)
  {
	  if(form.old_password.value == "") {
    	  alert("Password can not be left blank!");
    	  form.old_password.focus();
          return false;
      }
	  if(form.new_password.value == "") {
    	  alert("Confirm Password can not be left blank!");
    	  form.new_password.focus();
          return false;
      }
	  if(form.confirm_password.value == "") {
    	  alert("Confirm Password can not be left blank!");
    	  form.confirm_password.focus();
          return false;
      }
	 if(form.new_password.value != form.confirm_password.value) {
    	  alert("Password must be same as confirm password");
    	  form.password.focus();
          return false;
      }
	  
  }
 </script>
 
 </body>
</html>
