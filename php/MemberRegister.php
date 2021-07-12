<?php
	error_reporting(E_ALL);
	ini_set('display_errors',1);
	
	$con = mysqli_connect("localhost", "audtjddbfl","dnddod13**","audtjddbfl");
	mysqli_set_charset($con,"utf8");
	
	$userID = $_POST["userID"];
	$userPassword = $_POST["userPassword"];
	$userMail = $_POST["userMail"];
	$userNickname = $_POST["userNickname"];
	
	$checkedPassword = password_hash($userPassword, PASSWORD_DEFAULT);
	$statement = mysqli_prepare($con, "INSERT INTO MEMBER VALUES (?, ?, ?, ?)");
	mysqli_stmt_bind_param($statement,"ssss",$userID,$checkedPassword,$userMail,$userNickname);
	mysqli_stmt_execute($statement);
	
	$response = array();
	$response["success"] = true;
	
	echo json_encode($response);
?>
