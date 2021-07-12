<?php
   error_reporting(E_ALL);
   ini_set('display_errors',1);
   
   $con = mysqli_connect("localhost", "audtjddbfl","dnddod13**","audtjddbfl");
   mysqli_set_charset($con,"utf8");  
   
   $userID = $_POST["userID"];
   $menuID = $_POST["menuID"];
   
   $statement = mysqli_prepare($con, "INSERT INTO CART VALUES (?, ?)");
   mysqli_stmt_bind_param($statement, "si", $userID,$menuID);
   mysqli_stmt_execute($statement);

   $response = array();
   $response["success"] = true;

   echo json_encode($response);
?>
