<?php
require "connection.php";
$mydata = array();

if(isset($_GET['id'])) {
	
	$id = $_GET['id'];
	
	//sql query
	$sql = "SELECT pass FROM park.parking WHERE id=1;";
	
	$stmt = $conn->prepare($sql);
	$stmt->execute();

	$stmt->bind_result($status);

	while($stmt->fetch()){
	
		$temp = [
		//'id' => $id,
		'status' => $status
		];
	
	
		array_push($mydata, $temp);
	
	}

	//echo json_encode($mydata);
	echo "${status}";
	
	
	
}


?>