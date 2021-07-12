<?php
   	header("Content-Type: text/html; charset=UTF-8");
	$con = mysqli_connect("localhost","audtjddbfl","dnddod13**","audtjddbfl");
	mysqli_set_charset($con,"utf8"); 
    
    $result = mysqli_query($con, "SELECT MENU.menuID, MENU.menuCategory, MENU.menuName, MENU.menuPrice FROM MENU, CART WHERE MENU.menuID = CART.menuID GROUP BY CART.menuID ORDER BY COUNT(CART.menuID) DESC LIMIT 5;");
    $response = array();
    while($row = mysqli_fetch_array($result)) {
       array_push($response, array("menuID"=>$row[0],"menuCategory"=>$row[1],"menuName"=>$row[2],"menuPrice"=>$row[3]));
   }	
    
    echo json_encode(array("response"=>$response), JSON_UNESCAPED_UNICODE);
    mysqli_close($con);
?>
