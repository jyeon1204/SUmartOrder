<?php
	error_reporting(E_ALL);
	ini_set('display_errors',1);
	
	$con = mysqli_connect("localhost", "audtjddbfl","dnddod13**","audtjddbfl");
	
	mysqli_set_charset($con,"utf8");
	
	$userID = $_POST["userID"];

	$statement = mysqli_prepare($con, "SELECT userID FROM MEMBER WHERE userID = ?");	

	mysqli_stmt_bind_param($statement,"s",$userID);
	mysqli_stmt_execute($statement);
	//mysqli_stmt_store_result($statement);
	mysqli_stmt_bind_result($statement, $userID);
	
	$response = array();
	$response["success"] = true;
	
	while(mysqli_stmt_fetch($statement)){
		$response["success"] = false;
		$response["userID"] = $userID;
	}
	
	echo json_encode($response);
?>
