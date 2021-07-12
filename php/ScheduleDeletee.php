<?php
   $con = mysqli_connect("localhost","audtjddbfl","dnddod13**","audtjddbfl");
   
   $userID = $_POST["userID"];
   $menuID = $_POST["menuID"];
   
   $statement = mysqli_prepare($con, "DELETE FROM CART WHERE userID = '$userID' AND menuID = '$menuID'");
  
   mysqli_stmt_bind_param($statement, "si", $userID, $menuID);
   mysqli_stmt_execute($statement);
   
   $response = array();
   $response["success"] = true;
   
   echo json_encode($response);
?>
