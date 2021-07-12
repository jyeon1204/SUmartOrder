<?php
   error_reporting(E_ALL);
   ini_set('display_errors',1);
   
   $con = mysqli_connect("localhost", "audtjddbfl","dnddod13**","audtjddbfl");
   mysqli_set_charset($con,"utf8");  
   
   $userID = $_POST["userID"];
   $orderNumber = $_POST["orderNumber"];
     
   $statement = mysqli_prepare($con, "INSERT INTO PAYMENT VALUES (?,?)");
   mysqli_stmt_bind_param($statement, "si", $userID,$orderNumber);
   mysqli_stmt_execute($statement);

   $response = array();
   $response["success"] = true;

   echo json_encode($response);
?>
