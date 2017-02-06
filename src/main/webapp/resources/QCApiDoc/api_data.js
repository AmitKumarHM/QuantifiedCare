define({ "api": [
  {
    "type": "GET",
    "url": "APICODES",
    "title": "APICODES",
    "name": "APICODES",
    "version": "1.0.0",
    "group": "Quantified_Care_APIs",
    "description": "<p>Status codes to understand response messages</p> ",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "number",
            "optional": false,
            "field": "200",
            "description": "<p>Success.</p> "
          },
          {
            "group": "Parameter",
            "type": "number",
            "optional": false,
            "field": "201",
            "description": "<p>logout.</p> "
          },
          {
            "group": "Parameter",
            "type": "number",
            "optional": false,
            "field": "202",
            "description": "<p>Not Found.</p> "
          },
          {
            "group": "Parameter",
            "type": "number",
            "optional": false,
            "field": "203",
            "description": "<p>Invalid.</p> "
          },
          {
            "group": "Parameter",
            "type": "number",
            "optional": false,
            "field": "204",
            "description": "<p>error.</p> "
          },
          {
            "group": "Parameter",
            "type": "number",
            "optional": false,
            "field": "205",
            "description": "<p>Not Supported.</p> "
          }
        ]
      }
    },
    "filename": "QCApiDocSource/StatusCode.js",
    "groupTitle": "Quantified_Care_APIs"
  },
  {
    "type": "POST",
    "url": "/users/change_password",
    "title": "Change Password",
    "name": "Change_Password",
    "version": "1.0.0",
    "group": "Quantified_Care_APIs",
    "description": "<p>API will provide feature to change password if user want to change it.</p> ",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "access_token",
            "description": "<p>A Unique string for each user who is logged in*.</p> "
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "old_password",
            "description": "<p>User&#39;s old password*.</p> "
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "new_password",
            "description": "<p>New password to be changed*.</p> "
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "confirm_password",
            "description": "<p>Confirm password same as new password*.</p> "
          }
        ]
      }
    },
    "success": {
      "fields": {
        "Success 200": [
          {
            "group": "Success 200",
            "type": "Object",
            "optional": false,
            "field": "Success-Response",
            "description": "<p>Returns a json Object.</p> "
          }
        ],
        "Success-Response Object": [
          {
            "group": "Success-Response Object",
            "type": "String",
            "optional": false,
            "field": "statusCode",
            "description": "<p>Status Code.</p> "
          },
          {
            "group": "Success-Response Object",
            "type": "String",
            "optional": false,
            "field": "currentDate",
            "description": "<p>Server&#39;s current time stamp.</p> "
          },
          {
            "group": "Success-Response Object",
            "type": "String",
            "optional": false,
            "field": "result",
            "description": "<p>Result will be success.</p> "
          },
          {
            "group": "Success-Response Object",
            "type": "String",
            "optional": false,
            "field": "message",
            "description": "<p>Server&#39;s error message.</p> "
          }
        ]
      },
      "examples": [
        {
          "title": "Sample Success-Response:",
          "content": "{\n  \"statusCode\": \"200\",\n  \"currentDate\": \"Thu May 15 12:22:19 IST 2014\",\n  \"result\": \"success\",\n  \"message\": \"Password has been changed successfully.\"\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "fields": {
        "Error 4xx": [
          {
            "group": "Error 4xx",
            "type": "Object",
            "optional": false,
            "field": "Error-Response",
            "description": "<p>Returns a json Object.</p> "
          }
        ],
        "Error-Response Object": [
          {
            "group": "Error-Response Object",
            "type": "String",
            "optional": false,
            "field": "statusCode",
            "description": "<p>Status Code.</p> "
          },
          {
            "group": "Error-Response Object",
            "type": "String",
            "optional": false,
            "field": "currentDate",
            "description": "<p>Server&#39;s current time stamp.</p> "
          },
          {
            "group": "Error-Response Object",
            "type": "String",
            "optional": false,
            "field": "result",
            "description": "<p>Result will be success/error.</p> "
          },
          {
            "group": "Error-Response Object",
            "type": "String",
            "optional": false,
            "field": "message",
            "description": "<p>Server&#39;s error message.</p> "
          }
        ]
      },
      "examples": [
        {
          "title": "Sample Error-Response:",
          "content": "{\n  \"statusCode\": \"204\",\n  \"currentDate\": \"Thu May 15 12:23:46 IST 2014\",\n  \"result\": \"error\",\n  \"message\": \"Error occured, Please try again.\"\n} \n}",
          "type": "json"
        }
      ]
    },
    "filename": "QCApiDocSource/ChangePassword.js",
    "groupTitle": "Quantified_Care_APIs"
  },
  {
    "type": "GET",
    "url": "/users/forgot_password",
    "title": "Forgot Password",
    "name": "Forgot_Password",
    "version": "1.0.0",
    "group": "Quantified_Care_APIs",
    "description": "<p>API will provide feature to retrive password if user has forgot his password</p> ",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "email",
            "description": "<p>Email id of the user*.</p> "
          }
        ]
      }
    },
    "success": {
      "fields": {
        "Success 200": [
          {
            "group": "Success 200",
            "type": "Object",
            "optional": false,
            "field": "Success-Response",
            "description": "<p>Returns a json Object.</p> "
          }
        ],
        "Success-Response Object": [
          {
            "group": "Success-Response Object",
            "type": "String",
            "optional": false,
            "field": "statusCode",
            "description": "<p>Status Code.</p> "
          },
          {
            "group": "Success-Response Object",
            "type": "String",
            "optional": false,
            "field": "currentDate",
            "description": "<p>Server&#39;s current time stamp.</p> "
          },
          {
            "group": "Success-Response Object",
            "type": "String",
            "optional": false,
            "field": "result",
            "description": "<p>Result will be success.</p> "
          },
          {
            "group": "Success-Response Object",
            "type": "String",
            "optional": false,
            "field": "message",
            "description": "<p>Server&#39;s error message.</p> "
          }
        ]
      },
      "examples": [
        {
          "title": "Sample Success-Response:",
          "content": "{\n  \"statusCode\": \"200\",\n  \"currentDate\": \"Thu May 15 12:22:19 IST 2014\",\n  \"result\": \"success\",\n  \"message\": \"An email has been send to your email id.\"\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "fields": {
        "Error 4xx": [
          {
            "group": "Error 4xx",
            "type": "Object",
            "optional": false,
            "field": "Error-Response",
            "description": "<p>Returns a json Object.</p> "
          }
        ],
        "Error-Response Object": [
          {
            "group": "Error-Response Object",
            "type": "String",
            "optional": false,
            "field": "statusCode",
            "description": "<p>Status Code.</p> "
          },
          {
            "group": "Error-Response Object",
            "type": "String",
            "optional": false,
            "field": "currentDate",
            "description": "<p>Server&#39;s current time stamp.</p> "
          },
          {
            "group": "Error-Response Object",
            "type": "String",
            "optional": false,
            "field": "result",
            "description": "<p>Result will be success/error.</p> "
          },
          {
            "group": "Error-Response Object",
            "type": "String",
            "optional": false,
            "field": "message",
            "description": "<p>Server&#39;s error message.</p> "
          }
        ]
      },
      "examples": [
        {
          "title": "Sample Error-Response:",
          "content": "{\n  \"statusCode\": \"404\",\n  \"currentDate\": \"Thu May 15 12:23:46 IST 2014\",\n  \"result\": \"error\",\n  \"message\": \"Error occured, Please try again.\"\n}",
          "type": "json"
        }
      ]
    },
    "filename": "QCApiDocSource/ForgotPassword.js",
    "groupTitle": "Quantified_Care_APIs"
  },
  {
    "type": "POST",
    "url": "/users/login",
    "title": "Login",
    "name": "Login",
    "version": "1.0.0",
    "group": "Quantified_Care_APIs",
    "description": "<p>API will authenticate user on the basis of email and password provided</p> ",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "email",
            "description": "<p>Email id is used to authentication*.</p> "
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "password",
            "description": "<p>Password is used to authentication*.</p> "
          }
        ]
      }
    },
    "success": {
      "fields": {
        "Success 200": [
          {
            "group": "Success 200",
            "type": "Object",
            "optional": false,
            "field": "Success-Response",
            "description": "<p>Returns a json Object.</p> "
          }
        ],
        "Success-Response Object": [
          {
            "group": "Success-Response Object",
            "type": "Object",
            "optional": false,
            "field": "user",
            "description": "<p>User&#39;s object.</p> "
          },
          {
            "group": "Success-Response Object",
            "type": "String",
            "optional": false,
            "field": "statusCode",
            "description": "<p>Status Code.</p> "
          },
          {
            "group": "Success-Response Object",
            "type": "String",
            "optional": false,
            "field": "currentDate",
            "description": "<p>Server&#39;s current time stamp.</p> "
          },
          {
            "group": "Success-Response Object",
            "type": "String",
            "optional": false,
            "field": "result",
            "description": "<p>Result will be success.</p> "
          },
          {
            "group": "Success-Response Object",
            "type": "String",
            "optional": false,
            "field": "message",
            "description": "<p>Server&#39;s error message.</p> "
          },
          {
            "group": "Success-Response Object",
            "type": "String",
            "optional": false,
            "field": "access_token",
            "description": "<p>A Unique string for each user who is logged in.</p> "
          }
        ],
        "user": [
          {
            "group": "user",
            "type": "String",
            "optional": false,
            "field": "email",
            "description": "<p>Email id of the user.</p> "
          },
          {
            "group": "user",
            "type": "Number",
            "optional": false,
            "field": "role_id",
            "description": "<p>Role ID.</p> "
          },
          {
            "group": "user",
            "type": "String",
            "optional": false,
            "field": "user_id",
            "description": "<p>User&#39;s auto generated id.</p> "
          },
          {
            "group": "user",
            "type": "String",
            "optional": false,
            "field": "first_name",
            "description": "<p>User&#39;s First Name.</p> "
          },
          {
            "group": "user",
            "type": "String",
            "optional": false,
            "field": "middle_name",
            "description": "<p>User&#39;s Middle Name.</p> "
          },
          {
            "group": "user",
            "type": "String",
            "optional": false,
            "field": "last_name",
            "description": "<p>User&#39;s Last Name.</p> "
          }
        ]
      },
      "examples": [
        {
          "title": "Sample Success-Response:",
          "content": "{\n  \"user\": {\n      \"email\": \"test@kiwitech.com\",\n      \"role_id\": 1,\n      \"user_id\": \"a1b2c3d4e5f6\",\n      \"first_name\": \"Ajay\",\n      \"middle_name\": \"\",\n      \"last_name\": \"Gupta\"\n  },\n  \"access_token\": \"sjhdfghjsdgfhjsdfdjsfjdshgfsd\",\n  \"statusCode\": \"200\",\n  \"currentDate\": \"Thu May 15 12:23:46 IST 2014\",\n  \"result\": \"success\",\n  \"message\": \"User Logged In.\"\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "fields": {
        "Error 4xx": [
          {
            "group": "Error 4xx",
            "type": "Object",
            "optional": false,
            "field": "Error-Response",
            "description": "<p>Returns a json Object.</p> "
          }
        ],
        "Error-Response Object": [
          {
            "group": "Error-Response Object",
            "type": "String",
            "optional": false,
            "field": "statusCode",
            "description": "<p>Status Code.</p> "
          },
          {
            "group": "Error-Response Object",
            "type": "String",
            "optional": false,
            "field": "currentDate",
            "description": "<p>Server&#39;s current time stamp.</p> "
          },
          {
            "group": "Error-Response Object",
            "type": "String",
            "optional": false,
            "field": "result",
            "description": "<p>Result will be success/error.</p> "
          },
          {
            "group": "Error-Response Object",
            "type": "String",
            "optional": false,
            "field": "message",
            "description": "<p>Server&#39;s error message.</p> "
          }
        ]
      },
      "examples": [
        {
          "title": "Sample Error-Response:",
          "content": "{\n  \"statusCode\": \"404\",\n  \"currentDate\": \"Thu May 15 12:23:46 IST 2014\",\n  \"result\": \"error\",\n  \"message\": \"Error occured, Please try again.\"\n}",
          "type": "json"
        }
      ]
    },
    "filename": "QCApiDocSource/Login.js",
    "groupTitle": "Quantified_Care_APIs"
  }
] });