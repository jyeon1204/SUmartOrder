<?php
    header("Content-Type: text/html; charset=UTF-8");
    $con = mysqli_connect("localhost","audtjddbfl","dnddod13**","audtjddbfl");
    mysqli_set_charset($con,"utf8");

    $sungshinUniversity = $_GET["sungshinUniversity"];
    $menuCategory = $_GET["menuCategory"];

    $result = mysqli_query($con, "SELECT * FROM MENU WHERE sungshinUniversity='$sungshinUniversity'AND menuCategory='$menuCategory'");
	$response = array();
	while($row = mysqli_fetch_array($result)){
	 array_push($response, array("menuID"=>$row[0],"sungshinUniversity"=>$row[1],"menuCategory"=>$row[2],"menuName"=>$row[3],"menuPrice"=>$row[4]));
	}

	echo json_encode(array("response"=>$response),JSON_UNESCAPED_UNICODE);
	mysqli_close($con,"utf8");
?>
