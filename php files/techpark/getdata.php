<?php
require "connection.php";

//creating array for storing data
$mydata = array();

//sql query
$sql = "SELECT id FROM park.parking;";


$stmt = $conn->prepare($sql);
$stmt->execute();

$stmt->bind_result($id);

while($stmt->fetch()){
	
	$temp = [
	'id' => $id,
	//'status' => $status
	];
	
	
	array_push($mydata, $temp);
	
}

echo json_encode($mydata);


?>